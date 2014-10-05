package com.welovecoding.nbeditorconfig.netbeans;

import java.util.prefs.Preferences;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;

/**
 *
 * @author Michael Koppen
 */
public class ECProjectPreferences {

	private static final String LINE_ENDING = "line_ending";

	public static void setLineEnding(String le, Project p) {
		System.out.println("SETTING LINE ENDING");
		getPreferences(p).put(LINE_ENDING, le);
	}

	public static String getLineEnding(Project p) {
		System.out.println("GETTING LINE ENDING");
		return getPreferences(p).get(LINE_ENDING, System.getProperty("line.separator"));
	}

	private static Preferences getPreferences(Project p) {
		return ProjectUtils.getPreferences(p, ECProjectPreferences.class, true);
	}

}
