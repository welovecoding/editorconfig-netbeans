package com.welovecoding.netbeans.plugin.editorconfig.editor.lexer;

import com.welovecoding.netbeans.plugin.editorconfig.editor.api.lexer.ECTokenId;
import org.antlr.v4.runtime.misc.IntegerStack;
import org.antlr4.EditorConfigLexer;
import org.netbeans.api.lexer.Token;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 *
 * @author junichi11
 */
public class ECLexer implements Lexer<ECTokenId> {

  private final LexerRestartInfo<ECTokenId> info;
  private final EditorConfigLexer lexer;

  private ECLexer(LexerRestartInfo<ECTokenId> info) {
    this.info = info;
    AntlrCharStream charStream = new AntlrCharStream(info.input(), "EditorConfig"); // NOI18N
    lexer = new EditorConfigLexer(charStream);
    lexer.removeErrorListeners();
    // TODO add listener
    LexerState lexerMode = (LexerState) info.state();
    if (lexerMode != null) {
      lexer._mode = lexerMode.Mode;
      lexer._modeStack.addAll(lexerMode.Stack);
    }
  }

  public static synchronized ECLexer create(LexerRestartInfo<ECTokenId> info) {
    return new ECLexer(info);
  }

  @Override
  public Token<ECTokenId> nextToken() {
    org.antlr.v4.runtime.Token token = lexer.nextToken();
    int type = token.getType();
    ECTokenId tokenId = ECTokenId.toEnum(type);
    assert tokenId != null;
    if (tokenId != ECTokenId.EOF) {
      return info.tokenFactory().createToken(tokenId);
    }
    return null;
  }

  @Override
  public Object state() {
    return new LexerState(lexer._mode, lexer._modeStack);
  }

  @Override
  public void release() {
  }

  // If mode is used, keep state of it
  // http://stackoverflow.com/questions/23887888/antlr4-based-lexer-loses-syntax-hightlighting-during-typing-on-netbeans
  private static class LexerState {

    public int Mode = -1;
    public IntegerStack Stack = null;

    public LexerState(int mode, IntegerStack stack) {
      Mode = mode;
      Stack = new IntegerStack(stack);
    }
  }
}
