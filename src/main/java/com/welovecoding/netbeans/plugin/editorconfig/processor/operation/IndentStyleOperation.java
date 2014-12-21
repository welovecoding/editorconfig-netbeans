package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import static com.welovecoding.netbeans.plugin.editorconfig.processor.EditorConfigProcessor.OPERATION_LOG_LEVEL;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.netbeans.api.editor.settings.SimpleValueNames;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

public class IndentStyleOperation {

  private static final Logger LOG = Logger.getLogger(IndentStyleOperation.class.getSimpleName());

  static {
    LOG.setLevel(OPERATION_LOG_LEVEL);
  }

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
      LOG.log(Level.INFO, "Set indent style to \"{0}\".", new Object[]{indentStyle});
      boolean expandTabs = false;
      if (indentStyle.equals(EditorConfigConstant.INDENT_STYLE_SPACE)) {
        expandTabs = true;
      }

      Preferences codeStyle = CodeStylePreferences.get(fileObject, fileObject.getMIMEType()).getPreferences();
      boolean currentValue = codeStyle.getBoolean(SimpleValueNames.EXPAND_TABS, false);
      LOG.log(Level.INFO, "Should expand tabs? {0}", expandTabs);
      LOG.log(Level.INFO, "current value for expand tabs? {0}", currentValue);

      if (currentValue != expandTabs) {
        codeStyle.putBoolean(SimpleValueNames.EXPAND_TABS, expandTabs);
        LOG.log(Level.INFO, "Action: Changed indent style to space? {0}", new Object[]{expandTabs});
        return true;
      } else {
        LOG.log(Level.INFO, "Action not needed: Indent style is already set to spaces \"{0}\".", new Object[]{currentValue});
        return false;
      }
    }
  }
}
