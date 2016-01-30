package com.welovecoding.nbeditorconfig.io.writer;

import com.welovecoding.nbeditorconfig.io.exception.FileAccessException;
import com.welovecoding.nbeditorconfig.io.exception.FileObjectLockException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileAlreadyLockedException;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;
import org.openide.util.UserQuestionException;

public class FileObjectWriter {

  private static final Logger LOG = Logger.getLogger(FileObjectWriter.class.getName());

  public static synchronized void writeWithAtomicAction(final DataObject dataObject, final Charset cs, final String content) {
    try {
      final FileObject fo = dataObject.getPrimaryFile();
      final EditorCookie cookie = dataObject.getLookup().lookup(EditorCookie.class);
      NbDocument.runAtomicAsUser(cookie.openDocument(), new Runnable() {
        @Override
        public void run() {
          try (Writer out = new OutputStreamWriter(fo.getOutputStream(), cs)) {
            LOG.log(Level.INFO, "\u00ac Writing file");
            out.write(content);
          } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
          }
        }
      });
    } catch (BadLocationException ex) {
      Exceptions.printStackTrace(ex);
    } catch (IOException ex) {
      System.out.println("Document cannot be opened.");
      Exceptions.printStackTrace(ex);
    }
  }

  public static synchronized void write(FileObject fo, Charset cs, String content) throws FileObjectLockException {
    FileLock lock = FileLock.NONE;

    try {
      LOG.log(Level.INFO, "\u00ac Trying to lock file: \"{0}\"", fo.getPath());
      lock = fo.lock();
    } catch (FileAlreadyLockedException ex) {
      throw new FileObjectLockException("File is already locked: " + ex.getMessage());
    } catch (UserQuestionException ex2) {
      throw new FileObjectLockException("Lock cannot be obtained now: " + ex2.getMessage());
    } catch (IOException ex3) {
      throw new FileObjectLockException("File cannot be locked: " + ex3.getMessage());
    }

    try (Writer out = new OutputStreamWriter(fo.getOutputStream(lock), cs)) {
      LOG.log(Level.INFO, "\u00ac Writing file");
      out.write(content);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    } finally {
      LOG.log(Level.INFO, "\u00ac Released file lock");
      lock.releaseLock();
    }

  }

  public static synchronized void closeDocumentInEditor(DataObject dataObject) {
    final OpenCookie oc = dataObject.getLookup().lookup(OpenCookie.class);
    if (oc != null) {
      EditorCookie ec = dataObject.getLookup().lookup(EditorCookie.class);
      if (ec != null) {
        ec.close();
      }
    }
  }

  public static synchronized void openDocumentInEditor(DataObject dataObject) {
    final OpenCookie oc = dataObject.getLookup().lookup(OpenCookie.class);
    if (oc != null) {
      oc.open();
    }
  }

  public static synchronized void reopenDocumentInEditor(DataObject dataObject) {
    final OpenCookie oc = dataObject.getLookup().lookup(OpenCookie.class);
    if (oc != null) {
      EditorCookie ec = dataObject.getLookup().lookup(EditorCookie.class);
      if (ec != null) {
        ec.close();
      }
      oc.open();
    }
  }
}
