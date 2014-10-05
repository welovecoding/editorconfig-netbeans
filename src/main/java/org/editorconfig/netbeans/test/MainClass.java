package org.editorconfig.netbeans.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.editorconfig.core.EditorConfig;
import org.editorconfig.core.EditorConfigException;
import org.editorconfig.core.OutPair;
import org.openide.util.Exceptions;

public class MainClass {

  private static final Logger LOG = Logger.getLogger(MainClass.class.getName());

  public static void main(String[] args) {
    /*
     EditorConfigParser parser = new EditorConfigParser();
     String result = parser.parseResource("editorconfig-test.ini");
     System.out.println(result);
     */

    List<String> filePaths = new ArrayList<>();

    // Get test file
    String testFilePath = "org/editorconfig/example/editorconfig-test.ini";
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    String path = classLoader.getResource(testFilePath).getPath();
    
    filePaths.add(path);

    for (String filePath : filePaths) {
      EditorConfig editorConfig = new EditorConfig("editorconfig-test.ini", EditorConfig.VERSION);
      try {
        List<OutPair> properties = editorConfig.getProperties(filePath);

        for (int i = 0; i < properties.size(); ++i) {
          OutPair property = properties.get(i);
          System.out.println("Key: " + property.getKey());
          System.out.println("Value: " + property.getVal());
        }
      } catch (EditorConfigException ex) {
        Exceptions.printStackTrace(ex);
      }
    }

  }
}
