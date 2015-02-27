package com.welovecoding.netbeans.plugin.editorconfig.config;

import org.netbeans.api.annotations.common.StaticResource;

public final class Settings {

  public static final String MIME_TYPE = "text/x-editorconfig"; // NOI18N
  @StaticResource
  public static final String LOGO_PATH = "com/welovecoding/netbeans/plugin/editorconfig/filetype/ec.png"; // NOI18N
  @StaticResource
  public static final String SECTION_ICON_PATH = "com/welovecoding/nbeditorconfig/section16.png"; // NOI18N
  public static final String ENCODING_SETTING = "wlc.editorconfig.charset"; // NOI18N
  public static final String EDITORCONFIG = "editorconfig"; // NOI18N

  private Settings() {
  }

}
