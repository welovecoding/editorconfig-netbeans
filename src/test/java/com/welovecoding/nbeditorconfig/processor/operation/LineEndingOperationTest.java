package com.welovecoding.nbeditorconfig.processor.operation;

import com.welovecoding.nbeditorconfig.processor.operation.LineEndingOperation;
import com.welovecoding.nbeditorconfig.processor.FileInfo;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

public class LineEndingOperationTest {

  private DataObject dataObject = null;
  private File file;

  @Before
  public void setUp() throws DataObjectNotFoundException, URISyntaxException {
    String content = "alert('no final new line.');\n";

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

  public LineEndingOperationTest() {
  }

  /**
   * Test of operate method, of class LineEndingOperation.
   */
  @Test
  public void itChangesEndOfLine() throws IOException {
    StringBuilder content = new StringBuilder(dataObject.getPrimaryFile().asText());

    String expectedContent = content.toString() + "\r";

    FileInfo info = new FileInfo();
    info.setContent(content);
    info.setEndOfLine("\n\r");

    boolean wasPerformed = new LineEndingOperation().operate(info);

    assertEquals(true, wasPerformed);
    assertEquals(expectedContent, content.toString());
  }

}
