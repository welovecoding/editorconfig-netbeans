package com.welovecoding.netbeans.plugin.editorconfig.listener;

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
public class FileChangeListener extends FileChangeAdapter {

  private static final Logger LOG = Logger.getLogger(FileChangeListener.class.getName());
  private final Project project;
  private final EditorConfigProcessor processor;

  public FileChangeListener(Project project, FileObject editorConfigFileObject) {
    this.project = project;
    this.processor = EditorConfigProcessor.getInstance();
    LOG.log(Level.INFO, "Attached FileChangeListener to: {0}", editorConfigFileObject.getParent().getPath());
  }

  @Override
  public void fileAttributeChanged(FileAttributeEvent event) {
    super.fileAttributeChanged(event);
    LOG.log(Level.INFO, "FILECHANGELISTENER: Attribute changed: {0}", event.getFile().getPath());
  }

  @Override
  public void fileRenamed(FileRenameEvent event) {
    super.fileRenamed(event);
    LOG.log(Level.INFO, "FILECHANGELISTENER: Renamed file: {0}", event.getFile().getPath());
  }

  @Override
  public void fileDeleted(FileEvent event) {
    super.fileDeleted(event);
    LOG.log(Level.INFO, "FILECHANGELISTENER: Deleted file: {0}", event.getFile().getPath());
    //TODO processDeletedEditorConfig
    //TODO processDeletedFolderWhichMayContainsFoldersWithListeners -> remove them
  }

  @Override
  public void fileChanged(FileEvent event) {
    super.fileChanged(event);
    LOG.log(Level.INFO, "FILECHANGELISTENER: File content changed: {0}", event.getFile().getPath());
    if (!event.getFile().isFolder()) {
      processor.applyEditorConfigRules(event.getFile());
    }
  }

  @Override
  public void fileFolderCreated(FileEvent event) {
    super.fileFolderCreated(event);
    LOG.log(Level.INFO, "FILECHANGELISTENER: Created folder: {0}", event.getFile().getPath());
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
    LOG.log(Level.INFO, "FILECHANGELISTENER: fileDataCreated: {0}", event.getFile().getPath());
  }

}
