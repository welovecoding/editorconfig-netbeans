package com.welovecoding.netbeans.plugin.editorconfig.listener;

import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigProperty;
import com.welovecoding.netbeans.plugin.editorconfig.parser.EditorConfigParser;
import com.welovecoding.netbeans.plugin.editorconfig.parser.EditorConfigParserException;
import com.welovecoding.netbeans.plugin.editorconfig.printer.EditorConfigPrinter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.text.StyledDocument;
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
  }

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

  private void processDeletedEditorConfig() {

  }

  @Override
  public void fileChanged(FileEvent event) {
    super.fileChanged(event);
    applyEditorConfigRules(event);
    LOG.log(Level.INFO, "File content changed: {0}", event.getFile().getPath());
  }

  private void applyEditorConfigRules(FileEvent event) {

    LOG.log(Level.INFO, "Let the fun begin... {0}", event.getFile().getPath());

    EditorConfigParser parser = new EditorConfigParser();
    String filePath = event.getFile().getPath();

    FileObject fileObject = event.getFile();
    DataObject dataObject = null;

    try {
      dataObject = DataObject.find(fileObject);
    } catch (DataObjectNotFoundException ex) {
      LOG.log(Level.SEVERE, "Error accessing file object: {0}", ex.getMessage());
    }

    if (dataObject != null) {
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

            LOG.log(Level.INFO, "\t{0}: {1}", new Object[]{
              key, value
            });

            switch (key) {
              case EditorConfigConstant.INDENT_SIZE:
                int indentSize = Integer.valueOf(value);
                doIndentSize(dataObject.getPrimaryFile(), indentSize);
                break;
            }

          }
        }
      }
    }
  }

  /**
   * Method is triggered when content has changed and it's possible to display
   * content in NetBeans. Method is also triggered when project will be opened.
   *
   * @param event
   */
  @Override
  public void fileDataCreated(FileEvent event) {
    super.fileDataCreated(event);
    LOG.log(Level.INFO, "Here starts the fun... {0}", event.getFile().getPath());

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

  @Override
  public void fileFolderCreated(FileEvent event) {
    super.fileFolderCreated(event);
    LOG.log(Level.INFO, "Created folder: {0}", event.getFile().getPath());
    //TODO search for editor-configs and attach listeners
  }

  private void applyEditorConfigToFolder(DataFolder folder) {
    for (DataObject file : folder.getChildren()) {
      applyEditorConfigToFile(file);
    }
  }

  private void applyEditorConfigToFile(DataObject file) throws NumberFormatException {
    LOG.log(Level.INFO, "applyEditorConfigToFile: {0}", file.getPrimaryFile().getPath());

//			EditorConfig ec;
//			try {
//				ec = new EditorConfig();
//				List<EditorConfig.OutPair> l = null;
//				l = ec.getProperties(dobj.getPrimaryFile().getPath());
//				for (int i = 0; i < l.size(); ++i) {
//					if (l.get(i).getKey().equals("indent_size")) {
//						doIndentSize(
//							dobj.getPrimaryFile(),
//							Integer.valueOf(l.get(i).getVal()));
//					}
//				}
//			} catch (PythonException ex) {
//				Exceptions.printStackTrace(ex);
//			} catch (EditorConfigException ex) {
//				Exceptions.printStackTrace(ex);
//			}
  }

  private void doIndentSize(FileObject file, int value) {
    LOG.log(Level.INFO, "Set indent size to \"{0}\" for \"{1}\".", new Object[]{
      value, file.getPath()
    });

    Preferences codeStyle = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    codeStyle.putInt(SimpleValueNames.INDENT_SHIFT_WIDTH, value);

    try {
      codeStyle.flush();
    } catch (BackingStoreException ex) {
      LOG.log(Level.SEVERE, "Error while setting indent size: {0}", ex.getMessage());
    }
  }

}
