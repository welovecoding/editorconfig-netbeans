package com.welovecoding.netbeans.plugin.editorconfig.processor.io;

import com.welovecoding.netbeans.plugin.editorconfig.util.NetBeansFileUtil;
import java.io.File;
import java.nio.charset.Charset;
import org.openide.filesystems.FileObject;
import org.openide.util.Utilities;

public class FileAttributeReader {

  public static void readFile(FileObject fo) {
    Charset charset = NetBeansFileUtil.guessCharset(fo);
    File file = Utilities.toFile(fo.toURI());
    String lineEnding = NetBeansFileUtil.detectLineEnding(file);
  }
}
