package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import com.welovecoding.netbeans.plugin.editorconfig.model.FileAttributeName;
import com.welovecoding.netbeans.plugin.editorconfig.processor.WriteFileTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.netbeans.api.queries.FileEncodingQuery;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;

/**
 * Ja, der name is echt doof XD
 *
 * @author Michael Koppen
 */
public class CharsetANDFinalNewLineANDTrimTrailingWhitespaceOperation {

  private static final Logger LOG = Logger.getLogger(CharsetANDFinalNewLineANDTrimTrailingWhitespaceOperation.class.getName());

  public static boolean doCharsetANDFinalNewLineANDTrimTrailingWhitespace(final DataObject dataObject, final String lineEnding, final Charset charset, final String finalnewline, final String trimwhitespace) throws Exception {

    return new CharsetANDFinalNewLineANDTrimTrailingWhitespaceOperation().apply(dataObject, lineEnding, charset, finalnewline, trimwhitespace).call();
  }

  public Callable<Boolean> apply(final DataObject dataObject, final String lineEnding, final Charset charset, final String finalnewline, final String trimwhitespace) {
    boolean newline = Boolean.valueOf(finalnewline);
    boolean whitespace = Boolean.valueOf(trimwhitespace);
    return new ApplyCharsetANDFinalNewLineANDTrimTrailingWhitespaceTask(dataObject, lineEnding, charset, newline, whitespace);
  }

  private class ApplyCharsetANDFinalNewLineANDTrimTrailingWhitespaceTask implements Callable<Boolean> {

    private final DataObject dataObject;
    private final String lineEnding;
    private final Charset charset;
    private boolean finalnewline;
    private boolean trimwhitespace;

    public ApplyCharsetANDFinalNewLineANDTrimTrailingWhitespaceTask(final DataObject dataObject, final String lineEnding, final Charset charset, final boolean finalnewline, final boolean trimwhitespace) {
      LOG.log(Level.INFO, "Created new ApplyCharsetANDFinalNewLineANDTrimTrailingWhitespaceTask for File {0}", dataObject.getPrimaryFile().getPath());
      this.dataObject = dataObject;
      this.lineEnding = lineEnding;
      this.charset = charset;
      this.finalnewline = finalnewline;
      this.trimwhitespace = trimwhitespace;
    }

    @Override
    public Boolean call() throws Exception {
      LOG.log(Level.INFO, "Executing ApplyCharsetANDFinalNewLineANDTrimTrailingWhitespaceTask");

      FileObject fileObject = dataObject.getPrimaryFile();
      String oldContent = getFileContent(fileObject);
      EditorCookie cookie = getEditorCookie(fileObject);
      boolean isOpenedInEditor = cookie != null && cookie.getDocument() != null;

      if (isOpenedInEditor) {
        LOG.log(Level.INFO, "File is opened in Editor! Appling changes on Editor.");

        CharsetANDFinalNewLineANDTrimTrailingWhitespaceUITask action
                = new CharsetANDFinalNewLineANDTrimTrailingWhitespaceUITask(
                        fileObject,
                        cookie,
                        lineEnding,
                        finalnewline,
                        trimwhitespace);
        WindowManager.getDefault().invokeWhenUIReady(action);

      } else {
        String content = oldContent;
        LOG.log(Level.INFO, "File is NOT opened in Editor! Appling changes on filesystem.");
        // Change file on filesystem
        if (finalnewline) {
          LOG.log(Level.INFO, "INSERT_FINAL_NEWLINE = true");
          String tempContent = content;
          content = finalNewline(content, lineEnding);
          /**
           * If the content has not changed we can set boolean finalnewline to
           * false since no changes were made. Thus the file may not be saved.
           */
          if (tempContent.equals(content)) {
            LOG.log(Level.INFO, "INSERT_FINAL_NEWLINE : No changes");
            finalnewline = false;
          } else {
            LOG.log(Level.INFO, "INSERT_FINAL_NEWLINE : appended final new line");
          }
        }
        if (trimwhitespace) {
          LOG.log(Level.INFO, "TRIM_TRAILING_WHITESPACE = true");
          String tempContent = content;
          content = trimWhitespaces(content, lineEnding);
          /**
           * If the content has not changed we can set boolean trimwhitespace to
           * false since no changes were made. Thus the file may not be saved.
           */
          if (tempContent.equals(content)) {
            LOG.log(Level.INFO, "TRIM_TRAILING_WHITESPACE : No changes");
            trimwhitespace = false;
          } else {
            LOG.log(Level.INFO, "TRIM_TRAILING_WHITESPACE : trimmed trailing whitespaces");
          }
        }
        if (!finalnewline && !trimwhitespace) {
          charsetIfChanged(fileObject, content, charset);
        } else {
          charset(fileObject, content, charset);
        }
      }

      return true;
    }

