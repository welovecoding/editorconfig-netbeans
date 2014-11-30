package com.welovecoding.netbeans.plugin.editorconfig.io.reader;

import com.welovecoding.netbeans.plugin.editorconfig.io.model.FirstLineInfo;
import com.welovecoding.netbeans.plugin.editorconfig.io.model.SupportedCharsets;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Stream;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

/**
 * UTF-8 snowman:
 * http://www.fileformat.info/info/unicode/char/2603/index.htm<br/>
 * UTF-8 BOM: "EF BB BF"<br/>
 * UTF-16 BE: "FE FF"<br/>
 * UTF-16 LE: "FF FE"
 */
public class FileInfoReaderTest {

  private static final Logger LOG = Logger.getLogger(FileInfoReaderTest.class.getName());

  public FileInfoReaderTest() {
  }

  private File createLATIN_1_CR() {
    File file = null;
    String content = "Hello\rWorld!";

    try {
      file = File.createTempFile("latin-1-cr", ".txt");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, content.getBytes(StandardCharsets.ISO_8859_1), StandardOpenOption.CREATE);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return file;
  }

  private File createLATIN_1_CRLF() {
    File file = null;
    String content = "Hello\r\nWorld!";

    try {
      file = File.createTempFile("latin-1-crlf", ".txt");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, content.getBytes(StandardCharsets.ISO_8859_1), StandardOpenOption.CREATE);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return file;
  }

  private File createLATIN_1_LF() {
    File file = null;
    String content = "Hello\nWorld!";

    try {
      file = File.createTempFile("latin-1-lf", ".txt");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, content.getBytes(StandardCharsets.ISO_8859_1), StandardOpenOption.CREATE);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return file;
  }

  private File createUTF_16_BE_CR() {
    File file = null;
    String content = "\uFEFFHello\rWorld!";

    try {
      file = File.createTempFile("utf-16-be-cr", ".txt");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, content.getBytes(StandardCharsets.UTF_16BE), StandardOpenOption.CREATE);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return file;
  }

  private File createUTF_16_BE_CRLF() {
    File file = null;
    String content = "\uFEFFHello\r\nWorld!";

    try {
      file = File.createTempFile("utf-16-be-crlf", ".txt");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, content.getBytes(StandardCharsets.UTF_16BE), StandardOpenOption.CREATE);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return file;
  }

  private File createUTF_16_BE_LF() {
    File file = null;
    String content = "\uFEFFHello\nWorld!";

    try {
      file = File.createTempFile("utf-16-be-lf", ".txt");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, content.getBytes(StandardCharsets.UTF_16BE), StandardOpenOption.CREATE);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return file;
  }

  private File createUTF_16_LE_CR() {
    File file = null;
    String content = "\uFEFFHello\rWorld!";

    try {
      file = File.createTempFile("utf-16-le-cr", ".txt");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, content.getBytes(StandardCharsets.UTF_16LE), StandardOpenOption.CREATE);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return file;
  }

  private File createUTF_16_LE_CRLF() {
    File file = null;
    String content = "\uFEFFHello\r\nWorld!";

    try {
      file = File.createTempFile("utf-16-le-crlf", ".txt");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, content.getBytes(StandardCharsets.UTF_16LE), StandardOpenOption.CREATE);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return file;
  }

  private File createUTF_16_LE_LF() {
    File file = null;
    String content = "\uFEFFHello\nWorld!";

    try {
      file = File.createTempFile("utf-16-le-lf", ".txt");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, content.getBytes(StandardCharsets.UTF_16LE), StandardOpenOption.CREATE);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return file;
  }

