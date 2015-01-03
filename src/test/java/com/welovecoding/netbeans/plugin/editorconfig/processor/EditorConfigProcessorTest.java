package com.welovecoding.netbeans.plugin.editorconfig.processor;

import com.welovecoding.netbeans.plugin.editorconfig.io.model.MappedCharset;
import com.welovecoding.netbeans.plugin.editorconfig.model.MappedEditorConfig;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;

public class EditorConfigProcessorTest {

  private DataObject dataObject = null;

  @Before
  public void setUp() throws DataObjectNotFoundException, URISyntaxException {
    String path = "files/insert_final_newline/without-newline.js";
    URL url = Thread.currentThread().getContextClassLoader().getResource(path);
    Path testFilePath = Paths.get(url.toURI());
    dataObject = DataObject.find(FileUtil.toFileObject(testFilePath.toFile()));
  }

  public EditorConfigProcessorTest() {
  }

  @Test
  public void testExcuteOperations() throws Exception {
    MappedCharset charset = new MappedCharset(StandardCharsets.UTF_8.name());
    MappedEditorConfig config = new MappedEditorConfig();
    config.setCharset(charset);
    config.setEndOfLine(System.lineSeparator());
    config.setInsertFinalNewLine(true);

    EditorConfigProcessor proc = new EditorConfigProcessor();
    FileInfo info = proc.excuteOperations(dataObject, config);

    assertEquals(true, info.isFileChangeNeeded());
    
    // TODO: Execute the operation a second time and test that file change is
    // NOT needed (because it has been already performed)
  }

}
