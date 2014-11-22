package com.welovecoding.netbeans.plugin.editorconfig.processor;

import java.nio.charset.Charset;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;

public class FlushFileInfo {

  private FileObject fileObject;
  private StringBuilder content;
  private boolean changed;
  private boolean charsetChange;
  private Charset charset;
  private boolean flushInEditor;
  private EditorCookie cookie;

  public FlushFileInfo(FileObject fileObject) {
    this.fileObject = fileObject;
  }

  public String getStringWithCharset() {
    return new String(content.toString().getBytes(charset));
  }

  // <editor-fold defaultstate="collapsed" desc="Generated Getter and Setter...">
  public StringBuilder getContent() {
    return content;
  }

  public void setContent(StringBuilder content) {
    this.content = content;
  }

  public FileObject getFileObject() {
    return fileObject;
  }

  public void setFileObject(FileObject fileObject) {
    this.fileObject = fileObject;
  }

  public boolean isChanged() {
    return changed;
  }

  public void setChanged(boolean changed) {
    this.changed = changed;
  }

  public boolean isCharsetChange() {
    return charsetChange;
  }

  public void setCharsetChange(boolean charsetChange) {
    this.charsetChange = charsetChange;
  }

  public Charset getCharset() {
    return charset;
  }

  public void setCharset(Charset charset) {
    this.charset = charset;
  }

  public boolean isFlushInEditor() {
    return flushInEditor;
  }

  public void setFlushInEditor(boolean flushInEditor) {
    this.flushInEditor = flushInEditor;
  }

  public EditorCookie getCookie() {
    return cookie;
  }

  public void setCookie(EditorCookie cookie) {
    this.cookie = cookie;
  }
  // </editor-fold>
}
