package com.welovecoding.netbeans.plugin.editorconfig.processor;

import com.welovecoding.netbeans.plugin.editorconfig.mapper.EditorConfigPropertyMapper;
import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import com.welovecoding.netbeans.plugin.editorconfig.processor.io.DocumentReaderWriter;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.IndentSizeOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.IndentStyleOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.XFinalNewLineOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.XLineEndingOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.XTabWidthOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.XTrimTrailingWhitespacesOperation;
import com.welovecoding.netbeans.plugin.editorconfig.util.FileAccessException;
import com.welovecoding.netbeans.plugin.editorconfig.util.NetBeansFileUtil;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.text.StyledDocument;
import org.editorconfig.core.EditorConfig;
import org.editorconfig.core.EditorConfigException;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;

public class EditorConfigProcessor {

  private static final Logger LOG = Logger.getLogger(EditorConfigProcessor.class.getSimpleName());
  public static final Level OPERATION_LOG_LEVEL = Level.WARNING;
  private final EditorConfig ec;

  public EditorConfigProcessor() {
    ec = new EditorConfig(".editorconfig", EditorConfig.VERSION);
  }

  private HashMap<String, String> parseRulesForFile(DataObject dataObject) {
    String filePath = dataObject.getPrimaryFile().getPath();

    LOG.log(Level.INFO, "Apply rules for: {0}", filePath);

    List<EditorConfig.OutPair> rules = new ArrayList<>();
    HashMap<String, String> keyedRules = new HashMap<>();

    try {
      rules = ec.getProperties(filePath);
    } catch (EditorConfigException ex) {
      LOG.log(Level.SEVERE, ex.getMessage());
    }

    for (EditorConfig.OutPair rule : rules) {
      keyedRules.put(rule.getKey().toLowerCase(), rule.getVal().toLowerCase());
    }

    return keyedRules;
  }

  public void applyRulesToFile(DataObject dataObject) throws Exception {
    HashMap<String, String> keyedRules = parseRulesForFile(dataObject);

    // Save file before appling any changes when opened in editor
    EditorCookie cookie = getEditorCookie(dataObject);
    boolean isOpenedInEditor = (cookie != null) && (cookie.getDocument() != null);

    FileObject fileObject = dataObject.getPrimaryFile();
    StringBuilder content = new StringBuilder(fileObject.asText());

    boolean fileChangeNeeded = false;
    boolean charsetChangeNeeded = false;
    boolean styleChangeNeeded = false;

    for (Map.Entry<String, String> rule : keyedRules.entrySet()) {
      final String key = rule.getKey();
      final String value = rule.getValue();

      LOG.log(Level.INFO, "Found rule \"{0}\" with value \"{1}\".", new Object[]{key, value});

      switch (key) {
        case EditorConfigConstant.CHARSET:
          charsetChangeNeeded = doCharset(fileObject, keyedRules.get(EditorConfigConstant.CHARSET));
          break;
        case EditorConfigConstant.END_OF_LINE:
          String ecLineEnding = keyedRules.get(EditorConfigConstant.END_OF_LINE);
          boolean changedLineEndings = doEndOfLine(dataObject, ecLineEnding);
          fileChangeNeeded = fileChangeNeeded || changedLineEndings;
          break;
        case EditorConfigConstant.INDENT_SIZE:
          //TODO this should happen in the file!!
          boolean indentSizeChanged = IndentSizeOperation.doIndentSize(dataObject, value);
          if (indentSizeChanged) {
            LOG.log(Level.INFO, "Action: Indent size changed");
          }
          styleChangeNeeded = indentSizeChanged || styleChangeNeeded;
          break;
        case EditorConfigConstant.INDENT_STYLE:
          //TODO this happens in the file!!
          boolean indentStyleChanged = IndentStyleOperation.doIndentStyle(dataObject, key);
          if (indentStyleChanged) {
            LOG.log(Level.INFO, "Action: Indent style changed");
          }
          styleChangeNeeded = indentStyleChanged || styleChangeNeeded;

          break;
        case EditorConfigConstant.INSERT_FINAL_NEWLINE:
          String lineEnding = keyedRules.get(EditorConfigConstant.END_OF_LINE);
          String javaLineEnding = EditorConfigPropertyMapper.mapLineEnding(lineEnding);
          boolean newLineChanged = XFinalNewLineOperation.doFinalNewLine(content, value, javaLineEnding);
          fileChangeNeeded = newLineChanged || fileChangeNeeded;
          break;
        case EditorConfigConstant.TAB_WIDTH:
          boolean tabWidthChanged = XTabWidthOperation.doTabWidth(dataObject, value);
          if (tabWidthChanged) {
            LOG.log(Level.INFO, "Action: Tab width changed");
          }
          styleChangeNeeded = tabWidthChanged || styleChangeNeeded;
          break;
        case EditorConfigConstant.TRIM_TRAILING_WHITESPACE:
          boolean trimTrailingWhitespacesChanged = XTrimTrailingWhitespacesOperation.doTrimTrailingWhitespaces(
                  content,
                  value,
                  EditorConfigPropertyMapper.mapLineEnding(keyedRules.get(EditorConfigConstant.END_OF_LINE)));
          if (trimTrailingWhitespacesChanged) {
            LOG.log(Level.INFO, "Action: Trailing whitespaces changed");
          }
          fileChangeNeeded = trimTrailingWhitespacesChanged || fileChangeNeeded;
          break;
        default:
          LOG.log(Level.WARNING, "Unknown property: {0}", key);
          break;
      }
    }

    FileInfo info = new FileInfo(dataObject);
    info.setContent(content);
    info.setCharset(EditorConfigPropertyMapper.mapCharset(keyedRules.get(EditorConfigConstant.CHARSET)));
    info.setOpenedInEditor(isOpenedInEditor);
    info.setCookie(cookie);

    if (charsetChangeNeeded) {
      String ecCharset = keyedRules.get(EditorConfigConstant.CHARSET);
      String fileMark = EditorConfigPropertyMapper.getFileMark(ecCharset);
      Charset charset = EditorConfigPropertyMapper.mapCharset(ecCharset);

      info.setCharset(charset);
      info.setFileMark(fileMark);
    }

    if (fileChangeNeeded || charsetChangeNeeded) {
      flushFile(info);
    }

    if (styleChangeNeeded) {
      flushStyles(fileObject);
    }
  }

