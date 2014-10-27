package com.welovecoding.netbeans.plugin.editorconfig.listener;

import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import com.welovecoding.netbeans.plugin.editorconfig.util.FileAttributes;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
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

    try {
      rules = ec.getProperties(filePath);
    } catch (EditorConfigException ex) {
      LOG.log(Level.SEVERE, ex.getMessage());
    }

    for (int i = 0; i < rules.size(); ++i) {
      EditorConfig.OutPair rule = rules.get(i);
      String key = rule.getKey().toLowerCase();
      String value = rule.getVal().toLowerCase();

      LOG.log(Level.INFO, "{0}Found rule \"{1}\" with value: {2}", new Object[]{TAB_1, key, value});

      switch (key) {
        case EditorConfigConstant.CHARSET:
          doCharset(dataObject, value);
          break;
        case EditorConfigConstant.END_OF_LINE:
          doEndOfLine(dataObject, value);
          break;
        case EditorConfigConstant.INDENT_SIZE:
          doIndentSize(dataObject.getPrimaryFile(), value);
          break;
        case EditorConfigConstant.INSERT_FINAL_NEWLINE:
          doInsertFinalNewLine(dataObject.getPrimaryFile(), value);
          break;
      }
    }
  }

  private void doIndentSize(FileObject file, String value) {
    int indentSize = Integer.valueOf(value);

    LOG.log(Level.INFO, "{0}Set indent size to \"{1}\".", new Object[]{TAB_2, indentSize});

    Preferences codeStyle = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    int currentValue = codeStyle.getInt(SimpleValueNames.INDENT_SHIFT_WIDTH, -1);

    if (currentValue != indentSize) {
      codeStyle.putInt(SimpleValueNames.INDENT_SHIFT_WIDTH, indentSize);
      try {
        codeStyle.flush();
      } catch (BackingStoreException ex) {
        LOG.log(Level.SEVERE, "Error while setting indent size: {0}", ex.getMessage());
      }
    } else {
      LOG.log(Level.INFO, "{0}Change not needed. Value is already: {1}", new Object[]{TAB_2, currentValue});
    }
  }

  private void doInsertFinalNewLine(FileObject file, String value) {
    boolean needsFinalNewLine = Boolean.parseBoolean(value);

    String filePath = file.getPath();

    LOG.log(Level.INFO, "{0}Insert new line: {1}", new Object[]{TAB_2, needsFinalNewLine});

    if (file.canWrite() && needsFinalNewLine) {

      if (FileAttributes.hasFinalNewLine(filePath)) {
        LOG.log(Level.INFO, "{0}File ends already with a new line.", new Object[]{TAB_2});
      } else {
        LOG.log(Level.INFO, "{0}Inserting new line...", new Object[]{TAB_2});
        try (FileWriter fileWriter = new FileWriter(filePath, true);
                BufferedWriter bufferWritter = new BufferedWriter(fileWriter)) {
          bufferWritter.write(System.getProperty("line.separator", "\r\n"));
        } catch (IOException ex) {
          LOG.log(Level.SEVERE, "Cannot insert new line: {0}", ex.getMessage());
        }
      }

    }
  }

  private void doEndOfLine(DataObject dataObject, String value) {
    String normalizedLineEnding = normalizeLineEnding(value);

    LOG.log(Level.INFO, "{0}Change line endings to \"{1}\".", new Object[]{TAB_2, value});

    StyledDocument document = NbDocument.getDocument(dataObject);

    if (!document.getProperty(BaseDocument.READ_LINE_SEPARATOR_PROP).equals(normalizedLineEnding)) {
      document.putProperty(BaseDocument.READ_LINE_SEPARATOR_PROP, normalizedLineEnding);
    } else {
      LOG.log(Level.INFO, "{0}Change not needed. Line endings are already: {1}", new Object[]{TAB_2, value});
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

  private void doCharset(DataObject dataObject, String ecCharset) {
    Charset requestedCharset = convertCharset(ecCharset);

    LOG.log(Level.INFO, "{0}Set encoding to: \"{1}\".", new Object[]{TAB_2, requestedCharset.name()});

    FileObject fo = dataObject.getPrimaryFile();
    Charset currentCharset = getCharset(fo);

    if (currentCharset.name().equals(requestedCharset.name())) {
      LOG.log(Level.INFO, "{0}Change not needed. Encoding is already: \"{1}\".", new Object[]{TAB_2, currentCharset.name()});
    } else {
      LOG.log(Level.INFO, "{0}Rewriting file from encoding \"{1}\" to \"{2}\".",
              new Object[]{TAB_2, currentCharset.name(), requestedCharset.name()});

      ArrayList<String> content = readContentFromFileObject(fo, currentCharset);
      boolean wasWritten = writeContentToFileObject(fo, requestedCharset, content);

      if (wasWritten) {
        LOG.log(Level.INFO, "{0}Successfully changed encoding to: \"{1}\".", new Object[]{TAB_2, requestedCharset.name()});
      }
    }
  }

  private Charset convertCharset(String editorConfigCharset) {
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
    String fileEncoding = String.valueOf(fo.getAttribute("welovecoding.file.encoding"));

    if (fileEncoding.equals("null")) {
      Charset currentCharset = FileEncodingQuery.getEncoding(fo);
      fileEncoding = currentCharset.name();
    }

    return Charset.forName(fileEncoding);
  }

  private ArrayList<String> readContentFromFileObject(FileObject fo, Charset charset) {
    ArrayList<String> lines = new ArrayList<>();
    String line;

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(fo.getInputStream(), charset))) {
      while ((line = reader.readLine()) != null) {
        lines.add(line);
        // TODO: Use line separator from EditorConfig
        lines.add(System.getProperty("line.separator", "\r\n"));
      }

      // Remove last line-break
      lines.remove(lines.size() - 1);
    } catch (IOException ex) {
      LOG.log(Level.SEVERE, ex.getMessage());
    }

    return lines;
  }

  // TODO: Convert characters
  // http://tripoverit.blogspot.de/2007/04/javas-utf-8-and-unicode-writing-is.html
  private boolean writeContentToFileObject(FileObject fo, Charset charset, List<String> lines) {
    boolean wasWritten = false;
    FileLock lock = null;

    try {
      fo.lock();
      Files.write(Paths.get(fo.toURI()), lines, charset);
      fo.setAttribute("welovecoding.file.encoding", charset.name());
      wasWritten = true;
    } catch (IOException ex) {
      LOG.log(Level.INFO, "{0}Error writing file with charset \"{1}\": {2}",
              new Object[]{TAB_2, charset, ex.getMessage()});
    } finally {
      if (lock != null) {
        lock.releaseLock();
      }
    }

    return wasWritten;
  }
}
