package org.editorconfig.netbeans.parser;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.editorconfig.netbeans.model.EditorConfigProperty;
import org.junit.Test;
import static org.junit.Assert.*;

public class EditorConfigParserTest {

  private static final Logger LOG = Logger.getLogger(EditorConfigParserTest.class.getName());

  private final EditorConfigParser parser;

  private final String testFilePath = "org/editorconfig/example/editorconfig-test.ini";

  private final File testFile;

  private final String[] sampleFiles = new String[]{
    "src/main/webapp/categories.xhtml",
    "src/main/webapp/resources/js/wlc/DocumentHandler.js",
    "src/main/webapp/resources/js/wlc/Rollbar.js",
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

  @Test
  public void testParseConfig() throws URISyntaxException, EditorConfigParserException {
    Map<String, List<EditorConfigProperty>> config = parser.parseConfig(testFile);
    assertEquals("it parses the correct number of sections", config.size(), 5);
  }

  @Test
  public void testWildCardRegEx() {
    assertEquals(true, parser.matches("*", "DocumentHandler"));
    assertEquals(true, parser.matches("*", "src/main/webapp/resources/js/wlc/DocumentHandler.js"));
    assertEquals(true, parser.matches("*", "src/main/webapp/resources/js/wlc/DocumentHandler.py"));
  }

  @Test
  public void testFileEndingRegEx() {
    assertEquals(true, parser.matches("*.js", "src/main/webapp/resources/js/wlc/DocumentHandler.js"));
    assertEquals(false, parser.matches("*.js", "src/main/webapp/resources/js/wlc/DocumentHandler.py"));
  }

}
