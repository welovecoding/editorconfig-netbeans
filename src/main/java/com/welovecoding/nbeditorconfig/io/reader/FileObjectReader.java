package com.welovecoding.nbeditorconfig.io.reader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import org.netbeans.api.queries.FileEncodingQuery;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

public class FileObjectReader {

  public static synchronized String read(FileObject fo) {
    return FileObjectReader.read(fo, FileEncodingQuery.getEncoding(fo));
  }

  public static synchronized String read(FileObject fo, Charset cs) {
    final StringBuilder sb = new StringBuilder();
    final char[] buffer = new char[512];

    try (Reader in = new InputStreamReader(fo.getInputStream(), cs)) {
      int len;
      while ((len = in.read(buffer)) > 0) {
        sb.append(buffer, 0, len);
      }
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }

    return sb.toString();
  }

}
