package org.antlr4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 *
 * @author junichi11
 */
public class EditorConfigErrorListener extends BaseErrorListener {

  private final List<SyntaxError> errorMessages;

  public EditorConfigErrorListener(List<SyntaxError> errorMessages) {
    if (errorMessages == null) {
      errorMessages = Collections.synchronizedList(new ArrayList<SyntaxError>());
    }
    this.errorMessages = errorMessages;
  }

  @Override
  public synchronized void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
    errorMessages.add(new SyntaxError(msg, line, charPositionInLine, e));
  }

  public synchronized List<SyntaxError> getErrorMessages() {
    return errorMessages;
  }

}
