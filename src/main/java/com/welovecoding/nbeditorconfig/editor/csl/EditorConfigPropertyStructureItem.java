package com.welovecoding.nbeditorconfig.editor.csl;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import org.netbeans.modules.csl.api.ElementHandle;
import org.netbeans.modules.csl.api.ElementKind;
import org.netbeans.modules.csl.api.HtmlFormatter;
import org.netbeans.modules.csl.api.Modifier;
import org.netbeans.modules.csl.api.StructureItem;

/**
 *
 * @author junichi11
 */
public class EditorConfigPropertyStructureItem implements StructureItem {

  private final String key;
  private final String value;
  private final int startPosition;
  private final int endPosition;

  /**
   * StructureItem for a property.
   *
   * @param key property key
   * @param value property value
   * @param startPosition start position of a property
   * @param endPosition end position of a property
   */
  public EditorConfigPropertyStructureItem(String key, String value, int startPosition, int endPosition) {
    this.key = key;
    this.value = value;
    this.startPosition = startPosition;
    this.endPosition = endPosition;
  }

  @Override
  public String getName() {
    return key;
  }

  @Override
  public String getSortText() {
    return key;
  }

  @Override
  public String getHtml(HtmlFormatter formatter) {
    return String.format("%s : %s", key, value); // NOI18N
  }

  @Override
  public ElementHandle getElementHandle() {
    return new EditorConfigElementHandle(key, startPosition, endPosition, ElementKind.PROPERTY);
  }

  @Override
  public ElementKind getKind() {
    return ElementKind.PROPERTY;
  }

  @Override
  public Set<Modifier> getModifiers() {
    return Collections.emptySet();
  }

  @Override
  public boolean isLeaf() {
    return true;
  }

  @Override
  public List<? extends StructureItem> getNestedItems() {
    return Collections.emptyList();
  }

  @Override
  public long getPosition() {
    return startPosition;
  }

  @Override
  public long getEndPosition() {
    return endPosition;
  }

  @Override
  public ImageIcon getCustomIcon() {
    return null;
  }

}