    private class CharsetANDFinalNewLineANDTrimTrailingWhitespaceUITask implements Runnable {

      private final FileObject fileObject;
      private final EditorCookie cookie;
      private final StyledDocument document;
      private final String lineEnding;
      private final boolean finalnewline;
      private final boolean trimwhitespace;

      private CharsetANDFinalNewLineANDTrimTrailingWhitespaceUITask(final FileObject fileObject, EditorCookie cookie, final String lineEnding, final boolean finalnewline, final boolean trimwhitespace) {
        this.fileObject = fileObject;
        this.cookie = cookie;
        this.lineEnding = lineEnding;
        this.finalnewline = finalnewline;
        this.trimwhitespace = trimwhitespace;
        this.document = cookie.getDocument();
      }

      @Override
      public void run() {
        try {
          NbDocument.runAtomicAsUser(document, () -> {
            boolean changed = false;
            try {
              LOG.log(Level.INFO, "Describing Document: \n"
                      + "Length: {0}\n"
                      + "EndOffset: {1}\n"
                      + "EndsWithRorN: {2}",
                      new Object[]{
                        document.getLength(),
                        document.getEndPosition().getOffset(),
                        getLastSign().endsWith("\n") || getLastSign().endsWith("\r")});
              if (trimwhitespace) {
                changed = trimWhitespaces() || changed;
              }
              if (finalnewline) {
                changed = finalNewline() || changed;
              }

              if (changed) {
                LOG.log(Level.INFO, "Saving Document!");
                cookie.saveDocument();
              }

              charsetIfChanged(fileObject, charset);
            } catch (BadLocationException | IOException ex) {
              Exceptions.printStackTrace(ex);
            }
          });
        } catch (BadLocationException ex) {
          Exceptions.printStackTrace(ex);
        }
      }

      private String getLastSign() throws BadLocationException {
        return document.getText(document.getLength() - 1, 1);
      }

      private boolean trimWhitespaces() throws BadLocationException {
        String content = document.getText(0, document.getLength());
        BufferedReader reader = new BufferedReader(new StringReader(content));

        /**
         * Note: As a side effect this will strip a final newline!
         */
        String newContent = reader.lines().
                map((String t) -> {
                  return t.replaceAll("\\s+$", "");
                }).
                collect(Collectors.joining(lineEnding));

        /**
         * appending lineending only if that was the case in the old content.
         */
        if (content.endsWith("\n") || content.endsWith("\r")) {
          newContent = newContent + lineEnding;
        }
        if (!content.equals(newContent)) {
          LOG.log(Level.INFO, "TRIM_TRAILING_WHITESPACE : trimming trailing whitespaces");
          document.remove(0, document.getLength());
          document.insertString(0, newContent, null);

          return true;
        } else {
          LOG.log(Level.INFO, "TRIM_TRAILING_WHITESPACE : No changes");
          return false;
        }
      }

