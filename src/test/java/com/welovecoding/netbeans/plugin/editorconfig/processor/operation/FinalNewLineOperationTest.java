package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import com.welovecoding.netbeans.plugin.editorconfig.processor.FileInfo;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

public class FinalNewLineOperationTest {

  private DataObject dataObject = null;
  private File file;

  @Before
  public void setUp() throws DataObjectNotFoundException, URISyntaxException {
    String content = "alert('no final new line.');";

    try {
      file = File.createTempFile(this.getClass().getSimpleName(), ".js");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
      dataObject = DataObject.find(FileUtil.toFileObject(file));
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  @After
  public void tearDown() {
    file.delete();
  }

  public FinalNewLineOperationTest() {
  }

  @Test
  public void itAddsAFinalNewLine() throws IOException {
    StringBuilder content = new StringBuilder(dataObject.getPrimaryFile().asText());

    String expectedContent = content.toString();
    expectedContent += System.lineSeparator();

    FileInfo info = new FileInfo();
    info.setContent(content);
    info.setEndOfLine(System.lineSeparator());

    boolean wasPerformed = new FinalNewLineOperation().run(info);

    assertEquals(true, wasPerformed);
    assertEquals(expectedContent, content.toString());
  }

}
