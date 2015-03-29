package com.welovecoding.nbeditorconfig.processor;

import com.welovecoding.nbeditorconfig.io.exception.FileAccessException;
import com.welovecoding.nbeditorconfig.io.writer.StyledDocumentWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileSystem;

public class WriteEditorAction implements FileSystem.AtomicAction, Runnable {

  private static final Logger LOG = Logger.getLogger(WriteEditorAction.class.getName());

  private final FileInfo info;

  public WriteEditorAction() {
    this.info = null;
  }

  public WriteEditorAction(FileInfo info) {
    this.info = info;
  }

  @Override
  public void run() {
    try {
      StyledDocumentWriter.writeWithEditorKit(info);
    } catch (FileAccessException ex) {
      LOG.log(Level.SEVERE, ex.getMessage());
    }
  }

  @Override
  public boolean equals(Object obj) {
    return obj != null && obj instanceof WriteEditorAction;
  }

  @Override
  public int hashCode() {
    return getClass().getName().hashCode();
  }

}
