/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.welovecoding.netbeans.plugin.editorconfig.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;
import org.netbeans.api.queries.FileEncodingQuery;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

/**
 *
 * @author Michael Koppen
 */
public abstract class ReadFileTask implements Callable<String> {

  private final FileObject fo;
  private final Charset cs;
  private String content;

  public ReadFileTask(FileObject fo, Charset cs) {
    this.fo = fo;
    this.cs = cs;
  }

  public ReadFileTask(FileObject fo) {
    this.fo = fo;
    this.cs = FileEncodingQuery.getEncoding(fo);
  }

  @Override
  public String call() {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(fo.getInputStream(), cs));) {
      content = apply(reader);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
    return content;
  }

  public abstract String apply(BufferedReader reader);

}
