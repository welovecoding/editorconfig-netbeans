package com.welovecoding.netbeans.plugin.editorconfig.editor.csl;

import com.welovecoding.netbeans.plugin.editorconfig.config.Settings;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import org.netbeans.modules.csl.api.ElementHandle;
import org.netbeans.modules.csl.api.ElementKind;
import org.netbeans.modules.csl.api.HtmlFormatter;
import org.netbeans.modules.csl.api.Modifier;
import org.netbeans.modules.csl.api.StructureItem;
import org.openide.util.ImageUtilities;

/**
 *
 * @author junichi11
 */
public class EditorConfigSectionStructureItem implements StructureItem {

  private final String section;
  private final int startPosition;
  private final int endPosition;
  private final List<StructureItem> properties;

  /**
   * StructureItem for a section. It can have properties as children.
   *
   * @param section section name
   * @param startPosition start position of section
   * @param endPosition end position of section
   * @param properties properties (children) of section
   */
  public EditorConfigSectionStructureItem(String section, int startPosition, int endPosition, List<StructureItem> properties) {
    this.section = section;
    this.startPosition = startPosition;
    this.endPosition = endPosition;
    this.properties = properties;
  }

  @Override
  public String getName() {
    return section;
  }

  @Override
  public String getSortText() {
    return section;
  }

  @Override
  public String getHtml(HtmlFormatter formatter) {
    return section;
  }

  @Override
  public ElementHandle getElementHandle() {
    return new EditorConfigElementHandle(section, startPosition, endPosition, ElementKind.RULE);
  }

  @Override
  public ElementKind getKind() {
    return ElementKind.RULE;
  }

  @Override
  public Set<Modifier> getModifiers() {
    return Collections.emptySet();
  }

  @Override
  public boolean isLeaf() {
    return properties.isEmpty();
  }

  @Override
  public List<? extends StructureItem> getNestedItems() {
    return properties;
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
    return ImageUtilities.loadImageIcon(Settings.SECTION_ICON_PATH, true);
  }

}
