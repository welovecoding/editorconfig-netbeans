package org.editorconfig.netbeans.model;

public enum EditorConfigKey {

  CHARSET("charset"),
  END_OF_LINE("end_of_line"),
  INDENT_SIZE("indent_size"),
  INDENT_STYLE("indent_style"),
  INSERT_FINAL_NEWLINE("insert_final_newline"),
  MAX_LINE_LENGTH("max_line_length"),
  TAB_WIDTH("tab_width"),
  TRIM_TRAILING_WHITESPACE("trim_trailing_whitespace");

  private final String text;

  private EditorConfigKey(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
