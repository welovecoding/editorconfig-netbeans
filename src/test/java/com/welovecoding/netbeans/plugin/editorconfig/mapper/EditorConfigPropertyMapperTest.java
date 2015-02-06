package com.welovecoding.netbeans.plugin.editorconfig.mapper;

import com.welovecoding.netbeans.plugin.editorconfig.io.model.MappedCharset;
import com.welovecoding.netbeans.plugin.editorconfig.model.MappedEditorConfig;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

public class EditorConfigPropertyMapperTest {

  private static final Logger LOG = Logger.getLogger(EditorConfigPropertyMapperTest.class.getName());
  private File jsFile;

  public EditorConfigPropertyMapperTest() {
  }

  @Before
  public void setUp() {
    String content = "(function(){" + System.lineSeparator();
    content += "    alert('Hello World!');" + System.lineSeparator();
    content += "})();";

    try {
      jsFile = File.createTempFile(this.getClass().getSimpleName(), ".js");
      Path path = Paths.get(Utilities.toURI(jsFile));
      Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  @After
  public void tearDown() {
    jsFile.delete();
  }

  @Test
  public void itMapsTabWidthFromIndentSize() throws IOException {
    StringBuilder config = new StringBuilder("root = true");
    config.append(System.lineSeparator()).append(System.lineSeparator());
    config.append("[*]").append(System.lineSeparator());
    config.append("charset = utf-8").append(System.lineSeparator());
    config.append("end_of_line = lf").append(System.lineSeparator());
    config.append(System.lineSeparator());
    config.append("[*.js]").append(System.lineSeparator());
    config.append("indent_size = 2").append(System.lineSeparator());
    config.append("insert_final_newline = false").append(System.lineSeparator());
    config.append("trim_trailing_whitespace = true").append(System.lineSeparator());

    File ecFile = File.createTempFile(this.getClass().getSimpleName(), ".editorconfig");
    Path path = Paths.get(Utilities.toURI(ecFile));
    Files.write(path, config.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);

    MappedEditorConfig mappedConfig = EditorConfigPropertyMapper.createEditorConfig(jsFile, ecFile.getName());
    LOG.log(Level.INFO, "Config:\r\n{0}", mappedConfig.toString());

    assertEquals(true, ecFile.delete());
    assertEquals(StandardCharsets.UTF_8.name(), mappedConfig.getCharset().getName());
    assertEquals("\n", mappedConfig.getEndOfLine());
    assertEquals(2, mappedConfig.getIndentSize());
    assertEquals(null, mappedConfig.getIndentStyle());
    assertEquals(false, mappedConfig.isInsertFinalNewLine());
    assertEquals(2, mappedConfig.getTabWidth());
    assertEquals(true, mappedConfig.isTrimTrailingWhiteSpace());
  }

  @Test
  public void itMapsIndentSizeTabToMinusTwo() throws IOException {
    StringBuilder config = new StringBuilder("root = true");
    config.append(System.lineSeparator()).append(System.lineSeparator());
    config.append("[*]").append(System.lineSeparator());
    config.append("charset = utf-8").append(System.lineSeparator());
    config.append("end_of_line = lf").append(System.lineSeparator());
    config.append(System.lineSeparator());
    config.append("[*.js]").append(System.lineSeparator());
    config.append("indent_style = tab").append(System.lineSeparator());

    File ecFile = File.createTempFile(this.getClass().getSimpleName(), ".editorconfig");
    Path path = Paths.get(Utilities.toURI(ecFile));
    Files.write(path, config.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);

    MappedEditorConfig mappedConfig = EditorConfigPropertyMapper.createEditorConfig(jsFile, ecFile.getName());
    LOG.log(Level.INFO, "Config:\r\n{0}", mappedConfig.toString());

    assertEquals(true, ecFile.delete());
    assertEquals(StandardCharsets.UTF_8.name(), mappedConfig.getCharset().getName());
    assertEquals("\n", mappedConfig.getEndOfLine());
    assertEquals(-2, mappedConfig.getIndentSize());
    assertEquals("tab", mappedConfig.getIndentStyle());
    assertEquals(false, mappedConfig.isInsertFinalNewLine());
    assertEquals(-1, mappedConfig.getTabWidth());
    assertEquals(false, mappedConfig.isTrimTrailingWhiteSpace());
  }

  @Test
  public void itMapsNullIfFileDoesNotMatch() throws IOException {
    StringBuilder config = new StringBuilder("root = true");
    config.append(System.lineSeparator()).append(System.lineSeparator());
    config.append("[*.html]").append(System.lineSeparator());
    config.append("indent_size = 2").append(System.lineSeparator());
    config.append("insert_final_newline = false").append(System.lineSeparator());
    config.append("trim_trailing_whitespace = true").append(System.lineSeparator());

    File ecFile = File.createTempFile(this.getClass().getSimpleName(), ".editorconfig");
    Path path = Paths.get(Utilities.toURI(ecFile));
    Files.write(path, config.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);

    MappedEditorConfig mappedConfig = EditorConfigPropertyMapper.createEditorConfig(jsFile, ecFile.getName());
    LOG.log(Level.INFO, "Config:\r\n{0}", mappedConfig.toString());

    assertEquals(true, ecFile.delete());
    assertEquals(null, mappedConfig.getCharset());
    assertEquals(null, mappedConfig.getEndOfLine());
    assertEquals(-1, mappedConfig.getIndentSize());
    assertEquals(null, mappedConfig.getIndentStyle());
    assertEquals(false, mappedConfig.isInsertFinalNewLine());
    assertEquals(-1, mappedConfig.getTabWidth());
    assertEquals(false, mappedConfig.isTrimTrailingWhiteSpace());
  }

  @Test
  public void itReliesOnNullValues() {
    MappedEditorConfig config = new MappedEditorConfig();
    assertEquals(null, config.getCharset());
    assertEquals(null, config.getEndOfLine());
    assertEquals(-1, config.getIndentSize());
    assertEquals(null, config.getIndentStyle());
    assertEquals(-1, config.getTabWidth());
    assertEquals(false, config.isInsertFinalNewLine());
    assertEquals(false, config.isTrimTrailingWhiteSpace());
  }

  @Test
  public void itConvertsLineFeed() throws EditorConfigPropertyMappingException {
    String ecProperty = "lf";
    String expected = "\n";

    String mappedProperty = EditorConfigPropertyMapper.mapLineEnding(ecProperty);
    assertEquals(expected, mappedProperty);
  }

  @Test
  public void itConvertsCarriageReturn() throws EditorConfigPropertyMappingException {
    String ecProperty = "cr";
    String expected = "\r";

    String mappedProperty = EditorConfigPropertyMapper.mapLineEnding(ecProperty);
    assertEquals(expected, mappedProperty);
  }

  @Test
  public void itConvertsCRLF() throws EditorConfigPropertyMappingException {
    String ecProperty = "crlf";
    String expected = "\r\n";

    String mappedProperty = EditorConfigPropertyMapper.mapLineEnding(ecProperty);
    assertEquals(expected, mappedProperty);
  }

  @Test
  public void itConvertsUnknownValues() throws EditorConfigPropertyMappingException {
    String ecProperty = "something";
    String expected = System.lineSeparator();

    String mappedProperty = EditorConfigPropertyMapper.mapLineEnding(ecProperty);
    assertEquals(expected, mappedProperty);
  }

  @Test
  public void itWorksWithNullValues() throws EditorConfigPropertyMappingException {
    String ecProperty = null;
    String expected = System.lineSeparator();

    String mappedProperty = EditorConfigPropertyMapper.mapLineEnding(ecProperty);
    assertEquals(expected, mappedProperty);
  }

  @Test
  public void itConvertsLatin1() {
    String ecProperty = "latin1";
    String expected = "ISO-8859-1";

    MappedCharset charset = EditorConfigPropertyMapper.mapCharset(ecProperty);
    assertEquals(expected, charset.getName());
  }

  @Test
  public void itConvertsUTF8() {
    String ecProperty = "utf-8";
    String expected = "UTF-8";

    MappedCharset charset = EditorConfigPropertyMapper.mapCharset(ecProperty);
    assertEquals(expected, charset.getName());
  }

  @Test
  public void itConvertsUTF8BOM() {
    String ecProperty = "utf-8-bom";
    String expected = "UTF-8-BOM";

    MappedCharset charset = EditorConfigPropertyMapper.mapCharset(ecProperty);
    assertEquals(expected, charset.getName());
  }

  @Test
  public void itConvertsUTF16BE() {
    String ecProperty = "utf-16be";
    String expected = "UTF-16BE";

    MappedCharset charset = EditorConfigPropertyMapper.mapCharset(ecProperty);
    assertEquals(expected, charset.getName());
  }

  @Test
  public void itConvertsUTF16LE() {
    String ecProperty = "utf-16le";
    String expected = "UTF-16LE";

    MappedCharset charset = EditorConfigPropertyMapper.mapCharset(ecProperty);
    assertEquals(expected, charset.getName());
  }

}
