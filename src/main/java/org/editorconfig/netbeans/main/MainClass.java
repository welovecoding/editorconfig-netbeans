package org.editorconfig.netbeans.main;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.editorconfig.netbeans.model.EditorConfigConstant;
import org.editorconfig.netbeans.model.EditorConfigProperty;
import org.editorconfig.netbeans.parser.EditorConfigParser;
import org.editorconfig.netbeans.parser.EditorConfigParserException;

public class MainClass {

  private static final Logger LOG = Logger.getLogger(MainClass.class.getName());

  public static void main(String[] args) throws URISyntaxException, EditorConfigParserException {
    // RegEx test
    Pattern pattern1 = Pattern.compile("(package.json|.travis.yml)");
    Matcher matcher1 = pattern1.matcher("package.json");
    System.out.println("Matches: " + matcher1.matches());

    // Get test file
    String testFilePath = "org/editorconfig/example/editorconfig-test.ini";
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(testFilePath);
    String resourceFilePath = resource.getFile();
    File file = new File(resourceFilePath);

    EditorConfigParser parser = new EditorConfigParser();
    Map<String, List<EditorConfigProperty>> config = parser.parseConfig(file);
    // EditorConfigPrinter.printConfig(config);

    String[] sampleFiles = new String[]{
      "src/main/webapp/categories.xhtml",
      "src/main/webapp/resources/js/wlc/DocumentHandler.js",
      "src/main/webapp/resources/js/wlc/Rollbar.js",
      "src/main/webapp/resources/less/sidebar-widgets.less",
      "src/main/java/com/welovecoding/Debugger.java",
      "src/main/java/com/welovecoding/StringUtils.java"
    };

    for (String filePath : sampleFiles) {
      for (String regEx : config.keySet()) {
        try {
          Pattern pattern = Pattern.compile(regEx);
          Matcher matcher = pattern.matcher(filePath);
          if (matcher.matches()) {
            String text = String.format("\"%s\" matches '%s'", filePath, regEx);
            System.out.println(text);
            List<EditorConfigProperty> properties = config.get(regEx);
            for (EditorConfigProperty property : properties) {
              String key = property.getKey();
              String value = property.getValue();
              switch (key) {
                case EditorConfigConstant.CHARSET:
                  System.out.println("\tWe have to change the character set to: " + value);
                  break;
                case EditorConfigConstant.END_OF_LINE:
                  System.out.println("\tWe have to change the line endings to: " + value);
                  break;
                case EditorConfigConstant.INDENT_SIZE:
                  System.out.println("\tWe have to change the indent size to: " + value);
                  break;
                case EditorConfigConstant.INDENT_STYLE:
                  System.out.println("\tWe have to change the indent style to: " + value);
                  break;
                case EditorConfigConstant.INSERT_FINAL_NEWLINE:
                  System.out.println("\tWe have to insert a new line at the end of the file: " + value);
                  break;
                case EditorConfigConstant.MAX_LINE_LENGTH:
                  System.out.println("\tWe have to change the max line length to: " + value);
                  break;
                case EditorConfigConstant.TAB_WIDTH:
                  System.out.println("\tWe have to change the tab width to: " + value);
                  break;
                case EditorConfigConstant.TRIM_TRAILING_WHITESPACE:
                  System.out.println("\tWe have to trim trailing whitespaces: " + value);
                  break;
                default:
                  System.out.println("Unknown property: " + key);
              }
            }
          }
        } catch (PatternSyntaxException ignored) {
          // NOP.
        }
      }
    }
  }
}
