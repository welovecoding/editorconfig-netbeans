/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.welovecoding.netbeans.plugin.editorconfig.listener;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import org.netbeans.api.queries.FileEncodingQuery;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

/**
 *
 * @author Michael Koppen
 */
public abstract class WriteFileTask implements Runnable {

  private final FileObject fo;
  private final Charset cs;

  public WriteFileTask(FileObject fo, Charset cs) {
    this.fo = fo;
    this.cs = cs;
  }

  public WriteFileTask(FileObject fo) {
    this.fo = fo;
    this.cs = FileEncodingQuery.getEncoding(fo);
  }

  @Override
  public void run() {
    FileLock lock = FileLock.NONE;
    try (OutputStreamWriter writer = new OutputStreamWriter(fo.getOutputStream(lock), cs)) {
      apply(writer);
      writer.flush();
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  public abstract void apply(OutputStreamWriter writer);

}
