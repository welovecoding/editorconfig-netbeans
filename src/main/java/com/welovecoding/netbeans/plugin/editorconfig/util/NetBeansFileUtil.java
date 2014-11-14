package com.welovecoding.netbeans.plugin.editorconfig.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.netbeans.api.queries.FileEncodingQuery;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

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

    // Try if the file is ASCII only
    if (isASCII(fo)) {
      charset = StandardCharsets.ISO_8859_1;
    }

    // Read first four bytes and try to get the Byte Order Mark
    int tag = 0;
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(fo.getInputStream()));) {
      for (int i = 0; i < 4; i++) {
        int b = reader.read();

        if (b < 0) {
          break;
        }

        tag = (tag << 8) | b;
      }

      if ((tag & 0xfeff0000) == 0xfeff0000) {
        charset = StandardCharsets.UTF_16BE;
      }

      if ((tag & 0xfffe0000) == 0xfffe0000) {
        charset = StandardCharsets.UTF_16LE;
      }

      if ((tag & 0xefbbbf00) == 0xefbbbf00) {
        // UTF-8-BOM
        charset = StandardCharsets.UTF_8;
      }

    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return charset;
  }

  private static boolean isASCII(FileObject fo) {
    boolean isASCII = true;
    String content;

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(fo.getInputStream()));) {
      content = reader.lines().collect(Collectors.joining(System.lineSeparator()));
      for (int i = 0; i < content.length(); i++) {
        int c = content.charAt(i);
        if (c > 0x7F) {
          isASCII = false;
          break;
        }
      }
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return isASCII;
  }

  public static String trimTrailingWhitespace(Stream<String> lines, String lineEnding) {
    return lines.map((String content) -> {
      return content.replaceAll("\\s+$", "");
    }).collect(Collectors.joining(lineEnding));
  }

  /**
   * TODO: It looks like "FileEncodingQuery.getEncoding" always returns "UTF-8".
   *
   * Even if the charset of that file is already UTF-16LE. Therefore we should
   * change our charset lookup. After the charset has been changed by us, we add
   * a file attribute which helps us to detect the charset in future.
   *
   * Maybe we should use a CharsetDetector:
   * http://userguide.icu-project.org/conversion/detection
   *
   * @param fo
   * @return
   */
  public static Charset getCharset(FileObject fo, boolean bla) {
    Object fileEncoding = fo.getAttribute("ec.encoding");

    if (fileEncoding == null) {
      Charset currentCharset = FileEncodingQuery.getEncoding(fo);
      fileEncoding = currentCharset.name();
    }

    return Charset.forName((String) fileEncoding);
  }

}
