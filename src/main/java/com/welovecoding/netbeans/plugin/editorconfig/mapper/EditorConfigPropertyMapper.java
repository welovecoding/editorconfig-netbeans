package com.welovecoding.netbeans.plugin.editorconfig.mapper;

import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.netbeans.editor.BaseDocument;

public class EditorConfigPropertyMapper {

  public static String normalizeLineEnding(String lineEnding) {
    String normalizedLineEnding = System.getProperty("line.separator", "\r\n");

    if (lineEnding == null) {
      return normalizedLineEnding;
    }

    switch (lineEnding) {
      case EditorConfigConstant.END_OF_LINE_LF:
        normalizedLineEnding = BaseDocument.LS_LF;
        break;
      case EditorConfigConstant.END_OF_LINE_CR:
        normalizedLineEnding = BaseDocument.LS_CR;
        break;
      case EditorConfigConstant.END_OF_LINE_CRLF:
        normalizedLineEnding = BaseDocument.LS_CRLF;
        break;
    }

    return normalizedLineEnding;
  }

  public static Charset mapCharset(String editorConfigCharset) {
    Charset javaCharset;

    switch (editorConfigCharset) {
      case EditorConfigConstant.CHARSET_LATIN_1:
        javaCharset = StandardCharsets.ISO_8859_1;
        break;
      case EditorConfigConstant.CHARSET_UTF_16_BE:
        javaCharset = StandardCharsets.UTF_16BE;
        break;
      case EditorConfigConstant.CHARSET_UTF_16_LE:
        javaCharset = StandardCharsets.UTF_16LE;
        break;
      default:
        javaCharset = StandardCharsets.UTF_8;
        break;
    }

    return javaCharset;
  }
}
