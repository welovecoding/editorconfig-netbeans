package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import static com.welovecoding.netbeans.plugin.editorconfig.processor.EditorConfigProcessor.OPERATION_LOG_LEVEL;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class XTrimTrailingWhiteSpaceOperation {

  private static final Logger LOG = Logger.getLogger(XTrimTrailingWhiteSpaceOperation.class.getSimpleName());

  static {
    LOG.setLevel(OPERATION_LOG_LEVEL);
  }

  public boolean run(StringBuilder content, final String lineEnding) {
    boolean wasExecuted = false;

    try {
      wasExecuted = apply(content, true, lineEnding);
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, ex.getMessage());
    }

    return wasExecuted;
  }

  private boolean apply(StringBuilder content, final boolean trimWhiteSpace, final String lineEnding) {
    boolean changed = false;
    LOG.log(Level.INFO, "Executing ApplyTestTask");

    if (trimWhiteSpace) {
      LOG.log(Level.INFO, "TRIM_TRAILING_WHITESPACE = true");
      String tempContent = content.toString();
      LOG.log(Level.FINEST, "OLDCONTENT: {0}.", tempContent);
      content = trimWhitespaces(content, lineEnding);

      if (tempContent.equals(content.toString())) {
        LOG.log(Level.INFO, "TRIM_TRAILING_WHITESPACE : No changes");
        changed = false;
      } else {
        LOG.log(Level.INFO, "TRIM_TRAILING_WHITESPACE : trimmed trailing whitespaces");
        changed = true;
      }
      LOG.log(Level.FINEST, "NEWCONTENT: {0}.", content);
    }

    return changed;
  }

  private StringBuilder trimWhitespaces(StringBuilder content, String lineEnding) {
    BufferedReader reader = new BufferedReader(new StringReader(content.toString()));

    /**
     * Note: As a side effect this will strip a final newline!
     */
    String tempContent = reader.lines().
            map((String t) -> {
              return t.replaceAll("\\s+$", "");
            }).
            collect(Collectors.joining(lineEnding));

    /**
     * appending lineending only if that was the case in the old content.
     */
    if (content.toString().endsWith("\n") || content.toString().endsWith("\r")) {
      content.delete(0, content.length());
      content.append(tempContent).append(lineEnding);
    } else {
      content.delete(0, content.length());
      content.append(tempContent);
    }
    return content;
  }
}
