package com.welovecoding.netbeans.plugin.editorconfig.listener;

import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import com.welovecoding.netbeans.plugin.editorconfig.util.FileAttributes;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import javax.swing.text.StyledDocument;
import org.editorconfig.core.EditorConfig;
import org.editorconfig.core.EditorConfigException;
import org.netbeans.api.editor.settings.SimpleValueNames;
import org.netbeans.api.project.Project;
import org.netbeans.api.queries.FileEncodingQuery;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;

/**
 * http://bits.netbeans.org/dev/javadoc/
 */
public class EditorConfigChangeListener extends FileChangeAdapter {

  private static final Logger LOG = Logger.getLogger(EditorConfigChangeListener.class.getName());
  private Project project;

  private final String TAB_1 = "  ";
  private final String TAB_2 = "    ";
  private final String TAB_3 = "      ";

  public EditorConfigChangeListener(Project project, FileObject editorConfigFileObject) {
    this.project = project;
  }

  // <editor-fold defaultstate="collapsed" desc="Overrides">
  @Override
  public void fileAttributeChanged(FileAttributeEvent event) {
    super.fileAttributeChanged(event);
    LOG.log(Level.INFO, "Attribute changed: {0}", event.getFile().getPath());
  }

  @Override
  public void fileRenamed(FileRenameEvent event) {
    super.fileRenamed(event);
    LOG.log(Level.INFO, "Renamed file: {0}", event.getFile().getPath());
  }

  @Override
  public void fileDeleted(FileEvent event) {
    super.fileDeleted(event);
    LOG.log(Level.INFO, "Deleted file: {0}", event.getFile().getPath());
    //TODO processDeletedEditorConfig
    //TODO processDeletedFolderWhichMayContainsFoldersWithListeners -> remove them
  }

  @Override
  public void fileChanged(FileEvent event) {
    super.fileChanged(event);
    applyEditorConfigRules(event.getFile());
    LOG.log(Level.INFO, "File content changed: {0}", event.getFile().getPath());
  }

  @Override
  public void fileFolderCreated(FileEvent event) {
    super.fileFolderCreated(event);
    LOG.log(Level.INFO, "Created folder: {0}", event.getFile().getPath());
    //TODO search for editor-configs and attach listeners
  }
  // </editor-fold>  

  /**
   * Method is triggered when content has changed and it's possible to display
   * content in NetBeans. Method is also triggered when project will be opened.
   *
   * @param event
   */
  @Override
  public void fileDataCreated(FileEvent event) {
    super.fileDataCreated(event);
    LOG.log(Level.INFO, "fileDataCreated: {0}", event.getFile().getPath());
  }

  private void applyEditorConfigRules(FileObject fileObject) {
    DataObject dataObject = null;

    try {
      dataObject = DataObject.find(fileObject);
    } catch (DataObjectNotFoundException ex) {
      LOG.log(Level.SEVERE, "Error accessing file object: {0}", ex.getMessage());
    }

    if (dataObject != null) {
      applyEditorConfigRules(dataObject);
    }
  }

  private void applyEditorConfigRules(DataObject dataObject) {
    String filePath = dataObject.getPrimaryFile().getPath();

    LOG.log(Level.INFO, "Apply rules for: {0}", filePath);

    EditorConfig ec = new EditorConfig(".editorconfig", EditorConfig.VERSION);
    List<EditorConfig.OutPair> rules = new ArrayList<>();

    HashMap<String, String> keyedRules = new HashMap<>();
    for (EditorConfig.OutPair rule : rules) {
      keyedRules.put(rule.getKey().toLowerCase(), rule.getVal().toLowerCase());
    }

    try {
      rules = ec.getProperties(filePath);
    } catch (EditorConfigException ex) {
      LOG.log(Level.SEVERE, ex.getMessage());
    }

    FileObject primaryFile = dataObject.getPrimaryFile();
    boolean changedStyle = false;

    for (int i = 0; i < rules.size(); ++i) {
      EditorConfig.OutPair rule = rules.get(i);
      String key = rule.getKey().toLowerCase();
      String value = rule.getVal().toLowerCase();

      LOG.log(Level.INFO, "{0}Found rule \"{1}\" with value: {2}", new Object[]{TAB_1, key, value});

      switch (key) {
        case EditorConfigConstant.CHARSET:
          String lineEnding = keyedRules.get(EditorConfigConstant.END_OF_LINE);
          if (lineEnding == null) {
            lineEnding = System.getProperty("line.separator", "\r\n");
          } else {
            lineEnding = normalizeLineEnding(lineEnding);
          }
          changedStyle = changedStyle || doCharset(dataObject, value, lineEnding);
          break;
        case EditorConfigConstant.END_OF_LINE:
          changedStyle = changedStyle || doEndOfLine(dataObject, value);
          break;
        case EditorConfigConstant.INDENT_SIZE:
          changedStyle = changedStyle || doIndentSize(primaryFile, value);
          break;
        case EditorConfigConstant.INDENT_STYLE:
          changedStyle = changedStyle || doIndentStyle(primaryFile, value);
          break;
        case EditorConfigConstant.INSERT_FINAL_NEWLINE:
          changedStyle = changedStyle || doInsertFinalNewLine(primaryFile, value);
          break;
      }
    }

    Preferences codeStyle = CodeStylePreferences.get(primaryFile, primaryFile.getMIMEType()).getPreferences();

    if (changedStyle) {
      try {
        codeStyle.flush();
      } catch (BackingStoreException ex) {
        LOG.log(Level.SEVERE, "Error applying code style: {0}", ex.getMessage());
      }
    }
  }

