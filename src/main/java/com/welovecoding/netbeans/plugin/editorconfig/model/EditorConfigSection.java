package com.welovecoding.netbeans.plugin.editorconfig.model;

public class EditorConfigSection {

  private final String pattern;
  private final String javaPattern;
  private final int weight;

  public EditorConfigSection() {
    this.pattern = null;
    this.javaPattern = null;
    this.weight = -1;
  }

  public EditorConfigSection(String pattern, String javaPattern, int weight) {
    this.pattern = pattern;
    this.javaPattern = javaPattern;
    this.weight = weight;
  }

  public String getPattern() {
    return pattern;
  }

  public String getJavaPattern() {
    return javaPattern;
  }

  public int getWeight() {
    return weight;
  }

}
