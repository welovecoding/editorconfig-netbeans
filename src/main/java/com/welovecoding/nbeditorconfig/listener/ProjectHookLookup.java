package com.welovecoding.nbeditorconfig.listener;

import static com.welovecoding.nbeditorconfig.config.LoggerSettings.LISTENER_LOG_LEVEL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.LookupProvider;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 * Project types supported by NetBeans IDE:
 * https://platform.netbeans.org/tutorials/nbm-projectextension.html
 * 
 * Module overview:
 * http://bits.netbeans.org/nexus/content/groups/netbeans/org/netbeans/modules/
 * 
 * The module type can be found in the "project.xml" file of a NetBeans project.
 * Dots in the project type must be replaced with dashes.
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
  "org-netbeans-modules-php-project",
  "org-netbeans-modules-web-clientproject",
  "org-netbeans-modules-web-project"
})
/**
 * Listener for newly opened Projects.
 */
public class ProjectHookLookup implements LookupProvider {

  private static final Logger LOG = Logger.getLogger(ProjectHookLookup.class.getName());

  static {
    LOG.setLevel(LISTENER_LOG_LEVEL);
  }

  @Override
  public Lookup createAdditionalLookup(Lookup lookup) {
    final ClassLoader cl = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(ProjectHookLookup.class.getClassLoader());
      final Project project = lookup.lookup(Project.class);
      String projectName = project.getProjectDirectory().getName();

      LOG.log(Level.INFO, "Setup hooks for: {0}", projectName);
      final ProjectOpenCloseListener listener = new ProjectOpenCloseListener(project);
      return Lookups.fixed(listener);
    } finally {
      Thread.currentThread().setContextClassLoader(cl);
    }
  }

}
