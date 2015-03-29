package com.welovecoding.nbeditorconfig.processor.operation;

import com.welovecoding.nbeditorconfig.config.LoggerSettings;
import com.welovecoding.nbeditorconfig.processor.FileInfo;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LineEndingOperation {

  private static final Logger LOG = Logger.getLogger(LineEndingOperation.class.getName());

  static {
    LOG.setLevel(LoggerSettings.OPERATION_LOG_LEVEL);
  }

  public boolean operate(FileInfo info) {
    boolean changed;

    String source = info.getContentAsString();
    StringBuilder modified = replaceLineEndings(info.getContent(), info.getEndOfLine());

    if (source.equals(modified.toString())) {
      LOG.log(Level.INFO, "\u00ac No change needed");
      changed = false;
    } else {
      LOG.log(Level.INFO, "\u00ac Changed line endings");
      changed = true;
    }

    return changed;
  }

  private StringBuilder replaceLineEndings(StringBuilder content, String lineEnding) {
    BufferedReader reader = new BufferedReader(new StringReader(content.toString()));

    // Note: As a side effect this will strip a final newline
    String tempContent = reader.lines().collect(Collectors.joining(lineEnding));

    // Append line ending only if that was the case in the old content
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
