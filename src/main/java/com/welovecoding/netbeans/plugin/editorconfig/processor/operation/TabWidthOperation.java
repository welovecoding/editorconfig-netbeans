package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import com.welovecoding.netbeans.plugin.editorconfig.processor.Tab;
import java.util.concurrent.Callable;
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
public class TabWidthOperation {

  private static final Logger LOG = Logger.getLogger(TabWidthOperation.class.getName());

  /**
   * Changes {@code CodeStylePreferences}.
   *
   * @param dataObject
   * @param tabWidth
   * @return
   */
  public static boolean doTabWidth(final DataObject dataObject, final String tabWidth) throws Exception {
    return new FinalNewLineOperation().apply(dataObject, tabWidth).call();
  }

  public Callable<Boolean> apply(final DataObject dataObject, final String tabWidth) {
    return new ApplyTabWidthTask(dataObject, tabWidth);
  }

  private class ApplyTabWidthTask implements Callable<Boolean> {

    private final DataObject dataObject;
    private final int tabWidth;

    public ApplyTabWidthTask(final DataObject dataObject, final String tabWidth) {
      this.dataObject = dataObject;
      this.tabWidth = Integer.valueOf(tabWidth);
    }

    @Override
    public Boolean call() throws Exception {
      FileObject fileObject = dataObject.getPrimaryFile();
      LOG.log(Level.INFO, "{0}Set tab width to \"{1}\".", new Object[]{Tab.TWO, tabWidth});

      Preferences codeStyle = CodeStylePreferences.get(fileObject, fileObject.getMIMEType()).getPreferences();
      int actualTabWidth = codeStyle.getInt(SimpleValueNames.TAB_SIZE, -1);

      if (actualTabWidth != tabWidth) {
        codeStyle.putInt(SimpleValueNames.TAB_SIZE, tabWidth);
        LOG.log(Level.INFO, "{0}Action: Changed tab width to \"{1}\".", new Object[]{Tab.TWO, tabWidth});
        return true;
      } else {
        LOG.log(Level.INFO, "{0}Action not needed: Value is already \"{1}\".", new Object[]{Tab.TWO, tabWidth});
        return false;
      }
    }
  }

}
