package com.welovecoding.netbeans.plugin.editorconfig.util;

import com.glaforge.i18n.io.CharsetToolkit;
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
    Object fileEncoding = fo.getAttribute("ec.encoding");

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

  public static String trimTrailingWhitespace(Stream<String> lines, String lineEnding) {
    return lines.map((String content) -> {
      return content.replaceAll("\\s+$", "");
    }).collect(Collectors.joining(lineEnding));
  }
}
