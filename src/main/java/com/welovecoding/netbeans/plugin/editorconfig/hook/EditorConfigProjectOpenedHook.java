package com.welovecoding.netbeans.plugin.editorconfig.hook;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.openide.filesystems.FileObject;

public class EditorConfigProjectOpenedHook extends ProjectOpenedHook {

  private static final Logger LOG = Logger.getLogger(EditorConfigProjectOpenedHook.class.getName());
  private final Map<FileObject, EditorConfigChangeListener> listeners = new HashMap<>();
  Project project;

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
      EditorConfigChangeListener rootListener = new EditorConfigChangeListener(project, null);
      root.addRecursiveListener(rootListener);
      listeners.put(root, rootListener);
      LOG.log(Level.INFO, "Attached change listener to: {0}", project.getProjectDirectory().getPath());
    }

    for (FileObject file : root.getChildren()) {
      if (file.isFolder()) {
        attachListeners(file, project);
      } else if (file.getName().equals(".editorconfig")) {
        attachEditorConfigChangeListener(project, file);
      }
    }
  }

  private void attachEditorConfigChangeListener(Project project, FileObject file) {
    LOG.log(Level.INFO, "Found EditorConfig: {0}", file.getPath());

    EditorConfigChangeListener listener = new EditorConfigChangeListener(project, file);
    file.getParent().addRecursiveListener(listener);
    listeners.put(file.getParent(), listener);

    LOG.log(Level.INFO, "Attached EditorConfig listener to: {0}", file.getParent().getPath());
  }

}
