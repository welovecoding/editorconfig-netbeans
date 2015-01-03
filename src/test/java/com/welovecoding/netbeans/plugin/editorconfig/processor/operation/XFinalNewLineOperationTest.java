package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;

public class XFinalNewLineOperationTest {

  private DataObject dataObject = null;

  @Before
  public void setUp() throws DataObjectNotFoundException, URISyntaxException {
    String path = "files/insert_final_newline/without-newline.js";
    URL url = Thread.currentThread().getContextClassLoader().getResource(path);
    Path testFilePath = Paths.get(url.toURI());
    dataObject = DataObject.find(FileUtil.toFileObject(testFilePath.toFile()));
  }

  @After
  public void tearDown() {
  }

  public XFinalNewLineOperationTest() {
  }

  @Test
  public void testDoFinalNewLine() throws Exception {
    StringBuilder content = new StringBuilder(dataObject.getPrimaryFile().asText());

    String expectedContent = content.toString();
    expectedContent += System.lineSeparator();

    boolean wasPerformed = XFinalNewLineOperation.doFinalNewLine(content, true, System.lineSeparator());

    assertEquals(true, wasPerformed);
    assertEquals(expectedContent, content.toString());
  }

}
