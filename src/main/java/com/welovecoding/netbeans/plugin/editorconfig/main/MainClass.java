package com.welovecoding.netbeans.plugin.editorconfig.main;

import java.net.URL;
import java.util.List;
import java.util.logging.Logger;
import org.editorconfig.core.EditorConfig;
import org.editorconfig.core.EditorConfigException;

public class MainClass {

  private static final Logger LOG = Logger.getLogger(MainClass.class.getName());

  public static void main(String[] args) throws Exception {
    test("org/editorconfig/example/app.yaml");
    test("org/editorconfig/example/cache.py");
    test("org/editorconfig/example/test.js");
    test("org/editorconfig/example/lib/test.js");
    test("org/editorconfig/example/lib/test/test.js");
  }

  public static void test(String testFilePath) throws EditorConfigException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(testFilePath);

    EditorConfig ec = new EditorConfig("editorconfig-test.ini", EditorConfig.VERSION);
    List<EditorConfig.OutPair> l = ec.getProperties(resource.getPath());

    System.out.println("Config for: " + testFilePath);
    for (int i = 0; i < l.size(); ++i) {
      System.out.println(l.get(i).getKey() + "=" + l.get(i).getVal());
    }

    System.out.println(System.getProperty("line.separator", "\r\n"));
  }
}