      private boolean finalNewline() throws BadLocationException {
        if (!getLastSign().endsWith("\n") && !getLastSign().endsWith("\r")) {
          LOG.log(Level.INFO, "INSERT_FINAL_NEWLINE : Adding final newline");
          document.insertString(document.getLength(), lineEnding, null);
          return true;
        } else {
          LOG.log(Level.INFO, "INSERT_FINAL_NEWLINE : No changes");
          return false;
        }
      }
    }
  }

  private String finalNewline(String content, String lineEnding) {
    if (!content.endsWith("\n") && content.endsWith("\r")) {
      LOG.log(Level.INFO, "INSERT_FINAL_NEWLINE : Adding final newline");
      return content + lineEnding;
    } else {
      LOG.log(Level.INFO, "INSERT_FINAL_NEWLINE : No changes");
      return content;
    }
  }

  private String trimWhitespaces(String content, String lineEnding) {
    return Arrays.stream(content.split("\n")).
            map((String t) -> {
              return t.replaceAll("\\s+$", "");
            }).
            collect(Collectors.joining(lineEnding));
  }

  private void charsetIfChanged(FileObject fileObject, Charset charset) {
    Charset currentCharset = getCharset(fileObject);
    String content = getFileContent(fileObject);
    if (!currentCharset.name().equals(charset.name())) {
      LOG.log(Level.INFO, "CHARSET : Saving file with new charset");
      new WriteFileTask(fileObject, charset) {
        @Override
        public void apply(OutputStreamWriter writer) {
          try {
            writer.write(content);
          } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
          }
        }
      }.run();
    } else {
      LOG.log(Level.INFO, "CHARSET : No changes");
    }
  }

  private void charsetIfChanged(FileObject fileObject, String content, Charset charset) {
    Charset currentCharset = getCharset(fileObject);
    if (!currentCharset.name().equals(charset.name())) {
      LOG.log(Level.INFO, "CHARSET : Saving content with new charset");
      new WriteFileTask(fileObject, charset) {
        @Override
        public void apply(OutputStreamWriter writer) {
          try {
            writer.write(content);
          } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
          }
        }
      }.run();
    } else {
      LOG.log(Level.INFO, "CHARSET : No changes");
    }
  }

  private void charset(FileObject fileObject, String content, Charset charset) {
    LOG.log(Level.INFO, "CHARSET : Saving content with charset");
//    Charset currentCharset = getCharset(fileObject);
//    if (!currentCharset.name().equals(charset.name())) {
    new WriteFileTask(fileObject, charset) {
      @Override
      public void apply(OutputStreamWriter writer) {
        try {
          writer.write(content);
        } catch (IOException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
    }.run();
//    }
  }

  private EditorCookie getEditorCookie(FileObject fileObject) {
    try {
      return (EditorCookie) DataObject.find(fileObject).getCookie(EditorCookie.class);
    } catch (DataObjectNotFoundException ex) {
      Exceptions.printStackTrace(ex);
      return null;
    }
  }

  private String getFileContent(FileObject fileObject) {
    try {
      return fileObject.asText();
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
      return "";
    }
  }

  /**
   * TODO: It looks like "FileEncodingQuery.getEncoding" always returns "UTF-8".
   *
   * Even if the charset of that file is already UTF-16LE. Therefore we should
   * change our charset lookup. After the charset has been changed by us, we add
   * a file attribute which helps us to detect the charset in future.
   *
   * Maybe we should use a CharsetDetector:
   * http://userguide.icu-project.org/conversion/detection
   *
   * @param fo
   * @return
   */
  private Charset getCharset(FileObject fo) {
    Object fileEncoding = fo.getAttribute(FileAttributeName.ENCODING);

    if (fileEncoding == null) {
      Charset currentCharset = FileEncodingQuery.getEncoding(fo);
      fileEncoding = currentCharset.name();
    }

    return Charset.forName((String) fileEncoding);
  }
}