  private void flushFile(FileInfo info) {
    if (info.isOpenedInEditor()) {
      updateChangesInEditorWindow(info);
    } else {
      updateChangesInFile(info);
    }
  }

  private void updateChangesInFile(FileInfo info) {
    LOG.log(Level.INFO, "Write content (with all rules applied) to file: {0}", info.getFileObject().getPath());

    WriteStringToFileTask task = new WriteStringToFileTask(info);
    task.run();
  }

  private void updateChangesInEditorWindow(FileInfo info) {
    LOG.log(Level.INFO, "Update changes in Editor window for: {0}", info.getPath());

    EditorCookie cookie = info.getCookie();
    NbDocument.runAtomic(cookie.getDocument(), () -> {
      try {
        DocumentReaderWriter.writeWithEditorKit(info);
      } catch (FileAccessException ex) {
        LOG.log(Level.SEVERE, ex.getMessage());
      }
    });
  }

  private boolean doCharset(FileObject fileObject, String charset) {
    boolean hasToBeChanged = false;

    Charset currentCharset = NetBeansFileUtil.guessCharset(fileObject);
    Charset requestedCharset = EditorConfigPropertyMapper.mapCharset(charset);

    if (!currentCharset.equals(requestedCharset)) {
      LOG.log(Level.INFO, "Charset change needed from {0} to {1}",
              new Object[]{currentCharset.name(), requestedCharset.name()});
      hasToBeChanged = true;
    }

    return hasToBeChanged;
  }

  private boolean doEndOfLine(DataObject dataObject, String ecLineEnding) {
    FileObject fileObject = dataObject.getPrimaryFile();
    String javaLineEnding = EditorConfigPropertyMapper.mapLineEnding(ecLineEnding);
    boolean wasChanged = false;

    try {
      StringBuilder content = new StringBuilder(fileObject.asText());
      wasChanged = XLineEndingOperation.doLineEndings(content, javaLineEnding);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    StyledDocument document = NbDocument.getDocument(dataObject);
    if (document != null && wasChanged) {
      if (!document.getProperty(BaseDocument.READ_LINE_SEPARATOR_PROP).equals(javaLineEnding)) {
        document.putProperty(BaseDocument.READ_LINE_SEPARATOR_PROP, javaLineEnding);
        LOG.log(Level.INFO, "Action: Changed line endings in Document.");

      } else {
        LOG.log(Level.INFO, "Action not needed: Line endings are already set to: {0}", ecLineEnding);
      }
    }

    return wasChanged;
  }

  private void flushStyles(FileObject fileObject) {
    try {
      Preferences codeStyle = CodeStylePreferences.get(fileObject, fileObject.getMIMEType()).getPreferences();
      codeStyle.flush();
    } catch (BackingStoreException ex) {
      LOG.log(Level.SEVERE, "Error applying code style: {0}", ex.getMessage());
    }
  }

  private EditorCookie getEditorCookie(DataObject dataObject) {
    return dataObject.getLookup().lookup(EditorCookie.class);
  }
}
