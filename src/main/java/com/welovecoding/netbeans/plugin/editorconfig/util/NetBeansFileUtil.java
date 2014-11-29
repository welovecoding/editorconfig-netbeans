package com.welovecoding.netbeans.plugin.editorconfig.util;

import com.glaforge.i18n.io.CharsetToolkit;
import static com.welovecoding.netbeans.plugin.editorconfig.config.Settings.ENCODING_SETTING;
import com.welovecoding.netbeans.plugin.editorconfig.processor.io.FirstLineInfo;
import com.welovecoding.netbeans.plugin.editorconfig.processor.io.SupportedCharset;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    Charset charset = StandardCharsets.UTF_8;
    Object fileEncoding = fo.getAttribute(ENCODING_SETTING);

    if (fileEncoding == null) {
      try {
        charset = CharsetToolkit.guessEncoding(Utilities.toFile(fo.toURI()), 4096);
        if (charset.name().equals("US-ASCII")) {
          charset = StandardCharsets.ISO_8859_1;
        }
      } catch (IllegalArgumentException | IOException ex) {
        Exceptions.printStackTrace(ex);
      }
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

  public static String readFirstLine(File file) {
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      return br.readLine();
    } catch (IOException ex) {
      return "";
    }
  }

  private static String detectLineEnding(String line) {
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

  public static String detectLineEnding(File file) {
    String firstLine = readFirstLine(file);
    String lineEnding = NetBeansFileUtil.detectLineEnding(firstLine);
    return lineEnding;
  }

  public static FirstLineInfo detectCharset(File file) {
    Charset charset = NetBeansFileUtil.guessCharset(file);
    SupportedCharset supportedCharset;
    String charsetName = charset.name();
    String firstLine = readFirstLine(file);
    String lineEnding = NetBeansFileUtil.detectLineEnding(firstLine);
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
