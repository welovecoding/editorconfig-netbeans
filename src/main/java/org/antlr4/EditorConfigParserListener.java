// Generated from editorconfig/EditorConfigParser.g4 by ANTLR 4.5
package org.antlr4;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link EditorConfigParser}.
 */
public interface EditorConfigParserListener extends ParseTreeListener {

  /**
   * Enter a parse tree produced by {@link EditorConfigParser#file}.
   *
   * @param ctx the parse tree
   */
  void enterFile(EditorConfigParser.FileContext ctx);

  /**
   * Exit a parse tree produced by {@link EditorConfigParser#file}.
   *
   * @param ctx the parse tree
   */
  void exitFile(EditorConfigParser.FileContext ctx);

  /**
   * Enter a parse tree produced by {@link EditorConfigParser#section}.
   *
   * @param ctx the parse tree
   */
  void enterSection(EditorConfigParser.SectionContext ctx);

  /**
   * Exit a parse tree produced by {@link EditorConfigParser#section}.
   *
   * @param ctx the parse tree
   */
  void exitSection(EditorConfigParser.SectionContext ctx);

  /**
   * Enter a parse tree produced by {@link EditorConfigParser#sectionHeader}.
   *
   * @param ctx the parse tree
   */
  void enterSectionHeader(EditorConfigParser.SectionHeaderContext ctx);

  /**
   * Exit a parse tree produced by {@link EditorConfigParser#sectionHeader}.
   *
   * @param ctx the parse tree
   */
  void exitSectionHeader(EditorConfigParser.SectionHeaderContext ctx);

  /**
   * Enter a parse tree produced by
   * {@link EditorConfigParser#rootPropertyStatement}.
   *
   * @param ctx the parse tree
   */
  void enterRootPropertyStatement(EditorConfigParser.RootPropertyStatementContext ctx);

  /**
   * Exit a parse tree produced by
   * {@link EditorConfigParser#rootPropertyStatement}.
   *
   * @param ctx the parse tree
   */
  void exitRootPropertyStatement(EditorConfigParser.RootPropertyStatementContext ctx);

  /**
   * Enter a parse tree produced by
   * {@link EditorConfigParser#propertyStatement}.
   *
   * @param ctx the parse tree
   */
  void enterPropertyStatement(EditorConfigParser.PropertyStatementContext ctx);

  /**
   * Exit a parse tree produced by {@link EditorConfigParser#propertyStatement}.
   *
   * @param ctx the parse tree
   */
  void exitPropertyStatement(EditorConfigParser.PropertyStatementContext ctx);

  /**
   * Enter a parse tree produced by {@link EditorConfigParser#propertyKey}.
   *
   * @param ctx the parse tree
   */
  void enterPropertyKey(EditorConfigParser.PropertyKeyContext ctx);

  /**
   * Exit a parse tree produced by {@link EditorConfigParser#propertyKey}.
   *
   * @param ctx the parse tree
   */
  void exitPropertyKey(EditorConfigParser.PropertyKeyContext ctx);

  /**
   * Enter a parse tree produced by {@link EditorConfigParser#propertyValue}.
   *
   * @param ctx the parse tree
   */
  void enterPropertyValue(EditorConfigParser.PropertyValueContext ctx);

  /**
   * Exit a parse tree produced by {@link EditorConfigParser#propertyValue}.
   *
   * @param ctx the parse tree
   */
  void exitPropertyValue(EditorConfigParser.PropertyValueContext ctx);
}
