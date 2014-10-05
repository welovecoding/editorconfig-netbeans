package org.editorconfig.core;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EditorConfig handler
 *
 * @author Dennis.Ushakov
 */
public class EditorConfig {

  private static boolean DEBUG = System.getProperty("editorconfig.debug") != null;

  public static String VERSION = "0.11.3-final";

  private static final Pattern SECTION_PATTERN = Pattern.compile("\\s*\\[(([^#;]|\\\\#|\\\\;)+)]"
          + ".*"); // Python match searches from the line start
  private static final int HEADER = 1;

  private static final Pattern OPTION_PATTERN = Pattern.compile("\\s*([^:=\\s][^:=]*)\\s*[:=]\\s*(.*)");
  private static final int OPTION = 1;
  private static final int VAL = 2;

  private static final Pattern OPENING_BRACES = Pattern.compile("(?:^|[^\\\\])\\{");
  private static final Pattern CLOSING_BRACES = Pattern.compile("(?:^|[^\\\\])}");

  private final String configFilename;
  private final String version;

  /**
   * Creates EditorConfig handler with default configuration filename
   * (.editorconfig) and version {@link EditorConfig#VERSION}
   */
  public EditorConfig() {
    this(".editorconfig", VERSION);
  }

  /**
   * Creates EditorConfig handler with specified configuration filename and
   * version. Used mostly for debugging/testing.
   *
   * @param configFilename configuration file name to be searched for instead of
   * .editorconfig
   * @param version required version
   */
  public EditorConfig(String configFilename, String version) {
    this.configFilename = configFilename;
    this.version = version;
  }

  /**
   * Parse editorconfig files corresponding to the file path given by filename,
   * and return the parsing result.
   *
   * @param filePath The full path to be parsed. The path is usually the path of
   * the file which is currently edited by the editor.
   * @return The parsing result stored in a list of
   * {@link EditorConfig.OutPair}.
   * @throws org.editorconfig.core.ParsingException If an {@code .editorconfig}
   * file could not be parsed
   * @throws org.editorconfig.core.VersionException If version greater than
   * actual is specified in constructor
   * @throws org.editorconfig.core.EditorConfigException If an EditorConfig
   * exception occurs. Usually one of {@link ParsingException} or
   * {@link VersionException}
   */
  public List<OutPair> getProperties(String filePath) throws EditorConfigException {
    checkAssertions();
    Map<String, String> oldOptions = Collections.emptyMap();
    Map<String, String> options = new LinkedHashMap<String, String>();
    try {
      boolean root = false;
      String dir = new File(filePath).getParent();
      while (dir != null && !root) {
        String configPath = dir + "/" + configFilename;
        if (new File(configPath).exists()) {
          FileInputStream stream = new FileInputStream(configPath);
          InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
          BufferedReader bufferedReader = new BufferedReader(reader);
          try {
            root = parseFile(bufferedReader, dir + "/", filePath, options);
          } finally {
            bufferedReader.close();
            reader.close();
            stream.close();
          }
        }
        options.putAll(oldOptions);
        oldOptions = options;
        options = new LinkedHashMap<String, String>();
        dir = new File(dir).getParent();
      }
    } catch (IOException e) {
      throw new EditorConfigException(null, e);
    }

    preprocessOptions(oldOptions);

    final List<OutPair> result = new ArrayList<OutPair>();
    for (Map.Entry<String, String> keyValue : oldOptions.entrySet()) {
      result.add(new OutPair(keyValue.getKey(), keyValue.getValue()));
    }
    return result;
  }

  private void checkAssertions() throws VersionException {
    if (compareVersions(version, VERSION) > 0) {
      throw new VersionException("Required version is greater than the current version.");
    }
  }

  private static int compareVersions(String version1, String version2) {
    String[] version1Components = version1.split("(\\.|-)");
    String[] version2Components = version2.split("(\\.|-)");
    for (int i = 0; i < 3; i++) {
      String version1Component = version1Components[i];
      String version2Component = version2Components[i];
      int v1 = -1;
      int v2 = -1;
      try {
        v1 = Integer.parseInt(version1Component);
      } catch (NumberFormatException ignored) {
      }
      try {
        v2 = Integer.parseInt(version2Component);
      } catch (NumberFormatException ignored) {
      }
      if (v1 != v2) {
        return v1 - v2;
      }
    }
    return 0;
  }

