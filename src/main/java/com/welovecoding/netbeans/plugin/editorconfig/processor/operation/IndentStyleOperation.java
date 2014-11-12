package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
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
public class IndentStyleOperation {

  private static final Logger LOG = Logger.getLogger(IndentStyleOperation.class.getName());

  /**
   * Changes {@code CodeStylePreferences}.
   *
   * @param dataObject
   * @param indentStyle
   * @return
   */
  public static boolean doIndentStyle(final DataObject dataObject, final String indentStyle) throws Exception {
    return new IndentStyleOperation().apply(dataObject, indentStyle).call();
  }

  public Callable<Boolean> apply(final DataObject dataObject, final String lineEnding) {
    return new ApplyIndentStyleTask(dataObject, lineEnding);
  }

  private class ApplyIndentStyleTask implements Callable<Boolean> {

    private final DataObject dataObject;
    private final String indentStyle;

    public ApplyIndentStyleTask(final DataObject dataObject, final String indentStyle) {
      this.dataObject = dataObject;
      this.indentStyle = indentStyle;
    }

    @Override
    public Boolean call() throws Exception {
      FileObject fileObject = dataObject.getPrimaryFile();
      LOG.log(Level.INFO, "{0}Set indent style to \"{1}\".", new Object[]{Tab.TWO, indentStyle});
      boolean expandTabs = false;
      if (indentStyle.equals(EditorConfigConstant.INDENT_STYLE_SPACE)) {
        expandTabs = true;
      }

      Preferences codeStyle = CodeStylePreferences.get(fileObject, fileObject.getMIMEType()).getPreferences();
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
}
