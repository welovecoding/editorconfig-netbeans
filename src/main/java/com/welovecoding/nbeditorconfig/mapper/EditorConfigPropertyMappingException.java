package com.welovecoding.nbeditorconfig.mapper;

public class EditorConfigPropertyMappingException extends Exception {

  public EditorConfigPropertyMappingException() {
  }

  public EditorConfigPropertyMappingException(String message) {
    super(message);
  }

  public EditorConfigPropertyMappingException(Exception ex) {
    super(ex.getMessage());
  }
}
