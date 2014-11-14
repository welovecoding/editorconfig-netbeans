package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.netbeans.api.editor.settings.SimpleValueNames;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

/**
 *
 * @author Michael Koppen
 */
public class XTabWidthOperation {

  private static final Logger LOG = Logger.getLogger(XTabWidthOperation.class.getName());

  /**
   * Changes {@code CodeStylePreferences}.
   *
   * @param dataObject
   * @param tabWidth
   * @return
   */
  public static boolean doTabWidth(final DataObject dataObject, final String tabWidth) throws Exception {
    return new XTabWidthOperation().apply(dataObject, Integer.valueOf(tabWidth));
  }

  public boolean apply(final DataObject dataObject, final int tabWidth) {
    FileObject fileObject = dataObject.getPrimaryFile();
    LOG.log(Level.INFO, "Set tab width to \"{0}\".", new Object[]{tabWidth});

    Preferences codeStyle = CodeStylePreferences.get(fileObject, fileObject.getMIMEType()).getPreferences();
    int actualTabWidth = codeStyle.getInt(SimpleValueNames.TAB_SIZE, -1);

    if (actualTabWidth != tabWidth) {
      codeStyle.putInt(SimpleValueNames.TAB_SIZE, tabWidth);
      LOG.log(Level.INFO, "Action: Changed tab width to \"{0}\".", new Object[]{tabWidth});
      return true;
    } else {
      LOG.log(Level.INFO, "Action not needed: Value is already \"{0}\".", new Object[]{tabWidth});
      return false;
    }
  }
}
