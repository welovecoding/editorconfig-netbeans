package com.welovecoding.netbeans.plugin.editorconfig.io.writer;

import static com.welovecoding.netbeans.plugin.editorconfig.config.Settings.ENCODING_SETTING;
import com.welovecoding.netbeans.plugin.editorconfig.processor.FileInfo;
import com.welovecoding.netbeans.plugin.editorconfig.io.exception.FileAccessException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.EditorKit;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.modules.editor.indent.api.Reformat;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileLock;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.openide.filesystems.FileObject;
import org.openide.util.Utilities;

public class StyledDocumentWriter {

  private static final Logger LOG = Logger.getLogger(StyledDocumentWriter.class.getSimpleName());

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

  public static ArrayList<String> readFileObjectIntoLines(FileObject fo, Charset charset, String lineEnding)
          throws FileAccessException {
    ArrayList<String> lines = new ArrayList<>();
    String line;

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(fo.getInputStream(), charset))) {
      while ((line = reader.readLine()) != null) {
        lines.add(line);
        lines.add(lineEnding);
      }

      // Remove last line-break
      lines.remove(lines.size() - 1);
    } catch (IOException ex) {
      throw new FileAccessException("Document could not be read: " + ex.getMessage());
    }

    return lines;
  }

  public static void writeFile(FileObject fo, Charset charset, String content)
          throws FileAccessException {
    File file = Utilities.toFile(fo.toURI());

    try {
      Files.write(file.toPath(), content.getBytes(charset));
    } catch (IOException ex) {
      throw new FileAccessException("Document could not be written: " + ex.getMessage());
    }
  }

  public static void writeOnFile(FileObject fo, String content)
          throws FileAccessException {
    File file = Utilities.toFile(fo.toURI());

    // write file
    try (FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWriter)) {
      bufferWritter.write(content);
    } catch (IOException ex) {
      throw new FileAccessException("Document could not be written: " + ex.getMessage());
    }
  }

  public static void writeOnFileWithLines(FileObject fo, Charset charset, List<String> lines)
          throws FileAccessException {
    File file = Utilities.toFile(fo.toURI());
    writeOnFileWithLines(file, charset, lines);
  }

  /**
   * Writes a file with a proper (and detectable) character set.
   *
   * @param file
   * @param charset
   * @param lines
   * @throws FileAccessException
   */
  public static void writeOnFileWithLines(File file, Charset charset, List<String> lines)
          throws FileAccessException {
    try {
      Files.write(file.toPath(), lines, charset);
    } catch (IOException ex) {
      throw new FileAccessException("Document could not be written: " + ex.getMessage());
    }
  }

  public static void writeWithEditorKit(FileInfo info)
          throws FileAccessException {
    EditorCookie cookie = info.getCookie();
    EditorKit kit = getEditorKit(info.getDataObject());
    StyledDocument document = null;
    int caretPosition;

    try {
      document = cookie.openDocument();
    } catch (IOException ex) {
      throw new FileAccessException("Document could not be loaded: " + ex.getMessage());
    }

    try (InputStream is = new ByteArrayInputStream(info.getContentAsBytes())) {
      // Backup caret position
      Caret caret = info.getCaret();
      caretPosition = info.getCurrentCaretPosition();

      // Write file
      document.remove(0, document.getLength());

      LOG.log(Level.INFO, "Write to \"is\": {0}", is);
      LOG.log(Level.INFO, "Write to \"document\": {0}", is);

      kit.read(is, document, document.getLength());
      cookie.saveDocument();
      info.getFileObject().setAttribute(ENCODING_SETTING, info.getCharset().name());

      // Reset caret positon
      caretPosition -= info.getCaretOffset();
      if (caretPosition < document.getLength()) {
        LOG.log(Level.INFO, "Moving caret position to: {0} / {1}",
                new Object[]{caretPosition, document.getLength()});
        caret.setDot(caretPosition);
      }
    } catch (BadLocationException | IOException ex) {
      throw new FileAccessException("Document could not be saved: " + ex.getMessage());
    }

    // Reformat code (to apply ident size & styles)
    // TODO: Do this only if CodeStylePreferences have been changed
    Reformat reformat = Reformat.get(document);
    reformat.lock();

    try {
      reformat.reformat(0, document.getLength());
    } catch (BadLocationException ex) {
      LOG.log(Level.SEVERE, "AutoFormat on document not possible: {0}", ex.getMessage());
    } finally {
      reformat.unlock();
      // Save document after reformat
      try {
        cookie.saveDocument();
      } catch (IOException ex) {
        throw new FileAccessException("Document could not be saved: " + ex.getMessage());
      }
    }
  }

  public static void writeWithFilesystemAPI(FileInfo info, List<String> lines)
          throws FileAccessException {
    FileLock lock = FileLock.NONE;
    FileObject fo = info.getFileObject();

    // write file
    try {
      lock = fo.lock();
      if (fo.isLocked()) {
        Files.write(Paths.get(fo.toURI()), lines, info.getCharset());
        fo.setAttribute(ENCODING_SETTING, info.getCharset().name());
      }
    } catch (IOException ex) {
      throw new FileAccessException("Document could not be written: " + ex.getMessage());
    } finally {
      lock.releaseLock();
    }
  }

  public static void writeWithString(FileInfo info)
          throws FileAccessException {
    EditorCookie cookie = getEditorCookie(info.getDataObject());
    StyledDocument document = null;
    int caretPosition;

    try {
      document = cookie.openDocument();
    } catch (IOException ex) {
      throw new FileAccessException("Document could not be loaded: " + ex.getMessage());
    }

    try {
      // save caret position
      Caret caret = cookie.getOpenedPanes()[0].getCaret();
      caretPosition = caret.getDot();

      // write file
      document.remove(0, document.getLength());
      document.insertString(0, info.getContentAsString(), null);
      cookie.saveDocument();
      info.getFileObject().setAttribute(ENCODING_SETTING, info.getCharset().name());

      // reset the caret positon
      if (caretPosition < document.getLength()) {
        caret.setDot(caretPosition);
      }
    } catch (BadLocationException | IOException ex) {
      throw new FileAccessException("Document could not be written: " + ex.getMessage());
    }
  }

}
