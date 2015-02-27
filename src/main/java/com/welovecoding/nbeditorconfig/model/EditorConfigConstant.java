package com.welovecoding.nbeditorconfig.model;

public class EditorConfigConstant {

  /**
   * Set to latin1, utf-8, utf-8-bom, utf-16be or utf-16le to control the
   * character set. Use of utf-8-bom is discouraged.
   */
  public static final String CHARSET = "charset";
  public static final String CHARSET_LATIN_1 = "latin1";        // ISO-LATIN-1
  public static final String CHARSET_UTF_8 = "utf-8";           // UTF-8
  public static final String CHARSET_UTF_8_BOM = "utf-8-bom";   // UTF-8 with signature
  public static final String CHARSET_UTF_16_BE = "utf-16be";    // UTF-16BE
  public static final String CHARSET_UTF_16_LE = "utf-16le";    // UTF-16LE (UCS-2-LE)
  /**
   * Set to lf, cr, or crlf to control how line breaks are represented.
   */
  public static final String END_OF_LINE = "end_of_line";
  public static final String END_OF_LINE_LF = "lf"; // Linux, Mac OS X
  public static final String END_OF_LINE_CR = "cr"; // Mac OS 9
  public static final String END_OF_LINE_CRLF = "crlf";

  /**
   * A whole number defining the number of columns used for each indentation
   * level and the width of soft tabs (when supported). When set to tab, the
   * value of tab_width (if specified) will be used.
   */
  public static final String INDENT_SIZE = "indent_size";
  /**
   * Set to tab or space to use hard tabs or soft tabs respectively.
   */
  public static final String INDENT_STYLE = "indent_style";
  public static final String INDENT_STYLE_SPACE = "space";
  public static final String INDENT_STYLE_TAB = "tab";
  /**
   * Set to true ensure file ends with a newline when saving and false to ensure
   * it doesn't.
   */
  public static final String INSERT_FINAL_NEWLINE = "insert_final_newline";
  /**
   * A whole number defining the number of columns used to represent a tab
   * character. This defaults to the value of indent_size and doesn't usually
   * need to be specified.
   */
  public static final String TAB_WIDTH = "tab_width";
  /**
   * Set to true to remove any whitespace characters preceding newline
   * characters and false to ensure it doesn't.
   */
  public static final String TRIM_TRAILING_WHITESPACE = "trim_trailing_whitespace";

}
