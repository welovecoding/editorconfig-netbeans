package com.welovecoding.nbeditorconfig.processor;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.logging.Logger;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

public class WriteStringToFileAction extends WriteFileAction {

  private static final Logger LOG = Logger.getLogger(WriteStringToFileAction.class.getSimpleName());
  private final String content;

  public WriteStringToFileAction() {
    super();
    content = "";
  }

  public WriteStringToFileAction(FileInfo info) {
    super(info.getFileObject(), info.getCharset());
    this.content = info.getContent().toString();
  }

  public WriteStringToFileAction(FileObject fileObject, Charset charset, String content) {
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

  @Override
  public boolean equals(Object obj) {
    return obj != null && obj instanceof WriteStringToFileAction;
  }

  @Override
  public int hashCode() {
    return getClass().getName().hashCode();
  }

}
