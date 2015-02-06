package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import org.netbeans.api.editor.settings.SimpleValueNames;
import org.openide.filesystems.FileObject;

public class IndentSizeOperation extends CodeStyle {

  private IndentSizeOperation() {
    super();
  }

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
   * @param value
   * @return whether the operation has been performed
   */
  public boolean changeIndentSize(int value) {
    String simpleValueName = SimpleValueNames.INDENT_SHIFT_WIDTH;
    return operate(simpleValueName, value);
  }

}
