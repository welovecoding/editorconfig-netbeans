package com.welovecoding.nbeditorconfig.processor.operation;

import org.netbeans.api.editor.settings.SimpleValueNames;
import org.openide.filesystems.FileObject;

public class TabWidthOperation extends CodeStyleOperation {

  public TabWidthOperation(FileObject file) {
    super(file);
  }

  public boolean changeTabWidth(int value) {
    return operate(SimpleValueNames.TAB_SIZE, value);
  }

}
