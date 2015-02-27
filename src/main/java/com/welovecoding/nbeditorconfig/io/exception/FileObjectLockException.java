package com.welovecoding.nbeditorconfig.io.exception;

public class FileObjectLockException extends Exception {

  public FileObjectLockException(String message) {
    super("\u00ac " + message);
  }
}