  private File createUTF_8_BOM_CR() {
    File file = null;
    String content = "\uFEFFHello\rWorld!";

    try {
      file = File.createTempFile("utf-8-bom-cr", ".txt");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return file;
  }

  private File createUTF_8_BOM_CRLF() {
    File file = null;
    String content = "\uFEFFHello\r\nWorld!";

    try {
      file = File.createTempFile("utf-8-bom-crlf", ".txt");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return file;
  }

  private File createUTF_8_BOM_LF() {
    File file = null;
    String content = "\uFEFFHello\nWorld!";

    try {
      file = File.createTempFile("utf-8-bom-lf", ".txt");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return file;
  }

  private File createUTF_8_CR() {
    File file = null;
    String content = "Hello\rWorld!\u2603";

    try {
      file = File.createTempFile("utf-8-cr", ".txt");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return file;
  }

  private File createUTF_8_CRLF() {
    File file = null;
    String content = "Hello\r\nWorld!\u2603";

    try {
      file = File.createTempFile("utf-8-crlf", ".txt");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return file;
  }

  private File createUTF_8_LF() {
    File file = null;
    String content = "Hello\nWorld!\u2603";

    try {
      file = File.createTempFile("utf-8-lf", ".txt");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return file;
  }

  @Test
  public void guessEndcodingLATIN_1() {
    File file = createLATIN_1_CRLF();

    FileObject fo = FileUtil.toFileObject(file);
    Charset charset = FileInfoReader.guessCharset(fo);

    assertEquals(true, file.delete());
    assertEquals(StandardCharsets.ISO_8859_1, charset);
  }

  @Test
  public void guessEndcodingUTF_16BE_withFileMark() {
    File file = createUTF_16_BE_CRLF();
    FileObject fo = FileUtil.toFileObject(file);

    Charset charset = FileInfoReader.guessCharset(fo);

    assertEquals(true, file.delete());
    assertEquals(StandardCharsets.UTF_16BE, charset);
  }

  @Test
  public void guessEndcodingUTF_16LE_withFileMark() {
    File file = createUTF_16_LE_CRLF();
    FileObject fo = FileUtil.toFileObject(file);

    Charset charset = FileInfoReader.guessCharset(fo);

    assertEquals(true, file.delete());
    assertEquals(StandardCharsets.UTF_16LE, charset);
  }

  @Test(expected = AssertionError.class)
  public void guessEndcodingUTF_16LE_withoutFileMark() throws IOException {
    // Create temp file
    File file = File.createTempFile("utf-16-le", ".txt");
    Path path = Paths.get(Utilities.toURI(file));

    // Write temp file
    ArrayList<String> lines = new ArrayList<>();
    lines.add("Hello World");
    Files.write(path, lines, StandardCharsets.UTF_16LE, StandardOpenOption.CREATE);

    // Inspect file object
    FileObject fo = FileUtil.toFileObject(file);
    Charset charset = FileInfoReader.guessCharset(fo);

    assertEquals(true, file.delete());
    assertEquals(StandardCharsets.UTF_16LE, charset);
  }

  @Test
  public void guessEndcodingUTF_8() throws URISyntaxException {
    File file = createUTF_8_CRLF();
    FileObject fo = FileUtil.toFileObject(file);

    Charset charset = FileInfoReader.guessCharset(fo);

    assertEquals(true, file.delete());
    assertEquals(StandardCharsets.UTF_8, charset);
  }

  @Test
  public void guessEndcodingUTF_8_BOM() throws IOException {
    File file = createUTF_8_BOM_CRLF();
    FileObject fo = FileUtil.toFileObject(file);

    Charset charset = FileInfoReader.guessCharset(fo);

    assertEquals(true, file.delete());
    assertEquals(StandardCharsets.UTF_8, charset);
  }

  @Test
  public void readInfoLATIN_1_CR() throws IOException {
    File file = createLATIN_1_CR();
    FileObject fo = FileUtil.toFileObject(file);
    FirstLineInfo info = FileInfoReader.parseFirstLineInfo(fo);
    //
    assertEquals(true, file.delete());
    //
    assertEquals("\r", info.getLineEnding());
    assertEquals(SupportedCharsets.LATIN_1.getName(), info.getCharset().getName());
    assertEquals(false, info.isMarked());
  }

  @Test
  public void readInfoLATIN_1_CRLF() throws IOException {
    File file = createLATIN_1_CRLF();
    FileObject fo = FileUtil.toFileObject(file);
    FirstLineInfo info = FileInfoReader.parseFirstLineInfo(fo);
    //
    assertEquals(true, file.delete());
    //
    assertEquals("\r\n", info.getLineEnding());
    assertEquals(SupportedCharsets.LATIN_1.getName(), info.getCharset().getName());
    assertEquals(false, info.isMarked());
  }

  @Test
  public void readInfoLATIN_1_LF() throws IOException {
    File file = createLATIN_1_LF();
    FileObject fo = FileUtil.toFileObject(file);
    FirstLineInfo info = FileInfoReader.parseFirstLineInfo(fo);
    //
    assertEquals(true, file.delete());
    //
    assertEquals("\n", info.getLineEnding());
    assertEquals(SupportedCharsets.LATIN_1.getName(), info.getCharset().getName());
    assertEquals(false, info.isMarked());
  }

  @Test
  public void readInfoUTF_16_BE_CR() throws IOException {
    File file = createUTF_16_BE_CR();
    FileObject fo = FileUtil.toFileObject(file);
    FirstLineInfo info = FileInfoReader.parseFirstLineInfo(fo);
    //
    assertEquals(true, file.delete());
    //
    assertEquals("\r", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_16_BE.getName(), info.getCharset().getName());
    assertEquals(true, info.isMarked());
  }

  @Test
  public void readInfoUTF_16_BE_CRLF() throws IOException {
    File file = createUTF_16_BE_CRLF();
    FileObject fo = FileUtil.toFileObject(file);
    FirstLineInfo info = FileInfoReader.parseFirstLineInfo(fo);
    //
    assertEquals(true, file.delete());
    //
    assertEquals("\r\n", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_16_BE.getName(), info.getCharset().getName());
    assertEquals(true, info.isMarked());
  }

  @Test
  public void readInfoUTF_16_BE_LF() throws IOException {
    File file = createUTF_16_BE_LF();
    FileObject fo = FileUtil.toFileObject(file);
    FirstLineInfo info = FileInfoReader.parseFirstLineInfo(fo);
    //
    assertEquals(true, file.delete());
    //
    assertEquals("\n", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_16_BE.getName(), info.getCharset().getName());
    assertEquals(true, info.isMarked());
  }

  @Test
  public void readInfoUTF_16_LE_CR() throws IOException {
    File file = createUTF_16_LE_CR();
    FileObject fo = FileUtil.toFileObject(file);
    FirstLineInfo info = FileInfoReader.parseFirstLineInfo(fo);
    //
    assertEquals(true, file.delete());
    //
    assertEquals("\r", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_16_LE.getName(), info.getCharset().getName());
    assertEquals(true, info.isMarked());
  }

  @Test
  public void readInfoUTF_16_LE_CRLF() throws IOException {
    File file = createUTF_16_LE_CRLF();
    FileObject fo = FileUtil.toFileObject(file);
    FirstLineInfo info = FileInfoReader.parseFirstLineInfo(fo);
    //
    assertEquals(true, file.delete());
    //
    assertEquals("\r\n", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_16_LE.getName(), info.getCharset().getName());
    assertEquals(true, info.isMarked());
  }

  @Test
  public void readInfoUTF_16_LE_LF() throws IOException {
    File file = createUTF_16_LE_LF();
    FileObject fo = FileUtil.toFileObject(file);
    FirstLineInfo info = FileInfoReader.parseFirstLineInfo(fo);
    //
    assertEquals(true, file.delete());
    //
    assertEquals("\n", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_16_LE.getName(), info.getCharset().getName());
    assertEquals(true, info.isMarked());
  }

  @Test
  public void readInfoUTF_8_BOM_CR() throws IOException {
    File file = createUTF_8_BOM_CR();
    FileObject fo = FileUtil.toFileObject(file);
    FirstLineInfo info = FileInfoReader.parseFirstLineInfo(fo);
    //
    assertEquals(true, file.delete());
    //
    assertEquals("\r", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_8_BOM.getName(), info.getCharset().getName());
    assertEquals(true, info.isMarked());
  }

  @Test
  public void readInfoUTF_8_BOM_CRLF() throws IOException {
    File file = createUTF_8_BOM_CRLF();
    FileObject fo = FileUtil.toFileObject(file);
    FirstLineInfo info = FileInfoReader.parseFirstLineInfo(fo);
    //
    assertEquals(true, file.delete());
    //
    assertEquals("\r\n", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_8_BOM.getName(), info.getCharset().getName());
    assertEquals(true, info.isMarked());
  }

  @Test
  public void readInfoUTF_8_BOM_LF() throws IOException {
    File file = createUTF_8_BOM_LF();
    FileObject fo = FileUtil.toFileObject(file);
    FirstLineInfo info = FileInfoReader.parseFirstLineInfo(fo);
    //
    assertEquals(true, file.delete());
    //
    assertEquals("\n", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_8_BOM.getName(), info.getCharset().getName());
    assertEquals(true, info.isMarked());
  }

  @Test
  public void readInfoUTF_8_CR() throws IOException {
    File file = createUTF_8_CR();
    FileObject fo = FileUtil.toFileObject(file);
    FirstLineInfo info = FileInfoReader.parseFirstLineInfo(fo);
    //
    assertEquals(true, file.delete());
    //
    assertEquals("\r", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_8.getName(), info.getCharset().getName());
    assertEquals(false, info.isMarked());
  }

  @Test
  public void readInfoUTF_8_CRLF() throws IOException {
    File file = createUTF_8_CRLF();
    FileObject fo = FileUtil.toFileObject(file);
    FirstLineInfo info = FileInfoReader.parseFirstLineInfo(fo);
    //
    assertEquals(true, file.delete());
    //
    assertEquals("\r\n", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_8.getName(), info.getCharset().getName());
    assertEquals(false, info.isMarked());
  }

  @Test
  public void readInfoUTF_8_LF() throws IOException {
    File file = createUTF_8_LF();
    FileObject fo = FileUtil.toFileObject(file);
    FirstLineInfo info = FileInfoReader.parseFirstLineInfo(fo);
    //
    assertEquals(true, file.delete());
    //
    assertEquals("\n", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_8.getName(), info.getCharset().getName());
    assertEquals(false, info.isMarked());
  }

  @Test
  public void trimTrailingLineEndingCR() {
    Stream<String> stream = Stream.of("Hello\r", "World");
    String actual = FileInfoReader.trimTrailingWhitespace(stream, "\r");
    
    assertEquals("Hello\rWorld", actual);
  }

  @Test
  public void trimTrailingLineEndingCRLF() {
    Stream<String> stream = Stream.of("Hello\r\n", "World");
    String actual = FileInfoReader.trimTrailingWhitespace(stream, "\r\n");
    
    assertEquals("Hello\r\nWorld", actual);
  }

  @Test
  public void trimTrailingLineEndingLF() {
    Stream<String> stream = Stream.of("Hello\n", "World");
    String actual = FileInfoReader.trimTrailingWhitespace(stream, "\n");
    
    assertEquals("Hello\nWorld", actual);
  }

  @Test
  public void trimTrailingTab() {
    Stream<String> stream = Stream.of("Hello\t", "World");
    String actual = FileInfoReader.trimTrailingWhitespace(stream, System.lineSeparator());
    
    assertEquals("Hello" + System.lineSeparator() + "World", actual);
  }

  @Test
  public void trimTrailingTabs() {
    Stream<String> stream = Stream.of("Hello\t\t", "World");
    String actual = FileInfoReader.trimTrailingWhitespace(stream, System.lineSeparator());
    
    assertEquals("Hello" + System.lineSeparator() + "World", actual);
  }

  @Test
  public void trimTrailingWhitespace() {
    Stream<String> stream = Stream.of("Hello ", "World");
    String actual = FileInfoReader.trimTrailingWhitespace(stream, System.lineSeparator());
    
    assertEquals("Hello" + System.lineSeparator() + "World", actual);
  }

}