  private void preprocessOptions(Map<String, String> options) {
    // Lowercase option value for certain options
    for (String key : new String[]{"end_of_line", "indent_style", "indent_size", "insert_final_newline",
      "trim_trailing_whitespace", "charset"}) {
      String value = options.get(key);
      if (value != null) {
        options.put(key, value.toLowerCase(Locale.US));
      }
    }

    // Set indent_size to "tab" if indent_size is unspecified and
    // indent_style is set to "tab".
    if ("tab".equals(options.get("indent_style")) && !options.containsKey("indent_size")
            && compareVersions(version, "0.10.0") >= 0) {
      options.put("indent_size", "tab");
    }

    // Set tab_width to indent_size if indent_size is specified and
    // tab_width is unspecified
    String indent_size = options.get("indent_size");
    if (indent_size != null && !"tab".equals(indent_size) && !options.containsKey("tab_width")) {
      options.put("tab_width", indent_size);
    }

    // Set indent_size to tab_width if indent_size is "tab"
    String tab_width = options.get("tab_width");
    if ("tab".equals(indent_size) && tab_width != null) {
      options.put("indent_size", tab_width);
    }
  }

  private static boolean parseFile(BufferedReader bufferedReader, String dirName, String filePath, Map<String, String> result) throws IOException, EditorConfigException {
    final StringBuilder malformedLines = new StringBuilder();
    boolean root = false;
    boolean inSection = false;
    boolean matchingSection = false;
    while (bufferedReader.ready()) {
      String line = bufferedReader.readLine().trim();

      if (line.startsWith("\ufeff")) {
        line = line.substring(1);
      }

      // comment or blank line?
      if (line.isEmpty() || line.startsWith("#") || line.startsWith(";")) {
        continue;
      }

      Matcher matcher = SECTION_PATTERN.matcher(line);
      if (matcher.matches()) {
        inSection = true;
        matchingSection = filenameMatches(dirName, matcher.group(HEADER), filePath);
        continue;
      }
      matcher = OPTION_PATTERN.matcher(line);
      if (matcher.matches()) {
        String key = matcher.group(OPTION).trim().toLowerCase(Locale.US);
        String value = matcher.group(VAL);
        value = value.equals("\"\"") ? "" : value;
        if (!inSection && "root".equals(key)) {
          root = true;
        } else if (matchingSection) {
          int commentPos = value.indexOf(" ;");
          commentPos = commentPos < 0 ? value.indexOf(" #") : commentPos;
          value = commentPos >= 0 ? value.substring(0, commentPos) : value;
          result.put(key, value);
        }
        continue;
      }
      malformedLines.append(line).append("\n");
    }
    if (malformedLines.length() > 0) {
      throw new ParsingException(malformedLines.toString(), null);
    }
    return root;
  }

