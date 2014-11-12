package com.welovecoding.netbeans.plugin.editorconfig.processor;

import com.welovecoding.netbeans.plugin.editorconfig.mapper.EditorConfigPropertyMapper;
import com.welovecoding.netbeans.plugin.editorconfig.mapper.EditorConfigPropertyMappingException;
import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.CharsetOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.FinalNewLineOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.IndentSizeOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.IndentStyleOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.LineEndingOperation;
import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.TabWidthOperation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

  private static final class InstanceHolder {

    static final EditorConfigProcessor INSTANCE = new EditorConfigProcessor();
  }

  private EditorConfigProcessor() {
  }

  public static EditorConfigProcessor getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public void applyEditorConfigRules(DataObject dataObject) {
    String filePath = dataObject.getPrimaryFile().getPath();

    LOG.log(Level.INFO, "Apply rules for: {0}", filePath);

    EditorConfig ec = new EditorConfig(".editorconfig", EditorConfig.VERSION);
    List<EditorConfig.OutPair> rules = new ArrayList<>();

    HashMap<String, String> keyedRules = new HashMap<>();
    for (EditorConfig.OutPair rule : rules) {
      keyedRules.put(rule.getKey().toLowerCase(), rule.getVal().toLowerCase());
    }

    try {
      rules = ec.getProperties(filePath);
    } catch (EditorConfigException ex) {
      LOG.log(Level.SEVERE, ex.getMessage());
    }

    FileObject primaryFile = dataObject.getPrimaryFile();
    boolean changedStyle = false;
    boolean changed = false;

    for (EditorConfig.OutPair rule : rules) {
      String key = rule.getKey().toLowerCase();
      String value = rule.getVal().toLowerCase();

      LOG.log(Level.INFO, "{0}Found rule \"{1}\" with value \"{2}\".", new Object[]{Tab.ONE, key, value});

      switch (key) {
        case EditorConfigConstant.CHARSET:
          changed = CharsetOperation.doCharset(dataObject, value, getLineEnding(keyedRules.get(EditorConfigConstant.END_OF_LINE)));
          changedStyle = changedStyle || changed;
          break;
        case EditorConfigConstant.END_OF_LINE:
          changed = LineEndingOperation.doLineEnding(dataObject, value);
          changedStyle = changedStyle || changed;
          break;
        case EditorConfigConstant.INDENT_SIZE:
          changed = IndentSizeOperation.doIndentSize(primaryFile, value);
          changedStyle = changedStyle || changed;
          break;
        case EditorConfigConstant.INDENT_STYLE:
          changed = IndentStyleOperation.doIndentStyle(primaryFile, value);
          changedStyle = changedStyle || changed;
          break;
        case EditorConfigConstant.INSERT_FINAL_NEWLINE:
          changed = FinalNewLineOperation.doFinalNewLine(primaryFile, getLineEnding(keyedRules.get(EditorConfigConstant.END_OF_LINE)));
          changedStyle = changedStyle || changed;
          break;
        case EditorConfigConstant.TAB_WIDTH:
          changed = TabWidthOperation.doTabWidth(primaryFile, value);
          changedStyle = changedStyle || changed;
          break;
        default:
          LOG.log(Level.WARNING, "Unknown property: {0}", key);
      }
    }

    Preferences codeStyle = CodeStylePreferences.get(primaryFile, primaryFile.getMIMEType()).getPreferences();

    if (changedStyle) {
      try {
        codeStyle.flush();
      } catch (BackingStoreException ex) {
        LOG.log(Level.SEVERE, "Error applying code style: {0}", ex.getMessage());
      }
    }

  }

  private String getLineEnding(String ecLineEnding) {
    String javaLineEnding = ecLineEnding;
    try {
      javaLineEnding = EditorConfigPropertyMapper.normalizeLineEnding(javaLineEnding);
    } catch (EditorConfigPropertyMappingException ex) {
      javaLineEnding = System.lineSeparator();
      String printableLineEnding = Arrays.toString(javaLineEnding.toCharArray());
      LOG.log(Level.WARNING, "{0} Using default line ending: {1}",
              new Object[]{ex.getMessage(), printableLineEnding});
    }
    return javaLineEnding;
  }
}
