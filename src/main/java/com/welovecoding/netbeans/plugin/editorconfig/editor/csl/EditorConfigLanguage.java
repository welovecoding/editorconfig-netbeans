package com.welovecoding.netbeans.plugin.editorconfig.editor.csl;

import com.welovecoding.netbeans.plugin.editorconfig.editor.api.lexer.ECTokenId;
import com.welovecoding.netbeans.plugin.editorconfig.editor.parser.ECParser;
import com.welovecoding.netbeans.plugin.editorconfig.options.EditorConfigOptions;
import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.api.StructureScanner;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;
import org.netbeans.modules.parsing.spi.Parser;

/**
 *
 * @author junichi11
 */
@LanguageRegistration(mimeType = ECTokenId.EDITORCONFIG_MIME_TYPE, useMultiview = true)
public class EditorConfigLanguage extends DefaultLanguageConfig {

  @Override
  public Language getLexerLanguage() {
    return ECTokenId.language();
  }

  @Override
  public String getDisplayName() {
    return "EditorConfig"; // NOI18N
  }

  @Override
  public Parser getParser() {
    return new ECParser();
  }

  @Override
  public String getLineCommentPrefix() {
    // # or ;
    return EditorConfigOptions.getInstance().getLineCommentPrefix();
  }

  @Override
  public StructureScanner getStructureScanner() {
    return new EditorConfigStructureScanner();
  }

  @Override
  public boolean hasStructureScanner() {
    return true;
  }

  @Override
  public String getPreferredExtension() {
    return "editorconfig"; // NOI18N
  }

}
