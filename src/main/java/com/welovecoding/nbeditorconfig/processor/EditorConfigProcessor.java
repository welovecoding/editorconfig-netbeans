package com.welovecoding.nbeditorconfig.processor;

import static com.welovecoding.nbeditorconfig.config.LoggerSettings.PROCESSOR_LOG_LEVEL;
import com.welovecoding.nbeditorconfig.io.model.MappedCharset;
import com.welovecoding.nbeditorconfig.mapper.EditorConfigPropertyMapper;
import com.welovecoding.nbeditorconfig.model.EditorConfigConstant;
import com.welovecoding.nbeditorconfig.model.MappedEditorConfig;
import com.welovecoding.nbeditorconfig.processor.operation.FinalNewLineOperation;
import com.welovecoding.nbeditorconfig.processor.operation.IndentSizeOperation;
import com.welovecoding.nbeditorconfig.processor.operation.IndentStyleOperation;
import com.welovecoding.nbeditorconfig.processor.operation.LineEndingOperation;
import com.welovecoding.nbeditorconfig.processor.operation.TabWidthOperation;
import com.welovecoding.nbeditorconfig.processor.operation.TrimTrailingWhiteSpaceOperation;
import com.welovecoding.nbeditorconfig.processor.operation.tobedone.CharsetOperation;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.netbeans.api.project.Project;
import org.netbeans.api.queries.FileEncodingQuery;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem.AtomicAction;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.openide.util.Utilities;

public class EditorConfigProcessor {

  private static final Logger LOG = Logger.getLogger(EditorConfigProcessor.class.getName());

  private String filePath;
  private final String projectPath;

  static {
    LOG.setLevel(PROCESSOR_LOG_LEVEL);
  }

