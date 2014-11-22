package com.welovecoding.netbeans.plugin.editorconfig.listener;

import com.welovecoding.netbeans.plugin.editorconfig.processor.EditorConfigProcessor;
import com.welovecoding.netbeans.plugin.editorconfig.processor.SmartSkip;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;

/**
 * http://bits.netbeans.org/dev/javadoc/
 */
public class FileChangeListener extends FileChangeAdapter {

  private static final Logger LOG = Logger.getLogger(FileChangeListener.class.getSimpleName());
  private final Project project;

  public FileChangeListener(Project project, FileObject editorConfigFileObject) {
    this.project = project;
    LOG.log(Level.INFO, "Attached FileChangeListener to: {0}", editorConfigFileObject.getParent().getPath());
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

  @Override
  public void fileChanged(FileEvent event) {
    super.fileChanged(event);
    String path = event.getFile().getPath();

    LOG.log(Level.INFO, "File content changed: {0}", path);

    if (applyRulesToFile(event)) {
      try {
        new EditorConfigProcessor().applyRulesToFile(DataObject.find(event.getFile()));
      } catch (DataObjectNotFoundException ex) {
        Exceptions.printStackTrace(ex);
      } catch (Exception ex) {
        Exceptions.printStackTrace(ex);
      }
    } else {
      LOG.log(Level.INFO, "Rules will not be applied to: {0}", path);
    }
  }

  private boolean applyRulesToFile(FileEvent event) {
    FileObject file = event.getFile();

    boolean applyRules = false;
    boolean isntFolder = !file.isFolder();
    boolean isUnexpected = !event.isExpected();
    boolean isntSkipped = !SmartSkip.skipFile(file);

    if (isUnexpected && isntFolder && isntSkipped) {
      applyRules = true;
    }

    return applyRules;
  }

  @Override
  public void fileFolderCreated(FileEvent event) {
    super.fileFolderCreated(event);
    LOG.log(Level.INFO, "Created folder: {0}", event.getFile().getPath());
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
    LOG.log(Level.INFO, "fileDataCreated: {0}", event.getFile().getPath());
  }

}
