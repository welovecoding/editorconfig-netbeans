package com.welovecoding.netbeans.plugin.editorconfig.parser;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigProperty;
import com.welovecoding.netbeans.plugin.editorconfig.printer.EditorConfigPrinter;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EditorConfigParserTest {

  @Rule
  public ExpectedException exception = ExpectedException.none();

  private static final Logger LOG = Logger.getLogger(EditorConfigParserTest.class.getName());
  private final EditorConfigParser parser;
  private final String testFilePath = "org/editorconfig/example/editorconfig-test.ini";
  private final File testFile;

  private Map<String, List<EditorConfigProperty>> config;

  private final String[] sampleFiles = new String[]{
    ".travis.yml",
    "src/main/webapp/categories.xhtml",
    "src/main/webapp/resources/js/wlc/DocumentHandler.js",
    "src/main/webapp/resources/js/wlc/Rollbar.js",
    "src/main/webapp/resources/js/lib/jQuery.js",
    "src/main/webapp/resources/less/sidebar-widgets.less",
    "src/main/java/com/welovecoding/Debugger.java",
    "src/main/java/com/welovecoding/StringUtils.java"
  };

  public EditorConfigParserTest() {
    parser = new EditorConfigParser();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(testFilePath);
    testFile = new File(resource.getFile());
  }

  @Before
  public void initConfig() {
    try {
      config = parser.parseConfig(testFile);
      LOG.log(Level.INFO, "\r\n{0}", EditorConfigPrinter.logConfig(config));
    } catch (EditorConfigParserException ex) {
      LOG.log(Level.SEVERE, ex.getMessage());
    }
  }

  @Test
  public void parsesConfig() throws URISyntaxException, EditorConfigParserException {
    assertEquals("it parses the correct number of sections", config.size(), 5);
    assertEquals("it parses the correct number of properties per section", config.get(".*").size(), 4);
  }

  @Test
  public void matchesEverything() {
    String pattern = "*";

    assertEquals(true, parser.matches(pattern, "DocumentHandler"));
    assertEquals(true, parser.matches(pattern, "src/main/webapp/resources/js/wlc/DocumentHandler.js"));
    assertEquals(true, parser.matches(pattern, "src/main/webapp/resources/js/wlc/DocumentHandler.py"));
  }

  @Test
  public void matchesFileEndings() {
    String pattern = "*.js";

    assertEquals(true, parser.matches(pattern, "src/main/webapp/resources/js/wlc/DocumentHandler.js"));
    assertEquals(false, parser.matches(pattern, "src/main/webapp/resources/js/wlc/DocumentHandler.py"));
  }

  @Test
  public void matchesFileEndingsInSpecifiedDirectories() {
    String pattern = "lib/**.js";

    assertEquals(true, parser.matches(pattern, "src/main/lib/DocumentHandler.js"));
    assertEquals(true, parser.matches(pattern, "src/main/lib/sub/DocumentHandler.js"));
    assertEquals(false, parser.matches(pattern, "src/main/DocumentHandler.js"));
  }

  @Test
  public void matchesGivenStrings() {
    String pattern = "{package.json,.travis.yml}";

    assertEquals(true, parser.matches(pattern, "package.json"));
    assertEquals(true, parser.matches(pattern, ".travis.yml"));
    assertEquals(false, parser.matches(pattern, "travis.yml"));
    assertEquals(false, parser.matches(pattern, "src/package.json"));
  }

  @Test
  public void convertsUpperCaseValues() throws InvalidPropertyException {
    String line = "end_of_line = LF";
    EditorConfigProperty property = parser.parseProperty(line);

    assertEquals(EditorConfigConstant.END_OF_LINE, property.getKey());
    assertEquals(EditorConfigConstant.LINE_FEED, property.getValue());
  }

  @Test
  public void parsesValidProperty() throws InvalidPropertyException {
    String line = "indent_style = space";
    EditorConfigProperty property = parser.parseProperty(line);

    assertEquals(EditorConfigConstant.INDENT_STYLE, property.getKey());
    assertEquals(EditorConfigConstant.SPACE, property.getValue());
  }

  @Test
  public void handlesInvalidProperty() throws InvalidPropertyException {
    String line = "indent_style =";
    exception.expect(InvalidPropertyException.class);
    parser.parseProperty(line);
  }

  @Test
  public void skipsInvalidLines() {
    assertEquals(false, parser.isInterestingLine("; top-most EditorConfig file"));
    assertEquals(false, parser.isInterestingLine(" ; top-most EditorConfig file"));
    assertEquals(false, parser.isInterestingLine("; top-most EditorConfig file"));
    assertEquals(false, parser.isInterestingLine("# top-most EditorConfig file"));
    assertEquals(false, parser.isInterestingLine(" # top-most EditorConfig file"));
    assertEquals(false, parser.isInterestingLine("# top-most EditorConfig file"));
    assertEquals(false, parser.isInterestingLine(""));
    assertEquals(false, parser.isInterestingLine(" "));
    assertEquals(false, parser.isInterestingLine("  "));
    assertEquals(true, parser.isInterestingLine("end_of_line = lf"));
    assertEquals(true, parser.isInterestingLine("unknown_config = hello"));
  }

  @Test
  public void findsMatchingRules() {
    /**
     * 8x wildcard (*)<br/>
     * 3x JavaScript (*.js)<br/>
     * 1x JavaScript library (lib/**.js)<br/>
     * 1x .travis.yaml
     */
    int matchingRules = 13;
    int matchedRules = 0;

    for (String filePath : sampleFiles) {
      for (String regEx : config.keySet()) {
        boolean isMatching = parser.matches(regEx, filePath);
        if (isMatching) {
          matchedRules++;
          List<EditorConfigProperty> properties = config.get(regEx);
        }
      }
    }

    assertEquals(matchingRules, matchedRules);
  }

  @Test
  public void matchesIndentSizeRule() {
    int indentSize = 2;
    int indentSizeFound = -1;

    // Iterate project files
    for (String filePath : sampleFiles) {
      // Get RegEx definition from parsed EditorConfig file
      for (String regEx : config.keySet()) {
        // Check if path of project file matches the rule
        boolean isMatching = parser.matches(regEx, filePath);
        if (isMatching) {
          // If it matches the rule, get the declarations of the rule
          List<EditorConfigProperty> properties = config.get(regEx);
          // Check specific rule for test-case
          if (filePath.equals("src/main/webapp/resources/js/lib/jQuery.js")) {
            for (EditorConfigProperty property : properties) {

              String key = property.getKey();
              String value = property.getValue();

              switch (key) {
                case EditorConfigConstant.INDENT_SIZE:
                  indentSizeFound = Integer.valueOf(value);
                  break;
              }
            }
          }
        }
      }
    }

    assertEquals(indentSize, indentSizeFound);
  }

}
