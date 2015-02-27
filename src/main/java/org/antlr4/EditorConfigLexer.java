// Generated from editorconfig/EditorConfigLexer.g4 by ANTLR 4.5
package org.antlr4;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class EditorConfigLexer extends Lexer {

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
  public static final int SECTION_INNER_MODE = 1;
  public static final int PROPERTY_VALUE_MODE = 2;
  public static String[] modeNames = {
    "DEFAULT_MODE", "SECTION_INNER_MODE", "PROPERTY_VALUE_MODE"
  };

  public static final String[] ruleNames = {
    "PROPERTY_KEY_ROOT", "PROPERTY_KEY_CHARSET", "PROPERTY_KEY_END_OF_LINE",
    "PROPERTY_KEY_INDENT_SIZE", "PROPERTY_KEY_INDENT_STYLE", "PROPERTY_KEY_INSERT_FINAL_NEWLINE",
    "PROPERTY_KEY_MAX_LINE_LENGTH", "PROPERTY_KEY_TAB_WIDTH", "PROPERTY_KEY_TRIM_TRAILING_WHITESPACE",
    "PROPERTY_KEY_UNKNOWN", "SECTION_LDELIMITER", "SECTION_RDELIMITER", "ASSIGNMENT",
    "WS", "COMMENT", "TEXT", "SECTION_NAME", "SECTION_EXIT", "PROPERTY_VALUE_WS",
    "PROPERTY_VALUE"
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

  public EditorConfigLexer(CharStream input) {
    super(input);
    _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
  }

  @Override
  public String getGrammarFileName() {
    return "EditorConfigLexer.g4";
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
  public String[] getModeNames() {
    return modeNames;
  }

  @Override
  public ATN getATN() {
    return _ATN;
  }

  public static final String _serializedATN
          = "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\24\u00e6\b\1\b\1"
          + "\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4"
          + "\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t"
          + "\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\3\2\3\2\3\2\3\2\3\2\3\3\3"
          + "\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"
          + "\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3"
          + "\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"
          + "\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3"
          + "\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t"
          + "\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"
          + "\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\f\3"
          + "\r\3\r\3\16\3\16\3\16\3\16\3\17\6\17\u00b5\n\17\r\17\16\17\u00b6\3\17"
          + "\3\17\3\20\3\20\7\20\u00bd\n\20\f\20\16\20\u00c0\13\20\3\20\3\20\3\21"
          + "\6\21\u00c5\n\21\r\21\16\21\u00c6\3\22\3\22\3\22\7\22\u00cc\n\22\f\22"
          + "\16\22\u00cf\13\22\3\22\6\22\u00d2\n\22\r\22\16\22\u00d3\3\22\3\22\3\23"
          + "\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25"
          + "\3\u00cd\2\26\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33"
          + "\17\35\20\37\3!\21#\22%\23\'\2)\2+\24\5\2\3\4\7\5\2\13\f\17\17\"\"\4\2"
          + "%%==\4\2\f\f\17\17\n\2\13\f\17\17\"\"%%==??]]__\6\2\f\f\17\17%%]_\u00e9"
          + "\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2"
          + "\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2"
          + "\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\3%\3\2\2"
          + "\2\3\'\3\2\2\2\4)\3\2\2\2\4+\3\2\2\2\5-\3\2\2\2\7\62\3\2\2\2\t:\3\2\2"
          + "\2\13F\3\2\2\2\rR\3\2\2\2\17_\3\2\2\2\21t\3\2\2\2\23\u0084\3\2\2\2\25"
          + "\u008e\3\2\2\2\27\u00a7\3\2\2\2\31\u00a9\3\2\2\2\33\u00ad\3\2\2\2\35\u00af"
          + "\3\2\2\2\37\u00b4\3\2\2\2!\u00ba\3\2\2\2#\u00c4\3\2\2\2%\u00d1\3\2\2\2"
          + "\'\u00d7\3\2\2\2)\u00dd\3\2\2\2+\u00e2\3\2\2\2-.\7t\2\2./\7q\2\2/\60\7"
          + "q\2\2\60\61\7v\2\2\61\6\3\2\2\2\62\63\7e\2\2\63\64\7j\2\2\64\65\7c\2\2"
          + "\65\66\7t\2\2\66\67\7u\2\2\678\7g\2\289\7v\2\29\b\3\2\2\2:;\7g\2\2;<\7"
          + "p\2\2<=\7f\2\2=>\7a\2\2>?\7q\2\2?@\7h\2\2@A\7a\2\2AB\7n\2\2BC\7k\2\2C"
          + "D\7p\2\2DE\7g\2\2E\n\3\2\2\2FG\7k\2\2GH\7p\2\2HI\7f\2\2IJ\7g\2\2JK\7p"
          + "\2\2KL\7v\2\2LM\7a\2\2MN\7u\2\2NO\7k\2\2OP\7|\2\2PQ\7g\2\2Q\f\3\2\2\2"
          + "RS\7k\2\2ST\7p\2\2TU\7f\2\2UV\7g\2\2VW\7p\2\2WX\7v\2\2XY\7a\2\2YZ\7u\2"
          + "\2Z[\7v\2\2[\\\7{\2\2\\]\7n\2\2]^\7g\2\2^\16\3\2\2\2_`\7k\2\2`a\7p\2\2"
          + "ab\7u\2\2bc\7g\2\2cd\7t\2\2de\7v\2\2ef\7a\2\2fg\7h\2\2gh\7k\2\2hi\7p\2"
          + "\2ij\7c\2\2jk\7n\2\2kl\7a\2\2lm\7p\2\2mn\7g\2\2no\7y\2\2op\7n\2\2pq\7"
          + "k\2\2qr\7p\2\2rs\7g\2\2s\20\3\2\2\2tu\7o\2\2uv\7c\2\2vw\7z\2\2wx\7a\2"
          + "\2xy\7n\2\2yz\7k\2\2z{\7p\2\2{|\7g\2\2|}\7a\2\2}~\7n\2\2~\177\7g\2\2\177"
          + "\u0080\7p\2\2\u0080\u0081\7i\2\2\u0081\u0082\7v\2\2\u0082\u0083\7j\2\2"
          + "\u0083\22\3\2\2\2\u0084\u0085\7v\2\2\u0085\u0086\7c\2\2\u0086\u0087\7"
          + "d\2\2\u0087\u0088\7a\2\2\u0088\u0089\7y\2\2\u0089\u008a\7k\2\2\u008a\u008b"
          + "\7f\2\2\u008b\u008c\7v\2\2\u008c\u008d\7j\2\2\u008d\24\3\2\2\2\u008e\u008f"
          + "\7v\2\2\u008f\u0090\7t\2\2\u0090\u0091\7k\2\2\u0091\u0092\7o\2\2\u0092"
          + "\u0093\7a\2\2\u0093\u0094\7v\2\2\u0094\u0095\7t\2\2\u0095\u0096\7c\2\2"
          + "\u0096\u0097\7k\2\2\u0097\u0098\7n\2\2\u0098\u0099\7k\2\2\u0099\u009a"
          + "\7p\2\2\u009a\u009b\7i\2\2\u009b\u009c\7a\2\2\u009c\u009d\7y\2\2\u009d"
          + "\u009e\7j\2\2\u009e\u009f\7k\2\2\u009f\u00a0\7v\2\2\u00a0\u00a1\7g\2\2"
          + "\u00a1\u00a2\7u\2\2\u00a2\u00a3\7r\2\2\u00a3\u00a4\7c\2\2\u00a4\u00a5"
          + "\7e\2\2\u00a5\u00a6\7g\2\2\u00a6\26\3\2\2\2\u00a7\u00a8\5#\21\2\u00a8"
          + "\30\3\2\2\2\u00a9\u00aa\7]\2\2\u00aa\u00ab\3\2\2\2\u00ab\u00ac\b\f\2\2"
          + "\u00ac\32\3\2\2\2\u00ad\u00ae\7_\2\2\u00ae\34\3\2\2\2\u00af\u00b0\7?\2"
          + "\2\u00b0\u00b1\3\2\2\2\u00b1\u00b2\b\16\3\2\u00b2\36\3\2\2\2\u00b3\u00b5"
          + "\t\2\2\2\u00b4\u00b3\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6"
          + "\u00b7\3\2\2\2\u00b7\u00b8\3\2\2\2\u00b8\u00b9\b\17\4\2\u00b9 \3\2\2\2"
          + "\u00ba\u00be\t\3\2\2\u00bb\u00bd\n\4\2\2\u00bc\u00bb\3\2\2\2\u00bd\u00c0"
          + "\3\2\2\2\u00be\u00bc\3\2\2\2\u00be\u00bf\3\2\2\2\u00bf\u00c1\3\2\2\2\u00c0"
          + "\u00be\3\2\2\2\u00c1\u00c2\b\20\4\2\u00c2\"\3\2\2\2\u00c3\u00c5\n\5\2"
          + "\2\u00c4\u00c3\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00c4\3\2\2\2\u00c6\u00c7"
          + "\3\2\2\2\u00c7$\3\2\2\2\u00c8\u00d2\n\6\2\2\u00c9\u00cd\7]\2\2\u00ca\u00cc"
          + "\13\2\2\2\u00cb\u00ca\3\2\2\2\u00cc\u00cf\3\2\2\2\u00cd\u00ce\3\2\2\2"
          + "\u00cd\u00cb\3\2\2\2\u00ce\u00d0\3\2\2\2\u00cf\u00cd\3\2\2\2\u00d0\u00d2"
          + "\7_\2\2\u00d1\u00c8\3\2\2\2\u00d1\u00c9\3\2\2\2\u00d2\u00d3\3\2\2\2\u00d3"
          + "\u00d1\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5\u00d6\b\22"
          + "\5\2\u00d6&\3\2\2\2\u00d7\u00d8\t\4\2\2\u00d8\u00d9\3\2\2\2\u00d9\u00da"
          + "\b\23\6\2\u00da\u00db\b\23\4\2\u00db\u00dc\b\23\5\2\u00dc(\3\2\2\2\u00dd"
          + "\u00de\5\37\17\2\u00de\u00df\3\2\2\2\u00df\u00e0\b\24\6\2\u00e0\u00e1"
          + "\b\24\4\2\u00e1*\3\2\2\2\u00e2\u00e3\5#\21\2\u00e3\u00e4\3\2\2\2\u00e4"
          + "\u00e5\b\25\5\2\u00e5,\3\2\2\2\13\2\3\4\u00b6\u00be\u00c6\u00cd\u00d1"
          + "\u00d3\7\7\3\2\7\4\2\2\3\2\6\2\2\t\3\2";
  public static final ATN _ATN
          = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

  static {
    _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
    for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
      _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
    }
  }
}