  public EditorConfigProcessor() {
    // Project nbProject = OpenProjects.getDefault().getMainProject();
    Lookup lookup = Utilities.actionsGlobalContext();
    Project project = lookup.lookup(Project.class);
    if (project == null) {
      projectPath = "";
    } else {
      FileObject nbProjectDirectory = project.getProjectDirectory();
      projectPath = nbProjectDirectory.getPath();
    }
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
   * @param dataObject Object that represents the file which was recognized by
   * an EditorConfig rule
   */
  public void applyRulesToFile(DataObject dataObject) {
    LOG.log(Level.INFO, "Getting Primary File");
    FileObject primaryFile = dataObject.getPrimaryFile();
    LOG.log(Level.INFO, "Getting Path");
    filePath = primaryFile.getPath();

    LOG.log(Level.INFO, "Apply rules to file: {0} (MIME type: {1})",
            new Object[]{filePath, primaryFile.getMIMEType()});

    // Check if the file's MIME type can be edited
    // Allowed MIME types are: text/html, text/javascript, text/x-java, text/xml, ...
    if (!primaryFile.getMIMEType().startsWith("text/")) {
      LOG.log(Level.INFO, "Skipping file because it has an unsupported MIME type.");
      return;
    }

    // Check if file is stored in a critical path
    for (String directoryName : SmartSkip.IGNORED_FILES) {
      // Note: Always use forward slashes here
      String path = projectPath + "/" + directoryName;
      if (filePath.startsWith(path)) {
        LOG.log(Level.INFO, "Skipping file because it is located in an unsupported directory.", path);
        return;
      }
    }

    MappedEditorConfig config = readRulesForFile(filePath);
    FileInfo info = excuteOperations(dataObject, config);

    // Apply EditorConfig operations
    LOG.log(Level.INFO, "Flush style changes for: {0}", filePath);
    flushStyles(info);

    if (info.isFileChangeNeeded()) {
      LOG.log(Level.INFO, "Flush file changes for: {0}", filePath);
      flushFile(info);
    }
    LOG.log(Level.INFO, "Flush style changes for: {0}", filePath);
    flushStyles(info);
  }

  protected FileInfo excuteOperations(DataObject dataObject, MappedEditorConfig config) {
    LOG.log(Level.INFO, "Mapped rules for: {0}", filePath);
    LOG.log(Level.INFO, config.toString());

    FileInfo info = new FileInfo(dataObject);
    boolean fileChangeNeeded = false;
    boolean styleFlushNeeded = false;

    FileObject primaryFile = dataObject.getPrimaryFile();
    StringBuilder content;

    MappedCharset mappedCharset = config.getCharset();
    Charset defaultCharset = FileEncodingQuery.getEncoding(primaryFile);
    try {
      String charset = mappedCharset != null ? mappedCharset.getCharset().name() : defaultCharset.name();
      content = new StringBuilder(primaryFile.asText(charset));
    } catch (IOException ex) {
      LOG.log(Level.WARNING, "Failed to get the text of the file");
      content = new StringBuilder("");
    }

    info.setContent(content);
    if (config.getEndOfLine() != null) {
      info.setEndOfLine(config.getEndOfLine());
    }

    EditorCookie cookie = getEditorCookie(dataObject);
    boolean isOpenedInEditor = (cookie != null) && (cookie.getDocument() != null);

    info.setOpenedInEditor(isOpenedInEditor);
    info.setCookie(cookie);

    // 1. "charset"
    if (mappedCharset != null) {
      logOperation(EditorConfigConstant.CHARSET, mappedCharset.getName());
      boolean changedCharset = new CharsetOperation().run(dataObject, mappedCharset);
      fileChangeNeeded = fileChangeNeeded || changedCharset;
      info.setCharset(mappedCharset.getCharset());
    } else {
      info.setCharset(defaultCharset);
    }

    // 2. "end_of_line"
    if (config.getEndOfLine() != null) {
      logOperation(EditorConfigConstant.END_OF_LINE, config.getReadableEndOfLine());
      boolean changedLineEndings = new LineEndingOperation().operate(info);
      fileChangeNeeded = fileChangeNeeded || changedLineEndings;
    }

    // 3. "indent_size"
    if (config.getIndentSize() == -2 || config.getIndentSize() > -1) {
      logOperation(EditorConfigConstant.INDENT_SIZE, config.getIndentSize());
      boolean changedIndentSize = new IndentSizeOperation(primaryFile).changeIndentSize(config.getIndentSize());
      styleFlushNeeded = styleFlushNeeded || changedIndentSize;
    }

    // 4. "indent_style "
    if (config.getIndentStyle() != null) {
      logOperation(EditorConfigConstant.INDENT_STYLE, config.getIndentStyle());
      boolean changedIndentStyle = new IndentStyleOperation(primaryFile).changeIndentStyle(config.getIndentStyle());
      styleFlushNeeded = styleFlushNeeded || changedIndentStyle;
    }

    // 5. "insert_final_newline"
    if (config.isInsertFinalNewLine()) {
      logOperation(EditorConfigConstant.INSERT_FINAL_NEWLINE, config.isInsertFinalNewLine());
      boolean changedLineEndings = new FinalNewLineOperation().operate(info);
      fileChangeNeeded = fileChangeNeeded || changedLineEndings;
    }

    // 6. "tab_width"
    if ((config.getTabWidth() > -1)
            && (config.getIndentStyle() != null)
            && (config.getIndentStyle().equals(EditorConfigConstant.INDENT_STYLE_TAB))) {
      logOperation(EditorConfigConstant.TAB_WIDTH, config.getIndentStyle());
      boolean changedTabWidth = new TabWidthOperation(primaryFile).changeTabWidth(config.getTabWidth());
      fileChangeNeeded = fileChangeNeeded || changedTabWidth;
    }

    // 7. "trim_trailing_whitespace"
    if (config.isTrimTrailingWhiteSpace()) {
      logOperation(EditorConfigConstant.TRIM_TRAILING_WHITESPACE, config.isTrimTrailingWhiteSpace());
      boolean trimmedWhiteSpaces = new TrimTrailingWhiteSpaceOperation().operate(info);
      fileChangeNeeded = fileChangeNeeded || trimmedWhiteSpaces;
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

  private void flushStyles(FileInfo info) {
    try {
      Preferences codeStyle = CodeStylePreferences.get(info.getFileObject(), info.getFileObject().getMIMEType()).getPreferences();
      codeStyle.flush();
      if (info.isOpenedInEditor()) {
        updateChangesInEditorWindow(info);
      }
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

  /**
   * Takes the absolute path to a file which should be validated using the
   * EditorConfig file.
   *
   * @param filePath Example: C:\project\awesome\index.html
   * @return
   */
  private MappedEditorConfig readRulesForFile(String filePath) {
    return EditorConfigPropertyMapper.createEditorConfig(filePath);
  }

  private void updateChangesInEditorWindow(final FileInfo info) {
    LOG.log(Level.INFO, "Update changes in Editor window for: {0}", info.getPath());

        try {
          FileUtil.runAtomicAction((AtomicAction) new WriteEditorAction(info));
        } catch (IOException ex) {
          LOG.log(Level.SEVERE, ex.getMessage());
        }
  }

  /**
   * TODO: Make sure that a Reformat is done to write correct indentions.
   *
   * @param info
   */
  private void updateChangesInFile(FileInfo info) {
    LOG.log(Level.INFO, "Write content (with all rules applied) to file: {0}",
            info.getFileObject().getPath());

    try {
      FileUtil.runAtomicAction((AtomicAction) new WriteStringToFileAction(info));
    } catch (IOException ex) {
      LOG.log(Level.SEVERE, ex.getMessage());
    }
  }
}
