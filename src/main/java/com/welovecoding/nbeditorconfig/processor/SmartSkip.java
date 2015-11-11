package com.welovecoding.nbeditorconfig.processor;

import org.openide.filesystems.FileObject;

public class SmartSkip {

  public static final boolean IS_ON = true;
  static final String[] IGNORED_FILES = {
    ".git",
    ".grunt",
    ".idea",
    ".sass-cache",
    ".svn",
    ".tscache",
    "bower_components",
    "nbproject",
    "node_modules",
    "vendor"
  };

  public static boolean skipDirectory(FileObject directory) {
    String fileName = directory.getName();
    boolean skip = false;

    for (String pattern : IGNORED_FILES) {
      if (fileName.startsWith(pattern)) {
        skip = true;
      }
    }

    if (directory.isFolder() && fileName.startsWith(".")) {
      skip = true;
    }

    return skip;
  }
}
