package org.editorconfig.netbeans.printer;

import java.util.List;
import java.util.Map;
import org.editorconfig.netbeans.model.EditorConfigProperty;

public class EditorConfigPrinter {

  public static String logConfig(Map<String, List<EditorConfigProperty>> config) {
    StringBuilder sb = new StringBuilder();

    for (String section : config.keySet()) {
      sb.append("Section: ").append(section).append("\r\n");
      List<EditorConfigProperty> properties = config.get(section);
      for (EditorConfigProperty property : properties) {
        String text = String.format("\t%s: %s\r\n", property.getKey(), property.getValue());
        sb.append(text);
      }
    }

    return sb.toString();
  }

  public static void printConfig(Map<String, List<EditorConfigProperty>> config) {
    String configLog = logConfig(config);
    System.out.println(configLog);
  }
}
