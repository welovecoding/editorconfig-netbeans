package org.editorconfig.netbeans.model;

public class EditorConfigProperty {

  private final String key;
  private final String value;

  public EditorConfigProperty() {
    this.key = null;
    this.value = null;
  }

  public EditorConfigProperty(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

}
