package com.welovecoding.netbeans.plugin.editorconfig.processor.function;

import java.io.BufferedOutputStream;
import java.io.IOException;
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
public class FinalNewLineFunction {

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

    // Change file in editor
    InsertNewLineInEditorAction action = new InsertNewLineInEditorAction(fo, lineEnding);
    WindowManager.getDefault().invokeWhenUIReady(action);

    if (!action.isWasOpened()) {
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

  private static class InsertNewLineInEditorAction implements Runnable {

    private boolean wasOpened = false;
    private final FileObject fileObject;
    private final String lineEnding;

    public InsertNewLineInEditorAction(FileObject fileObject, String lineEnding) {
      this.fileObject = fileObject;
      this.lineEnding = lineEnding;
    }

    @Override
    public void run() {
      try {
        EditorCookie cookie = (EditorCookie) DataObject.find(fileObject).getCookie(EditorCookie.class);
        System.out.println("Cookie: " + cookie);
        if (cookie != null) {
          FileLock lock = FileLock.NONE;
          if (!fileObject.isLocked()) {
            StyledDocument document = cookie.openDocument();
            System.out.println("Document: " + document);
            for (JEditorPane pane : cookie.getOpenedPanes()) {
              wasOpened = true;
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

    public boolean isWasOpened() {
      return wasOpened;
    }

  }

}
