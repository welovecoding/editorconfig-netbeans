package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

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
public class IndentSizeOperation {

  private static final Logger LOG = Logger.getLogger(IndentSizeOperation.class.getName());

  /**
   * Changes {@code CodeStylePreferences}.
   *
   * @param dataObject
   * @param indentSize
   * @return
   */
  public static boolean doIndentSize(final DataObject dataObject, final String indentSize) throws Exception {
    return new IndentSizeOperation().apply(dataObject, indentSize).call();
  }

  public Callable<Boolean> apply(final DataObject dataObject, final String indentSize) {
    return new ApplyIndentSizeTask(dataObject, indentSize);
  }

  private class ApplyIndentSizeTask implements Callable<Boolean> {

    private final DataObject dataObject;
    private final int indentSize;

    public ApplyIndentSizeTask(final DataObject dataObject, final String indentSize) {
      this.dataObject = dataObject;
      this.indentSize = Integer.valueOf(indentSize);
    }

    @Override
    public Boolean call() throws Exception {
      FileObject fileObject = dataObject.getPrimaryFile();

      LOG.log(Level.INFO, "Set indent size to \"{1}\".", new Object[]{indentSize});

      Preferences codeStyle = CodeStylePreferences.get(fileObject, fileObject.getMIMEType()).getPreferences();
      int currentValue = codeStyle.getInt(SimpleValueNames.INDENT_SHIFT_WIDTH, -1);

      if (currentValue != indentSize) {
        codeStyle.putInt(SimpleValueNames.INDENT_SHIFT_WIDTH, indentSize);
        LOG.log(Level.INFO, "Action: Change indent size to \"{1}\".", new Object[]{indentSize});
        return true;
      } else {
        LOG.log(Level.INFO, "Action not needed: Value is already \"{1}\".", new Object[]{currentValue});
        return false;
      }

    }

  }
}
