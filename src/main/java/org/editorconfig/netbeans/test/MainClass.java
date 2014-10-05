package org.editorconfig.netbeans.test;

import org.editorconfig.netbeans.Parser;

public class MainClass {

  public static void main(String[] args) {
    Parser parser = new Parser();
    String result = parser.parseResource("editorconfig-test.ini");
    System.out.println(result);
  }

}
