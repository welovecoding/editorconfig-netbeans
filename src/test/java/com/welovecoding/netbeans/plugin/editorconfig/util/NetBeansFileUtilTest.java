package com.welovecoding.netbeans.plugin.editorconfig.util;

import com.welovecoding.netbeans.plugin.editorconfig.processor.io.FirstLineInfo;
import com.welovecoding.netbeans.plugin.editorconfig.processor.io.SupportedCharsets;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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
 * UTF-8 snowman: http://www.fileformat.info/info/unicode/char/2603/index.htm
 * UTF-8 BOM: "EF BB BF"
 */
public class NetBeansFileUtilTest {

  private static final Logger LOG = Logger.getLogger(NetBeansFileUtilTest.class.getName());

  public NetBeansFileUtilTest() {
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

  @Test
  public void testUTF8_BOM_CRLF() throws IOException {
    File file = createUTF_8_BOM_CRLF();
    FirstLineInfo info = NetBeansFileUtil.parseFirstLineInfo(file);
    //
    assertEquals("\r\n", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_8_BOM.getName(), info.getCharset().getName());
    assertEquals(true, info.isMarked());
    //
    assertEquals(true, file.delete());
  }

  @Test
  public void testUTF8_CR() throws IOException {
    File file = createUTF_8_CR();
    FirstLineInfo info = NetBeansFileUtil.parseFirstLineInfo(file);
    //
    assertEquals("\r", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_8.getName(), info.getCharset().getName());
    assertEquals(false, info.isMarked());
    //
    assertEquals(true, file.delete());
  }

  @Test
  public void testUTF8_LF() throws IOException {
    File file = createUTF_8_LF();
    FirstLineInfo info = NetBeansFileUtil.parseFirstLineInfo(file);
    //
    assertEquals("\n", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_8.getName(), info.getCharset().getName());
    assertEquals(false, info.isMarked());
    //
    assertEquals(true, file.delete());
  }

  @Test
  public void testUTF8_CRLF() throws IOException {
    File file = createUTF_8_CRLF();
    FirstLineInfo info = NetBeansFileUtil.parseFirstLineInfo(file);
    //
    assertEquals("\r\n", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_8.getName(), info.getCharset().getName());
    assertEquals(false, info.isMarked());
    //
    assertEquals(true, file.delete());
  }

  @Test
  public void testLATIN_1_CR() throws IOException {
    File file = createLATIN_1_CR();
    FirstLineInfo info = NetBeansFileUtil.parseFirstLineInfo(file);
    //
    assertEquals("\r", info.getLineEnding());
    assertEquals(SupportedCharsets.LATIN_1.getName(), info.getCharset().getName());
    assertEquals(false, info.isMarked());
    //
    assertEquals(true, file.delete());
  }

  @Test
  public void testLATIN_1_LF() throws IOException {
    File file = createLATIN_1_LF();
    FirstLineInfo info = NetBeansFileUtil.parseFirstLineInfo(file);
    //
    assertEquals("\n", info.getLineEnding());
    assertEquals(SupportedCharsets.LATIN_1.getName(), info.getCharset().getName());
    assertEquals(false, info.isMarked());
    //
    assertEquals(true, file.delete());
  }

  @Test
  public void testLATIN_1_CRLF() throws IOException {
    File file = createLATIN_1_CRLF();
    FirstLineInfo info = NetBeansFileUtil.parseFirstLineInfo(file);
    //
    assertEquals("\r\n", info.getLineEnding());
    assertEquals(SupportedCharsets.LATIN_1.getName(), info.getCharset().getName());
    assertEquals(false, info.isMarked());
    //
    assertEquals(true, file.delete());
  }

  @Test
  public void testUTF8_BOM_CR() throws IOException {
    File file = createUTF_8_BOM_CR();
    FirstLineInfo info = NetBeansFileUtil.parseFirstLineInfo(file);
    //
    assertEquals("\r", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_8_BOM.getName(), info.getCharset().getName());
    assertEquals(true, info.isMarked());
    //
    assertEquals(true, file.delete());
  }

  @Test
  public void testUTF8_BOM_LF() throws IOException {
    File file = createUTF_8_BOM_LF();
    FirstLineInfo info = NetBeansFileUtil.parseFirstLineInfo(file);
    //
    assertEquals("\n", info.getLineEnding());
    assertEquals(SupportedCharsets.UTF_8_BOM.getName(), info.getCharset().getName());
    assertEquals(true, info.isMarked());
    //
    assertEquals(true, file.delete());
  }

  @Test
  public void testISO_8859_1() throws URISyntaxException {
    String path = "files/charsets/latin1.txt";
    URL url = Thread.currentThread().getContextClassLoader().getResource(path);
    Path testFilePath = Paths.get(url.toURI());
    FileObject fo = FileUtil.toFileObject(testFilePath.toFile());

    Charset charset = NetBeansFileUtil.guessCharset(fo);

    assertEquals(StandardCharsets.ISO_8859_1, charset);
  }

  @Test
  public void testUTF_8() throws URISyntaxException {
    String path = "files/charsets/utf-8.txt";
    URL url = Thread.currentThread().getContextClassLoader().getResource(path);
    Path testFilePath = Paths.get(url.toURI());
    FileObject fo = FileUtil.toFileObject(testFilePath.toFile());

    Charset charset = NetBeansFileUtil.guessCharset(fo);

    assertEquals(StandardCharsets.UTF_8, charset);
  }

  @Test
  public void testUTF_8BOM() throws URISyntaxException {
    String path = "files/charsets/utf-8-bom.txt";
    URL url = Thread.currentThread().getContextClassLoader().getResource(path);
    Path testFilePath = Paths.get(url.toURI());
    FileObject fo = FileUtil.toFileObject(testFilePath.toFile());

    Charset charset = NetBeansFileUtil.guessCharset(fo);

    assertEquals(StandardCharsets.UTF_8, charset);
  }

  @Test
  public void testUTF_16BE() throws URISyntaxException {
    String path = "files/charsets/utf-16-be.txt";
    URL url = Thread.currentThread().getContextClassLoader().getResource(path);
    Path testFilePath = Paths.get(url.toURI());
    FileObject fo = FileUtil.toFileObject(testFilePath.toFile());

    Charset charset = NetBeansFileUtil.guessCharset(fo);

    assertEquals(StandardCharsets.UTF_16BE, charset);
  }

  @Test
  public void testUTF_16LE() throws URISyntaxException, IOException {
    String path = "files/charsets/utf-16-le.txt";
    URL url = Thread.currentThread().getContextClassLoader().getResource(path);
    Path testFilePath = Paths.get(url.toURI());
    FileObject fo = FileUtil.toFileObject(testFilePath.toFile());

    Charset charset = NetBeansFileUtil.guessCharset(fo);

    assertEquals(StandardCharsets.UTF_16LE, charset);
  }

  @Test(expected = AssertionError.class)
  public void itDoesntRecognize_UTF_16LE_withoutFileMark() throws IOException {
    // Create temp file
    File file = File.createTempFile("utf-16-le", ".txt");
    Path path = Paths.get(Utilities.toURI(file));

    // Write temp file
    ArrayList<String> lines = new ArrayList<>();
    lines.add("Hello World");
    Files.write(path, lines, StandardCharsets.UTF_16LE, StandardOpenOption.CREATE);

    // Inspect file object
    FileObject fo = FileUtil.toFileObject(file);
    Charset charset = NetBeansFileUtil.guessCharset(fo);

    assertEquals(true, file.delete());
    assertEquals(StandardCharsets.UTF_16LE, charset);
  }

  @Test
  public void itRecognizes_UTF_8_withFileMark() throws IOException {
    // Create temp file
    File file = File.createTempFile("utf-8-bom", ".txt");
    Path path = Paths.get(Utilities.toURI(file));

    // Write temp file
    ArrayList<String> lines = new ArrayList<>();
    lines.add("\uFEFF"); // -> generates "EF BB BF"
    lines.add("Hello World");
    Files.write(path, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE);

    // Inspect file object
    FileObject fo = FileUtil.toFileObject(file);
    Charset charset = NetBeansFileUtil.guessCharset(fo);

//    assertEquals(true, file.delete());
    assertEquals(StandardCharsets.UTF_8, charset);
  }

  @Test
  public void itRecognizes_UTF_16LE_withFileMark() throws IOException {
    // Create temp file
    File file = File.createTempFile("utf-16-le", ".txt");
    Path path = Paths.get(Utilities.toURI(file));

    // Write temp file
    ArrayList<String> lines = new ArrayList<>();
    lines.add("\uFEFF"); // -> generates "FF FE" (it's a bit confusing!)
    lines.add("Hello World");
    Files.write(path, lines, StandardCharsets.UTF_16LE, StandardOpenOption.CREATE);

    // Inspect file object
    FileObject fo = FileUtil.toFileObject(file);
    Charset charset = NetBeansFileUtil.guessCharset(fo);

    assertEquals(true, file.delete());
    assertEquals(StandardCharsets.UTF_16LE, charset);
  }

  @Test
  public void itRecognizes_UTF_16BE_withFileMark() throws IOException {
    // Create temp file
    File file = File.createTempFile("utf-16-be", ".txt");
    Path path = Paths.get(Utilities.toURI(file));

    // Write temp file
    ArrayList<String> lines = new ArrayList<>();
    lines.add("\uFEFF"); // -> generates "FE FF"
    lines.add("Hello World");
    Files.write(path, lines, StandardCharsets.UTF_16BE, StandardOpenOption.CREATE);

    // Inspect file object
    FileObject fo = FileUtil.toFileObject(file);
    Charset charset = NetBeansFileUtil.guessCharset(fo);

    assertEquals(true, file.delete());
    assertEquals(StandardCharsets.UTF_16BE, charset);
  }

  @Test
  public void testTrimTrailingWhitespace() {
    Stream<String> stream = Stream.of("Hello ", "World");
    String actual = NetBeansFileUtil.trimTrailingWhitespace(stream, System.lineSeparator());

    assertEquals("Hello" + System.lineSeparator() + "World", actual);
  }

  @Test
  public void testTrimTrailingTab() {
    Stream<String> stream = Stream.of("Hello\t", "World");
    String actual = NetBeansFileUtil.trimTrailingWhitespace(stream, System.lineSeparator());

    assertEquals("Hello" + System.lineSeparator() + "World", actual);
  }

  @Test
  public void testTrimTrailingTabs() {
    Stream<String> stream = Stream.of("Hello\t\t", "World");
    String actual = NetBeansFileUtil.trimTrailingWhitespace(stream, System.lineSeparator());

    assertEquals("Hello" + System.lineSeparator() + "World", actual);
  }

  @Test
  public void testTrimTrailingLineEndingCR() {
    Stream<String> stream = Stream.of("Hello\r", "World");
    String actual = NetBeansFileUtil.trimTrailingWhitespace(stream, "\r");

    assertEquals("Hello\rWorld", actual);
  }

  @Test
  public void testTrimTrailingLineEndingLF() {
    Stream<String> stream = Stream.of("Hello\n", "World");
    String actual = NetBeansFileUtil.trimTrailingWhitespace(stream, "\n");

    assertEquals("Hello\nWorld", actual);
  }

  @Test
  public void testTrimTrailingLineEndingCRLF() {
    Stream<String> stream = Stream.of("Hello\r\n", "World");
    String actual = NetBeansFileUtil.trimTrailingWhitespace(stream, "\r\n");

    assertEquals("Hello\r\nWorld", actual);
  }

}
