package com.welovecoding.nbeditorconfig.listener;

import static com.welovecoding.nbeditorconfig.config.LoggerSettings.LISTENER_LOG_LEVEL;
import com.welovecoding.nbeditorconfig.processor.EditorConfigProcessor;
import com.welovecoding.nbeditorconfig.processor.SmartSkip;
import com.welovecoding.nbeditorconfig.processor.WriteEditorAction;
import com.welovecoding.nbeditorconfig.processor.WriteStringToFileAction;
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

  private static final Logger LOG = Logger.getLogger(FileChangeListener.class.getName());
  private final Project project;
  private final FileObject editorConfigFileObject;
  private final EditorConfigProcessor editorConfigProcessor = new EditorConfigProcessor();

  static {
    LOG.setLevel(LISTENER_LOG_LEVEL);
  }

  public FileChangeListener(Project project, FileObject editorConfigFileObject) {
    this.project = project;
    this.editorConfigFileObject = editorConfigFileObject;
    LOG.log(Level.INFO, "[EC for {0}] Attached FileChangeListener to: {1}", new Object[]{editorConfigFileObject.getPath(), editorConfigFileObject.getParent().getPath()});
  }

  @Override
  public void fileDeleted(FileEvent event) {
    super.fileDeleted(event);
    LOG.log(Level.INFO, "[EC for {0}] Deleted file: {1}", new Object[]{editorConfigFileObject.getPath(), event.getFile().getPath()});
    event.getFile().removeRecursiveListener(this);
  }

  @Override
  public void fileChanged(FileEvent event) {
    super.fileChanged(event);
    String path = event.getFile().getPath();

    LOG.log(Level.INFO, "[EC for {0}] File content changed: {1}", new Object[]{editorConfigFileObject.getPath(), path});

    if (!event.firedFrom(new WriteEditorAction()) && !event.firedFrom(new WriteStringToFileAction())) {
      if (applyRulesToFile(event)) {
        try {
          editorConfigProcessor.applyRulesToFile(DataObject.find(event.getFile()));
        } catch (DataObjectNotFoundException ex) {
          Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
          Exceptions.printStackTrace(ex);
        }
      } else {
        LOG.log(Level.INFO, "[EC for {0}] Rules will not be applied to: {1}", new Object[]{editorConfigFileObject.getPath(), path});
      }
    } else {
      LOG.log(Level.INFO, "[EC for {0}] Rules will not be applied to: {1} - Change triggered by EditorConfig plugin", new Object[]{editorConfigFileObject.getPath(), path});
    }

  }

  private boolean applyRulesToFile(FileEvent event) {
    FileObject file = event.getFile();

    boolean applyRules = false;
    boolean isntFolder = !file.isFolder();
    boolean isUnexpected = !event.isExpected();
    boolean isntSkipped = !SmartSkip.skipDirectory(file);

    if (isUnexpected && isntFolder && isntSkipped) {
      applyRules = true;
    }

    return applyRules;
  }

  @Override
  public void fileFolderCreated(FileEvent event) {
    super.fileFolderCreated(event);
    LOG.log(Level.FINE, "[EC for {0}] Created folder: {1}", new Object[]{editorConfigFileObject.getPath(), event.getFile().getPath()});
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
    FileObject primaryFile = event.getFile();
    LOG.log(Level.FINE, "[EC for {0}] Added new file to project: {1} (MIME type: {2})",
            new Object[]{editorConfigFileObject.getPath(), primaryFile.getPath(), primaryFile.getMIMEType()});
  }

  @Override
  public void fileAttributeChanged(FileAttributeEvent event) {
    super.fileAttributeChanged(event);
    LOG.log(Level.FINE, "[EC for {0}] Attribute changed: {1}", new Object[]{editorConfigFileObject.getPath(), event.getFile().getPath()});
  }

  @Override
  public void fileRenamed(FileRenameEvent event) {
    super.fileRenamed(event);
    LOG.log(Level.FINE, "[EC for {0}] Renamed file: {1}", new Object[]{editorConfigFileObject.getPath(), event.getFile().getPath()});
  }
}
