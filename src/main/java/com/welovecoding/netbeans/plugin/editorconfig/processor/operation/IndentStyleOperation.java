package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import org.netbeans.api.editor.settings.SimpleValueNames;
import org.openide.filesystems.FileObject;

public class IndentStyleOperation extends CodeStyleOperation {

  private IndentStyleOperation() {
    super();
  }

  public IndentStyleOperation(FileObject file) {
    super(file);
  }

  public boolean changeIndentStyle(String value) {
    if (value.equals(EditorConfigConstant.INDENT_STYLE_SPACE)) {
      return operate(SimpleValueNames.EXPAND_TABS, true);
    } else {
      return operate(SimpleValueNames.EXPAND_TABS, false);
    }
  }

}
