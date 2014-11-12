package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

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
public class IndentSizeOperation {

  private static final Logger LOG = Logger.getLogger(IndentSizeOperation.class.getName());

  public static boolean doIndentSize(FileObject file, String value) {
    int indentSize = Integer.valueOf(value);

    LOG.log(Level.INFO, "{0}Set indent size to \"{1}\".", new Object[]{Tab.TWO, indentSize});

    Preferences codeStyle = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    int currentValue = codeStyle.getInt(SimpleValueNames.INDENT_SHIFT_WIDTH, -1);

    if (currentValue != indentSize) {
      codeStyle.putInt(SimpleValueNames.INDENT_SHIFT_WIDTH, indentSize);
      LOG.log(Level.INFO, "{0}Action: Change indent size to \"{1}\".", new Object[]{Tab.TWO, indentSize});
      return true;
    } else {
      LOG.log(Level.INFO, "{0}Action not needed: Value is already \"{1}\".", new Object[]{Tab.TWO, currentValue});
      return false;
    }
  }

}
