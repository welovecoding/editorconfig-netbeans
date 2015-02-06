package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import static com.welovecoding.netbeans.plugin.editorconfig.processor.EditorConfigProcessor.OPERATION_LOG_LEVEL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.filesystems.FileObject;

public abstract class CodeStyleOperation {

  private static final Logger LOG = Logger.getLogger(IndentSizeOperation.class.getSimpleName());
  protected FileObject file;

  public CodeStyleOperation() {
    LOG.setLevel(OPERATION_LOG_LEVEL);
  }

  public CodeStyleOperation(FileObject file) {
    super();
    this.file = file;
  }

  protected boolean operate(String simpleValueName, boolean value) {
    boolean codeStyleChangeNeeded = false;

    Preferences codeStyle = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    boolean currentValue = codeStyle.getBoolean(simpleValueName, false);

    LOG.log(Level.INFO, "\u00ac Current value: {0}", currentValue);
    LOG.log(Level.INFO, "\u00ac New value: {0}", value);

    if (currentValue != value) {
      codeStyle.putBoolean(simpleValueName, value);
      codeStyleChangeNeeded = true;
      LOG.log(Level.INFO, "\u00ac Changing value from \"{0}\" to \"{1}\"",
              new Object[]{currentValue, value});
    } else {
      LOG.log(Level.INFO, "\u00ac No change needed");
    }

    return codeStyleChangeNeeded;
  }

  protected boolean operate(String simpleValueName, int value) {
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
