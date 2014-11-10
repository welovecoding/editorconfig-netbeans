package com.welovecoding.netbeans.plugin.editorconfig.processor.function;

import com.welovecoding.netbeans.plugin.editorconfig.listener.InsertNewLineInEditorAction;
import java.io.BufferedOutputStream;
import java.io.IOException;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;

/**
 *
 * @author Michael Koppen
 */
public class FinalNewLineFunction {

  public static boolean doFinalNewLine(FileObject fo) {
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
    InsertNewLineInEditorAction action = new InsertNewLineInEditorAction(fo);
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

}
