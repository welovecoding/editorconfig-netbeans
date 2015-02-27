package com.welovecoding.nbeditorconfig.editor.parser;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr4.EditorConfigErrorListener;
import org.antlr4.EditorConfigLexer;
import org.antlr4.EditorConfigParser;
import org.antlr4.SyntaxError;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;

/**
 *
 * @author junichi11
 */
public class ECParser extends Parser {

  private Snapshot snapshot;
  private EditorConfigParser parser;
  private List<SyntaxError> syntaxErrors;
  private ECParserResult result;

  @Override
  public void parse(Snapshot snapshot, Task task, SourceModificationEvent event) throws ParseException {
    this.snapshot = snapshot;
    String text = snapshot.getText().toString();
    ANTLRInputStream input = new ANTLRInputStream(text);
    Lexer lexer = new EditorConfigLexer(input);
    lexer.removeErrorListeners();

    CommonTokenStream tokens = new CommonTokenStream(lexer);
    parser = new EditorConfigParser(tokens);
    parser.removeErrorListeners();
    syntaxErrors = new ArrayList<>();
    EditorConfigErrorListener errorListener = new EditorConfigErrorListener(syntaxErrors);
    parser.addErrorListener(errorListener);
    EditorConfigParser.FileContext root = parser.file();
    result = new ECParserResult(snapshot, parser, root);
  }

  @Override
  public Result getResult(Task task) throws ParseException {
    return result;
  }

  @Override
  public void addChangeListener(ChangeListener listener) {
  }

  @Override
  public void removeChangeListener(ChangeListener listener) {
  }

}
