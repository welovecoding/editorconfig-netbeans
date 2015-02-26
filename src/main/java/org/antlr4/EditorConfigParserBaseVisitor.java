// Generated from editorconfig/EditorConfigParser.g4 by ANTLR 4.5
package org.antlr4;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of
 * {@link EditorConfigParserVisitor}, which can be extended to create a visitor
 * which only needs to handle a subset of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public class EditorConfigParserBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements EditorConfigParserVisitor<T> {

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override
  public T visitFile(EditorConfigParser.FileContext ctx) {
    return visitChildren(ctx);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override
  public T visitSection(EditorConfigParser.SectionContext ctx) {
    return visitChildren(ctx);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override
  public T visitSectionHeader(EditorConfigParser.SectionHeaderContext ctx) {
    return visitChildren(ctx);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override
  public T visitRootPropertyStatement(EditorConfigParser.RootPropertyStatementContext ctx) {
    return visitChildren(ctx);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override
  public T visitPropertyStatement(EditorConfigParser.PropertyStatementContext ctx) {
    return visitChildren(ctx);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override
  public T visitPropertyKey(EditorConfigParser.PropertyKeyContext ctx) {
    return visitChildren(ctx);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override
  public T visitPropertyValue(EditorConfigParser.PropertyValueContext ctx) {
    return visitChildren(ctx);
  }
}
