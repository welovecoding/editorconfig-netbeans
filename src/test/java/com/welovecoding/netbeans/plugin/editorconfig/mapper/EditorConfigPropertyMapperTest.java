package com.welovecoding.netbeans.plugin.editorconfig.mapper;

import java.nio.charset.Charset;
import org.junit.Test;
import static org.junit.Assert.*;

public class EditorConfigPropertyMapperTest {

  public EditorConfigPropertyMapperTest() {
  }

  @Test
  public void itConvertsLineFeed() {
    String ecProperty = "lf";
    String javaProperty = "\n";
    String mappedProperty = EditorConfigPropertyMapper.normalizeLineEnding(ecProperty);

    assertEquals(mappedProperty, javaProperty);
  }

  @Test
  public void itConvertsCarriageReturn() {
    String ecProperty = "cr";
    String javaProperty = "\r";
    String mappedProperty = EditorConfigPropertyMapper.normalizeLineEnding(ecProperty);

    assertEquals(mappedProperty, javaProperty);
  }

  @Test
  public void itConvertsCRLF() {
    String ecProperty = "crlf";
    String javaProperty = "\r\n";
    String mappedProperty = EditorConfigPropertyMapper.normalizeLineEnding(ecProperty);

    assertEquals(mappedProperty, javaProperty);
  }

  @Test
  public void itConvertsUnknownValues() {
    String ecProperty = "something";
    String javaProperty = System.getProperty("line.separator");
    String mappedProperty = EditorConfigPropertyMapper.normalizeLineEnding(ecProperty);

    assertEquals(mappedProperty, javaProperty);
  }

  @Test
  public void itWorksWithNullValues() {
    String ecProperty = null;
    String javaProperty = System.getProperty("line.separator");
    String mappedProperty = EditorConfigPropertyMapper.normalizeLineEnding(ecProperty);

    assertEquals(mappedProperty, javaProperty);
  }

  @Test
  public void itConvertsLatin1() {
    String ecProperty = "latin1";
    String javaProperty = "ISO-8859-1";

    Charset charset = EditorConfigPropertyMapper.mapCharset(ecProperty);
    assertEquals(charset.name(), javaProperty);
  }

  @Test
  public void itConvertsUTF8() {
    String ecProperty = "utf-8";
    String javaProperty = "UTF-8";

    Charset charset = EditorConfigPropertyMapper.mapCharset(ecProperty);
    assertEquals(charset.name(), javaProperty);
  }

  @Test
  public void itConvertsUTF8BOM() {
    String ecProperty = "utf-8-bom";
    String javaProperty = "UTF-8";

    Charset charset = EditorConfigPropertyMapper.mapCharset(ecProperty);
    assertEquals(charset.name(), javaProperty);
  }

  @Test
  public void itConvertsUTF16BE() {
    String ecProperty = "utf-16be";
    String javaProperty = "UTF-16BE";

    Charset charset = EditorConfigPropertyMapper.mapCharset(ecProperty);
    assertEquals(charset.name(), javaProperty);
  }

  @Test
  public void itConvertsUTF16LE() {
    String ecProperty = "utf-16le";
    String javaProperty = "UTF-16LE";

    Charset charset = EditorConfigPropertyMapper.mapCharset(ecProperty);
    assertEquals(charset.name(), javaProperty);
  }

}
