package com.welovecoding.netbeans.plugin.editorconfig.listener;

import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigProperty;
import com.welovecoding.netbeans.plugin.editorconfig.parser.EditorConfigParser;
import com.welovecoding.netbeans.plugin.editorconfig.parser.EditorConfigParserException;
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
  private Project project;
  private Map<String, List<EditorConfigProperty>> editorConfig = new HashMap<>();

  public EditorConfigChangeListener(Project project, FileObject editorConfigFileObject) {
    this.project = project;

    if (editorConfigFileObject != null) {
      EditorConfigParser parser = new EditorConfigParser();
      try {
        editorConfig = parser.parseConfig(FileUtil.toFile(editorConfigFileObject));
      } catch (EditorConfigParserException ex) {
        Exceptions.printStackTrace(ex);
      }

      for (Map.Entry<String, List<EditorConfigProperty>> entry : editorConfig.entrySet()) {
        String key = entry.getKey();
        List<EditorConfigProperty> value = entry.getValue();
        System.out.println("Key: " + key);
        for (EditorConfigProperty editorConfigProperty : value) {
          System.out.println(editorConfigProperty.getKey() + " : " + editorConfigProperty.getValue());
        }
      }
    }

  }

  @Override
  public void fileAttributeChanged(FileAttributeEvent fe) {
    super.fileAttributeChanged(fe);
    System.out.println("ECChangeListener :: fileAttributeChanged \n" + fe.getFile().getPath());
  }

  @Override
  public void fileRenamed(FileRenameEvent fe) {
    super.fileRenamed(fe);
    System.out.println("ECChangeListener :: fileRenamed \n" + fe.getFile().getPath());
  }

  @Override
  public void fileDeleted(FileEvent fe) {
    super.fileDeleted(fe);
    System.out.println("ECChangeListener :: fileDeleted \n" + fe.getFile().getPath());
    //TODO processDeletedEditorConfig
    //TODO processDeletedFolderWhichMayContainsFoldersWithListeners -> remove them
  }

  private void processDeletedEditorConfig() {

  }

  /**
   * method is triggered when content has changed
   * <p>
   * @param fe
   */
  @Override
  public void fileChanged(FileEvent fe) {
    super.fileChanged(fe);
    System.out.println("ECChangeListener :: fileChanged \n" + fe.getFile().getPath());
  }

  /**
   * Method is triggered when content has changed and its possible to display
   * content in netbeans. Method is also triggered when project will be opened.
   * <p>
   * @param event
   */
  @Override
  public void fileDataCreated(FileEvent event) {
    super.fileDataCreated(event);
    System.out.println("ECChangeListener :: fileDataCreated \n" + event.getFile().getPath());

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
  public void fileFolderCreated(FileEvent fe) {
    super.fileFolderCreated(fe);
    System.out.println("ECChangeListener :: fileFolderCreated \n" + fe.getFile().getPath());
    //TODO search for editor-configs and attach listeners
  }

  private void applyEditorConfigToFolder(DataFolder dof) {
    for (DataObject dobj : dof.getChildren()) {
      applyEditorConfigToFile(dobj);
    }
  }

  private void applyEditorConfigToFile(DataObject dobj) throws NumberFormatException {
    System.out.println("APPLY");

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
  public static final String indentSize = SimpleValueNames.INDENT_SHIFT_WIDTH;

  private void doIndentSize(FileObject file, int value) {
    Preferences prefs = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    prefs.putInt(indentSize, value);
    try {
      prefs.flush();
    } catch (BackingStoreException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

}
