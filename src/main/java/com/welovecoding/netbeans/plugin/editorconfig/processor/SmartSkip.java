package com.welovecoding.netbeans.plugin.editorconfig.processor;

import org.openide.filesystems.FileObject;

public class SmartSkip {

  public static final boolean IS_ON = true;
  private static final String[] IGNORED_FILES = {
    "bower_components",
    "nbproject",
    "node_modules"
  };

  public static boolean skipFile(FileObject file) {
    String fileName = file.getName();
    boolean skip = false;

    for (String pattern : IGNORED_FILES) {
      if (fileName.startsWith(pattern)) {
        skip = true;
      }
    }

    if (file.isFolder() && fileName.startsWith(".")) {
      skip = true;
    }

    return skip;
  }
}
