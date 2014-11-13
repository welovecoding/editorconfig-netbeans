package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Ignore;
import org.junit.Test;
import org.netbeans.junit.NbTestCase;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

public class CharsetOperationTest extends NbTestCase {

  public CharsetOperationTest() {
    super("CharsetOperationTest");
  }

  @Test
  @Ignore
  public void testSetup() throws FileNotFoundException, IOException {
    File file = new File("files/utf-8-bom.txt");
    FileObject fo = FileUtil.toFileObject(file.getAbsoluteFile());
    assertEquals(file.getName(), fo.getName());
  }

}
