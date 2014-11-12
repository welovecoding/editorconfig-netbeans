package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import com.welovecoding.netbeans.plugin.editorconfig.processor.Tab;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.StyledDocument;
import org.netbeans.editor.BaseDocument;
import org.openide.loaders.DataObject;
import org.openide.text.NbDocument;

public class LineEndingOperation {

  private static final Logger LOG = Logger.getLogger(LineEndingOperation.class.getName());

  public static boolean doLineEnding(final DataObject dataObject, final String lineEnding) throws Exception {
    return new LineEndingOperation().apply(dataObject, lineEnding).call();
  }

  public Callable<Boolean> apply(final DataObject dataObject, final String lineEnding) {
    return new ApplyLineEndingTask(dataObject, lineEnding);
  }

  private class ApplyLineEndingTask implements Callable<Boolean> {

    private final DataObject dataObject;
    private final String lineEnding;

    public ApplyLineEndingTask(final DataObject dataObject, final String lineEnding) {
      this.dataObject = dataObject;
      this.lineEnding = lineEnding;
    }

    @Override
    public Boolean call() throws Exception {
      StyledDocument document = NbDocument.getDocument(dataObject);

      if (document != null) {
        if (!document.getProperty(BaseDocument.READ_LINE_SEPARATOR_PROP).equals(lineEnding)) {
          document.putProperty(BaseDocument.READ_LINE_SEPARATOR_PROP, lineEnding);
          LOG.log(Level.INFO, "{0}Action: Changed line endings to \"{1}\".", new Object[]{Tab.TWO, lineEnding});
          return true;
        } else {
          LOG.log(Level.INFO, "{0}Action not needed: Line endings are already \"{1}\".", new Object[]{Tab.TWO, lineEnding});
          return false;
        }
      }
      return false;
    }
  }
}
