package com.welovecoding.netbeans.plugin.editorconfig.util;

import com.glaforge.i18n.io.CharsetToolkit;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import org.junit.Ignore;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Utilities;

public class NetBeansFileUtilTest {

  public NetBeansFileUtilTest() {
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
  @Ignore
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

    Charset guessEncoding = CharsetToolkit.guessEncoding(Utilities.toFile(url.toURI()), 4096);
    Charset charset = NetBeansFileUtil.guessCharset(fo);

    assertEquals(StandardCharsets.UTF_16LE, guessEncoding);
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
