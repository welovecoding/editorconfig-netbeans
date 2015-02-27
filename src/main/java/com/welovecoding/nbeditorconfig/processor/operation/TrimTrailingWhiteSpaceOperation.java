package com.welovecoding.nbeditorconfig.processor.operation;

import static com.welovecoding.nbeditorconfig.processor.EditorConfigProcessor.OPERATION_LOG_LEVEL;
import com.welovecoding.nbeditorconfig.processor.FileInfo;
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

  public boolean operate(FileInfo info) {
    return operate(info, true);
  }

  private boolean operate(FileInfo info, final boolean trimWhiteSpace) {
    StringBuilder content = info.getContent();
    boolean trimmedWhiteSpaces = false;

    if (trimWhiteSpace) {
      String contentBeforeOperation = content.toString();

      detectCaretOffset(info);
      content = trim(info);

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

  private void detectCaretOffset(FileInfo info) {
    StringBuilder content = info.getContent();
    String contentCopy = content.toString();

    // Trim until caret
    String contentUntilCaret = "";
    int caretPosition = info.getCurrentCaretPosition();
    if (caretPosition > 0) {
      contentUntilCaret = contentCopy.substring(0, caretPosition);
    }

    BufferedReader reader = new BufferedReader(new StringReader(contentUntilCaret));

    String trimmedContent = reader.lines().map((String line) -> {
      return line.replaceAll("\\s+$", "");
    }).collect(Collectors.joining(info.getEndOfLine()));

    // Count the characters which have been trimmed until the caret positon
    // (this will be our caret offset)
    int offset = contentUntilCaret.length() - trimmedContent.length();
    info.setCaretOffset(offset);

    LOG.log(Level.INFO, "\u00ac Content length until caret: {0}", contentUntilCaret.length());
    LOG.log(Level.INFO, "\u00ac Trimmed content length: {0}", trimmedContent.length());
    LOG.log(Level.INFO, "\u00ac Caret offset: {0}", offset);
  }

  // TODO: Caret position is not set properly when text is trimmed
  // If the caret is in a line where we trim text, then we have to move it
  // minus the amount of characters which have been removed.
  // We need to find AND save the caret offset.
  private StringBuilder trim(FileInfo info) {
    StringBuilder content = info.getContent();
    String lineEnding = info.getEndOfLine();
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
