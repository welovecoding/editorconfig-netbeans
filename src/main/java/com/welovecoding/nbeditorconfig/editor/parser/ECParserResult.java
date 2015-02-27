package com.welovecoding.nbeditorconfig.editor.parser;

import java.util.Collections;
import java.util.List;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr4.EditorConfigErrorListener;
import org.antlr4.EditorConfigParser;
import org.antlr4.SyntaxError;
import org.netbeans.modules.csl.api.Error;
import org.netbeans.modules.csl.spi.ParserResult;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.spi.ParseException;

/**
 *
 * @author junichi11
 */
public class ECParserResult extends ParserResult {

  private final EditorConfigParser parser;
  private boolean valid = true;
  private final ParseTree root;

  public ECParserResult(Snapshot snapshot, EditorConfigParser parser, ParseTree root) {
    super(snapshot);
    this.parser = parser;
    this.root = root;
  }

  public EditorConfigParser getEditorConfigParser() throws ParseException {
    if (!valid) {
      throw new ParseException();
    }
    return parser;
  }

  public ParseTree getRoot() {
    return root;
  }

  @Override
  protected void invalidate() {
    valid = false;
  }

  @Override
  public List<? extends Error> getDiagnostics() {
    return Collections.emptyList();
  }

  public List<SyntaxError> getErrors() {
    List<? extends ANTLRErrorListener> errorListeners = parser.getErrorListeners();
    for (ANTLRErrorListener errorListener : errorListeners) {
      if (errorListener instanceof EditorConfigErrorListener) {
        EditorConfigErrorListener ecErrorListener = (EditorConfigErrorListener) errorListener;
        return ecErrorListener.getErrorMessages();
      }
    }
    return Collections.emptyList();
  }

}
