package org.editorconfig.netbeans.model;

public interface EditorConfigConstants {

  // Keys
  public static final String CHARSET = "charset";
  public static final String END_OF_LINE = "end_of_line";
  public static final String INDENT_SIZE = "indent_size";
  public static final String INDENT_STYLE = "indent_style";
  public static final String INSERT_FINAL_NEWLINE = "insert_final_newline";
  public static final String MAX_LINE_LENGTH = "max_line_length";
  public static final String TAB_WIDTH = "tab_width";
  public static final String TRIM_TRAILING_WHITESPACE = "trim_trailing_whitespace";

  /**
   * Special character signifying the end of a line of text. Mainly used and
   * standard in: Unix, Linux, Android, Mac OS X, AmigaOS, BSD.F
   */
  public static final String LINE_FEED = "lf"; // Linux, Mac OS X
  /**
   * Special character signifying the end of a line of text. Mainly used and
   * standard in: Mac OS until version 9, Apple II, C64
   */
  public static final String CARRIAGE_RETURN = "cr"; // Mac OS 9
  /**
   * Special sequence of characters signifying the end of a line of text. Mainly
   * used and standard in: Windows, DOS, OS/2, CP/M, TOS (Atari).
   */
  public static final String CARRIAGE_RETURN_LINE_FEED = "crlf";
  public static final String SPACE = "space";
  public static final String TAB = "tab";
}
