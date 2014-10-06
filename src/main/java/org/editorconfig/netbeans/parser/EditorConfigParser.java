package org.editorconfig.netbeans.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.editorconfig.netbeans.model.EditorConfigProperty;

public class EditorConfigParser {

  private static final Logger LOG = Logger.getLogger(EditorConfigParser.class.getName());
  private Map<String, List<EditorConfigProperty>> result;

  public EditorConfigParser() {
  }

  public Map<String, List<EditorConfigProperty>> parseConfig(File file) throws EditorConfigParserException {
    result = new HashMap<>();
    String section = null;
    if (file != null) {

      String line;

      try (
              FileInputStream fis = new FileInputStream(file);
              InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
              BufferedReader br = new BufferedReader(isr)) {
        while ((line = br.readLine()) != null) {
          boolean isInteresting = !(line.startsWith("#") || line.isEmpty());

          if (isInteresting) {
            if (line.startsWith("[")) {
              section = parseSection(line);
            } else if (section != null) {
              EditorConfigProperty property = parseProperty(line);
              addProperty(section, property);
            }
          }
        }
      } catch (IOException ex) {
        LOG.log(Level.SEVERE, "Error reading file: {0}", ex.getMessage());
      }
    } else {
      throw new EditorConfigParserException("Given file cannot be found.");
    }

    return result;
  }

  private void addProperty(String section, EditorConfigProperty property) {
    List<EditorConfigProperty> properties = getProperties(section);
    properties.add(property);
  }

  private List<EditorConfigProperty> getProperties(String section) {
    List<EditorConfigProperty> properties = result.get(section);
    if (properties == null) {
      properties = new ArrayList<>();
      result.put(section, properties);
    }

    return properties;
  }

  private String parseSection(String line) {
    String regex = line.substring(1, line.lastIndexOf("]"));
    return convertRegEx(regex);
  }

  private String convertRegEx(String regEx) {
    final String CASE_1 = "*";
    final String CASE_2 = "**.";
    final String CASE_3 = "*.";
    final String CASE_4 = "{";

    String expression = regEx;
    String template;
    String value;

    if (regEx.equals(CASE_1)) {
      template = ".*";

      expression = template;
    } else if (regEx.indexOf(CASE_2) > 0) {
      template = "(.*)({0})(.*?)\\.{1}$";

      String startsWith = regEx.substring(0, regEx.indexOf(CASE_2));
      String endsWith = regEx.substring(startsWith.length() + CASE_2.length());

      expression = MessageFormat.format(template, new Object[]{
        startsWith, endsWith
      });
    } else if (regEx.startsWith(CASE_3)) {
      template = "^(.*)\\.{0}$";

      value = regEx.substring(CASE_3.length(), regEx.length());

      expression = MessageFormat.format(template, value);
    } else if (regEx.startsWith(CASE_4)) {
      template = "({0})";

      value = regEx.substring(1, regEx.length() - 1);
      String[] fileNames = value.split(",");
      String names = String.join("|", fileNames);

      expression = MessageFormat.format(template, names);
    }

    return expression;
  }

  public boolean matches(String regEx, String filePath) {
    String javaRegEx = convertRegEx(regEx);

    Pattern pattern = Pattern.compile(javaRegEx);
    Matcher matcher = pattern.matcher(filePath);

    return matcher.matches();
  }

  private EditorConfigProperty parseProperty(String line) {
    String[] splitted = line.split("=");
    String key = splitted[0].trim();
    String value = splitted[1].trim();

    return new EditorConfigProperty(key, value);
  }
}
