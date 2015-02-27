package com.welovecoding.nbeditorconfig.editor.lexer;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.misc.Interval;
import org.netbeans.spi.lexer.LexerInput;

public class AntlrCharStream implements CharStream {

  private final LexerInput input;
  private final String name;
  private int index = 0;
  private int markDepth = 0;

  public AntlrCharStream(LexerInput input, String name) {
    this.input = input;
    this.name = name;
  }

  @Override
  public String getText(Interval interval) {
    return input.readText().toString();
  }

  @Override
  public void consume() {
    int character = read();
    if (character == EOF) {
      backup(1);
      throw new IllegalStateException("Attempting to consume EOF");
    }
    index++;
  }

  @Override
  public int LA(int lookaheadAmount) {
    if (lookaheadAmount < 0) {
      return lookBack(-lookaheadAmount);
    } else if (lookaheadAmount > 0) {
      return lookAhead(lookaheadAmount);
    } else {
      return 0; //Behaviour is undefined when lookaheadAmount == 0
    }
  }

  private int lookBack(int amount) {
    backup(amount);
    int character = read();
    for (int i = 1; i < amount; i++) {
      read();
    }
    return character;
  }

  private int lookAhead(int amount) {
    int character = 0;
    for (int i = 0; i < amount; i++) {
      character = read();
    }
    backup(amount);
    return character;
  }

  @Override
  public int mark() {
    return ++markDepth;
  }

  @Override
  public void release(int marker) {
    // unwind any other markers made after m and release m
    markDepth = marker;
    // release this marker
    markDepth--;
  }

  @Override
  public void seek(int index) {
    if (index < 0) {
      throw new IllegalArgumentException(String.format("Invalid index (%s < 0)", index));
    }

    if (index < this.index) {
      backup(this.index - index);
      this.index = index;
      return;
    }

    // seek forward, consume until p hits index
    while (this.index < index) {
      consume();
    }
  }

  @Override
  public int index() {
    return index;
  }

  @Override
  public int size() {
    throw new UnsupportedOperationException("Stream size unknown");
  }

  @Override
  public String getSourceName() {
    return name;
  }

  private int read() {
    int result = input.read();
    if (result == LexerInput.EOF) {
      return EOF;
    } else {
      return result;
    }
  }

  private void backup(int count) {
    input.backup(count);
  }
}
