package com.welovecoding.netbeans.plugin.editorconfig.project;

import java.util.prefs.Preferences;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;

public class EditorConfigProjectPreferences {

  private static final String LINE_ENDING = "line_ending";

  public static void setLineEnding(String lineEnding, Project project) {
    getPreferences(project).put(LINE_ENDING, lineEnding);
  }

  public static String getLineEnding(Project project) {
    return getPreferences(project).get(LINE_ENDING, System.getProperty("line.separator"));
  }

  private static Preferences getPreferences(Project project) {
    return ProjectUtils.getPreferences(project, EditorConfigProjectPreferences.class, true);
  }

}
