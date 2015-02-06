package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import org.netbeans.api.editor.settings.SimpleValueNames;
import org.openide.filesystems.FileObject;

public class TabWidthOperation extends CodeStyleOperation {

  private TabWidthOperation() {
    super();
  }

  public TabWidthOperation(FileObject file) {
    super(file);
  }

  public boolean changeTabWidth(int value) {
    return operate(SimpleValueNames.TAB_SIZE, value);
  }

}
