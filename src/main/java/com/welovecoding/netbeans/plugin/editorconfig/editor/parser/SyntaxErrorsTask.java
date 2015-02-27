package com.welovecoding.netbeans.plugin.editorconfig.editor.parser;

import static com.welovecoding.netbeans.plugin.editorconfig.config.Settings.EDITORCONFIG;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.Document;
import org.antlr4.SyntaxError;
import org.netbeans.modules.parsing.spi.ParserResultTask;
import org.netbeans.modules.parsing.spi.Scheduler;
import org.netbeans.modules.parsing.spi.SchedulerEvent;
import org.netbeans.spi.editor.hints.ErrorDescription;
import org.netbeans.spi.editor.hints.ErrorDescriptionFactory;
import org.netbeans.spi.editor.hints.HintsController;
import org.netbeans.spi.editor.hints.Severity;

/**
 *
 * @author junichi11
 */
public class SyntaxErrorsTask extends ParserResultTask<ECParserResult> {

  @Override
  public void run(ECParserResult result, SchedulerEvent event) {
    List<SyntaxError> syntaxErrors = result.getErrors();
    Document document = result.getSnapshot().getSource().getDocument(false);
    List<ErrorDescription> errors = new ArrayList<>();
    for (SyntaxError syntaxError : syntaxErrors) {
      String message = syntaxError.getMessage();
      int line = syntaxError.getLine();
      if (line <= 0) {
        continue;
      }
      ErrorDescription errorDescription = ErrorDescriptionFactory.createErrorDescription(
              Severity.ERROR,
              message,
              document,
              line);
      errors.add(errorDescription);
    }
    HintsController.setErrors(document, EDITORCONFIG, errors); // NOI18N
  }

  @Override
  public int getPriority() {
    return 100;
  }

  @Override
  public Class<? extends Scheduler> getSchedulerClass() {
    return Scheduler.EDITOR_SENSITIVE_TASK_SCHEDULER;
  }

  @Override
  public void cancel() {
  }

}
