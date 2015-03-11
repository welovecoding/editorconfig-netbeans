// Generated from editorconfig/EditorConfigParser.g4 by ANTLR 4.5
package org.antlr4;

import java.util.List;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class EditorConfigParser extends Parser {

  static {
    RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION);
  }

  protected static final DFA[] _decisionToDFA;
  protected static final PredictionContextCache _sharedContextCache
          = new PredictionContextCache();
  public static final int WS = 1, PROPERTY_KEY_ROOT = 2, PROPERTY_KEY_CHARSET = 3, PROPERTY_KEY_END_OF_LINE = 4,
          PROPERTY_KEY_INDENT_SIZE = 5, PROPERTY_KEY_INDENT_STYLE = 6, PROPERTY_KEY_INSERT_FINAL_NEWLINE = 7,
          PROPERTY_KEY_MAX_LINE_LENGTH = 8, PROPERTY_KEY_TAB_WIDTH = 9, PROPERTY_KEY_TRIM_TRAILING_WHITESPACE = 10,
          PROPERTY_KEY_UNKNOWN = 11, SECTION_LDELIMITER = 12, SECTION_RDELIMITER = 13,
          ASSIGNMENT = 14, COMMENT = 15, TEXT = 16, SECTION_NAME = 17, PROPERTY_VALUE = 18;
  public static final int RULE_file = 0, RULE_section = 1, RULE_sectionHeader = 2, RULE_rootPropertyStatement = 3,
          RULE_propertyStatement = 4, RULE_propertyKey = 5, RULE_propertyValue = 6;
  public static final String[] ruleNames = {
    "file", "section", "sectionHeader", "rootPropertyStatement", "propertyStatement",
    "propertyKey", "propertyValue"
  };

  private static final String[] _LITERAL_NAMES = {
    null, null, "'root'", "'charset'", "'end_of_line'", "'indent_size'", "'indent_style'",
    "'insert_final_newline'", "'max_line_length'", "'tab_width'", "'trim_trailing_whitespace'",
    null, "'['", "']'", "'='"
  };
  private static final String[] _SYMBOLIC_NAMES = {
    null, "WS", "PROPERTY_KEY_ROOT", "PROPERTY_KEY_CHARSET", "PROPERTY_KEY_END_OF_LINE",
    "PROPERTY_KEY_INDENT_SIZE", "PROPERTY_KEY_INDENT_STYLE", "PROPERTY_KEY_INSERT_FINAL_NEWLINE",
    "PROPERTY_KEY_MAX_LINE_LENGTH", "PROPERTY_KEY_TAB_WIDTH", "PROPERTY_KEY_TRIM_TRAILING_WHITESPACE",
    "PROPERTY_KEY_UNKNOWN", "SECTION_LDELIMITER", "SECTION_RDELIMITER", "ASSIGNMENT",
    "COMMENT", "TEXT", "SECTION_NAME", "PROPERTY_VALUE"
  };
  public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

  /**
   * @deprecated Use {@link #VOCABULARY} instead.
   */
  @Deprecated
  public static final String[] tokenNames;

  static {
    tokenNames = new String[_SYMBOLIC_NAMES.length];
    for (int i = 0; i < tokenNames.length; i++) {
      tokenNames[i] = VOCABULARY.getLiteralName(i);
      if (tokenNames[i] == null) {
        tokenNames[i] = VOCABULARY.getSymbolicName(i);
      }

      if (tokenNames[i] == null) {
        tokenNames[i] = "<INVALID>";
      }
    }
  }

  @Override
  @Deprecated
  public String[] getTokenNames() {
    return tokenNames;
  }

  @Override
  public Vocabulary getVocabulary() {
    return VOCABULARY;
  }

  @Override
  public String getGrammarFileName() {
    return "EditorConfigParser.g4";
  }

  @Override
  public String[] getRuleNames() {
    return ruleNames;
  }

  @Override
  public String getSerializedATN() {
    return _serializedATN;
  }

  @Override
  public ATN getATN() {
    return _ATN;
  }

  public EditorConfigParser(TokenStream input) {
    super(input);
    _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
  }

  public static class FileContext extends ParserRuleContext {

    public TerminalNode EOF() {
      return getToken(EditorConfigParser.EOF, 0);
    }

    public RootPropertyStatementContext rootPropertyStatement() {
      return getRuleContext(RootPropertyStatementContext.class, 0);
    }

    public List<SectionContext> section() {
      return getRuleContexts(SectionContext.class);
    }

    public SectionContext section(int i) {
      return getRuleContext(SectionContext.class, i);
    }

    public FileContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_file;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof EditorConfigParserListener) {
        ((EditorConfigParserListener) listener).enterFile(this);
      }
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof EditorConfigParserListener) {
        ((EditorConfigParserListener) listener).exitFile(this);
      }
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof EditorConfigParserVisitor) {
        return ((EditorConfigParserVisitor<? extends T>) visitor).visitFile(this);
      } else {
        return visitor.visitChildren(this);
      }
    }
  }

  public final FileContext file() throws RecognitionException {
    FileContext _localctx = new FileContext(_ctx, getState());
    enterRule(_localctx, 0, RULE_file);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(23);
        switch (getInterpreter().adaptivePredict(_input, 2, _ctx)) {
          case 1: {
            setState(15);
            _la = _input.LA(1);
            if (_la == PROPERTY_KEY_ROOT) {
              {
                setState(14);
                rootPropertyStatement();
              }
            }

            setState(20);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == SECTION_LDELIMITER) {
              {
                {
                  setState(17);
                  section();
                }
              }
              setState(22);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
          }
          break;
        }
        setState(25);
        match(EOF);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class SectionContext extends ParserRuleContext {

    public SectionHeaderContext sectionHeader() {
      return getRuleContext(SectionHeaderContext.class, 0);
    }

    public List<PropertyStatementContext> propertyStatement() {
      return getRuleContexts(PropertyStatementContext.class);
    }

    public PropertyStatementContext propertyStatement(int i) {
      return getRuleContext(PropertyStatementContext.class, i);
    }

    public SectionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_section;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof EditorConfigParserListener) {
        ((EditorConfigParserListener) listener).enterSection(this);
      }
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof EditorConfigParserListener) {
        ((EditorConfigParserListener) listener).exitSection(this);
      }
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof EditorConfigParserVisitor) {
        return ((EditorConfigParserVisitor<? extends T>) visitor).visitSection(this);
      } else {
        return visitor.visitChildren(this);
      }
    }
  }

  public final SectionContext section() throws RecognitionException {
    SectionContext _localctx = new SectionContext(_ctx, getState());
    enterRule(_localctx, 2, RULE_section);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(27);
        sectionHeader();
        setState(31);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PROPERTY_KEY_ROOT) | (1L << PROPERTY_KEY_CHARSET) | (1L << PROPERTY_KEY_END_OF_LINE) | (1L << PROPERTY_KEY_INDENT_SIZE) | (1L << PROPERTY_KEY_INDENT_STYLE) | (1L << PROPERTY_KEY_INSERT_FINAL_NEWLINE) | (1L << PROPERTY_KEY_MAX_LINE_LENGTH) | (1L << PROPERTY_KEY_TAB_WIDTH) | (1L << PROPERTY_KEY_TRIM_TRAILING_WHITESPACE) | (1L << PROPERTY_KEY_UNKNOWN))) != 0)) {
          {
            {
              setState(28);
              propertyStatement();
            }
          }
          setState(33);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class SectionHeaderContext extends ParserRuleContext {

    public TerminalNode SECTION_NAME() {
      return getToken(EditorConfigParser.SECTION_NAME, 0);
    }

    public SectionHeaderContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_sectionHeader;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof EditorConfigParserListener) {
        ((EditorConfigParserListener) listener).enterSectionHeader(this);
      }
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof EditorConfigParserListener) {
        ((EditorConfigParserListener) listener).exitSectionHeader(this);
      }
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof EditorConfigParserVisitor) {
        return ((EditorConfigParserVisitor<? extends T>) visitor).visitSectionHeader(this);
      } else {
        return visitor.visitChildren(this);
      }
    }
  }

  public final SectionHeaderContext sectionHeader() throws RecognitionException {
    SectionHeaderContext _localctx = new SectionHeaderContext(_ctx, getState());
    enterRule(_localctx, 4, RULE_sectionHeader);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(34);
        match(SECTION_LDELIMITER);
        setState(35);
        match(SECTION_NAME);
        setState(36);
        match(SECTION_RDELIMITER);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RootPropertyStatementContext extends ParserRuleContext {

    public PropertyValueContext propertyValue() {
      return getRuleContext(PropertyValueContext.class, 0);
    }

    public RootPropertyStatementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_rootPropertyStatement;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof EditorConfigParserListener) {
        ((EditorConfigParserListener) listener).enterRootPropertyStatement(this);
      }
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof EditorConfigParserListener) {
        ((EditorConfigParserListener) listener).exitRootPropertyStatement(this);
      }
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof EditorConfigParserVisitor) {
        return ((EditorConfigParserVisitor<? extends T>) visitor).visitRootPropertyStatement(this);
      } else {
        return visitor.visitChildren(this);
      }
    }
  }

  public final RootPropertyStatementContext rootPropertyStatement() throws RecognitionException {
    RootPropertyStatementContext _localctx = new RootPropertyStatementContext(_ctx, getState());
    enterRule(_localctx, 6, RULE_rootPropertyStatement);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(38);
        match(PROPERTY_KEY_ROOT);
        setState(39);
        match(ASSIGNMENT);
        setState(40);
        propertyValue();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class PropertyStatementContext extends ParserRuleContext {

    public PropertyKeyContext propertyKey() {
      return getRuleContext(PropertyKeyContext.class, 0);
    }

    public PropertyValueContext propertyValue() {
      return getRuleContext(PropertyValueContext.class, 0);
    }

    public PropertyStatementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_propertyStatement;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof EditorConfigParserListener) {
        ((EditorConfigParserListener) listener).enterPropertyStatement(this);
      }
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof EditorConfigParserListener) {
        ((EditorConfigParserListener) listener).exitPropertyStatement(this);
      }
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof EditorConfigParserVisitor) {
        return ((EditorConfigParserVisitor<? extends T>) visitor).visitPropertyStatement(this);
      } else {
        return visitor.visitChildren(this);
      }
    }
  }

  public final PropertyStatementContext propertyStatement() throws RecognitionException {
    PropertyStatementContext _localctx = new PropertyStatementContext(_ctx, getState());
    enterRule(_localctx, 8, RULE_propertyStatement);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(42);
        propertyKey();
        setState(43);
        match(ASSIGNMENT);
        setState(44);
        propertyValue();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class PropertyKeyContext extends ParserRuleContext {

    public TerminalNode PROPERTY_KEY_INDENT_STYLE() {
      return getToken(EditorConfigParser.PROPERTY_KEY_INDENT_STYLE, 0);
    }

    public TerminalNode PROPERTY_KEY_INDENT_SIZE() {
      return getToken(EditorConfigParser.PROPERTY_KEY_INDENT_SIZE, 0);
    }

    public TerminalNode PROPERTY_KEY_TAB_WIDTH() {
      return getToken(EditorConfigParser.PROPERTY_KEY_TAB_WIDTH, 0);
    }

    public TerminalNode PROPERTY_KEY_END_OF_LINE() {
      return getToken(EditorConfigParser.PROPERTY_KEY_END_OF_LINE, 0);
    }

    public TerminalNode PROPERTY_KEY_CHARSET() {
      return getToken(EditorConfigParser.PROPERTY_KEY_CHARSET, 0);
    }

    public TerminalNode PROPERTY_KEY_TRIM_TRAILING_WHITESPACE() {
      return getToken(EditorConfigParser.PROPERTY_KEY_TRIM_TRAILING_WHITESPACE, 0);
    }

    public TerminalNode PROPERTY_KEY_INSERT_FINAL_NEWLINE() {
      return getToken(EditorConfigParser.PROPERTY_KEY_INSERT_FINAL_NEWLINE, 0);
    }

    public TerminalNode PROPERTY_KEY_ROOT() {
      return getToken(EditorConfigParser.PROPERTY_KEY_ROOT, 0);
    }

    public TerminalNode PROPERTY_KEY_MAX_LINE_LENGTH() {
      return getToken(EditorConfigParser.PROPERTY_KEY_MAX_LINE_LENGTH, 0);
    }

    public TerminalNode PROPERTY_KEY_UNKNOWN() {
      return getToken(EditorConfigParser.PROPERTY_KEY_UNKNOWN, 0);
    }

    public PropertyKeyContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_propertyKey;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof EditorConfigParserListener) {
        ((EditorConfigParserListener) listener).enterPropertyKey(this);
      }
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof EditorConfigParserListener) {
        ((EditorConfigParserListener) listener).exitPropertyKey(this);
      }
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof EditorConfigParserVisitor) {
        return ((EditorConfigParserVisitor<? extends T>) visitor).visitPropertyKey(this);
      } else {
        return visitor.visitChildren(this);
      }
    }
  }

  public final PropertyKeyContext propertyKey() throws RecognitionException {
    PropertyKeyContext _localctx = new PropertyKeyContext(_ctx, getState());
    enterRule(_localctx, 10, RULE_propertyKey);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(46);
        _la = _input.LA(1);
        if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PROPERTY_KEY_ROOT) | (1L << PROPERTY_KEY_CHARSET) | (1L << PROPERTY_KEY_END_OF_LINE) | (1L << PROPERTY_KEY_INDENT_SIZE) | (1L << PROPERTY_KEY_INDENT_STYLE) | (1L << PROPERTY_KEY_INSERT_FINAL_NEWLINE) | (1L << PROPERTY_KEY_MAX_LINE_LENGTH) | (1L << PROPERTY_KEY_TAB_WIDTH) | (1L << PROPERTY_KEY_TRIM_TRAILING_WHITESPACE) | (1L << PROPERTY_KEY_UNKNOWN))) != 0))) {
          _errHandler.recoverInline(this);
        } else {
          consume();
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class PropertyValueContext extends ParserRuleContext {

    public TerminalNode PROPERTY_VALUE() {
      return getToken(EditorConfigParser.PROPERTY_VALUE, 0);
    }

    public PropertyValueContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_propertyValue;
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof EditorConfigParserListener) {
        ((EditorConfigParserListener) listener).enterPropertyValue(this);
      }
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof EditorConfigParserListener) {
        ((EditorConfigParserListener) listener).exitPropertyValue(this);
      }
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof EditorConfigParserVisitor) {
        return ((EditorConfigParserVisitor<? extends T>) visitor).visitPropertyValue(this);
      } else {
        return visitor.visitChildren(this);
      }
    }
  }

  public final PropertyValueContext propertyValue() throws RecognitionException {
    PropertyValueContext _localctx = new PropertyValueContext(_ctx, getState());
    enterRule(_localctx, 12, RULE_propertyValue);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(48);
        match(PROPERTY_VALUE);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static final String _serializedATN
          = "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\24\65\4\2\t\2\4\3"
          + "\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3\2\5\2\22\n\2\3\2\7\2\25"
          + "\n\2\f\2\16\2\30\13\2\5\2\32\n\2\3\2\3\2\3\3\3\3\7\3 \n\3\f\3\16\3#\13"
          + "\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\b\3\b\3\b"
          + "\2\2\t\2\4\6\b\n\f\16\2\3\3\2\4\r\61\2\31\3\2\2\2\4\35\3\2\2\2\6$\3\2"
          + "\2\2\b(\3\2\2\2\n,\3\2\2\2\f\60\3\2\2\2\16\62\3\2\2\2\20\22\5\b\5\2\21"
          + "\20\3\2\2\2\21\22\3\2\2\2\22\26\3\2\2\2\23\25\5\4\3\2\24\23\3\2\2\2\25"
          + "\30\3\2\2\2\26\24\3\2\2\2\26\27\3\2\2\2\27\32\3\2\2\2\30\26\3\2\2\2\31"
          + "\21\3\2\2\2\31\32\3\2\2\2\32\33\3\2\2\2\33\34\7\2\2\3\34\3\3\2\2\2\35"
          + "!\5\6\4\2\36 \5\n\6\2\37\36\3\2\2\2 #\3\2\2\2!\37\3\2\2\2!\"\3\2\2\2\""
          + "\5\3\2\2\2#!\3\2\2\2$%\7\16\2\2%&\7\23\2\2&\'\7\17\2\2\'\7\3\2\2\2()\7"
          + "\4\2\2)*\7\20\2\2*+\5\16\b\2+\t\3\2\2\2,-\5\f\7\2-.\7\20\2\2./\5\16\b"
          + "\2/\13\3\2\2\2\60\61\t\2\2\2\61\r\3\2\2\2\62\63\7\24\2\2\63\17\3\2\2\2"
          + "\6\21\26\31!";
  public static final ATN _ATN
          = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

  static {
    _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
    for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
      _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
    }
  }
}
