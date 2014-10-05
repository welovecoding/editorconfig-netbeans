package org.editorconfig.core;

import java.io.IOException;

/**
 * Exception which is thrown by {@link EditorConfig#getProperties(String)} if an
 * EditorConfig file could not be parsed
 *
 * @author Dennis.Ushakov
 */
public class ParsingException extends EditorConfigException {

  public ParsingException(String s, IOException e) {
    super(s, e);
  }
}
