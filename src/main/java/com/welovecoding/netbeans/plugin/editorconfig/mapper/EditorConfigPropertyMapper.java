package com.welovecoding.netbeans.plugin.editorconfig.mapper;

import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.editor.BaseDocument;

public class EditorConfigPropertyMapper {

  private static final Logger LOG = Logger.getLogger(EditorConfigPropertyMapper.class.getSimpleName());

  public synchronized static String mapLineEnding(String ecLineEnding) {
    String normalizedLineEnding;

    if (ecLineEnding == null) {
      return System.lineSeparator();
    }

    switch (ecLineEnding) {
      case EditorConfigConstant.END_OF_LINE_LF:
        normalizedLineEnding = BaseDocument.LS_LF;
        break;
      case EditorConfigConstant.END_OF_LINE_CR:
        normalizedLineEnding = BaseDocument.LS_CR;
        break;
      case EditorConfigConstant.END_OF_LINE_CRLF:
        normalizedLineEnding = BaseDocument.LS_CRLF;
        break;
      default:
        normalizedLineEnding = System.lineSeparator();
        break;
    }

    LOG.log(Level.INFO, "Using line ending: \"{0}\"", normalizedLineEnding);
    return normalizedLineEnding;
  }

  public synchronized static Charset mapCharset(String editorConfigCharset) {
    Charset javaCharset;

    if (editorConfigCharset == null) {
      return StandardCharsets.UTF_8;
    }

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

    LOG.log(Level.INFO, "Using charset: \"{0}\"", javaCharset);
    return javaCharset;
  }

  public synchronized static String getFileMark(String editorConfigCharset) {
    String fileMark = null;

    if (editorConfigCharset != null) {
      switch (editorConfigCharset) {
        case EditorConfigConstant.CHARSET_UTF_8_BOM:
          fileMark = "\uFEFF"; // "EF BB BF"
          break;
        case EditorConfigConstant.CHARSET_UTF_16_BE:
          fileMark = "\uFEFF"; // "FE FF"
          break;
        case EditorConfigConstant.CHARSET_UTF_16_LE:
          fileMark = "\uFEFF"; // "FF FE" (reverse)
          break;
      }
    }

    return fileMark;
  }
}
