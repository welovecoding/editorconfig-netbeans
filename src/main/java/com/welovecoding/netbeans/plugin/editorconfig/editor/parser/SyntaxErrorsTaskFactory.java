package com.welovecoding.netbeans.plugin.editorconfig.editor.parser;

import com.welovecoding.netbeans.plugin.editorconfig.editor.api.lexer.ECTokenId;
import java.util.Collection;
import java.util.Collections;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.spi.SchedulerTask;
import org.netbeans.modules.parsing.spi.TaskFactory;

/**
 *
 * @author junichi11
 */
@MimeRegistration(mimeType = ECTokenId.EDITORCONFIG_MIME_TYPE, service = TaskFactory.class)
public class SyntaxErrorsTaskFactory extends TaskFactory {

  @Override
  public Collection<? extends SchedulerTask> create(Snapshot snapshot) {
    return Collections.singleton(new SyntaxErrorsTask());
  }

}
