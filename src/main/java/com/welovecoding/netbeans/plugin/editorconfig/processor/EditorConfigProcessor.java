package com.welovecoding.netbeans.plugin.editorconfig.processor;

import com.welovecoding.netbeans.plugin.editorconfig.mapper.EditorConfigPropertyMapper;
import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.CharsetOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.FinalNewLineOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.IndentSizeOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.IndentStyleOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.LineEndingOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.TabWidthOperation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.editorconfig.core.EditorConfig;
import org.editorconfig.core.EditorConfigException;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

/**
 *
 * @author Michael Koppen
 */
public class EditorConfigProcessor {

  private static final Logger LOG = Logger.getLogger(EditorConfigProcessor.class.getName());

//  private static final class InstanceHolder {
//
//    static final EditorConfigProcessor INSTANCE = new EditorConfigProcessor();
//  }
//
//  private EditorConfigProcessor() {
//  }
//  public static EditorConfigProcessor getInstance() {
//    return InstanceHolder.INSTANCE;
//  }
  public EditorConfigProcessor() {
  }

  private HashMap<String, String> parseRulesForFile(DataObject dataObject) {
    String filePath = dataObject.getPrimaryFile().getPath();

    LOG.log(Level.INFO, "Apply rules for: {0}", filePath);

    EditorConfig ec = new EditorConfig(".editorconfig", EditorConfig.VERSION);
    List<EditorConfig.OutPair> rules = new ArrayList<>();

    try {
      rules = ec.getProperties(filePath);
    } catch (EditorConfigException ex) {
      LOG.log(Level.SEVERE, ex.getMessage());
    }

    HashMap<String, String> keyedRules = new HashMap<>();
    StringBuilder props = new StringBuilder();
    for (EditorConfig.OutPair rule : rules) {
      props.append(rule.getKey().toLowerCase()).append(" : ").append(rule.getVal().toLowerCase());
      props.append("\n");
      keyedRules.put(rule.getKey().toLowerCase(), rule.getVal().toLowerCase());
    }
    System.out.println("");
    System.out.println(props.toString());
    System.out.println("");
    return keyedRules;
  }

  /**
   * Applies EditorConfig rules for the given file.
   *
   * @param dataObject
   */
  public void applyRulesToFile(DataObject dataObject) throws Exception {

    HashMap<String, String> keyedRules = parseRulesForFile(dataObject);

//    for (Map.Entry<String, String> entrySet : keyedRules.entrySet()) {
//      String key = entrySet.getKey();
//      String value = entrySet.getValue();
//      System.out.println(key + "      :" + value);
//    }
    FileObject fileObject = dataObject.getPrimaryFile();
    boolean changedStyle = false;
    boolean changed = false;

    for (Map.Entry<String, String> rule : keyedRules.entrySet()) {
      final String key = rule.getKey();
      final String value = rule.getValue();

      LOG.log(Level.INFO, "{0}Found rule \"{1}\" with value \"{2}\".", new Object[]{Tab.ONE, key, value});

      switch (key) {
        case EditorConfigConstant.CHARSET:
          CharsetOperation.doCharset(dataObject, value,
                  EditorConfigPropertyMapper.normalizeLineEnding(
                          keyedRules.get(EditorConfigConstant.END_OF_LINE)));
          break;
        case EditorConfigConstant.END_OF_LINE:
          LineEndingOperation.doLineEnding(dataObject,
                  EditorConfigPropertyMapper.normalizeLineEnding(value));
          break;
        case EditorConfigConstant.INDENT_SIZE:
          changed = IndentSizeOperation.doIndentSize(dataObject, value);
          changedStyle = changedStyle || changed;
          break;
        case EditorConfigConstant.INDENT_STYLE:
          changed = IndentStyleOperation.doIndentStyle(dataObject, value);
          changedStyle = changedStyle || changed;
          break;
        case EditorConfigConstant.INSERT_FINAL_NEWLINE:
          FinalNewLineOperation.doFinalNewLine(dataObject,
                  EditorConfigPropertyMapper.normalizeLineEnding(
                          keyedRules.get(EditorConfigConstant.END_OF_LINE)));
          break;
        case EditorConfigConstant.TAB_WIDTH:
          changed = TabWidthOperation.doTabWidth(dataObject, value);
          changedStyle = changedStyle || changed;
          break;
        default:
          LOG.log(Level.WARNING, "Unknown property: {0}", key);
      }
    }

    Preferences codeStyle = CodeStylePreferences.get(fileObject, fileObject.getMIMEType()).getPreferences();

    if (changedStyle) {
      try {
        codeStyle.flush();
      } catch (BackingStoreException ex) {
        LOG.log(Level.SEVERE, "Error applying code style: {0}", ex.getMessage());
      }
    }

  }
}
