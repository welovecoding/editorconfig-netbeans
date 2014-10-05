package org.editorconfig.netbeans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditorConfigParser {

  private static final Logger LOG = Logger.getLogger(EditorConfigParser.class.getName());

  // https://github.com/editorconfig/editorconfig-core-java/blob/master/src/org/editorconfig/core/EditorConfig.java
  public EditorConfigParser() {
  }

  public String parseResource(String filePath) {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream stream = classLoader.getResourceAsStream(filePath);
    StringBuilder sb = new StringBuilder();
    String result = null;
    String line;

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      while ((line = reader.readLine()) != null) {
        sb.append(line);
        sb.append(System.getProperty("line.separator", "\r\n"));
      }
      result = sb.toString();
    } catch (IOException ex) {
      LOG.log(Level.SEVERE, "Error reading file: {0}", ex.getMessage());
    }

    return result;
  }
}
