package com.welovecoding.netbeans.plugin.editorconfig.processor;

import java.nio.charset.Charset;
import javax.swing.text.Caret;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

public class FileInfo {

  // File references
  private DataObject dataObject;
  private EditorCookie cookie;
  private StringBuilder content;

  // Configurations
  private Charset charset;
  private String endOfLine;
  private String fileMark;

  // Switches
  private boolean fileChangeNeeded = false;
  private boolean openedInEditor = false;

  public FileInfo() {
  }

  public FileInfo(DataObject dataObject) {
    this.dataObject = dataObject;
  }

  public Caret getCaret() {
    return cookie.getOpenedPanes()[0].getCaret();
  }

  public int getCaretPosition() {
    Caret caret = cookie.getOpenedPanes()[0].getCaret();
    return caret.getDot();
  }

  public String getPath() {
    return this.getFileObject().getPath();
  }

  public FileObject getFileObject() {
    return this.dataObject.getPrimaryFile();
  }

  public String getContentAsString() {
    String contentAsString = content.toString();

    if (fileMark != null && !contentAsString.startsWith(fileMark)) {
      contentAsString = fileMark + contentAsString;
    }

    return contentAsString;
  }

  public byte[] getContentAsBytes() {
    return getContentAsString().getBytes(charset);
  }

  // <editor-fold defaultstate="collapsed" desc="Getter & Setter">
  public String getEndOfLine() {
    return endOfLine;
  }

  public void setEndOfLine(String endOfLine) {
    this.endOfLine = endOfLine;
  }

  public boolean isFileChangeNeeded() {
    return fileChangeNeeded;
  }

  public void setFileChangeNeeded(boolean fileChangeNeeded) {
    this.fileChangeNeeded = fileChangeNeeded;
  }

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
    return content;
  }

  public void setContent(StringBuilder content) {
    this.content = content;
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
