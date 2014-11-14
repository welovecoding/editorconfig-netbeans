package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;

/**
 *
 * @author Michael Koppen
 */
public class FinalNewLineOperation {

  private static final Logger LOG = Logger.getLogger(FinalNewLineOperation.class.getName());

  public static boolean doFinalNewLine(final DataObject dataObject, final String value, final String lineEnding) throws Exception {
    if (Boolean.valueOf(value)) {
      return new FinalNewLineOperation().apply(dataObject, lineEnding).call();
    } else {
      return false;
    }
  }

  public Callable<Boolean> apply(final DataObject dataObject, final String lineEnding) {
    return new ApplyFinalNewLineTask(dataObject, lineEnding);
  }

  private class ApplyFinalNewLineTask implements Callable<Boolean> {

    private final DataObject dataObject;
    private final String lineEnding;

    public ApplyFinalNewLineTask(final DataObject dataObject, final String lineEnding) {
      LOG.log(Level.INFO, "Created new ApplyFinalNewLineTask for File {0}", dataObject.getPrimaryFile().getPath());
      this.dataObject = dataObject;
      this.lineEnding = lineEnding;
    }

    @Override
    public Boolean call() throws Exception {
      LOG.log(Level.INFO, "Executing ApplyFinalNewLineTask");
      FileObject fileObject = dataObject.getPrimaryFile();

      final String content;
      try {
        content = fileObject.asText();
        if (content.endsWith("\n") || content.endsWith("\r")) {
          LOG.log(Level.INFO, "File already ends with a newline");
          return false;
        }
        LOG.log(Level.INFO, "File does not already ends with a newline");
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
        return false;
      }

      EditorCookie cookie = null;
      try {
        cookie = (EditorCookie) DataObject.find(fileObject).getLookup().lookup(EditorCookie.class);
      } catch (DataObjectNotFoundException ex) {
        Exceptions.printStackTrace(ex);
      }

      if (cookie != null && cookie.getDocument() != null) {
        LOG.log(Level.INFO, "File is opened in Editor! Appling on Editor.");
        // Change file in editor
        InsertNewLineInEditorTask action = new InsertNewLineInEditorTask(fileObject, cookie, lineEnding);
        WindowManager.getDefault().invokeWhenUIReady(action);
      } else {
        LOG.log(Level.INFO, "File is NOT opened in Editor! Appling on filesystem.");
        // Change file on filesystem
        FileLock lock = FileLock.NONE;

        LOG.log(Level.INFO, "file is unlocked");
        if (!fileObject.isLocked()) {
          lock = fileObject.lock();
          try {
            LOG.log(Level.INFO, "Adding final newline \"{0}\"", lineEnding);
            final String newContent = content + lineEnding;
            BufferedOutputStream os = new BufferedOutputStream(fileObject.getOutputStream(lock));
            os.write(newContent.getBytes("ASCII"));
            os.flush();
            os.close();
          } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            return false;
          } finally {
            lock.releaseLock();
          }
        }
      }

      return true;
    }

    private class InsertNewLineInEditorTask implements Runnable {

      private final FileObject fileObject;
      private final String lineEnding;
      private final EditorCookie cookie;

      public InsertNewLineInEditorTask(FileObject fileObject, final EditorCookie cookie, final String lineEnding) {
        this.fileObject = fileObject;
        this.lineEnding = lineEnding;
        this.cookie = cookie;
      }

      @Override
      public void run() {
        try {
          LOG.log(Level.INFO, "Cookie: {0}", cookie);
          if (cookie != null) {
            LOG.log(Level.INFO, "getting document");
            final StyledDocument document = cookie.getDocument();
            LOG.log(Level.INFO, "Document: {0}", document);
            NbDocument.runAtomicAsUser(document, () -> {
              try {
                String end = document.getText(document.getEndPosition().getOffset() - 2, 1);
                LOG.log(Level.INFO, "End: {0}", end);
                if (!end.endsWith("\n") && !end.endsWith("\r")) {
                  LOG.log(Level.INFO, "Adding final newline \"{0}\"", lineEnding);
                  document.insertString(document.getEndPosition().getOffset() - 1, lineEnding, null);
                  String result = document.getText(document.getEndPosition().getOffset() - 10, 10);
                  System.out.println("Result: " + result);
                  LOG.log(Level.INFO, "Saving Document");
                  cookie.saveDocument();
                }
              } catch (BadLocationException | IOException ex) {
                Exceptions.printStackTrace(ex);
              }
            });
          }
        } catch (BadLocationException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
    }
  }
}
