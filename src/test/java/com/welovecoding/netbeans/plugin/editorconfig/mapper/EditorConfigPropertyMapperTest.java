package com.welovecoding.netbeans.plugin.editorconfig.mapper;

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

}
