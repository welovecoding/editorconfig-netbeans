package com.welovecoding.netbeans.plugin.editorconfig.processor;

import static com.welovecoding.netbeans.plugin.editorconfig.config.Settings.ENCODING_SETTING;
import com.welovecoding.netbeans.plugin.editorconfig.mapper.EditorConfigPropertyMapper;
import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.IndentSizeOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.IndentStyleOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.XFinalNewLineOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.XLineEndingOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.XTabWidthOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.XTrimTrailingWhitespacesOperation;
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
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.editorconfig.core.EditorConfig;
import org.editorconfig.core.EditorConfigException;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
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

      for (EditorConfig.OutPair rule : rules) {
        keyedRules.put(rule.getKey().toLowerCase(), rule.getVal().toLowerCase());
      }
    } catch (EditorConfigException ex) {
      LOG.log(Level.SEVERE, ex.getMessage());
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

    boolean fileChange = false;
    boolean charsetChange = false;
    boolean styleChange = false;

    for (Map.Entry<String, String> rule : keyedRules.entrySet()) {
      final String key = rule.getKey();
      final String value = rule.getValue();

      LOG.log(Level.INFO, "Found rule \"{0}\" with value \"{1}\".", new Object[]{key, value});

      switch (key) {
        case EditorConfigConstant.CHARSET:
          charsetChange = doCharset(fileObject, keyedRules.get(EditorConfigConstant.CHARSET));
          break;
        case EditorConfigConstant.END_OF_LINE:
          String ecLineEnding = keyedRules.get(EditorConfigConstant.END_OF_LINE);
          boolean changedLineEndings = doEndOfLine(dataObject, ecLineEnding);
          fileChange = fileChange || changedLineEndings;
          break;
        case EditorConfigConstant.INDENT_SIZE:
          //TODO this should happen in the file!!
          boolean indentSizeChanged = IndentSizeOperation.doIndentSize(dataObject, value);
          if (indentSizeChanged) {
            LOG.log(Level.INFO, "Action: Indent size changed");
          }
          styleChange = indentSizeChanged || styleChange;
          break;
        case EditorConfigConstant.INDENT_STYLE:
          //TODO this happens in the file!!
          boolean indentStyleChanged = IndentStyleOperation.doIndentStyle(dataObject, key);
          if (indentStyleChanged) {
            LOG.log(Level.INFO, "Action: Indent style changed");
          }
          styleChange = indentStyleChanged || styleChange;

          break;
        case EditorConfigConstant.INSERT_FINAL_NEWLINE:
          String lineEnding = keyedRules.get(EditorConfigConstant.END_OF_LINE);
          String javaLineEnding = EditorConfigPropertyMapper.mapLineEnding(lineEnding);
          boolean newLineChanged = XFinalNewLineOperation.doFinalNewLine(content, value, javaLineEnding);
          fileChange = newLineChanged || fileChange;
          break;
        case EditorConfigConstant.TAB_WIDTH:
          boolean tabWidthChanged = XTabWidthOperation.doTabWidth(dataObject, value);
          if (tabWidthChanged) {
            LOG.log(Level.INFO, "Action: Tab width changed");
          }
          styleChange = tabWidthChanged || styleChange;
          break;
        case EditorConfigConstant.TRIM_TRAILING_WHITESPACE:
          boolean trimTrailingWhitespacesChanged = XTrimTrailingWhitespacesOperation.doTrimTrailingWhitespaces(
                  content,
                  value,
                  EditorConfigPropertyMapper.mapLineEnding(keyedRules.get(EditorConfigConstant.END_OF_LINE)));
          if (trimTrailingWhitespacesChanged) {
            LOG.log(Level.INFO, "Action: Trailing whitespaces changed");
          }
          fileChange = trimTrailingWhitespacesChanged || fileChange;
          break;
        default:
          LOG.log(Level.WARNING, "Unknown property: {0}", key);
          break;
      }
    }

    FlushFileInfo fileInfo = new FlushFileInfo(fileObject);
    fileInfo.setContent(content);
    fileInfo.setChanged(fileChange);
    fileInfo.setCharsetChange(charsetChange);
    fileInfo.setCharset(EditorConfigPropertyMapper.mapCharset(keyedRules.get(EditorConfigConstant.CHARSET)));
    fileInfo.setFlushInEditor(isOpenedInEditor);
    fileInfo.setCookie(cookie);

    flushFile(fileInfo);
    flushStyles(fileObject, styleChange);
  }

  private void flushFile(FlushFileInfo info) {
    EditorCookie cookie = info.getCookie();
    FileObject fileObject = info.getFileObject();

    if (info.isChanged() || info.isCharsetChange()) {
      if (!info.isFlushInEditor()) {
        LOG.log(Level.INFO, "Write content (with all rules applied) to file: {0}", fileObject.getPath());
        WriteStringToFileTask task = new WriteStringToFileTask(info);
        task.run();
      } else {
        LOG.log(Level.INFO, "Update changes in Editor window");

        NbDocument.runAtomic(cookie.getDocument(), () -> {
          updateChangesInEditorWindow(info);
        });
      }
    }
  }

  private static void updateChangesInEditorWindow(FlushFileInfo info) {
    EditorCookie cookie = info.getCookie();
    Charset charset = info.getCharset();
    FileObject fileObject = info.getFileObject();

    try {
      StyledDocument newDocument = cookie.openDocument();
      newDocument.remove(0, newDocument.getLength());
      newDocument.insertString(0, info.getStringWithCharset(), null);
      fileObject.setAttribute(ENCODING_SETTING, charset.name());
      cookie.saveDocument();
    } catch (BadLocationException | IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  private boolean doCharset(FileObject fileObject, String charset) {
    boolean wasChanged = false;

    Charset currentCharset = NetBeansFileUtil.guessCharset(fileObject);
    Charset requestedCharset = EditorConfigPropertyMapper.mapCharset(charset);

    if (!currentCharset.equals(requestedCharset)) {
      LOG.log(Level.INFO, "Charset change needed.");
      wasChanged = true;
    }

    return wasChanged;
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

  private void flushStyles(FileObject fileObject, boolean styleChanged) {
    if (styleChanged) {
      try {
        Preferences codeStyle = CodeStylePreferences.get(fileObject, fileObject.getMIMEType()).getPreferences();
        codeStyle.flush();
      } catch (BackingStoreException ex) {
        LOG.log(Level.SEVERE, "Error applying code style: {0}", ex.getMessage());
      }
    }
  }

  private EditorCookie getEditorCookie(FileObject fileObject) {
    try {
      return (EditorCookie) DataObject.find(fileObject).getLookup().lookup(EditorCookie.class);
    } catch (DataObjectNotFoundException ex) {
      Exceptions.printStackTrace(ex);
      return null;
    }
  }

  private EditorCookie getEditorCookie(DataObject dataObject) {
    return dataObject.getLookup().lookup(EditorCookie.class);
  }

  private void setFileAttribute(FileObject fo, String key, String value) {
    try {
      fo.setAttribute(key, value);
    } catch (IOException ex) {
      LOG.log(Level.SEVERE, "Error setting file attribute \"{0}\" with value \"{1}\" for {2}. {3}",
              new Object[]{
                key,
                value,
                fo.getPath(),
                ex.getMessage()
              });
    }
  }
}
