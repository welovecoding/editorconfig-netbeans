package com.welovecoding.netbeans.plugin.editorconfig.processor.function;

import com.welovecoding.netbeans.plugin.editorconfig.mapper.EditorConfigPropertyMapper;
import com.welovecoding.netbeans.plugin.editorconfig.mapper.EditorConfigPropertyMappingException;
import com.welovecoding.netbeans.plugin.editorconfig.processor.Tab;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.StyledDocument;
import org.netbeans.editor.BaseDocument;
import org.openide.loaders.DataObject;
import org.openide.text.NbDocument;

public class LineEndingFunction {

  private static final Logger LOG = Logger.getLogger(LineEndingFunction.class.getName());

  public static boolean doLineEnding(DataObject dataObject, String value) {
    String normalizedLineEnding;
    try {
      normalizedLineEnding = EditorConfigPropertyMapper.normalizeLineEnding(value);
    } catch (EditorConfigPropertyMappingException ex) {
      normalizedLineEnding = System.lineSeparator();
      LOG.log(Level.WARNING, ex.getMessage());
    }
    StyledDocument document = NbDocument.getDocument(dataObject);

    if (document != null) {
      if (!document.getProperty(BaseDocument.READ_LINE_SEPARATOR_PROP).equals(normalizedLineEnding)) {
        document.putProperty(BaseDocument.READ_LINE_SEPARATOR_PROP, normalizedLineEnding);
        LOG.log(Level.INFO, "{0}Action: Changed line endings to \"{1}\".", new Object[]{Tab.TWO, value});
        return true;
      } else {
        LOG.log(Level.INFO, "{0}Action not needed: Line endings are already \"{1}\".", new Object[]{Tab.TWO, value});
        return false;
      }
    }
    return false;
  }

}
