package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import static com.welovecoding.netbeans.plugin.editorconfig.processor.EditorConfigProcessor.OPERATION_LOG_LEVEL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XFinalNewLineOperation {

  private static final Logger LOG = Logger.getLogger(XFinalNewLineOperation.class.getSimpleName());

  static {
    LOG.setLevel(OPERATION_LOG_LEVEL);
  }

  /**
   * Inserts the given content with a final new line.
   *
   * @param content Content of a file
   * @param insertFinalNewLine whether to insert a new line at the end of the
   * content
   * @param lineEnding the string representation of a new line ("\r", "\n" or
   * "\r\n")
   * @return whether the operation could be performed
   */
  public static boolean doFinalNewLine(String content, final boolean insertFinalNewLine, final String lineEnding) {
    return new XFinalNewLineOperation().apply(content, insertFinalNewLine, lineEnding);
  }

  public boolean apply(String content, final boolean insertFinalNewLine, final String lineEnding) {
    boolean changed = false;

    LOG.log(Level.INFO, "\u00ac Executing final new line operation");

    if (insertFinalNewLine) {
      String tempContent = content;

      content = addFinalNewLine(content, lineEnding);

      if (tempContent.equals(content)) {
        LOG.log(Level.INFO, "\u00ac No final new line added to copy");
        changed = false;
      } else {
        LOG.log(Level.INFO, "\u00ac Added final new line to copy");
        changed = true;
      }
    }

    return changed;
  }

  private String addFinalNewLine(String content, String lineEnding) {
    if (content.endsWith("\n") || content.endsWith("\r")) {
      LOG.log(Level.INFO, "\u00ac There is already a final new line. No change needed");
      return content;
    } else {
      LOG.log(Level.INFO, "\u00ac Final new line will be added");
      return content + lineEnding;
    }
  }
}
