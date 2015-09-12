package com.welovecoding.nbeditorconfig.listener;

import static com.welovecoding.nbeditorconfig.config.LoggerSettings.LISTENER_LOG_LEVEL;
import static com.welovecoding.nbeditorconfig.config.Settings.DEFAULT_FILE_NAME;
import static com.welovecoding.nbeditorconfig.config.Settings.EXTENSION;
import static com.welovecoding.nbeditorconfig.listener.ProjectOpenCloseListener.LISTENER_REGISTRY;
import com.welovecoding.nbeditorconfig.processor.SmartSkip;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;

public class ListenerAttacher {

  private static final Logger LOG = Logger.getLogger(ListenerAttacher.class.getName());

  static {
    LOG.setLevel(LISTENER_LOG_LEVEL);
  }

  /**
   * Recursively attach listeners to folders containing a .editorconfig file.
   *
   * @param file file or folder to attach listener
   * @param project the project the file is related to
   */
  public static void attachListeners(FileObject file, Project project) {
    if (project.getProjectDirectory().equals(file)) {
      ProjectChangeListener projectChangeListener = new ProjectChangeListener(project);
      LISTENER_REGISTRY.put(file, projectChangeListener);
      file.addRecursiveListener(projectChangeListener);
    }

    if (file.isFolder()) {
      if (SmartSkip.skipDirectory(file)) {
        LOG.log(Level.INFO, "\u00ac Skipped directory: {0}", file.getPath());
      } else {
        for (FileObject child : file.getChildren()) {
          attachListeners(child, project);
        }
      }
    } else {
      if (file.getExt().equals(EXTENSION) || file.getName().equals(DEFAULT_FILE_NAME)) {
        EditorConfigChangeListener editorConfigChangeListener = new EditorConfigChangeListener(project, file);
        LISTENER_REGISTRY.put(file, editorConfigChangeListener);
        file.addFileChangeListener(editorConfigChangeListener);
        LOG.log(Level.INFO, "\u00ac Found EditorConfig: {0}", file.getPath());
      } else {
        LOG.log(Level.FINE, "\u00ac No EditorConfig Found: {0}", file.getPath());
      }
    }
  }

  public static void removeListeners(FileObject file, Project project) {
    if (project.getProjectDirectory().equals(file)) {
      file.removeRecursiveListener(LISTENER_REGISTRY.get(file));
    }

    if (file.isFolder()) {
      if (SmartSkip.skipDirectory(file)) {
        LOG.log(Level.INFO, "\u00ac Skipped directory: {0}", file.getPath());
      } else {
        for (FileObject child : file.getChildren()) {
          removeListeners(child, project);
        }
      }
    } else {
      if (file.getExt().equals(EXTENSION) || file.getName().equals(DEFAULT_FILE_NAME)) {
        file.removeFileChangeListener(LISTENER_REGISTRY.get(file));
        LOG.log(Level.INFO, "\u00ac Found EditorConfig: {0}", file.getPath());
      } else {
        LOG.log(Level.FINE, "\u00ac No EditorConfig Found: {0}", file.getPath());
      }
    }
  }
}
