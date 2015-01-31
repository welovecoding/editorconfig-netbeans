package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import static com.welovecoding.netbeans.plugin.editorconfig.processor.EditorConfigProcessor.OPERATION_LOG_LEVEL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.netbeans.api.editor.settings.SimpleValueNames;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.filesystems.FileObject;

public class IndentSizeOperation {

  private static final Logger LOG = Logger.getLogger(IndentSizeOperation.class.getSimpleName());

  static {
    LOG.setLevel(OPERATION_LOG_LEVEL);
  }

  /**
   * Changes the indent size of the NetBeans editor. This change affects only
   * the editor's view. Indent sizes of the actual file will not be changed. To
   * change the indent size for the file, a reformat of the code is needed
   * combined with saving the file. Reformatting and saving the file is part of
   * {@link com.welovecoding.netbeans.plugin.editorconfig.io.writer.StyledDocumentWriter#writeWithEditorKit}.
   *
   * @param file
   * @param value
   * @return whether the operation has been performed
   */
  public boolean operate(FileObject file, int value) {
    boolean changedIndentSize = false;

    Preferences codeStyle = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    int currentValue = codeStyle.getInt(SimpleValueNames.INDENT_SHIFT_WIDTH, -1);

    LOG.log(Level.INFO, "\u00ac Current indent size: {0}", currentValue);

    if (currentValue != value) {
      codeStyle.putInt(SimpleValueNames.INDENT_SHIFT_WIDTH, value);
      changedIndentSize = true;
      LOG.log(Level.INFO, "\u00ac Changing indent size from \"{0}\" to \"{1}\"",
              new Object[]{currentValue, value});
    } else {
      LOG.log(Level.INFO, "\u00ac No change needed");
    }

    return changedIndentSize;
  }
}
