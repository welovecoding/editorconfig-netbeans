package com.welovecoding.netbeans.plugin.editorconfig.util;

import com.glaforge.i18n.io.CharsetToolkit;
import static com.welovecoding.netbeans.plugin.editorconfig.config.Settings.ENCODING_SETTING;
import com.welovecoding.netbeans.plugin.editorconfig.processor.io.FirstLineInfo;
import com.welovecoding.netbeans.plugin.editorconfig.processor.io.SupportedCharset;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

/**
 * @see
 * <a href="http://www.w3.org/TR/REC-xml/#sec-guessing-no-ext-info">Detection
 * Without External Encoding Information</a>
 */
public class NetBeansFileUtil {

  /**
   * @see
   * <a href="https://github.com/4ndrew/monqjfa/blob/master/monq/stuff/EncodingDetector.java">EncodingDetector.java</a>
   *
   * @param fo
   * @return
   */
  public static Charset guessCharset(FileObject fo) {
    Charset charset;
    Object fileEncoding = fo.getAttribute(ENCODING_SETTING);

    if (fileEncoding == null) {
      File file = Utilities.toFile(fo.toURI());
      charset = guessCharset(file);
    } else {
      charset = Charset.forName((String) fileEncoding);
    }

    return charset;
  }

  public static Charset guessCharset(File file) {
    Charset charset = StandardCharsets.UTF_8;

    try {
      charset = CharsetToolkit.guessEncoding(file, 4096);
      if (charset.name().equals("US-ASCII")) {
        charset = StandardCharsets.ISO_8859_1;
      }
    } catch (IllegalArgumentException | IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return charset;
  }

  public static String trimTrailingWhitespace(Stream<String> lines, String lineEnding) {
    return lines.map((String content) -> {
      return content.replaceAll("\\s+$", "");
    }).collect(Collectors.joining(lineEnding));
  }

  @Deprecated
  public static String readFirstLine(File file) {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
      return br.readLine();
    } catch (IOException ex) {
      return "";
    }
  }

  /**
   * Reads the first line of a file with it's termination sequence. A
   * termination sequence can be a line feed ('\n'), a carriage return ('\r'),
   * or a carriage return followed immediately by a linefeed.
   *
   * @param file
   * @param charset
   *
   * @return First line of a file.
   */
  public static String readFirstLineWithSeparator(File file, Charset charset) {
    StringBuilder sb = new StringBuilder();
    String firstLine;
    int c;

    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) {
      // Read first line
      while ((c = br.read()) != -1) {
        if (c == '\r') {
          // Mac OS
          sb.append('\r');
          // Windows
          if (br.read() == '\n') {
            sb.append('\n');
          }
          break;
        } else if (c == '\n') {
          // Mac OS X
          sb.append('\n');
          break;
        } else {
          sb.append((char) c);
        }
      }

      firstLine = sb.toString();

    } catch (IOException ex) {
      firstLine = "";
    }

    return firstLine;
  }

  protected static String detectLineEnding(String line) {
    String lineEnding = System.lineSeparator();

    if (line.endsWith("\r\n")) {
      lineEnding = "\r\n";
    } else if (line.endsWith("\n")) {
      lineEnding = "\n";
    } else if (line.endsWith("\r")) {
      lineEnding = "\r";
    }

    return lineEnding;
  }

  @Deprecated
  public static String detectLineEnding(File file) {
    String firstLine = readFirstLine(file);
    String lineEnding = NetBeansFileUtil.detectLineEnding(firstLine);
    return lineEnding;
  }

  /**
   * Die Mutter aller Funktionen!
   *
   * @param file
   * @return
   */
  public static FirstLineInfo parseFirstLineInfo(File file) {
    Charset charset = NetBeansFileUtil.guessCharset(file);
    SupportedCharset supportedCharset;
    String charsetName = charset.name();
    String firstLine = readFirstLineWithSeparator(file, charset);
    String lineEnding = detectLineEnding(firstLine);
    boolean marked = false;

    if (charset.equals(StandardCharsets.UTF_8)
            && firstLine.startsWith(SupportedCharset.FILE_MARK)) {
      charsetName = "UTF-8-BOM";
      marked = true;
    } else if (charset.equals(StandardCharsets.UTF_16BE)
            && firstLine.startsWith(SupportedCharset.FILE_MARK)) {
      marked = true;
    } else if (charset.equals(StandardCharsets.UTF_16LE)
            && firstLine.startsWith(SupportedCharset.FILE_MARK)) {
      marked = true;
    }

    supportedCharset = new SupportedCharset(charsetName);

    return new FirstLineInfo(supportedCharset, lineEnding, marked);
  }
}
