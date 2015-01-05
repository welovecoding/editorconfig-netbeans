package com.welovecoding.netbeans.plugin.editorconfig.processor;

import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.IndentSizeOperation;
import com.welovecoding.netbeans.plugin.editorconfig.mapper.EditorConfigPropertyMapper;
import com.welovecoding.netbeans.plugin.editorconfig.io.writer.StyledDocumentWriter;
import com.welovecoding.netbeans.plugin.editorconfig.io.exception.FileAccessException;
import com.welovecoding.netbeans.plugin.editorconfig.io.model.MappedCharset;
import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import com.welovecoding.netbeans.plugin.editorconfig.model.MappedEditorConfig;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.FinalNewLineOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.TrimTrailingWhiteSpaceOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.tobedone.CharsetOperation;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.text.NbDocument;

public class EditorConfigProcessor {

  private static final Logger LOG = Logger.getLogger(EditorConfigProcessor.class.getSimpleName());
  public static final Level OPERATION_LOG_LEVEL = Level.INFO;

  private String filePath;

  public EditorConfigProcessor() {
  }

  /**
   * Applies properties defined in an ".editorconfig" file to a DataObject.
   *
   * If a supported property is found, then changes are made to a StringBuilder
   * instance.
   *
   * The StringBuilder instance is passed as a reference to operations that can
   * then perform their actions on this instance.
   *
   * After all operations were performed, the changes will be flushed.
   *
   * @param dataObject
   */
  public void applyRulesToFile(DataObject dataObject) {
    FileObject primaryFile = dataObject.getPrimaryFile();
    filePath = primaryFile.getPath();

    MappedEditorConfig config = readRulesForFile(filePath);
    FileInfo info = excuteOperations(dataObject, config);

    // Apply EditorConfig operations
    if (info.isStyleFlushNeeded()) {
      LOG.log(Level.INFO, "Flush style changes for: {0}", filePath);
      flushStyles(info.getFileObject());
    }

    if (info.isFileChangeNeeded()) {
      LOG.log(Level.INFO, "Flush file changes for: {0}", filePath);
      flushFile(info);
    }
  }

  protected FileInfo excuteOperations(DataObject dataObject, MappedEditorConfig config) {
    LOG.log(Level.INFO, "Mapped rules for: {0}", filePath);
    LOG.log(Level.INFO, config.toString());

    FileInfo info = new FileInfo(dataObject);
    boolean fileChangeNeeded = false;
    boolean styleFlushNeeded = false;

    FileObject primaryFile = dataObject.getPrimaryFile();
    StringBuilder content;

    try {
      content = new StringBuilder(primaryFile.asText());
    } catch (IOException ex) {
      content = new StringBuilder();
    }

    info.setContent(content);
    info.setEndOfLine(config.getEndOfLine());

    EditorCookie cookie = getEditorCookie(dataObject);
    boolean isOpenedInEditor = (cookie != null) && (cookie.getDocument() != null);

    info.setOpenedInEditor(isOpenedInEditor);
    info.setCookie(cookie);

    // 1. "charset"
    MappedCharset mappedCharset = config.getCharset();

    if (mappedCharset != null) {
      logOperation(EditorConfigConstant.CHARSET, mappedCharset.getName());
      boolean changedCharset = new CharsetOperation().run(dataObject, mappedCharset);
      fileChangeNeeded = fileChangeNeeded || changedCharset;
      info.setCharset(mappedCharset.getCharset());
    } else {
      info.setCharset(StandardCharsets.UTF_8);
    }

    // 3. "indent_size"
    if (config.getIndentSize() > -1) {
      logOperation(EditorConfigConstant.INDENT_SIZE, config.getIndentSize());
      boolean changedIndentSize = new IndentSizeOperation().run(primaryFile, config.getIndentSize());
      styleFlushNeeded = styleFlushNeeded || changedIndentSize;
    }

    // 5. "insert_final_newline"
    if (config.isInsertFinalNewLine()) {
      logOperation(EditorConfigConstant.INSERT_FINAL_NEWLINE, config.isInsertFinalNewLine());
      boolean changedLineEndings = new FinalNewLineOperation().run(info);
      fileChangeNeeded = fileChangeNeeded || changedLineEndings;
    }

    // 7. "trim_trailing_whitespace"
    if (config.isTrimTrailingWhiteSpace()) {
      logOperation(EditorConfigConstant.TRIM_TRAILING_WHITESPACE, config.isTrimTrailingWhiteSpace());
      boolean trimmedWhiteSpaces = new TrimTrailingWhiteSpaceOperation().run(info);
      fileChangeNeeded = fileChangeNeeded || trimmedWhiteSpaces;
    }

    if (mappedCharset != null) {
    } else {
      info.setCharset(StandardCharsets.UTF_8);
    }

    info.setFileChangeNeeded(fileChangeNeeded);
    info.setStyleFlushNeeded(styleFlushNeeded);

    return info;
  }

  protected void flushFile(FileInfo info) {
    if (info.isOpenedInEditor()) {
      updateChangesInEditorWindow(info);
    } else {
      updateChangesInFile(info);
    }
  }

  private void flushStyles(FileObject fileObject) {
    try {
      Preferences codeStyle = CodeStylePreferences.get(fileObject, fileObject.getMIMEType()).getPreferences();
      codeStyle.flush();
    } catch (BackingStoreException ex) {
      LOG.log(Level.SEVERE, "Error flushing code styles: {0}", ex.getMessage());
    }
  }

  private EditorCookie getEditorCookie(DataObject dataObject) {
    return dataObject.getLookup().lookup(EditorCookie.class);
  }

  private void logOperation(String key, Object value) {
    LOG.log(Level.INFO, "\"{0}\": {1} ({2})", new Object[]{
      key,
      value,
      filePath
    });
  }

  private MappedEditorConfig readRulesForFile(String filePath) {
    return EditorConfigPropertyMapper.createEditorConfig(filePath);
  }

  private void updateChangesInEditorWindow(FileInfo info) {
    LOG.log(Level.INFO, "Update changes in Editor window for: {0}", info.getPath());

    EditorCookie cookie = info.getCookie();
    NbDocument.runAtomic(cookie.getDocument(), () -> {
      try {
        StyledDocumentWriter.writeWithEditorKit(info);
      } catch (FileAccessException ex) {
        LOG.log(Level.SEVERE, ex.getMessage());
      }
    });
  }

  private void updateChangesInFile(FileInfo info) {
    LOG.log(Level.INFO, "Write content (with all rules applied) to file: {0}",
            info.getFileObject().getPath());

    WriteStringToFileTask task = new WriteStringToFileTask(info);
    task.run();
  }
}
