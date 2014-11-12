package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
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
  
  

  public static boolean doFinalNewLine(FileObject fo, String lineEnding) {
    final String content;
    try {
      content = fo.asText();
      if (content.endsWith("\n") || content.endsWith("\r")) {
        return false;
      }
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
      return false;
    }

    EditorCookie cookie = null;
    try {
      cookie = (EditorCookie) DataObject.find(fo).getCookie(EditorCookie.class);
    } catch (DataObjectNotFoundException ex) {
      Exceptions.printStackTrace(ex);
    }

    if (cookie != null) {
      LOG.log(Level.INFO, "Editing file in Editor!");
      // Change file in editor
      InsertNewLineInEditorTask action = new InsertNewLineInEditorTask(fo, cookie, lineEnding);
      WindowManager.getDefault().invokeWhenUIReady(action);
    } else {
      LOG.log(Level.INFO, "Editing file in Filesystem!");
      // Change file on filesystem
      try {
        final String newContent = content + System.lineSeparator();
        FileLock lock = FileLock.NONE;
        if (!fo.isLocked()) {
          BufferedOutputStream os = new BufferedOutputStream(fo.getOutputStream(lock));
          os.write(newContent.getBytes("ASCII"));
          os.flush();
          os.close();
          lock.releaseLock();
        } else {
          DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message("Couldn't apply newline at the end of file \"" + fo.getName() + "." + fo.getExt() + "\"", NotifyDescriptor.WARNING_MESSAGE));
          return false;
        }
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
        return false;
      }
    }

    return true;

  }

  private static class InsertNewLineInEditorTask implements Runnable {
    
    private static final Logger LOG = Logger.getLogger(InsertNewLineInEditorTask.class.getName());

    private final FileObject fileObject;
    private final String lineEnding;
    private final EditorCookie cookie;

    public InsertNewLineInEditorTask(final FileObject fileObject, final EditorCookie cookie, final String lineEnding) {
      this.fileObject = fileObject;
      this.lineEnding = lineEnding;
      this.cookie = cookie;
    }

    @Override
    public void run() {
      try {
        LOG.log(Level.INFO, "Cookie: {0}", cookie);
        if (cookie != null) {
          FileLock lock = FileLock.NONE;
          if (!fileObject.isLocked()) {
            final StyledDocument document = cookie.openDocument();
            LOG.log(Level.INFO, "Document: {0}", document);
            for (JEditorPane pane : cookie.getOpenedPanes()) {
              JTextComponent comp = (JTextComponent) pane;
              NbDocument.runAtomicAsUser(document, () -> {
                try {
                  document.insertString(document.getEndPosition().getOffset() - 1, lineEnding, null);
                  cookie.saveDocument();
                } catch (BadLocationException | IOException ex) {
                  Exceptions.printStackTrace(ex);
                }
              });
            }
            lock.releaseLock();
          } else {
            DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message("Couldn't apply newline at the end of file \"" + fileObject.getName() + "." + fileObject.getExt() + "\"", NotifyDescriptor.WARNING_MESSAGE));
          }
        }
      } catch (BadLocationException ex) {
        Exceptions.printStackTrace(ex);
      } catch (DataObjectNotFoundException ex) {
        Exceptions.printStackTrace(ex);
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
  }
}
