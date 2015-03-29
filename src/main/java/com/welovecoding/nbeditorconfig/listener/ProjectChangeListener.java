package com.welovecoding.nbeditorconfig.listener;

import static com.welovecoding.nbeditorconfig.config.LoggerSettings.LISTENER_LOG_LEVEL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileRenameEvent;

/**
 * http://bits.netbeans.org/dev/javadoc/
 */
public class ProjectChangeListener extends FileChangeAdapter {

  private static final Logger LOG = Logger.getLogger(ProjectChangeListener.class.getSimpleName());
  private final Project project;

  static {
    LOG.setLevel(LISTENER_LOG_LEVEL);
  }

  public ProjectChangeListener(Project project) {
    this.project = project;
  }

  @Override
  public void fileFolderCreated(FileEvent event) {
    super.fileFolderCreated(event);
    LOG.log(Level.INFO, "Created folder: {0}", event.getFile().getPath());
    ListenerAttacher.attachListeners(event.getFile(), project);
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
    LOG.log(Level.INFO, "fileDataCreated: {0}", event.getFile().getPath());
    ListenerAttacher.attachListeners(event.getFile(), project);
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

  @Override
  public void fileDeleted(FileEvent event) {
    super.fileDeleted(event);
    LOG.log(Level.FINE, "Deleted file: {0}", event.getFile().getPath());
  }

  @Override
  public void fileChanged(FileEvent event) {
    super.fileChanged(event);
    LOG.log(Level.FINE, "File content changed: {0}", event.getFile().getPath());
  }

}
