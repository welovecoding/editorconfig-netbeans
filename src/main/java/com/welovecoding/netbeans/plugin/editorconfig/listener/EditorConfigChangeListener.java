package com.welovecoding.netbeans.plugin.editorconfig.listener;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;

/**
 * http://bits.netbeans.org/dev/javadoc/
 */
public class EditorConfigChangeListener extends FileChangeAdapter {

  private static final Logger LOG = Logger.getLogger(EditorConfigChangeListener.class.getName());
  private final Project project;
  private final FileObject editorConfigFileObject;
//  private final EditorConfigProcessor processor;
  private final FileChangeListener subsequentFilesListener;

  public EditorConfigChangeListener(Project project, FileObject editorConfigFileObject) {
    this.project = project;
    this.editorConfigFileObject = editorConfigFileObject;
//    this.processor = EditorConfigProcessor.getInstance();

    LOG.log(Level.INFO, "Attached EditorConfigChangeListener to: {0}", editorConfigFileObject.getPath());
    this.subsequentFilesListener = new FileChangeListener(project, editorConfigFileObject);
    editorConfigFileObject.getParent().addRecursiveListener(subsequentFilesListener);
  }

  @Override
  public void fileAttributeChanged(FileAttributeEvent event) {
    super.fileAttributeChanged(event);
    LOG.log(Level.INFO, "EDITORCONFIGCHANGELISTENER: Attribute changed: {0}", event.getFile().getPath());
  }

  @Override
  public void fileRenamed(FileRenameEvent event) {
    super.fileRenamed(event);
    LOG.log(Level.INFO, "EDITORCONFIGCHANGELISTENER: Renamed file: {0}", event.getFile().getPath());
  }

  @Override
  public void fileDeleted(FileEvent event) {
    super.fileDeleted(event);
    LOG.log(Level.INFO, "EDITORCONFIGCHANGELISTENER: Deleted file: {0}", event.getFile().getPath());
    //TODO processDeletedEditorConfig
    //TODO processDeletedFolderWhichMayContainsFoldersWithListeners -> remove them
  }

  @Override
  public void fileChanged(FileEvent event) {
    super.fileChanged(event);
    LOG.log(Level.INFO, "EDITORCONFIGCHANGELISTENER: EditorConfigs content changed: {0}", event.getFile().getPath());
//    processor.applyEditorConfigRules(event.getFile());

    for (FileObject fo : Collections.list(editorConfigFileObject.getParent().getChildren(true))) {
      LOG.log(Level.INFO, "Updating subsequent file: {0}", fo.getPath());
      subsequentFilesListener.fileChanged(new FileEvent(fo));
    }
  }

  @Override
  public void fileFolderCreated(FileEvent event) {
    super.fileFolderCreated(event);
    LOG.log(Level.INFO, "EDITORCONFIGCHANGELISTENER: Created folder: {0}", event.getFile().getPath());
    //TODO search for editor-configs and attach listeners
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
    LOG.log(Level.INFO, "EDITORCONFIGCHANGELISTENER: fileDataCreated: {0}", event.getFile().getPath());
  }

}
