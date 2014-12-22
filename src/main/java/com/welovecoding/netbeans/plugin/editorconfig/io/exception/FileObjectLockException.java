package com.welovecoding.netbeans.plugin.editorconfig.io.exception;

public class FileObjectLockException extends Exception {

  public FileObjectLockException(String message) {
    super("\u00ac " + message);
  }
}
