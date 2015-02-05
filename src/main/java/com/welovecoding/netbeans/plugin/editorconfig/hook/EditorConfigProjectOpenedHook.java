package com.welovecoding.netbeans.plugin.editorconfig.hook;

import com.welovecoding.netbeans.plugin.editorconfig.listener.EditorConfigChangeListener;
import com.welovecoding.netbeans.plugin.editorconfig.listener.ProjectChangeListener;
import com.welovecoding.netbeans.plugin.editorconfig.processor.SmartSkip;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.openide.filesystems.FileObject;

public class EditorConfigProjectOpenedHook extends ProjectOpenedHook {

  private static final Logger LOG = Logger.getLogger(EditorConfigProjectOpenedHook.class.getSimpleName());
  private final Map<FileObject, EditorConfigChangeListener> editorConfigListeners = new HashMap<>();
  private Project project;
  private ProjectChangeListener rootListener;

  public EditorConfigProjectOpenedHook() {
    super();
  }

  public EditorConfigProjectOpenedHook(Project project) {
    this();
    this.project = project;
  }

  @Override
  protected void projectOpened() {
    FileObject projectFileObject = project.getProjectDirectory();
    LOG.log(Level.INFO, "Opened project: {0}", projectFileObject.getName());
    attachListeners(projectFileObject, project);
  }

  @Override
  protected void projectClosed() {
    LOG.log(Level.INFO, "Closed project: {0}", project.getProjectDirectory().getName());
  }

  /**
   * Recursively attach listeners to folders containing a .editorconfig file.
   *
   * @param root
   * @param project
   */
  private void attachListeners(FileObject root, Project project) {
    if (project.getProjectDirectory().equals(root)) {
      rootListener = new ProjectChangeListener(project);
      root.addRecursiveListener(rootListener);
    }

    for (FileObject file : root.getChildren()) {
      if (file.getName().equals(".editorconfig")) {
        attachEditorConfigChangeListener(project, file);
        LOG.log(Level.INFO, "\u00ac Found EditorConfig: {0}", file.getPath());
      } else if (file.isFolder()) {
        if (SmartSkip.skipDirectory(file)) {
          LOG.log(Level.INFO, "\u00ac Skipped directory: {0}", file.getPath());
        } else {
          attachListeners(file, project);
          LOG.log(Level.INFO, "\u00ac Attached ProjectChangeListener: {0}", file.getPath());
        }
      }
    }
  }

  private void attachEditorConfigChangeListener(Project project, FileObject editorConfigFileObject) {
    EditorConfigChangeListener listener = new EditorConfigChangeListener(project, editorConfigFileObject);
    editorConfigFileObject.addFileChangeListener(listener);
    editorConfigListeners.put(editorConfigFileObject, listener);
  }

}
