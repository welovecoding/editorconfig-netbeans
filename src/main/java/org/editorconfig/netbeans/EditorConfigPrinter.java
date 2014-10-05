package org.editorconfig.netbeans;

import java.util.List;
import java.util.Map;
import org.editorconfig.netbeans.parser.EditorConfigProperty;

public class EditorConfigPrinter {

  public static void printConfig(Map<String, List<EditorConfigProperty>> config) {
    for (String section : config.keySet()) {
      System.out.println("Section: " + section);
      List<EditorConfigProperty> properties = config.get(section);
      for (EditorConfigProperty property : properties) {
        String output = String.format("\t%s: %s", property.getKey(), property.getValue());
        System.out.println(output);
      }
    }
  }
}
