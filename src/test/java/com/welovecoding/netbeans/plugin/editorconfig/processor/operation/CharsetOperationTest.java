package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import com.welovecoding.netbeans.plugin.editorconfig.util.NetBeansFileUtil;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.netbeans.junit.NbTestCase;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

public class CharsetOperationTest extends NbTestCase {

  public CharsetOperationTest() {
    super("CharsetOperationTest");
  }

  public void testUTF_8() throws URISyntaxException {
    String path = "files/charsets/utf-8.txt";
    URL url = Thread.currentThread().getContextClassLoader().getResource(path);
    Path testFilePath = Paths.get(url.toURI());
    FileObject fo = FileUtil.toFileObject(testFilePath.toFile());

    Charset charset = NetBeansFileUtil.getCharset(fo);

    assertEquals(StandardCharsets.UTF_8, charset);
  }

  public void testUTF_8BOM() throws URISyntaxException {
    String path = "files/charsets/utf-8-bom.txt";
    URL url = Thread.currentThread().getContextClassLoader().getResource(path);
    Path testFilePath = Paths.get(url.toURI());
    FileObject fo = FileUtil.toFileObject(testFilePath.toFile());

    Charset charset = NetBeansFileUtil.getCharset(fo);

    assertEquals(StandardCharsets.UTF_8, charset);
  }

  public void testUTF_16BE() throws URISyntaxException {
    String path = "files/charsets/utf-16-be.txt";
    URL url = Thread.currentThread().getContextClassLoader().getResource(path);
    Path testFilePath = Paths.get(url.toURI());
    FileObject fo = FileUtil.toFileObject(testFilePath.toFile());

    Charset charset = NetBeansFileUtil.getCharset(fo);

    assertEquals(StandardCharsets.UTF_16BE, charset);
  }

  public void testUTF_16LE() throws URISyntaxException {
    String path = "files/charsets/utf-16-le.txt";
    URL url = Thread.currentThread().getContextClassLoader().getResource(path);
    Path testFilePath = Paths.get(url.toURI());
    FileObject fo = FileUtil.toFileObject(testFilePath.toFile());

    Charset charset = NetBeansFileUtil.getCharset(fo);

    assertEquals(StandardCharsets.UTF_16LE, charset);
  }
}
