// Generated from editorconfig/EditorConfigParser.g4 by ANTLR 4.5
package org.antlr4;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link EditorConfigParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface EditorConfigParserVisitor<T> extends ParseTreeVisitor<T> {

  /**
   * Visit a parse tree produced by {@link EditorConfigParser#file}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitFile(EditorConfigParser.FileContext ctx);

  /**
   * Visit a parse tree produced by {@link EditorConfigParser#section}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSection(EditorConfigParser.SectionContext ctx);

  /**
   * Visit a parse tree produced by {@link EditorConfigParser#sectionHeader}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSectionHeader(EditorConfigParser.SectionHeaderContext ctx);

  /**
   * Visit a parse tree produced by
   * {@link EditorConfigParser#rootPropertyStatement}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRootPropertyStatement(EditorConfigParser.RootPropertyStatementContext ctx);

  /**
   * Visit a parse tree produced by
   * {@link EditorConfigParser#propertyStatement}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitPropertyStatement(EditorConfigParser.PropertyStatementContext ctx);

  /**
   * Visit a parse tree produced by {@link EditorConfigParser#propertyKey}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitPropertyKey(EditorConfigParser.PropertyKeyContext ctx);

  /**
   * Visit a parse tree produced by {@link EditorConfigParser#propertyValue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitPropertyValue(EditorConfigParser.PropertyValueContext ctx);
}
