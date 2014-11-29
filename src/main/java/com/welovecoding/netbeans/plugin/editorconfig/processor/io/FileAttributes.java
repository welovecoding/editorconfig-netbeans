package com.welovecoding.netbeans.plugin.editorconfig.processor.io;

import java.io.File;
import java.nio.charset.Charset;

public class FileAttributes {

  private Charset charset;
  private File file;
  private String lineEnding;
  private boolean marked;

  public FileAttributes(File file, Charset charset, String lineEnding) {
    this.file = file;
    this.charset = charset;
    this.lineEnding = lineEnding;
  }

  // <editor-fold defaultstate="collapsed" desc="Getter & Setter">
  public boolean isMarked() {
    return marked;
  }

  public void setMarked(boolean marked) {
    this.marked = marked;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public Charset getCharset() {
    return charset;
  }

  public void setCharset(Charset charset) {
    this.charset = charset;
  }

  public String getLineEnding() {
    return lineEnding;
  }

  public void setLineEnding(String lineEnding) {
    this.lineEnding = lineEnding;
  }
 // </editor-fold>
}
