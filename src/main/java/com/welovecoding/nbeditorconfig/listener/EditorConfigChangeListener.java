package com.welovecoding.nbeditorconfig.listener;

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
 * This kind of listener is attached to editorconfig files within a project.
 * When this listener is attached to an editorconfig a FileChangeListener will
 * be attached to all files in the folder with the editorconfig and subsequent
 * ones. http://bits.netbeans.org/dev/javadoc/
 */
public class EditorConfigChangeListener extends FileChangeAdapter {

  private static final Logger LOG = Logger.getLogger(EditorConfigChangeListener.class.getSimpleName());
  private final Project project;
  private final FileObject editorConfigFileObject;
  private final FileChangeListener subsequentFilesListener;

  static {
    LOG.setLevel(Level.INFO);
  }

  public EditorConfigChangeListener(Project project, FileObject editorConfigFileObject) {
    this.project = project;
    this.editorConfigFileObject = editorConfigFileObject;

    LOG.log(Level.INFO, "Attached EditorConfigChangeListener to: {0}", editorConfigFileObject.getPath());
    this.subsequentFilesListener = new FileChangeListener(project, editorConfigFileObject);
    editorConfigFileObject.getParent().addRecursiveListener(subsequentFilesListener);

    // immediately apply editorconfig
    propagateChanges();
  }

  @Override
  public void fileDeleted(FileEvent event) {
    super.fileDeleted(event);
    LOG.log(Level.INFO, "Deleted file: {0}", event.getFile().getPath());
    event.getFile().getParent().removeRecursiveListener(subsequentFilesListener);
    event.getFile().removeFileChangeListener(this);
  }

  @Override
  public void fileChanged(FileEvent event) {
    super.fileChanged(event);
    LOG.log(Level.INFO, "EditorConfigs content changed: {0}", event.getFile().getPath());

    propagateChanges();
  }

  private void propagateChanges() {
    for (FileObject fo : Collections.list(editorConfigFileObject.getParent().getChildren(true))) {
      LOG.log(Level.INFO, "Updating subsequent file: {0}", fo.getPath());
      subsequentFilesListener.fileChanged(new FileEvent(fo));
    }
  }

  @Override
  public void fileFolderCreated(FileEvent event) {
    super.fileFolderCreated(event);
    LOG.log(Level.FINE, "Created folder: {0}", event.getFile().getPath());
    //TODO search for editor-configs and attach listeners
  }

  /**
   * Method is triggered when content has changed and it's possible to display
   * content in NetBeans. Method is also triggered when project will be opened.
   *
   * @param event Event for listening on filesystem changes
   */
  @Override
  public void fileDataCreated(FileEvent event) {
    super.fileDataCreated(event);
    LOG.log(Level.FINE, "fileDataCreated: {0}", event.getFile().getPath());
  }

  @Override
  public void fileAttributeChanged(FileAttributeEvent event) {
    super.fileAttributeChanged(event);
    LOG.log(Level.FINE, "Attribute changed: {0}", event.getFile().getPath());
  }

  @Override
  public void fileRenamed(FileRenameEvent event) {
    super.fileRenamed(event);
    LOG.log(Level.FINE, "Renamed file: {0}", event.getFile().getPath());
  }
}
