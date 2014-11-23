package com.welovecoding.netbeans.plugin.editorconfig.processor;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

public class WriteStringToFileTask extends WriteFileTask {

  private static final Logger LOG = Logger.getLogger(WriteStringToFileTask.class.getSimpleName());
  private final String content;

  public WriteStringToFileTask(FileInfo info) {
    super(info.getFileObject(), info.getCharset());
    this.content = info.getContent().toString();
  }

  public WriteStringToFileTask(FileObject fileObject, Charset charset, String content) {
    super(fileObject, charset);
    this.content = content;
  }

  @Override
  public void apply(OutputStreamWriter writer) {
    try {
      writer.write(content);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

}
