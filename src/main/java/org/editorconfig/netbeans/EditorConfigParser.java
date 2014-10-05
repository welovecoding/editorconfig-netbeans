package org.editorconfig.netbeans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditorConfigParser {

  private static final Logger LOG = Logger.getLogger(EditorConfigParser.class.getName());

  // https://github.com/editorconfig/editorconfig-core-java/blob/master/src/org/editorconfig/core/EditorConfig.java
  public EditorConfigParser() {
  }

  public void parseConfig(URL resource) {
    File file = new File(resource.getFile());
    StringBuilder sb = new StringBuilder();
    String line;

    try (
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr)) {
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }
    } catch (IOException ex) {
      //
    } finally {
      System.out.println(sb.toString());
    }
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
