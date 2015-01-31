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
   * @return whether the operation has been performed
   */
  public boolean operate(FileInfo info) {
    return operate(info.getContent(), true, info.getEndOfLine());
  }

  private boolean operate(StringBuilder content, final boolean insertFinalNewLine, final String lineEnding) {
    boolean changedLineEndings = false;

    if (insertFinalNewLine) {
      String contentBeforeOperation = content.toString();
      content = addFinalNewLine(content, lineEnding);
      changedLineEndings = !contentBeforeOperation.equals(content.toString());
    }

    return changedLineEndings;
  }

  private StringBuilder addFinalNewLine(StringBuilder content, String lineEnding) {
    if (!content.toString().endsWith("\n") && !content.toString().endsWith("\r")) {
      LOG.log(Level.INFO, "\u00ac Added final new line");
      return content.append(lineEnding);
    } else {
      LOG.log(Level.INFO, "\u00ac No change needed");
      return content;
    }
  }
}
