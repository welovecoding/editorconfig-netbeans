package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import org.junit.Test;
import static org.junit.Assert.*;

public class XTrimTrailingWhiteSpaceOperationTest {

  public XTrimTrailingWhiteSpaceOperationTest() {
  }

  @Test
  public void itRemovesSpacesFromEndOfLine() {
    StringBuilder content = new StringBuilder();
    content.append("(function(){ ").append(System.lineSeparator());
    content.append("  alert('Hello World!'); ").append(System.lineSeparator());
    content.append("})(); ").append(System.lineSeparator());

    StringBuilder expectedContent = new StringBuilder();
    expectedContent.append("(function(){").append(System.lineSeparator());
    expectedContent.append("  alert('Hello World!');").append(System.lineSeparator());
    expectedContent.append("})();").append(System.lineSeparator());

    boolean removedWhiteSpaces = new TrimTrailingWhiteSpaceOperation().run(content, System.lineSeparator());

    assertEquals(true, removedWhiteSpaces);
    assertEquals(expectedContent.toString(), content.toString());
  }

  @Test
  public void itRemovesMultipleSpacesFromEndOfLine() {
    StringBuilder content = new StringBuilder();
    content.append("(function(){  ").append(System.lineSeparator());
    content.append("  alert('Hello World!');  ").append(System.lineSeparator());
    content.append("})();  ").append(System.lineSeparator());

    StringBuilder expectedContent = new StringBuilder();
    expectedContent.append("(function(){").append(System.lineSeparator());
    expectedContent.append("  alert('Hello World!');").append(System.lineSeparator());
    expectedContent.append("})();").append(System.lineSeparator());

    boolean removedWhiteSpaces = new TrimTrailingWhiteSpaceOperation().run(content, System.lineSeparator());

    assertEquals(true, removedWhiteSpaces);
    assertEquals(expectedContent.toString(), content.toString());
  }

  @Test
  public void itRemovesTabsFromEndOfLine() {
    StringBuilder content = new StringBuilder();
    content.append("(function(){\t").append(System.lineSeparator());
    content.append("  alert('Hello World!');\t").append(System.lineSeparator());
    content.append("})();\t").append(System.lineSeparator());

    StringBuilder expectedContent = new StringBuilder();
    expectedContent.append("(function(){").append(System.lineSeparator());
    expectedContent.append("  alert('Hello World!');").append(System.lineSeparator());
    expectedContent.append("})();").append(System.lineSeparator());

    boolean removedWhiteSpaces = new TrimTrailingWhiteSpaceOperation().run(content, System.lineSeparator());

    assertEquals(true, removedWhiteSpaces);
    assertEquals(expectedContent.toString(), content.toString());
  }

  @Test
  public void itRemovesMultipleTabsFromEndOfLine() {
    StringBuilder content = new StringBuilder();
    content.append("(function(){\t\t").append(System.lineSeparator());
    content.append("  alert('Hello World!');\t\t").append(System.lineSeparator());
    content.append("})();\t\t").append(System.lineSeparator());

    StringBuilder expectedContent = new StringBuilder();
    expectedContent.append("(function(){").append(System.lineSeparator());
    expectedContent.append("  alert('Hello World!');").append(System.lineSeparator());
    expectedContent.append("})();").append(System.lineSeparator());

    boolean removedWhiteSpaces = new TrimTrailingWhiteSpaceOperation().run(content, System.lineSeparator());

    assertEquals(true, removedWhiteSpaces);
    assertEquals(expectedContent.toString(), content.toString());
  }

  @Test
  public void itRemovesMixedTabsAndSpacesFromEndOfLine() {
    StringBuilder content = new StringBuilder();
    content.append("(function(){  ").append(System.lineSeparator());
    content.append("  alert('Hello World!');\t \t").append(System.lineSeparator());
    content.append("})();  \t\t").append(System.lineSeparator());

    StringBuilder expectedContent = new StringBuilder();
    expectedContent.append("(function(){").append(System.lineSeparator());
    expectedContent.append("  alert('Hello World!');").append(System.lineSeparator());
    expectedContent.append("})();").append(System.lineSeparator());

    boolean removedWhiteSpaces = new TrimTrailingWhiteSpaceOperation().run(content, System.lineSeparator());

    assertEquals(true, removedWhiteSpaces);
    assertEquals(expectedContent.toString(), content.toString());
  }

}
