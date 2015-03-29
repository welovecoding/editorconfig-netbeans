package com.welovecoding.nbeditorconfig.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.plaf.metal.MetalIconFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.NotificationDisplayer;
import org.openide.modules.ModuleInstall;
import org.openide.util.NbBundle;

public class Installer extends ModuleInstall {

  private static final int MIN_JAVA_VERSION = 8;

  @Override
  public void restored() {
    if (detectOldJava()) {
      String title = NbBundle.getMessage(Installer.class, "wlc-nbeditorconfig-version-error-title");
      String message = NbBundle.getMessage(Installer.class, "wlc-nbeditorconfig-version-error-message");
      int messageType = NotifyDescriptor.ERROR_MESSAGE;

      ActionListener actionListener = (ActionEvent e) -> {
        DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(message, messageType));
      };
      NotificationDisplayer.getDefault().notify(title, new MetalIconFactory.FileIcon16(), message, actionListener);
    }
  }

  private boolean detectOldJava() {
    return JavaVersion.getMinor() < MIN_JAVA_VERSION;
  }

  private static class JavaVersion {

    private JavaVersion() {
    }

    public int getMajor() {
      try {
        return Integer.parseInt(getMappedVersion()[0]);
      } catch (Exception ex) {
        return 0;
      }
    }

    public static int getMinor() {
      try {
        return Integer.parseInt(getMappedVersion()[1]);
      } catch (Exception ex) {
        return 0;
      }
    }
    
    /**
     * Example: Patch version is "0_31" in Java 1.8.0_31
     * 
     * @return 
     */
    public static String getPatch() {
      try {
        return getMappedVersion()[2];
      } catch (Exception ex) {
        return "";
      }
    }

    private static String[] getMappedVersion() throws Exception {
      String[] splittedVersion = System.getProperty("java.version").split("\\."); // NOI18N
      if (splittedVersion.length >= 3) {
        return splittedVersion;
      } else {
        throw new RuntimeException("Could not determine Java version");
      }
    }
  }
}
