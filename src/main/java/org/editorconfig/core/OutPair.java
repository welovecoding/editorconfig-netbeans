package org.editorconfig.core;

public class OutPair {

  private final String key;
  private final String val;

  public OutPair(String key, String val) {
    this.key = key;
    this.val = val;
  }

  public String getKey() {
    return key;
  }

  public String getVal() {
    return val;
  }
}
