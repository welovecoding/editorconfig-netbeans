package com.welovecoding.netbeans.plugin.editorconfig.mapper;

import com.welovecoding.netbeans.plugin.editorconfig.io.model.MappedCharset;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class EditorConfigPropertyMapperTest {

  public EditorConfigPropertyMapperTest() {
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
