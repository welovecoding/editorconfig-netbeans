package com.welovecoding.netbeans.plugin.editorconfig.mapper;

import com.welovecoding.netbeans.plugin.editorconfig.io.model.MappedCharset;
import com.welovecoding.netbeans.plugin.editorconfig.io.model.SupportedCharsets;
import com.welovecoding.netbeans.plugin.editorconfig.model.MappedEditorConfig;
import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.editorconfig.core.EditorConfig;
import org.editorconfig.core.EditorConfigException;
import org.netbeans.editor.BaseDocument;

public class EditorConfigPropertyMapper {

  private static final Logger LOG = Logger.getLogger(EditorConfigPropertyMapper.class.getSimpleName());

  /**
   * <b>Keyed Rules</b> <br/>
   * "charset": "utf-8"<br/>
   * "end_of_line": "lf"<br/>
   * "indent_size": "2"<br/>
   * "indent_style": "space"<br/>
   * "insert_final_newline": "true"<br/>
   * "tab_width": "2"<br/>
   * "trim_trailing_whitespace": "true"<br/>
   */
  public static synchronized MappedEditorConfig createEditorConfig(String filePath) {
    EditorConfig ec = new EditorConfig(".editorconfig", EditorConfig.VERSION);
    MappedEditorConfig mappedConfig = new MappedEditorConfig();

    List<EditorConfig.OutPair> rules = new ArrayList<>();
    HashMap<String, String> keyedRules = new HashMap<>();

    try {
      rules = ec.getProperties(filePath);
    } catch (EditorConfigException ex) {
      LOG.log(Level.SEVERE, ex.getMessage());
    }

    for (EditorConfig.OutPair rule : rules) {
      keyedRules.put(rule.getKey().toLowerCase(), rule.getVal().toLowerCase());
    }

    for (String key : keyedRules.keySet()) {
      String value = keyedRules.get(key);

      switch (key) {
        case "charset":
          MappedCharset charset = mapCharset(value);
          mappedConfig.setSupportedCharset(charset);
          break;
        case "end_of_line":
          String lineEnding = mapLineEnding(value);
          mappedConfig.setEndOfLine(lineEnding);
          break;
        case "indent_size":
          Integer indentSize = Integer.valueOf(value);
          mappedConfig.setIndentSize(indentSize);
          break;
        case "indent_style":
          mappedConfig.setIndentStyle(value);
          break;
        case "insert_final_newline":
          boolean insertFinalNewLine = Boolean.valueOf(value);
          mappedConfig.setInsertFinalNewLine(insertFinalNewLine);
          break;
        case "tab_width":
          int tabWidth = Integer.valueOf(value);
          mappedConfig.setTabWidth(tabWidth);
          break;
        case "trim_trailing_whitespace":
          boolean trimTrailingWhiteSpace = Boolean.valueOf(value);
          mappedConfig.setTrimTrailingWhiteSpace(trimTrailingWhiteSpace);
          break;
        default:
          LOG.log(Level.INFO, "Unknown EditorConfig property: {0} ({1})",
                  new Object[]{key, value});
      }
    }

    return mappedConfig;
  }

  protected static synchronized String getFileMark(String editorConfigCharset) {
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

  /**
   *
   * @param ecCharset latin1, utf-8, utf-8-bom, utf-16be or utf-16le
   * @return
   */
  protected static synchronized MappedCharset mapCharset(String ecCharset) {
    MappedCharset charset;

    if (ecCharset == null) {
      return SupportedCharsets.UTF_8;
    }

    switch (ecCharset) {
      case "latin1":
        charset = SupportedCharsets.LATIN_1;
        break;
      case "utf-8":
        charset = SupportedCharsets.UTF_8;
        break;
      case "utf-8-bom":
        charset = SupportedCharsets.UTF_8_BOM;
        break;
      case "utf-16be":
        charset = SupportedCharsets.UTF_16_BE;
        break;
      case "utf-16le":
        charset = SupportedCharsets.UTF_16_LE;
        break;
      default:
        charset = SupportedCharsets.UTF_8;
        LOG.log(Level.INFO, "Unsported charset: {0}. Using: {1}.",
                new Object[]{ecCharset, charset.getName()});
        break;
    }

    return charset;
  }

  protected static synchronized String mapLineEnding(String ecLineEnding) {
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
}
