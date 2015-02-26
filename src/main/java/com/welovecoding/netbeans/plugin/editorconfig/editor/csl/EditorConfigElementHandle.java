package com.welovecoding.netbeans.plugin.editorconfig.editor.csl;

import com.welovecoding.netbeans.plugin.editorconfig.editor.api.lexer.ECTokenId;
import java.util.Collections;
import java.util.Set;
import org.netbeans.modules.csl.api.ElementHandle;
import org.netbeans.modules.csl.api.ElementKind;
import org.netbeans.modules.csl.api.Modifier;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.spi.ParserResult;
import org.openide.filesystems.FileObject;

/**
 *
 * @author junichi11
 */
public class EditorConfigElementHandle implements ElementHandle {

  private final String name;
  private final int startPosition;
  private final int endPosition;
  private final ElementKind elementKind;

  public EditorConfigElementHandle(String name, int startPosition, int endPosition, ElementKind elementKind) {
    this.name = name;
    this.startPosition = startPosition;
    this.endPosition = endPosition;
    this.elementKind = elementKind;
  }

  @Override
  public FileObject getFileObject() {
    return null;
  }

  @Override
  public String getMimeType() {
    return ECTokenId.EDITORCONFIG_MIME_TYPE;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getIn() {
    return null;
  }

  @Override
  public ElementKind getKind() {
    return elementKind;
  }

  @Override
  public Set<Modifier> getModifiers() {
    return Collections.emptySet();
  }

  @Override
  public boolean signatureEquals(ElementHandle elementHandle) {
    if (elementHandle instanceof EditorConfigElementHandle) {
      return name.equals(((EditorConfigElementHandle) elementHandle).name);
    }
    return false;
  }

  @Override
  public OffsetRange getOffsetRange(ParserResult pr) {
    return new OffsetRange(startPosition, endPosition);
  }

}
