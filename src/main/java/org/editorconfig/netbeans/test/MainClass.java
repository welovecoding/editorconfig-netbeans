package org.editorconfig.netbeans.test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.editorconfig.core.EditorConfig;
import org.editorconfig.core.EditorConfigException;
import org.editorconfig.core.OutPair;
import org.editorconfig.netbeans.parser.EditorConfigParser;
import org.openide.util.Exceptions;

public class MainClass {

  private static final Logger LOG = Logger.getLogger(MainClass.class.getName());

  public static void main(String[] args) throws URISyntaxException {
    Pattern pattern = Pattern.compile("^(.*)\\.py$");
    Matcher matcher = pattern.matcher("myfile.py");
    boolean isMatched = matcher.matches();
    System.out.println("Is matched: " + isMatched);

    // Get test file
    String testFilePath = "org/editorconfig/example/editorconfig-test.ini";
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(testFilePath);

    EditorConfigParser parser = new EditorConfigParser();
    parser.parseConfig(resource);
  }
}
