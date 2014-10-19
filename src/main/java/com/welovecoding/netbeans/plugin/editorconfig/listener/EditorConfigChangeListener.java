package com.welovecoding.netbeans.plugin.editorconfig.listener;

import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigProperty;
import com.welovecoding.netbeans.plugin.editorconfig.parser.EditorConfigParser;
import com.welovecoding.netbeans.plugin.editorconfig.parser.EditorConfigParserException;
import com.welovecoding.netbeans.plugin.editorconfig.printer.EditorConfigPrinter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.text.StyledDocument;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.netbeans.api.editor.settings.SimpleValueNames;
import org.netbeans.api.project.Project;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;

public class EditorConfigChangeListener extends FileChangeAdapter {

  private static final Logger LOG = Logger.getLogger(EditorConfigChangeListener.class.getName());
  private Map<String, List<EditorConfigProperty>> editorConfig = new HashMap<>();
  private Project project;

  public EditorConfigChangeListener(Project project, FileObject editorConfigFileObject) {
    this.project = project;

    if (editorConfigFileObject != null) {
      readEditorConfigFile(editorConfigFileObject);
    }
  }

  private void readEditorConfigFile(FileObject editorConfigFileObject) {
    LOG.log(Level.INFO, "Parsing EditorConfig: {0}", editorConfigFileObject.getPath());
    EditorConfigParser parser = new EditorConfigParser();

    try {
      editorConfig = parser.parseConfig(FileUtil.toFile(editorConfigFileObject));
    } catch (EditorConfigParserException ex) {
      LOG.log(Level.SEVERE, "Exception parsing config file: {0}", ex.getMessage());
    }

    String config = EditorConfigPrinter.logConfig(editorConfig);
    LOG.log(Level.INFO, config);
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

    FileObject fileObject = event.getFile();
    DataObject dataObject;

    try {
      dataObject = DataObject.find(fileObject);

      for (Map.Entry<String, List<EditorConfigProperty>> entry : editorConfig.entrySet()) {
        List<EditorConfigProperty> properties = entry.getValue();

        if (fileObject.getPath().matches(entry.getKey())) {

          for (EditorConfigProperty property : properties) {
            String propertyKey = property.getKey();

            switch (propertyKey) {
              case EditorConfigConstant.END_OF_LINE:
                StyledDocument document = NbDocument.getDocument(dataObject);
                String lineEnding = property.getValue();

                LOG.log(Level.INFO, "Changing line ending for \"{0}\" to \"{1}\".",
                        new Object[]{propertyKey, lineEnding});

                if (document != null) {
                  switch (lineEnding) {
                    case EditorConfigConstant.LINE_FEED:
                      document.putProperty(BaseDocument.READ_LINE_SEPARATOR_PROP, BaseDocument.LS_LF);
                      break;
                    case EditorConfigConstant.CARRIAGE_RETURN:
                      document.putProperty(BaseDocument.READ_LINE_SEPARATOR_PROP, BaseDocument.LS_CR);
                      break;
                    case EditorConfigConstant.CARRIAGE_RETURN_LINE_FEED:
                      document.putProperty(BaseDocument.READ_LINE_SEPARATOR_PROP, BaseDocument.LS_CRLF);
                      break;
                  }

                } else {
                  System.out.println("ERROR ->> DOC IS NULL :/");
                }
                break;
            }
          }

        }

      }

      final DataFolder dof = dataObject.getFolder();

      if (fileObject.getNameExt().equals(".editorconfig")) {
//					file.addFileChangeListener(new FileChangeAdapter() {
//						@Override
//						public void fileChanged(FileEvent fe) {
//							applyEditorConfigToFolder(dof);
//						}
//					});
//					NotificationDisplayer.getDefault().notify(
//						".editorconfig",
//						ImageUtilities.loadImageIcon("org/netbeans/ec/editorconfig.png", false),
//						"Affected folder: " + dof.getName(),
//						null);
//					applyEditorConfigToFolder(dof);
      } else {
        // if it isn't an editorconfig that's been added,
        // apply the editorconfig file to it:
//					applyEditorConfigToFile(dobj);
      }
    } catch (DataObjectNotFoundException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  private void applyEditorConfigToFolder(DataFolder folder) {
    for (DataObject dataObject : folder.getChildren()) {
      applyEditorConfigRules(dataObject);
    }
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
    EditorConfigParser parser = new EditorConfigParser();
    String filePath = dataObject.getPrimaryFile().getPath();

    LOG.log(Level.INFO, "Apply rules for: {0}", filePath);

    for (String regEx : editorConfig.keySet()) {
      boolean isMatching = parser.matches(regEx, filePath);
      if (isMatching) {
        LOG.log(Level.INFO, "Matched \"{0}\" with \"{1}\".", new Object[]{
          filePath, regEx
        });

        List<EditorConfigProperty> properties = editorConfig.get(regEx);
        for (EditorConfigProperty property : properties) {

          String key = property.getKey();
          String value = property.getValue();

          LOG.log(Level.INFO, "  {0}: {1}", new Object[]{
            key, value
          });

          switch (key) {

            case EditorConfigConstant.INDENT_SIZE:
              int indentSize = Integer.valueOf(value);
              doIndentSize(dataObject.getPrimaryFile(), indentSize);
              break;

            case EditorConfigConstant.INSERT_FINAL_NEWLINE:
              boolean insertFinalNewline = Boolean.parseBoolean(value);
              doNewLine(dataObject.getPrimaryFile(), insertFinalNewline);
              break;

          }

        }
      }
    }
  }

  private void doIndentSize(FileObject file, int value) {
    LOG.log(Level.INFO, "    Set indent size for \"{0}\" to \"{1}\".", new Object[]{
      file.getPath(), value
    });

    Preferences codeStyle = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    codeStyle.putInt(SimpleValueNames.INDENT_SHIFT_WIDTH, value);

    try {
      codeStyle.flush();
    } catch (BackingStoreException ex) {
      LOG.log(Level.SEVERE, "Error while setting indent size: {0}", ex.getMessage());
    }
  }

  private void doNewLine(FileObject file, boolean insertFinalNewline) {
    String filePath = file.getPath();
    LOG.log(Level.INFO, "    Insert new line in \"{0}\": \"{1}\".", new Object[]{
      filePath, insertFinalNewline
    });

    if (file.canWrite() && insertFinalNewline) {

      if (isAlreadyNewLine(filePath)) {
        LOG.log(Level.INFO, "    File ends already with an empty line.");
      } else {
        LOG.log(Level.INFO, "    Inserting new line...");
        try (FileWriter fileWriter = new FileWriter(filePath, true);
                BufferedWriter bufferWritter = new BufferedWriter(fileWriter)) {
          bufferWritter.write(System.getProperty("line.separator", "\r\n"));
        } catch (IOException ex) {
          LOG.log(Level.SEVERE, "Cannot insert new line: {0}", ex.getMessage());
        }
      }

    }
  }

  private boolean isAlreadyNewLine(String filePath) {
    String lastLine = "";
    boolean isNewLine = false;

    try (ReversedLinesFileReader reader = new ReversedLinesFileReader(FileUtils.getFile(filePath));) {
      lastLine = reader.readLine();
    } catch (IOException ex) {
      LOG.log(Level.SEVERE, "Cannot read file: {0}", ex.getMessage());
    }

    if (lastLine.equals("\r") || lastLine.equals("\n") && lastLine.equals("\r\n")) {
      isNewLine = true;
    }

    return isNewLine;
  }

}
