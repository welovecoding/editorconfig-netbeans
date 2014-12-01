package com.welovecoding.netbeans.plugin.editorconfig.io.model;

/**
 * File information which can be parsed from the first line of a file.
 */
public class FirstLineInfo {

  private final MappedCharset charset;
  private final String lineEnding;
  private final boolean marked;

  public FirstLineInfo(MappedCharset charset, String lineEnding, boolean marked) {
    this.charset = charset;
    this.lineEnding = lineEnding;
    this.marked = marked;
  }

  public MappedCharset getCharset() {
    return charset;
  }

  public String getLineEnding() {
    return lineEnding;
  }

  public boolean isMarked() {
    return marked;
  }

}
