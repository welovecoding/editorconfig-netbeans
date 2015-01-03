package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import static com.welovecoding.netbeans.plugin.editorconfig.processor.EditorConfigProcessor.OPERATION_LOG_LEVEL;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TrimTrailingWhiteSpaceOperation {

  private static final Logger LOG = Logger.getLogger(TrimTrailingWhiteSpaceOperation.class.getSimpleName());

  static {
    LOG.setLevel(OPERATION_LOG_LEVEL);
  }

  public boolean run(StringBuilder content, final String lineEnding) {
    return run(content, true, lineEnding);
  }

  private boolean run(StringBuilder content, final boolean trimWhiteSpace, final String lineEnding) {
    LOG.log(Level.INFO, "\u00ac Executing trim whitespaces operation");

    boolean trimmedWhiteSpaces = false;

    if (trimWhiteSpace) {
      String contentBeforeOperation = content.toString();

      content = trim(content, lineEnding);

      if (contentBeforeOperation.equals(content.toString())) {
        LOG.log(Level.INFO, "\u00ac No whitespace trimmed");
        trimmedWhiteSpaces = false;
      } else {
        LOG.log(Level.INFO, "\u00ac Trimmed whitespaces");
        trimmedWhiteSpaces = true;
      }
    }

    return trimmedWhiteSpaces;
  }

  // TODO: Caret position is not set properly when text is trimmed
  // If the caret is in a line where we trim text, then we have to move it
  // minus the amount of characters which have been removed.
  // We need to find AND save the caret offset.
  private StringBuilder trim(StringBuilder content, String lineEnding) {
    String contentCopy = content.toString();
    BufferedReader reader = new BufferedReader(new StringReader(contentCopy));

    // Note: As a side effect this will strip a final newline!
    String trimmedContent = reader.lines().map((String line) -> {
      return line.replaceAll("\\s+$", "");
    }).collect(Collectors.joining(lineEnding));

    // Exchange original content with trimmed content
    content.delete(0, content.length());
    content.append(trimmedContent);

    // Append line ending if old content had a line ending
    if (contentCopy.endsWith("\n") || contentCopy.endsWith("\r")) {
      content.append(lineEnding);
    }

    return content;
  }
}