  private boolean doIndentStyle(FileObject file, String value) {
    LOG.log(Level.INFO, "{0}Set indent style to \"{1}\".", new Object[]{TAB_2, value});
    boolean expandTabs = false;
    if (value.equals(EditorConfigConstant.INDENT_STYLE_SPACE)) {
      expandTabs = true;
    }

    Preferences codeStyle = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    boolean currentValue = codeStyle.getBoolean(SimpleValueNames.EXPAND_TABS, false);

    if (currentValue != expandTabs) {
      codeStyle.putBoolean(SimpleValueNames.EXPAND_TABS, expandTabs);
      LOG.log(Level.INFO, "{0}Action: Changed indent style to space? {1}", new Object[]{TAB_2, expandTabs});
      return true;
    } else {
      LOG.log(Level.INFO, "{0}Action not needed: Indent style is already set to spaces \"{1}\".", new Object[]{TAB_2, currentValue});
      return false;
    }
  }

  private boolean doIndentSize(FileObject file, String value) {
    int indentSize = Integer.valueOf(value);

    LOG.log(Level.INFO, "{0}Set indent size to \"{1}\".", new Object[]{TAB_2, indentSize});

    Preferences codeStyle = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    int currentValue = codeStyle.getInt(SimpleValueNames.INDENT_SHIFT_WIDTH, -1);

    if (currentValue != indentSize) {
      codeStyle.putInt(SimpleValueNames.INDENT_SHIFT_WIDTH, indentSize);
      LOG.log(Level.INFO, "{0}Action: Change indent size to \"{1}\".", new Object[]{TAB_2, indentSize});
      return true;
    } else {
      LOG.log(Level.INFO, "{0}Action not needed: Value is already \"{1}\".", new Object[]{TAB_2, currentValue});
      return false;
    }
  }

  private boolean doInsertFinalNewLine(FileObject file, String value) {
    boolean wasChanged = false;
    boolean needsFinalNewLine = Boolean.parseBoolean(value);

    String filePath = file.getPath();

    LOG.log(Level.INFO, "{0}Insert new line? {1}", new Object[]{TAB_2, needsFinalNewLine});

    if (file.canWrite() && needsFinalNewLine) {

      if (FileAttributes.hasFinalNewLine(filePath)) {
        LOG.log(Level.INFO, "{0}Action not needed: File ends already with a new line.", new Object[]{TAB_2});
      } else {
        LOG.log(Level.INFO, "{0}Action: Inserting new line...", new Object[]{TAB_2});
        try (FileWriter fileWriter = new FileWriter(filePath, true);
                BufferedWriter bufferWritter = new BufferedWriter(fileWriter)) {
          // TODO: Take line separator from EditorConfig (if present)
          bufferWritter.newLine();
          wasChanged = true;
        } catch (IOException ex) {
          LOG.log(Level.SEVERE, "{0}Action: Cannot insert new line: {1}", new Object[]{TAB_2, ex.getMessage()});
        }
      }

    }

    return wasChanged;
  }

  private boolean doEndOfLine(DataObject dataObject, String value) {
    LOG.log(Level.INFO, "{0}Change line endings to \"{1}\".", new Object[]{TAB_2, value});

    String normalizedLineEnding = normalizeLineEnding(value);
    StyledDocument document = NbDocument.getDocument(dataObject);

    if (!document.getProperty(BaseDocument.READ_LINE_SEPARATOR_PROP).equals(normalizedLineEnding)) {
      document.putProperty(BaseDocument.READ_LINE_SEPARATOR_PROP, normalizedLineEnding);
      LOG.log(Level.INFO, "{0}Action: Changed line endings to \"{1}\".", new Object[]{TAB_2, value});
      return true;
    } else {
      LOG.log(Level.INFO, "{0}Action not needed: Line endings are already \"{1}\".", new Object[]{TAB_2, value});
      return false;
    }
  }

