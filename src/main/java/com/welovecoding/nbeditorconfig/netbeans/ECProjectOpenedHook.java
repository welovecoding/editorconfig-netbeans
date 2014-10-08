package com.welovecoding.nbeditorconfig.netbeans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.text.StyledDocument;
import org.editorconfig.netbeans.model.EditorConfigConstant;
import org.editorconfig.netbeans.model.EditorConfigProperty;
import org.editorconfig.netbeans.parser.EditorConfigParser;
import org.editorconfig.netbeans.parser.EditorConfigParserException;
import org.netbeans.api.editor.settings.SimpleValueNames;
import org.netbeans.api.project.Project;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.netbeans.spi.project.LookupProvider;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.openide.filesystems.*;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Michael Koppen
 */
@LookupProvider.Registration(projectType = {
  "org-netbeans-modules-java-j2seproject",
  "org-netbeans-modules-web-project",
  "org.netbeans.modules.web.clientproject",
  "org-netbeans-modules-web-clientproject",
  "org-netbeans-modules-maven",
  "org-netbeans-modules-apisupport-project"}
)
/**
 * Listener for newly opened Projects.
 */
public class ECProjectOpenedHook implements LookupProvider {

  private static final Logger LOG = Logger.getLogger(ECProjectOpenedHook.class.getName());
  private final Map<FileObject, ECChangeListener> listeners = new HashMap<>();

  @Override
  public Lookup createAdditionalLookup(Lookup lookup) {
    final Project project = lookup.lookup(Project.class);
    String projectName = project.getProjectDirectory().getName();

    LOG.log(Level.INFO, "Setup hooks for: {0}", projectName);

    return Lookups.fixed(new ProjectOpenedHook() {
      @Override
      protected void projectOpened() {
        FileObject projectFileObject = project.getProjectDirectory();
        LOG.log(Level.INFO, "Opened project: {0}", projectFileObject.getName());

        attachListeners(projectFileObject, project);
        LOG.log(Level.INFO, "Attached listeners to project: {0}", projectFileObject.getName());
        // ECProjectPreferences.setLineEnding(BaseDocument.LS_CRLF, project);
      }

      @Override
      protected void projectClosed() {
        LOG.log(Level.INFO, "Closed project: {0}", project.getProjectDirectory().getName());
      }
    });
  }

  /**
   * recursively attaches recursive listeners to folders containing a
   * .editorconfig file.
   * <p>
   * @param root
   * @param p
   */
  private void attachListeners(FileObject root, Project p) {
    if (p.getProjectDirectory().equals(root)) {
      System.out.println("ALWAYS ATTACHING LISTENER TO PROJECT ROOT");
      ECChangeListener rootListener = new ECChangeListener(p, null);
      root.addRecursiveListener(rootListener);
      listeners.put(root, rootListener);
    }

    for (FileObject file : root.getChildren()) {
      // LOG.log(Level.INFO, "Inspecting file: {0}", file.getPath());
      if (file.isFolder()) {
        attachListeners(file, p);
      } else if (file.getName().equals(".editorconfig")) {
        LOG.log(Level.INFO, "Found EditorConfig file: {0}", file.getPath());

        ECChangeListener newListener = new ECChangeListener(p, file);
        file.getParent().addRecursiveListener(newListener);
        listeners.put(file.getParent(), newListener);
        System.out.println("ATTACHED LISTENER TO " + file.getParent().getPath());
      }
    }
  }

  private class ECChangeListener extends FileChangeAdapter {

    private Project project;
    private Map<String, List<EditorConfigProperty>> editorConfig = new HashMap<>();

    public ECChangeListener(Project project, FileObject editorConfigFileObject) {
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
     * content in netbeans. Method is also triggered when project will be
     * opened.
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
}
