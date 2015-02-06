package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import static com.welovecoding.netbeans.plugin.editorconfig.processor.EditorConfigProcessor.OPERATION_LOG_LEVEL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.netbeans.api.editor.settings.SimpleValueNames;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.filesystems.FileObject;

public class CodeStyleOperation {

  private static final Logger LOG = Logger.getLogger(CodeStyleOperation.class.getSimpleName());
  private FileObject file;

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
  public boolean changeIndentSize(FileObject file, int value) {
    this.file = file;
    String simpleValueName = SimpleValueNames.INDENT_SHIFT_WIDTH;
    return operate(simpleValueName, value);
  }

  private boolean operate(String simpleValueName, int value) {
    boolean codeStyleChangeNeeded = false;

    Preferences codeStyle = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    int currentValue = codeStyle.getInt(simpleValueName, -1);

    LOG.log(Level.INFO, "\u00ac Current value: {0}", currentValue);
    LOG.log(Level.INFO, "\u00ac New value: {0}", value);

    if (currentValue != value) {
      codeStyle.putInt(simpleValueName, value);
      codeStyleChangeNeeded = true;
      LOG.log(Level.INFO, "\u00ac Changing value from \"{0}\" to \"{1}\"",
              new Object[]{currentValue, value});
    } else {
      LOG.log(Level.INFO, "\u00ac No change needed");
    }

    return codeStyleChangeNeeded;
  }
}
