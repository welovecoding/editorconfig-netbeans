/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.welovecoding.netbeans.plugin.editorconfig.listener;

import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;

/**
 *
 * @author Michael Koppen
 */
public class InsertNewLineInEditorAction implements Runnable {

  private boolean wasOpened = false;
  private final FileObject fileObject;

  public InsertNewLineInEditorAction(FileObject fileObject) {
    this.fileObject = fileObject;
  }

  @Override
  public void run() {
    try {
      EditorCookie cookie = (EditorCookie) DataObject.find(fileObject).getCookie(EditorCookie.class);
      System.out.println("Cookie: " + cookie);
      if (cookie != null) {
        StyledDocument document = cookie.openDocument();
        System.out.println("Document: " + document);
        for (JEditorPane pane : cookie.getOpenedPanes()) {
          wasOpened = true;
          JTextComponent comp = (JTextComponent) pane;
          NbDocument.runAtomicAsUser(document, () -> {
            try {
              document.insertString(document.getEndPosition().getOffset() - 1, "\n", null);
              cookie.saveDocument();
            } catch (BadLocationException ex) {
              Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
              Exceptions.printStackTrace(ex);
            }
          });
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
