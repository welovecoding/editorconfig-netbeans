package com.welovecoding.netbeans.plugin.editorconfig.processor.function;

import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import com.welovecoding.netbeans.plugin.editorconfig.processor.Tab;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.netbeans.api.editor.settings.SimpleValueNames;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Michael Koppen
 */
public class IndentStyleFunction {

  private static final Logger LOG = Logger.getLogger(IndentStyleFunction.class.getName());

  public static boolean doIndentStyle(FileObject file, String value) {
    LOG.log(Level.INFO, "{0}Set indent style to \"{1}\".", new Object[]{Tab.TWO, value});
    boolean expandTabs = false;
    if (value.equals(EditorConfigConstant.INDENT_STYLE_SPACE)) {
      expandTabs = true;
    }

    Preferences codeStyle = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    boolean currentValue = codeStyle.getBoolean(SimpleValueNames.EXPAND_TABS, false);

    if (currentValue != expandTabs) {
      codeStyle.putBoolean(SimpleValueNames.EXPAND_TABS, expandTabs);
      LOG.log(Level.INFO, "{0}Action: Changed indent style to space? {1}", new Object[]{Tab.TWO, expandTabs});
      return true;
    } else {
      LOG.log(Level.INFO, "{0}Action not needed: Indent style is already set to spaces \"{1}\".", new Object[]{Tab.TWO, currentValue});
      return false;
    }
  }
}
