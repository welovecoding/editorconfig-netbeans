package com.welovecoding.netbeans.plugin.editorconfig.model;

public class EditorConfigRules {

  private EditorConfigProperty charset;
  private EditorConfigProperty endOfLine;
  private EditorConfigProperty indentSize;
  private EditorConfigProperty indentStyle;
  private EditorConfigProperty insertFinalNewLine;
  private EditorConfigProperty maxLineLength;
  private EditorConfigProperty tabWidth;
  private EditorConfigProperty trimTrailingWhiteSpace;

  public EditorConfigRules() {
    this.charset = new EditorConfigProperty(EditorConfigConstant.CHARSET, null);
    this.endOfLine = new EditorConfigProperty(EditorConfigConstant.END_OF_LINE, null);
    this.indentSize = new EditorConfigProperty(EditorConfigConstant.INDENT_SIZE, null);
    this.indentStyle = new EditorConfigProperty(EditorConfigConstant.INDENT_STYLE, null);
    this.insertFinalNewLine = new EditorConfigProperty(EditorConfigConstant.INSERT_FINAL_NEWLINE, null);
    this.maxLineLength = new EditorConfigProperty(EditorConfigConstant.MAX_LINE_LENGTH, null);
    this.tabWidth = new EditorConfigProperty(EditorConfigConstant.TAB_WIDTH, null);
    this.trimTrailingWhiteSpace = new EditorConfigProperty(EditorConfigConstant.TRIM_TRAILING_WHITESPACE, null);
  }

  // <editor-fold defaultstate="collapsed" desc="Getter and Setter">
  public EditorConfigProperty getCharset() {
    return charset;
  }

  public void setCharset(EditorConfigProperty charset) {
    this.charset = charset;
  }

  public EditorConfigProperty getEndOfLine() {
    return endOfLine;
  }

  public void setEndOfLine(EditorConfigProperty endOfLine) {
    this.endOfLine = endOfLine;
  }

  public EditorConfigProperty getIndentSize() {
    return indentSize;
  }

  public void setIndentSize(EditorConfigProperty indentSize) {
    this.indentSize = indentSize;
  }

  public EditorConfigProperty getIndentStyle() {
    return indentStyle;
  }

  public void setIndentStyle(EditorConfigProperty indentStyle) {
    this.indentStyle = indentStyle;
  }

  public EditorConfigProperty getInsertFinalNewLine() {
    return insertFinalNewLine;
  }

  public void setInsertFinalNewLine(EditorConfigProperty insertFinalNewLine) {
    this.insertFinalNewLine = insertFinalNewLine;
  }

  public EditorConfigProperty getMaxLineLength() {
    return maxLineLength;
  }

  public void setMaxLineLength(EditorConfigProperty maxLineLength) {
    this.maxLineLength = maxLineLength;
  }

  public EditorConfigProperty getTabWidth() {
    return tabWidth;
  }

  public void setTabWidth(EditorConfigProperty tabWidth) {
    this.tabWidth = tabWidth;
  }

  public EditorConfigProperty getTrimTrailingWhiteSpace() {
    return trimTrailingWhiteSpace;
  }

  public void setTrimTrailingWhiteSpace(EditorConfigProperty trimTrailingWhiteSpace) {
    this.trimTrailingWhiteSpace = trimTrailingWhiteSpace;
  }
  // </editor-fold>

}