  static boolean filenameMatches(String configDirname, String pattern, String filePath) {
    pattern = pattern.replace(File.separatorChar, '/');
    pattern = pattern.replaceAll("\\\\#", "#");
    pattern = pattern.replaceAll("\\\\;", ";");
    int separator = pattern.indexOf("/");
    if (separator >= 0) {
      pattern = configDirname + (separator == 0 ? pattern.substring(1) : pattern);
    } else {
      pattern = "**/" + pattern;
    }
    final ArrayList<int[]> ranges = new ArrayList<int[]>();
    final String regex = convertGlobToRegEx(pattern, ranges, false);
    if (DEBUG) {
      System.err.println(regex);
      for (int[] range : ranges) {
        System.err.println("numeric range: {" + range[0] + ".." + range[1] + "}");
      }
    }
    final Matcher matcher = Pattern.compile(regex).matcher(filePath);
    if (matcher.matches()) {
      for (int i = 0; i < matcher.groupCount(); i++) {
        final int[] range = ranges.get(i);
        final String numberString = matcher.group(i + 1);
        if (numberString == null || numberString.startsWith("0")) {
          return false;
        }
        int number = Integer.parseInt(numberString);
        if (number < range[0] || number > range[1]) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  static String convertGlobToRegEx(String pattern, ArrayList<int[]> ranges, boolean nested) {
    int length = pattern.length();
    StringBuilder result = new StringBuilder(length);
    int i = 0;
    int braceLevel = 0;
    boolean matchingBraces = countAll(OPENING_BRACES, pattern) == countAll(CLOSING_BRACES, pattern);
    boolean escaped = false;
    boolean inBrackets = false;
    while (i < length) {
      char current = pattern.charAt(i);
      i++;
      if ('*' == current) {
        if (i < length && pattern.charAt(i) == '*') {
          result.append(".*");
          i++;
        } else {
          result.append("[^/]*");
        }
      } else if ('?' == current) {
        result.append(".");
      } else if ('[' == current) {
        boolean seenSlash = findChar('/', ']', pattern, length, i) >= 0;
        if (seenSlash || escaped) {
          result.append("\\[");
        } else if (i < length && "!^".indexOf(pattern.charAt(i)) >= 0) {
          i++;
          result.append("[^");
        } else {
          result.append("[");
        }
        inBrackets = true;
      } else if (']' == current || ('-' == current && inBrackets)) {
        if (escaped) {
          result.append("\\");
        }
        result.append(current);
        inBrackets = current != ']' || escaped;
      } else if ('{' == current) {
        int j = findChar(',', '}', pattern, length, i);
        if (j < 0 && -j < length) {
          final String choice = pattern.substring(i, -j);
          final int[] range = getNumericRange(choice);
          if (range != null) {
            result.append("(\\d+)");
            ranges.add(range);
          } else {
            result = new StringBuilder(result);
            result.append("\\{");
            result.append(convertGlobToRegEx(choice, ranges, true));
            result.append("\\}");
          }
          i = -j + 1;
        } else if (matchingBraces) {
          result.append("(?:");
          braceLevel++;
        } else {
          result.append("\\{");
        }
      } else if (',' == current) {
        result.append(braceLevel > 0 && !escaped ? "|" : ",");
      } else if ('/' == current) {
        if (i < length && pattern.charAt(i) == '*') {
          if (i + 1 < length && pattern.charAt(i + 1) == '*'
                  && i + 2 < length && pattern.charAt(i + 2) == '/') {
            result.append("(?:/|/.*/)");
            i += 3;
          } else {
            result.append(current);
          }
        } else {
          result.append(current);
        }
      } else if ('}' == current) {
        if (braceLevel > 0 && !escaped) {
          result.append(")");
          braceLevel--;
        } else {
          result.append("}");
        }
      } else if ('\\' != current) {
        result.append(escapeToRegex(String.valueOf(current)));
      }
      if ('\\' == current) {
        if (escaped) {
          result.append("\\\\");
        }
        escaped = !escaped;
      } else {
        escaped = false;
      }
    }

    return result.toString();
  }

  private static int[] getNumericRange(String choice) {
    final int separator = choice.indexOf("..");
    if (separator < 0) {
      return null;
    }
    try {
      int start = Integer.parseInt(choice.substring(0, separator));
      int end = Integer.parseInt(choice.substring(separator + 2));
      return new int[]{start, end};
    } catch (NumberFormatException ignored) {
    }
    return null;
  }

  private static int findChar(final char c, final char stopAt, String pattern, int length, int start) {
    int j = start;
    boolean escapedChar = false;
    while (j < length && (pattern.charAt(j) != stopAt || escapedChar)) {
      if (pattern.charAt(j) == c && !escapedChar) {
        return j;
      }
      escapedChar = pattern.charAt(j) == '\\' && !escapedChar;
      j++;
    }
    return -j;
  }

  private static String escapeToRegex(String group) {
    final StringBuilder builder = new StringBuilder(group.length());
    for (char c : group.toCharArray()) {
      if (c == ' ' || Character.isLetter(c) || Character.isDigit(c) || c == '_' || c == '-') {
        builder.append(c);
      } else if (c == '\n') {
        builder.append("\\n");
      } else {
        builder.append("\\").append(c);
      }
    }
    return builder.toString();
  }

  private static int countAll(Pattern regex, String pattern) {
    final Matcher matcher = regex.matcher(pattern);
    int count = 0;
    while (matcher.find()) {
      count++;
    }
    return count;
  }

  /**
   * String-String pair to store the parsing result.
   */
  public static class OutPair {

    private final String key;
    private final String val;

    public OutPair(String key, String val) {
      this.key = key;
      this.val = val;
    }

    public String getKey() {
      return key;
    }

    public String getVal() {
      return val;
    }
  }
}
