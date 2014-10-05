package org.editorconfig.netbeans.test;

import java.util.ArrayList;
import java.util.List;
import org.editorconfig.core.EditorConfig;

public class MainClass {

  public static void main(String[] args) throws Exception {
    /*
     EditorConfigParser parser = new EditorConfigParser();
     String result = parser.parseResource("editorconfig-test.ini");
     System.out.println(result);
     */

    List<String> filePaths = new ArrayList<>();

    // Get test file
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    String path = classLoader.getResource("editorconfig-test.ini").getPath();
    filePaths.add(path);

    for (String filePath : filePaths) {
      EditorConfig editorConfig = new EditorConfig();
      List<EditorConfig.OutPair> properties = editorConfig.getProperties(filePath);
      System.out.println("Properties found: " + properties.size());
    }
  }

}
