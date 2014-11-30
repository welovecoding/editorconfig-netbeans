package com.welovecoding.netbeans.plugin.editorconfig.processor.io;

import com.welovecoding.netbeans.plugin.editorconfig.io.writer.StyledDocumentWriter;
import com.welovecoding.netbeans.plugin.editorconfig.mapper.EditorConfigPropertyMapper;
import com.welovecoding.netbeans.plugin.editorconfig.io.exception.FileAccessException;
import com.welovecoding.netbeans.plugin.editorconfig.util.FileInfoReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

public class DocumentReaderWriterTest {

  public DocumentReaderWriterTest() {
  }

  @Test
  public void testReadAndWrite() throws IOException, FileAccessException {
    // create temp file
    File file = File.createTempFile("utf8-bom-crlf", ".txt");
    String ecCharset = "utf-8-bom";
    String ecLineEnding = "crlf";

    Charset javaCharset = EditorConfigPropertyMapper.mapCharset(ecCharset);
    String javaLineEnding = EditorConfigPropertyMapper.mapLineEnding(ecLineEnding);

    byte[] content;
    content = ("\uFEFFHello" + javaLineEnding + "World!").getBytes(javaCharset);

    // write temp file
    Files.write(file.toPath(), content, StandardOpenOption.CREATE);

    // read temp file
    FileObject fo = FileUtil.toFileObject(file);
    Charset guessedCharset = FileInfoReader.guessCharset(fo);

    assertEquals(javaCharset, guessedCharset);

    ArrayList<String> lines = StyledDocumentWriter.readFileObjectIntoLines(fo, javaCharset, ecLineEnding);

    // delete temp file
    file.delete();
  }

}
