package com.welovecoding.nbeditorconfig.listener;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.LookupProvider;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 * Project types supported by NetBeans IDE:
 * https://platform.netbeans.org/tutorials/nbm-projectextension.html
 */
@LookupProvider.Registration(projectType = {
  "org-netbeans-modules-ant-freeform",
  "org-netbeans-modules-apisupport-project",
  "org-netbeans-modules-apisupport-project-suite",
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
public class ProjectHookLookup implements LookupProvider {

  private static final Logger LOG = Logger.getLogger(ProjectHookLookup.class.getName());

  static {
    LOG.setLevel(Level.INFO);
  }

  @Override
  public Lookup createAdditionalLookup(Lookup lookup) {
    final Project project = lookup.lookup(Project.class);
    String projectName = project.getProjectDirectory().getName();

    LOG.log(Level.INFO, "Setup hooks for: {0}", projectName);

    return Lookups.fixed(new ProjectOpenCloseListener(project));
  }

}
