package com.welovecoding.netbeans.plugin.editorconfig.processor;

import java.nio.charset.Charset;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

public class FileInfo {

  private Charset charset;
  private EditorCookie cookie;
  private DataObject dataObject;
  private String fileMark;
  private StringBuilder sb;
  private boolean openedInEditor;

  public FileInfo(DataObject dataObject) {
    this.dataObject = dataObject;
  }

  public String getPath() {
    return this.getFileObject().getPath();
  }

  public FileObject getFileObject() {
    return this.dataObject.getPrimaryFile();
  }

  public String getContentAsString() {
    String content = sb.toString();

    if (fileMark != null && !content.startsWith(fileMark)) {
      content = fileMark + content;
    }

    return content;
  }

  public byte[] getContentAsBytes() {
    return getContentAsString().getBytes(charset);
  }

  // <editor-fold defaultstate="collapsed" desc="Generated Getter and Setter...">
  public DataObject getDataObject() {
    return dataObject;
  }

  public void setDataObject(DataObject dataObject) {
    this.dataObject = dataObject;
  }

  public String getFileMark() {
    return fileMark;
  }

  public void setFileMark(String fileMark) {
    this.fileMark = fileMark;
  }

  public StringBuilder getContent() {
    return sb;
  }

  public void setContent(StringBuilder content) {
    this.sb = content;
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
