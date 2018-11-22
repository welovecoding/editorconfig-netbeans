package com.welovecoding.nbeditorconfig.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.plaf.metal.MetalIconFactory;
import org.netbeans.api.java.platform.JavaPlatform;
import org.netbeans.api.java.platform.JavaPlatformManager;
import org.netbeans.api.java.platform.Specification;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.NotificationDisplayer;
import org.openide.modules.ModuleInstall;
import org.openide.modules.SpecificationVersion;
import org.openide.util.NbBundle;

public class Installer extends ModuleInstall {
  
  /**
   * Class Logger
   */
  private static final Logger INSTALLER_LOGGER = Logger.getLogger(Installer.class.getName());

  /**
   * A representation of the minimum required version of Java as a
   * {@link SpecificationVersion} compatible "Dewey-decimal version".
   */
  private static final String MIN_JAVA_VERSION = "1.7.0";

  @Override
  public void restored() {    
    if (isJavaVersionIncompatiable()) {
      registerIncompatabilityNotification();
    }
  }

  /**
   * Displays a notification in the default notification UI containing
   * information on the plugin's incompatibility with the current IDE's version
   * of the JVM
   */
  private static void registerIncompatabilityNotification() {

    //These are the title, message and icon displayed in the notification area and within popup if user interacts with notification
    final String incompatTitle = NbBundle.getMessage(Installer.class, "wlc-nbeditorconfig-version-error-title");
    final String incompatMessage = NbBundle.getMessage(Installer.class, "wlc-nbeditorconfig-version-error-message");
    final Icon incompatIcon = new MetalIconFactory.FileIcon16();
    
    INSTALLER_LOGGER.log(Level.SEVERE, incompatMessage);

    final ActionListener notificationInteractionListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        NotifyDescriptor.Message pluginIncompatiableMessage = new NotifyDescriptor.Message(incompatMessage, NotifyDescriptor.ERROR_MESSAGE);
        DialogDisplayer.getDefault().notify(pluginIncompatiableMessage);
      }
    };

    NotificationDisplayer.getDefault().notify(incompatTitle, incompatIcon, incompatMessage, notificationInteractionListener);

  }

  /**
   * Evaluates the compatibility of the current JVM running the IDE against
   * {@link #MIN_JAVA_VERSION}.
   *
   * @return True if the JVM running the IDE is not compatible with this
   * plugin.
   */
  private boolean isJavaVersionIncompatiable() {
    SpecificationVersion javaVersion = getJavaPlatformVersion();
    
    INSTALLER_LOGGER.log(Level.FINE, "Found Java version: " + javaVersion);
    INSTALLER_LOGGER.log(Level.FINE, "Expected Java version: " + MIN_JAVA_VERSION);
    
    return javaVersion.compareTo(getMinimumRequiredJavaVersion()) < 0;
  }

  /**
   * Convenience/readability method for generating a
   * {@link SpecificationVersion} containing the version information equivalent
   * to {@link #MIN_JAVA_VERSION}.
   *
   * @return
   */
  private static SpecificationVersion getMinimumRequiredJavaVersion() {
    return new SpecificationVersion(MIN_JAVA_VERSION);
  }

  /**
   * Determines the version of the Java environment the IDE is running within
   * utilizing {@link JavaPlatform}.
   *
   * @return A {@link SpecificationVersion} instance containing the version
   * information of the IDE JVM using a "Dewey-decimal format".
   */
  private static SpecificationVersion getJavaPlatformVersion() {
    JavaPlatformManager idePlatformManager = JavaPlatformManager.getDefault();
    JavaPlatform idePlatform = idePlatformManager.getDefaultPlatform();
    Specification idePlatformSpec = idePlatform.getSpecification();

    return idePlatformSpec.getVersion();
  }
}
