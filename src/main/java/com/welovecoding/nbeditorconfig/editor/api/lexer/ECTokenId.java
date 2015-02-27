package com.welovecoding.nbeditorconfig.editor.api.lexer;

import com.welovecoding.nbeditorconfig.config.Settings;
import com.welovecoding.nbeditorconfig.editor.lexer.ECLexer;
import com.welovecoding.nbeditorconfig.model.EditorConfigConstant;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 *
 * @author junichi11
 */
public enum ECTokenId implements TokenId {

  WS(null, Category.WHITESPACE, ECTokenId.TOKEN_WS),
  PROPERTY_ROOT(EditorConfigConstant.ROOT, Category.KEYWORD, ECTokenId.TOKEN_PROPERTY_KEY_ROOT), // NOI18N
  PROPERTY_CHARSET(EditorConfigConstant.CHARSET, Category.KEYWORD, ECTokenId.TOKEN_PROPERTY_KEY_CHARSET), // NOI18N
  PROPERTY_END_OF_LINE(EditorConfigConstant.END_OF_LINE, Category.KEYWORD, ECTokenId.TOKEN_PROPERTY_KEY_END_OF_LINE), // NOI18N
  PROPERTY_INDENT_SIZE(EditorConfigConstant.INDENT_SIZE, Category.KEYWORD, ECTokenId.TOKEN_PROPERTY_KEY_INDENT_SIZE), // NOI18N
  PROPERTY_INDENT_STYLE(EditorConfigConstant.INDENT_STYLE, Category.KEYWORD, ECTokenId.TOKEN_PROPERTY_KEY_INDENT_STYLE), // NOI18N
  PROPERTY_INSERT_FINAL_NEWLINE(EditorConfigConstant.INSERT_FINAL_NEWLINE, Category.KEYWORD, ECTokenId.TOKEN_PROPERTY_KEY_INSERT_FINAL_NEWLINE), // NOI18N
  PROPERTY_MAX_LINE_LENGTH(EditorConfigConstant.MAX_LINE_LENGTH, Category.KEYWORD, ECTokenId.TOKEN_PROPERTY_KEY_MAX_LINE_LENGTH), // NOI18N
  PROPERTY_TAB_WIDTH(EditorConfigConstant.TAB_WIDTH, Category.KEYWORD, ECTokenId.TOKEN_PROPERTY_KEY_TAB_WIDTH), // NOI18N
  PROPERTY_TRIM_TRAILING_WHITESPACE(EditorConfigConstant.TRIM_TRAILING_WHITESPACE, Category.KEYWORD, ECTokenId.TOKEN_PROPERTY_KEY_TRIM_TRAILING_WHITESPACE), // NOI18N
  PROPERTY_UNKNOWN(null, Category.KEYWORD, ECTokenId.TOKEN_PROPERTY_KEY_UNKNOWN),
  SECTION_LDELIMITER("[", Category.SECTION_DELIMITER, ECTokenId.TOKEN_SECTION_LDELIMITER), // NOI18N
  SECTION_RDELIMITER("]", Category.SECTION_DELIMITER, ECTokenId.TOKEN_SECTION_RDELIMITER), // NOI18N
  OPERATOR_ASSIGNMENT("=", Category.EQUALS, ECTokenId.TOKEN_ASSIGNMENT), // NOI18N
  COMMENT(null, Category.COMMENT, ECTokenId.TOKEN_COMMENT),
  TEXT(null, Category.STRING, ECTokenId.TOKEN_TEXT),
  SECTION_NAME(null, Category.SECTION, ECTokenId.TOKEN_SECTION_NAME),
  PROPERTY_VALUE(null, Category.STRING, ECTokenId.TOKEN_PROPERTY_VALUE),
  EOF(null, Category.WHITESPACE, ECTokenId.TOKEN_EOF);

  private enum Category {

    WHITESPACE("wlc-nbeditorconfig-whitespace"), // NOI18N
    KEYWORD("wlc-nbeditorconfig-keyword"), // NOI18N
    SECTION("wlc-nbeditorconfig-section"), // NOI18N
    SECTION_DELIMITER("wlc-nbeditorconfig-section-delimiter"), // NOI18N
    COMMENT("wlc-nbeditorconfig-comment"), // NOI18N
    STRING("wlc-nbeditorconfig-string"), // NOI18N
    EQUALS("wlc-nbeditorconfig-equals"); // NOI18N
    private final String name;

    Category(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  public static final int TOKEN_WS = 1;
  public static final int TOKEN_PROPERTY_KEY_ROOT = 2;
  public static final int TOKEN_PROPERTY_KEY_CHARSET = 3;
  public static final int TOKEN_PROPERTY_KEY_END_OF_LINE = 4;
  public static final int TOKEN_PROPERTY_KEY_INDENT_SIZE = 5;
  public static final int TOKEN_PROPERTY_KEY_INDENT_STYLE = 6;
  public static final int TOKEN_PROPERTY_KEY_INSERT_FINAL_NEWLINE = 7;
  public static final int TOKEN_PROPERTY_KEY_MAX_LINE_LENGTH = 8;
  public static final int TOKEN_PROPERTY_KEY_TAB_WIDTH = 9;
  public static final int TOKEN_PROPERTY_KEY_TRIM_TRAILING_WHITESPACE = 10;
  public static final int TOKEN_PROPERTY_KEY_UNKNOWN = 11;
  public static final int TOKEN_SECTION_LDELIMITER = 12;
  public static final int TOKEN_SECTION_RDELIMITER = 13;
  public static final int TOKEN_ASSIGNMENT = 14;
  public static final int TOKEN_COMMENT = 15;
  public static final int TOKEN_TEXT = 16;
  public static final int TOKEN_SECTION_NAME = 17;
  public static final int TOKEN_PROPERTY_VALUE = 18;
  public static final int TOKEN_EOF = -1;

  private final String fixedText;
  private final Category primaryCategory;
  private final int id;
  private static final Map<Integer, ECTokenId> TOKENS = Collections.synchronizedMap(new HashMap<Integer, ECTokenId>());

  static {
    for (ECTokenId value : values()) {
      TOKENS.put(value.getId(), value);
    }
  }

  ECTokenId(String fixedText, Category primaryCategory, int id) {
    this.fixedText = fixedText;
    this.primaryCategory = primaryCategory;
    this.id = id;
  }

  @Override
  public String primaryCategory() {
    return primaryCategory.getName();
  }

  public String getFixedText() {
    return fixedText;
  }

  public int getId() {
    return id;
  }

  public static ECTokenId toEnum(int tokenType) {
    return TOKENS.get(tokenType);
  }

  private static final Language<ECTokenId> EDITORCONFIG_LANGUAGE = new LanguageHierarchy<ECTokenId>() {

    @Override
    protected Collection<ECTokenId> createTokenIds() {
      return EnumSet.allOf(ECTokenId.class);
    }

    @Override
    protected Lexer<ECTokenId> createLexer(LexerRestartInfo<ECTokenId> info) {
      return ECLexer.create(info);
    }

    @Override
    protected String mimeType() {
      return Settings.MIME_TYPE;
    }

  }.language();

  public static Language<ECTokenId> language() {
    return EDITORCONFIG_LANGUAGE;
  }

}
