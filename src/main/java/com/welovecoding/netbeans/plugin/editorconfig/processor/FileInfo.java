package com.welovecoding.netbeans.plugin.editorconfig.processor;

import java.nio.charset.Charset;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;

public class FileInfo {

  private FileObject fileObject;
  private StringBuilder content;
  private Charset charset;
  private boolean openedInEditor;
  private EditorCookie cookie;

  public FileInfo(FileObject fileObject) {
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

  public Charset getCharset() {
    return charset;
  }

  public void setCharset(Charset charset) {
    this.charset = charset;
  }

  public boolean isOpenedInEditor() {
    return openedInEditor;
  }

  public void setOpenedInEditor(boolean openedInEditor) {
    this.openedInEditor = openedInEditor;
  }

  public EditorCookie getCookie() {
    return cookie;
  }

  public void setCookie(EditorCookie cookie) {
    this.cookie = cookie;
  }
  // </editor-fold>
}
