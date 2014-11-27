package com.welovecoding.netbeans.plugin.editorconfig.processor.io;

import com.welovecoding.netbeans.plugin.editorconfig.processor.FileInfo;
import com.welovecoding.netbeans.plugin.editorconfig.util.FileAccessException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.EditorKit;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;

public class DocumentReaderWriter {

  private static final Logger LOG = Logger.getLogger(DocumentReaderWriter.class.getName());

  private static EditorCookie getEditorCookie(DataObject dataObject) {
    return dataObject.getLookup().lookup(EditorCookie.class);
  }

  public static EditorKit getEditorKit(DataObject dataObject) {
    FileObject fileObject = dataObject.getPrimaryFile();
    String mimePath = fileObject.getMIMEType();
    Lookup lookup = MimeLookup.getLookup(MimePath.parse(mimePath));
    EditorKit kit = lookup.lookup(EditorKit.class);

    return kit;
  }

  public static void writeWithEditorKit(FileInfo info)
          throws FileAccessException {
    EditorCookie cookie = info.getCookie();
    EditorKit kit = getEditorKit(info.getDataObject());
    StyledDocument document = null;
    int caretPosition = -1;

    try {
      document = cookie.openDocument();
    } catch (IOException ex) {
      throw new FileAccessException("Document could not be loaded: " + ex.getMessage());
    }

    try (InputStream is = new ByteArrayInputStream(info.getContentAsBytes())) {
      Caret caret = kit.createCaret();
      caretPosition = caret.getDot();

      LOG.log(Level.INFO, "#1 Document length is: {0}", document.getLength());
      LOG.log(Level.INFO, "#1 Caret is at position: {0}", caretPosition);

      document.remove(0, document.getLength());
      kit.read(is, document, document.getLength());
      cookie.saveDocument();

      int newCaretPosition = kit.createCaret().getDot();
      int newDocumentLength = document.getLength();

      LOG.log(Level.INFO, "#2 Document length is: {0}", document.getLength());
      LOG.log(Level.INFO, "#2 Caret is at position: {0}", newCaretPosition);

      if (caretPosition > -1 && caretPosition < newDocumentLength) {
        LOG.log(Level.INFO, "#3 We should place the caret to a new position.");
      }

    } catch (BadLocationException | IOException ex) {
      throw new FileAccessException("Document could not be written: " + ex.getMessage());
    }

  }

  public static void writeWithString(FileInfo info)
          throws FileAccessException {
    EditorCookie cookie = getEditorCookie(info.getDataObject());
    StyledDocument document = null;

    try {
      document = cookie.openDocument();
    } catch (IOException ex) {
      throw new FileAccessException("Document could not be loaded: " + ex.getMessage());
    }

    try {
      document.remove(0, document.getLength());
      document.insertString(0, info.getContentAsString(), null);
      cookie.saveDocument();
    } catch (BadLocationException | IOException ex) {
      throw new FileAccessException("Document could not be written: " + ex.getMessage());
    }
  }
}
