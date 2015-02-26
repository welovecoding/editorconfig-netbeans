package org.antlr4;

import org.antlr.v4.runtime.RecognitionException;

/**
 *
 * @author junichi11
 */
public class SyntaxError {

  private final String message;
  private final int line;
  private final int charPositionInLine;
  private final RecognitionException recognitionException;

  public SyntaxError(String message, int line, int charPositionInLine, RecognitionException recognitionException) {
    this.message = message;
    this.line = line;
    this.charPositionInLine = charPositionInLine;
    this.recognitionException = recognitionException;
  }

  public String getMessage() {
    return message;
  }

  public int getLine() {
    return line;
  }

  public int getCharPositionInLine() {
    return charPositionInLine;
  }

  public RecognitionException getRecognitionException() {
    return recognitionException;
  }

}
