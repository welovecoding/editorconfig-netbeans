package com.welovecoding.nbeditorconfig.processor.operation;

import static com.welovecoding.nbeditorconfig.config.LoggerSettings.OPERATION_LOG_LEVEL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.filesystems.FileObject;

public abstract class CodeStyleOperation {

  private static final Logger LOG = Logger.getLogger(CodeStyleOperation.class.getName());
  protected FileObject file;

  static {
    LOG.setLevel(OPERATION_LOG_LEVEL);
  }

  public CodeStyleOperation(FileObject file) {
    this.file = file;
  }

  protected boolean operate(String simpleValueName, String value) {
    boolean codeStyleChangeNeeded = false;

    Preferences codeStyle = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    String currentValue = codeStyle.get(simpleValueName, "");

    LOG.log(Level.INFO, "\u00ac Current value: {0}", currentValue);
    LOG.log(Level.INFO, "\u00ac New value: {0}", value);

    if (currentValue.equals(value)) {
      LOG.log(Level.INFO, "\u00ac No change needed");
    } else {
      codeStyle.put(simpleValueName, value);
      codeStyleChangeNeeded = true;
      LOG.log(Level.INFO, "\u00ac Changing value from \"{0}\" to \"{1}\"",
              new Object[]{currentValue, value});
    }

    return codeStyleChangeNeeded;
  }

  protected boolean operate(String simpleValueName, boolean value) {
    boolean codeStyleChangeNeeded = false;

    Preferences codeStyle = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    boolean currentValue = codeStyle.getBoolean(simpleValueName, false);

    LOG.log(Level.INFO, "\u00ac Current value: {0}", currentValue);
    LOG.log(Level.INFO, "\u00ac New value: {0}", value);

    if (currentValue == value) {
      LOG.log(Level.INFO, "\u00ac No change needed");
    } else {
      codeStyle.putBoolean(simpleValueName, value);
      codeStyleChangeNeeded = true;
      LOG.log(Level.INFO, "\u00ac Changing value from \"{0}\" to \"{1}\"",
              new Object[]{currentValue, value});
    }

    return codeStyleChangeNeeded;
  }

  protected boolean operate(String simpleValueName, int value) {
    boolean codeStyleChangeNeeded = false;

    if (value < 0) {
      return false;
    }

    Preferences codeStyle = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    int currentValue = codeStyle.getInt(simpleValueName, -1);

    LOG.log(Level.INFO, "\u00ac Current value: {0}", currentValue);
    LOG.log(Level.INFO, "\u00ac New value: {0}", value);

    if (currentValue == value) {
      LOG.log(Level.INFO, "\u00ac No change needed");
    } else {
      codeStyle.putInt(simpleValueName, value);
      codeStyleChangeNeeded = true;
      LOG.log(Level.INFO, "\u00ac Changing value from \"{0}\" to \"{1}\"",
              new Object[]{currentValue, value});
    }

    return codeStyleChangeNeeded;
  }

}
