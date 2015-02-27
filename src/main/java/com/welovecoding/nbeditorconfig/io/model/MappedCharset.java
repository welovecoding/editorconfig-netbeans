package com.welovecoding.nbeditorconfig.io.model;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class MappedCharset {

  public static final String FILE_MARK = "\uFEFF";

  private Charset charset = null;
  private String mark = null;
  private String name = null;

  public MappedCharset(String name) {
    this.charset = null;
    this.mark = null;
    this.name = name;
    init(name);
  }

  private void init(String name) {
    switch (name) {
      case "ISO-8859-1":
        charset = StandardCharsets.ISO_8859_1;
        break;
      case "UTF-8":
        charset = StandardCharsets.UTF_8;
        break;
      case "UTF-8-BOM":
        charset = StandardCharsets.UTF_8;
        mark = FILE_MARK;
        break;
      case "UTF-16BE":
        charset = StandardCharsets.UTF_16BE;
        mark = FILE_MARK;
        break;
      case "UTF-16LE":
        charset = StandardCharsets.UTF_16LE;
        mark = FILE_MARK;
        break;
      default:
        charset = StandardCharsets.UTF_8;
        break;
    }
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 79 * hash + Objects.hashCode(this.name);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MappedCharset other = (MappedCharset) obj;
    return Objects.equals(this.name, other.name);
  }

  @Override
  public String toString() {
    return "MappedCharset{" + "charset=" + charset + ", mark=" + mark + ", name=" + name + '}';
  }
  
  public Charset getCharset() {
    return charset;
  }

  public void setCharset(Charset charset) {
    this.charset = charset;
  }

  public String getMark() {
    return mark;
  }

  public void setMark(String mark) {
    this.mark = mark;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
