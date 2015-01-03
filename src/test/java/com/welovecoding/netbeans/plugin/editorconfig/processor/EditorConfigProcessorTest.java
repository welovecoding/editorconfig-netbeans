package com.welovecoding.netbeans.plugin.editorconfig.processor;

import com.welovecoding.netbeans.plugin.editorconfig.io.model.MappedCharset;
import com.welovecoding.netbeans.plugin.editorconfig.model.MappedEditorConfig;
import java.io.File;
import java.io.IOException;
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
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

public class EditorConfigProcessorTest {

  private DataObject dataObject = null;
  private File file;

  @Before
  public void setUp() {
    String content = "alert('Hello World! or Καλημέρα κόσμε! or こんにちは 世界!');";

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

  public EditorConfigProcessorTest() {
  }

  @Test
  public void itDoesNotLoopOnFinalNewLineOperation() throws Exception {
    MappedCharset charset = new MappedCharset(StandardCharsets.UTF_8.name());
    MappedEditorConfig config = new MappedEditorConfig();
    config.setCharset(charset);
    config.setEndOfLine(System.lineSeparator());
    config.setInsertFinalNewLine(true);

    EditorConfigProcessor proc = new EditorConfigProcessor();
    FileInfo info = proc.excuteOperations(dataObject, config);
    assertEquals(true, info.isFileChangeNeeded());

    /* 
     Run the processor a second time and test that a file change is NOT 
     needed (because it has been already performed during the first run).
     */
    proc.flushFile(info);
    info = proc.excuteOperations(dataObject, config);
    assertEquals(false, info.isFileChangeNeeded());

  }

}
