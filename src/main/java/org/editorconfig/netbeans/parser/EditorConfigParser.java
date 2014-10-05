package org.editorconfig.netbeans.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
        boolean isInteresting = !(line.startsWith("#") || line.isEmpty());
        
        if (isInteresting) {
          if (line.startsWith("[")) {
            // RegEx
            String regex = line.substring(1, line.lastIndexOf("]"));
            System.out.println("REGEX: " + regex);
            // TODO: Convert RegEx to Java compliant RegEx
          } else {
            // Key / Value
            String[] splitted = line.split("=");
            System.out.println("Key: " + splitted[0]);
            System.out.println("Value: " + splitted[1]);
            // TODO: Save Key / Value pairs together with RegEx in result set
          }
        }
      }
    } catch (IOException ex) {
      LOG.log(Level.SEVERE, "Error reading file: {0}", ex.getMessage());
    }
  }
}
