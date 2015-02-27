package com.welovecoding.netbeans.plugin.editorconfig.options;

import com.welovecoding.netbeans.plugin.editorconfig.config.Settings;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author junichi11
 */
public class EditorConfigOptions {

  private static final EditorConfigOptions INSTANCE = new EditorConfigOptions();
  private static final String LINE_COMMENT_PREFIX = "comment.prefix"; // NOI18N

  private EditorConfigOptions() {
  }

  public static EditorConfigOptions getInstance() {
    return INSTANCE;
  }

  /**
   * Get a comment prefix. Can use "#" or ";".
   *
   * @return a comment prefix
   */
  public String getLineCommentPrefix() {
    return getPreferences().get(LINE_COMMENT_PREFIX, "#"); // NOI18N
  }

  public void setLineCommentPrefix(String lineCommentPrefix) {
    getPreferences().put(LINE_COMMENT_PREFIX, lineCommentPrefix);
  }

  private Preferences getPreferences() {
    return NbPreferences.forModule(EditorConfigOptions.class).node(Settings.EDITORCONFIG);
  }
}
