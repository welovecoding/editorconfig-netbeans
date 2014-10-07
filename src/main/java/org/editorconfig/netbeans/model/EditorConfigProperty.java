package org.editorconfig.netbeans.model;

public class EditorConfigProperty {

  // Keys
  public static final String CHARSET = "charset";
  public static final String END_OF_LINE = "end_of_line";
  public static final String INDENT_SIZE = "indent_size";
  public static final String INDENT_STYLE = "indent_style";
  public static final String INSERT_FINAL_NEWLINE = "insert_final_newline";
  public static final String MAX_LINE_LENGTH = "max_line_length";
  public static final String TAB_WIDTH = "tab_width";
  public static final String TRIM_TRAILING_WHITESPACE = "trim_trailing_whitespace";
  
  // Values
  public static final String LINE_FEED = "lf"; // Linux, Mac OS X
  public static final String CARRIAGE_RETURN = "cr"; // Mac OS 9
  public static final String CARRIAGE_RETURN_LINE_FEED = "crlf"; // Windows, DOS

  private final String key;
  private final String value;

  public EditorConfigProperty() {
    this.key = null;
    this.value = null;
  }

  public EditorConfigProperty(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

}
