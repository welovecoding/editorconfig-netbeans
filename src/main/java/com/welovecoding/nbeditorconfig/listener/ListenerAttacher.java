package com.welovecoding.nbeditorconfig.listener;

import com.welovecoding.nbeditorconfig.config.Settings;
import com.welovecoding.nbeditorconfig.processor.SmartSkip;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Michael Koppen
 */
public class ListenerAttacher {

  private static final Logger LOG = Logger.getLogger(ListenerAttacher.class.getSimpleName());

  static {
    LOG.setLevel(Level.INFO);
  }

  /**
   * Recursively attach listeners to folders containing a .editorconfig file.
   *
   * @param root
   * @param project
   */
  public static void attachListeners(FileObject file, Project project) {
    if (project.getProjectDirectory().equals(file)) {
      file.addRecursiveListener(new ProjectChangeListener(project));
    }

    if (file.isFolder()) {
      if (SmartSkip.skipDirectory(file)) {
        LOG.log(Level.INFO, "\u00ac Skipped directory: {0}", file.getPath());
      } else {
        for (FileObject subFile : file.getChildren()) {
          attachListeners(subFile, project);
        }
      }
    } else {
      if (file.getExt().equals(Settings.EXTENSION)) {
        file.addFileChangeListener(new EditorConfigChangeListener(project, file));
        LOG.log(Level.INFO, "\u00ac Found EditorConfig: {0}", file.getPath());
      } else {
        LOG.log(Level.FINE, "\u00ac No EditorConfig Found: {0}", file.getPath());
      }
    }
  }
}
