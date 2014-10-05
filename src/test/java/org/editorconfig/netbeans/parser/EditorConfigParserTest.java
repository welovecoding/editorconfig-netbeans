package org.editorconfig.netbeans.parser;

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
  }

  @Test
  public void testParseConfig() throws URISyntaxException {
    String testFilePath = "org/editorconfig/example/editorconfig-test.ini";
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(testFilePath);

    Map<String, List<EditorConfigProperty>> config = parser.parseConfig(resource);

    // Test number of sections
    assertEquals(config.size(), 5);
  }

}
