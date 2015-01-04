package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import static com.welovecoding.netbeans.plugin.editorconfig.processor.EditorConfigProcessor.OPERATION_LOG_LEVEL;
import com.welovecoding.netbeans.plugin.editorconfig.processor.FileInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FinalNewLineOperation {

  private static final Logger LOG = Logger.getLogger(FinalNewLineOperation.class.getSimpleName());

  static {
    LOG.setLevel(OPERATION_LOG_LEVEL);
  }

  /**
   * Inserts the given content with a final new line. We have to work with a
   * StringBuilder to work with a reference of the content.
   *
   * @param content Content of a file
   * @param lineEnding the string representation of a new line ("\r", "\n" or
   * "\r\n")
   * @return whether the operation could be performed
   */
  public boolean run(FileInfo info) {
    return run(info.getContent(), true, info.getEndOfLine());
  }

  private StringBuilder addFinalNewLine(StringBuilder content, String lineEnding) {
    if (!content.toString().endsWith("\n") && !content.toString().endsWith("\r")) {
      LOG.log(Level.INFO, "\u00ac Final new line will be added");
      return content.append(lineEnding);
    } else {
      LOG.log(Level.INFO, "\u00ac There is already a final new line. No change needed");
      return content;
    }
  }

  private boolean run(StringBuilder content, final boolean insertFinalNewLine, final String lineEnding) {
    LOG.log(Level.INFO, "\u00ac Executing final new line operation");

    boolean changedLineEndings = false;

    if (insertFinalNewLine) {
      String contentBeforeOperation = content.toString();

      content = addFinalNewLine(content, lineEnding);

      if (contentBeforeOperation.equals(content.toString())) {
        LOG.log(Level.INFO, "\u00ac No final new line added");
        changedLineEndings = false;
      } else {
        LOG.log(Level.INFO, "\u00ac Added final new line");
        changedLineEndings = true;
      }
    }

    return changedLineEndings;
  }
}
