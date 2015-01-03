package com.welovecoding.netbeans.plugin.editorconfig.processor;

import com.welovecoding.netbeans.plugin.editorconfig.mapper.EditorConfigPropertyMapper;
import com.welovecoding.netbeans.plugin.editorconfig.io.writer.StyledDocumentWriter;
import com.welovecoding.netbeans.plugin.editorconfig.io.exception.FileAccessException;
import com.welovecoding.netbeans.plugin.editorconfig.io.model.MappedCharset;
import com.welovecoding.netbeans.plugin.editorconfig.io.reader.FileInfoReader;
import com.welovecoding.netbeans.plugin.editorconfig.io.reader.FileObjectReader;
import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import com.welovecoding.netbeans.plugin.editorconfig.model.MappedEditorConfig;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.XFinalNewLineOperation;
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
   * @throws Exception
   */
  public void applyRulesToFile(DataObject dataObject) throws Exception {
    FileObject primaryFile = dataObject.getPrimaryFile();
    filePath = primaryFile.getPath();

    MappedEditorConfig config = readRulesForFile(filePath);
    FileInfo info = excuteOperations(dataObject, config);

    // Apply EditorConfig operations
    if (info.isFileChangeNeeded()) {
      LOG.log(Level.INFO, "Flush file changes for: {0}", filePath);
      flushFile(info);
    }
  }

  private void doCharset(DataObject dataObject, MappedCharset requestedCharset) {
    FileObject fo = dataObject.getPrimaryFile();
    MappedCharset currentCharset = FileInfoReader.readCharset(fo);

    LOG.log(Level.INFO, "\u00ac Current charset: {0}", currentCharset.getName());

    if (currentCharset != requestedCharset) {
      LOG.log(Level.INFO, "\u00ac Changing charset from \"{0}\" to \"{1}\"",
              new Object[]{currentCharset.getName(), requestedCharset.getName()});

      String content = FileObjectReader.read(fo, currentCharset.getCharset());
      // FileObjectWriter.writeWithAtomicAction(dataObject, requestedCharset.getCharset(), content);

    } else {
      /*
       try {
       // TODO: A bit dangerous atm!
       // ConfigWriter.rewrite(dataObject, currentCharset, requestedCharset);
       } catch (IOException ex) {
       Exceptions.printStackTrace(ex);
       }
       */
      LOG.log(Level.INFO, "No charset change needed.");
    }
  }

  protected FileInfo excuteOperations(DataObject dataObject, MappedEditorConfig config) throws IOException, Exception {

    FileObject primaryFile = dataObject.getPrimaryFile();
    StringBuilder content = new StringBuilder(primaryFile.asText());
    boolean fileChangeNeeded = false;

    LOG.log(Level.INFO, "Mapped rules for: {0}", filePath);
    LOG.log(Level.INFO, config.toString());

    // 1. "charset"
    MappedCharset mappedCharset = config.getCharset();

    if (mappedCharset != null) {
      logOperation(EditorConfigConstant.CHARSET, mappedCharset.getName());
      doCharset(dataObject, mappedCharset);
    }

    // 5. "insert_final_newline"
    boolean insertFinalNewLine = config.isInsertFinalNewLine();

    if (insertFinalNewLine) {
      logOperation(EditorConfigConstant.INSERT_FINAL_NEWLINE, insertFinalNewLine);
      boolean changedLineEndings = XFinalNewLineOperation.doFinalNewLine(content, insertFinalNewLine, config.getEndOfLine());
      fileChangeNeeded = fileChangeNeeded || changedLineEndings;
    }

    // Construct FileInfo object
    // TODO: FileInfo duplicates values from MappedEditorConfig
    FileInfo info = new FileInfo(dataObject);
    info.setContent(content);

    if (mappedCharset != null) {
      info.setCharset(mappedCharset.getCharset());
    } else {
      info.setCharset(StandardCharsets.UTF_8);
    }

    EditorCookie cookie = getEditorCookie(dataObject);
    boolean isOpenedInEditor = (cookie != null) && (cookie.getDocument() != null);
    info.setOpenedInEditor(isOpenedInEditor);
    info.setCookie(cookie);
    info.setFileChangeNeeded(fileChangeNeeded);

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
      LOG.log(Level.SEVERE, "Error applying code style: {0}", ex.getMessage());
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
