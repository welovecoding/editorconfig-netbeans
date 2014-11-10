package com.welovecoding.netbeans.plugin.editorconfig.processor.function;

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
public class TabWidthFunction {

  private static final Logger LOG = Logger.getLogger(TabWidthFunction.class.getName());

  public static boolean doTabWidth(FileObject file, String value) {
    int desiredTabWidth = Integer.valueOf(value);
    LOG.log(Level.INFO, "{0}Set tab width to \"{1}\".", new Object[]{Tab.TWO, desiredTabWidth});

    Preferences codeStyle = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    int actualTabWidth = codeStyle.getInt(SimpleValueNames.TAB_SIZE, -1);

    if (actualTabWidth != desiredTabWidth) {
      codeStyle.putInt(SimpleValueNames.TAB_SIZE, desiredTabWidth);
      LOG.log(Level.INFO, "{0}Action: Changed tab width to \"{1}\".", new Object[]{Tab.TWO, desiredTabWidth});
      return true;
    } else {
      LOG.log(Level.INFO, "{0}Action not needed: Value is already \"{1}\".", new Object[]{Tab.TWO, desiredTabWidth});
      return false;
    }
  }

}
