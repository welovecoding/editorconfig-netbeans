package com.welovecoding.nbeditorconfig.processor.operation;

import org.netbeans.api.editor.settings.SimpleValueNames;
import org.openide.filesystems.FileObject;

public class IndentSizeOperation extends CodeStyleOperation {

  public IndentSizeOperation(FileObject file) {
    super(file);
  }

  /**
   * Changes the indent size of the NetBeans editor. This change affects only
   * the editor's view. Indent sizes of the actual file will not be changed. To
   * change the indent size for the file, a reformat of the code is needed
   * combined with saving the file. Reformatting and saving the file is part of
   * {@link com.welovecoding.netbeans.plugin.editorconfig.io.writer.StyledDocumentWriter#writeWithEditorKit}.
   *
   * @param value Indent size which should be set
   *
   * @return true if the operation was performed
   */
  public boolean changeIndentSize(int value) {
    if (value == -2) {
      return operate(SimpleValueNames.EXPAND_TABS, false);
    } else {
      return operate(SimpleValueNames.INDENT_SHIFT_WIDTH, value);
    }
  }

}
