package com.welovecoding.nbeditorconfig.io.writer;

import com.welovecoding.nbeditorconfig.io.model.MappedCharset;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileAlreadyLockedException;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

public class ConfigWriter {

  private static final Logger LOG = Logger.getLogger(ConfigWriter.class.getName());
  
  public static void rewrite(DataObject dataObject, MappedCharset currentCharset, MappedCharset requestedCharset) throws IOException {
    FileObject fo = dataObject.getPrimaryFile();

    // Read file
    final StringBuilder sb = new StringBuilder();
    final char[] buffer = new char[512];

    try (Reader in = new InputStreamReader(fo.getInputStream(), currentCharset.getCharset())) {
      int len;
      while ((len = in.read(buffer)) > 0) {
        sb.append(buffer, 0, len);
      }
    } catch (IOException ex) {
      LOG.log(Level.WARNING, "Cannot write file: {0}", ex.getMessage());
    }

    // Write file
    FileLock lock = null;

    try {
      lock = fo.lock();
    } catch (FileAlreadyLockedException ex) {
      // Try again later; perhaps display a warning dialog.
      LOG.log(Level.WARNING, "File is alreay locked: {0}", ex.getMessage());
    } catch (IOException ex) {
      LOG.log(Level.WARNING, "Cannot lock file: {0}", ex.getMessage());
    }

    try (Writer out = new OutputStreamWriter(fo.getOutputStream(lock), requestedCharset.getCharset())) {
      out.write(sb.toString());
    } finally {
      if (lock != null) {
        lock.releaseLock();
      }
    }

    final DataObject newDobj = DataObject.find(fo);
    final OpenCookie oc = newDobj.getLookup().lookup(OpenCookie.class);

    if (oc != null) {
      EditorCookie ec = dataObject.getLookup().lookup(EditorCookie.class);
      if (ec != null) {
        ec.close();
      }
      oc.open();
    }
  }
}
