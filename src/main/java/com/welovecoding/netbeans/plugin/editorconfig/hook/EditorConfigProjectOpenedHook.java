package com.welovecoding.netbeans.plugin.editorconfig.hook;

import com.welovecoding.netbeans.plugin.editorconfig.listener.EditorConfigChangeListener;
import com.welovecoding.netbeans.plugin.editorconfig.listener.ProjectChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.openide.filesystems.FileObject;

public class EditorConfigProjectOpenedHook extends ProjectOpenedHook {

  private static final Logger LOG = Logger.getLogger(EditorConfigProjectOpenedHook.class.getName());
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
      LOG.log(Level.INFO, "Scanning File: {0}", file.getPath());
      if (file.isFolder()) {
        LOG.log(Level.INFO, "is folder");
        attachListeners(file, project);
      } else if (file.getName().equals(".editorconfig")) {
        LOG.log(Level.INFO, "is editorconfig");
        attachEditorConfigChangeListener(project, file);
      }
    }
  }

  private void attachEditorConfigChangeListener(Project project, FileObject editorConfigFileObject) {
    LOG.log(Level.INFO, "Found EditorConfig: {0}", editorConfigFileObject.getPath());

    EditorConfigChangeListener listener = new EditorConfigChangeListener(project, editorConfigFileObject);
    editorConfigFileObject.addFileChangeListener(listener);
    editorConfigListeners.put(editorConfigFileObject, listener);

    LOG.log(Level.INFO, "Attached EditorConfigChangeListener to: {0}", editorConfigFileObject.getPath());
  }

}
