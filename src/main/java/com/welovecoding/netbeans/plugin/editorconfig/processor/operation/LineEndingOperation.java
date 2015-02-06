package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import static com.welovecoding.netbeans.plugin.editorconfig.processor.EditorConfigProcessor.OPERATION_LOG_LEVEL;
import com.welovecoding.netbeans.plugin.editorconfig.processor.FileInfo;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LineEndingOperation {

  private static final Logger LOG = Logger.getLogger(LineEndingOperation.class.getSimpleName());

  static {
    LOG.setLevel(OPERATION_LOG_LEVEL);
  }

  public boolean operate(FileInfo info) {
    boolean changed;

    LOG.log(Level.INFO, "LINE_ENDING = true");
    String tempContent = info.getContentAsString();
    LOG.log(Level.FINEST, "OLDCONTENT: {0}.", tempContent);
    StringBuilder newContent = replaceLineEndings(info.getContent(), info.getEndOfLine());

    if (tempContent.equals(newContent.toString())) {
      LOG.log(Level.INFO, "LINE_ENDING : No changes");
      changed = false;
    } else {
      LOG.log(Level.INFO, "LINE_ENDING : changed line endings");
      changed = true;
    }
    LOG.log(Level.FINEST, "NEWCONTENT: {0}.", newContent);

    return changed;
  }

  private StringBuilder replaceLineEndings(StringBuilder content, String lineEnding) {
    BufferedReader reader = new BufferedReader(new StringReader(content.toString()));

    /**
     * Note: As a side effect this will strip a final newline!
     */
    String tempContent = reader.lines().
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