  /**
   * Turns line ending settings into file line endings. Example: lnrf -> \r\n
   *
   * @param lineEnding
   * @return
   */
  private String normalizeLineEnding(String lineEnding) {
    String normalizedLineEnding = System.getProperty("line.separator", "\r\n");

    switch (lineEnding) {
      case EditorConfigConstant.END_OF_LINE_LF:
        normalizedLineEnding = BaseDocument.LS_LF;
        break;
      case EditorConfigConstant.END_OF_LINE_CR:
        normalizedLineEnding = BaseDocument.LS_CR;
        break;
      case EditorConfigConstant.END_OF_LINE_CRLF:
        normalizedLineEnding = BaseDocument.LS_CRLF;
        break;
    }

    return normalizedLineEnding;
  }

  private boolean doCharset(DataObject dataObject, String ecCharset, final String lineEnding) {
    Charset requestedCharset = mapCharset(ecCharset);
    boolean wasChanged = false;

    LOG.log(Level.INFO, "{0}Set encoding to: \"{1}\".", new Object[]{TAB_2, requestedCharset.name()});

    FileObject fo = dataObject.getPrimaryFile();
    Charset currentCharset = getCharset(fo);

    if (currentCharset.name().equals(requestedCharset.name())) {
      LOG.log(Level.INFO, "{0}Action not needed: Encoding is already \"{1}\".", new Object[]{TAB_2, currentCharset.name()});
    } else {
      LOG.log(Level.INFO, "{0}Action: Rewriting file from encoding \"{1}\" to \"{2}\".",
              new Object[]{TAB_2, currentCharset.name(), requestedCharset.name()});

      final String content = new ReadFileTask(fo) {

        @Override
        public String apply(BufferedReader reader) {
          return reader.lines().collect(Collectors.joining(lineEnding));
        }
      }.call();

      boolean wasWritten = writeFile(new WriteFileTask(fo) {

        @Override
        public void apply(OutputStreamWriter writer) {
          try {
            writer.write(content);
          } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
          }
        }
      });

      if (wasWritten) {
        LOG.log(Level.INFO, "{0}Action: Successfully changed encoding to \"{1}\".", new Object[]{TAB_2, requestedCharset.name()});
        wasChanged = true;
      }
    }

    return wasChanged;
  }

  private Charset mapCharset(String editorConfigCharset) {
    Charset javaCharset;

    switch (editorConfigCharset) {
      case EditorConfigConstant.CHARSET_LATIN_1:
        javaCharset = StandardCharsets.ISO_8859_1;
        break;
      case EditorConfigConstant.CHARSET_UTF_16_BE:
        javaCharset = StandardCharsets.UTF_16BE;
        break;
      case EditorConfigConstant.CHARSET_UTF_16_LE:
        javaCharset = StandardCharsets.UTF_16LE;
        break;
      default:
        javaCharset = StandardCharsets.UTF_8;
        break;
    }

    return javaCharset;
  }

  /**
   * TODO: It looks like "FileEncodingQuery.getEncoding" always returns "UTF-8".
   *
   * Even if the charset of that file is already UTF-16LE. Therefore we should
   * change our charset lookup. After the charset has been changed by us, we add
   * a file attribute which helps us to detect the charset in future.
   *
   * Maybe we should use a CharsetDetector:
   * http://userguide.icu-project.org/conversion/detection
   *
   * @param fo
   * @return
   */
  private Charset getCharset(FileObject fo) {
    Object fileEncoding = fo.getAttribute("welovecoding.file.encoding");

    if (fileEncoding == null) {
      Charset currentCharset = FileEncodingQuery.getEncoding(fo);
      fileEncoding = currentCharset.name();
    }

    return Charset.forName((String) fileEncoding);
  }

  private ArrayList<String> readContentFromFileObject(FileObject fo, Charset charset, String lineEnding) {
    ArrayList<String> lines = new ArrayList<>();
    String line;

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(fo.getInputStream(), charset))) {
      while ((line = reader.readLine()) != null) {
        lines.add(line);
        lines.add(lineEnding);
      }

      // Remove last line-break
      lines.remove(lines.size() - 1);
    } catch (IOException ex) {
      LOG.log(Level.SEVERE, ex.getMessage());
    }

    return lines;
  }

  // TODO: @micha
  private boolean writeContentToFileObject(FileObject fo, Charset charset, List<String> lines) {
    boolean wasWritten = false;
    FileLock lock = FileLock.NONE;

    try (PrintWriter output = new PrintWriter(fo.getOutputStream(lock))) {

    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return wasWritten;
  }

  private boolean writeFile(WriteFileTask task) {
    task.run();
    return true;
  }

}
