package com.welovecoding.nbeditorconfig.netbeans;

import com.welovecoding.netbeans.plugin.editorconfig.hook.EditorConfigChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.LookupProvider;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.openide.filesystems.*;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 * Project types supported by NetBeans IDE:
 * https://platform.netbeans.org/tutorials/nbm-projectextension.html
 */
@LookupProvider.Registration(projectType = {
  "org-netbeans-modules-ant-freeform",
  "org-netbeans-modules-j2ee-archiveproject",
  "org-netbeans-modules-j2ee-clientproject",
  "org-netbeans-modules-j2ee-earproject",
  "org-netbeans-modules-j2ee-ejbjarproject",
  "org-netbeans-modules-java-j2seproject",
  "org-netbeans-modules-maven",
  "org-netbeans-modules-web-clientproject", // HTML5 project
  "org-netbeans-modules-web-project"
})
/**
 * Listener for newly opened Projects.
 */
public class ECProjectOpenedHook implements LookupProvider {

  private static final Logger LOG = Logger.getLogger(ECProjectOpenedHook.class.getName());
  private final Map<FileObject, EditorConfigChangeListener> listeners = new HashMap<>();

  @Override
  public Lookup createAdditionalLookup(Lookup lookup) {
    final Project project = lookup.lookup(Project.class);
    String projectName = project.getProjectDirectory().getName();

    LOG.log(Level.INFO, "Setup hooks for: {0}", projectName);

    return Lookups.fixed(new ProjectOpenedHook() {
      @Override
      protected void projectOpened() {
        FileObject projectFileObject = project.getProjectDirectory();
        LOG.log(Level.INFO, "Opened project: {0}", projectFileObject.getName());
        attachListeners(projectFileObject, project);
        // ECProjectPreferences.setLineEnding(BaseDocument.LS_CRLF, project);
      }

      @Override
      protected void projectClosed() {
        LOG.log(Level.INFO, "Closed project: {0}", project.getProjectDirectory().getName());
      }
    });
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
