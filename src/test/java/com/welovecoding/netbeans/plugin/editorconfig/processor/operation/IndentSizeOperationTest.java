package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.netbeans.modules.editor.indent.api.Reformat;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

public class IndentSizeOperationTest {

  private DataObject dataObject;
  private File file;

  @Before
  public void setUp() {
    String with4Spaces = "(function(){" + System.lineSeparator();
    with4Spaces += "    alert('Hello World!');" + System.lineSeparator();
    with4Spaces += "})();";

    try {
      file = File.createTempFile(this.getClass().getSimpleName(), ".js");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, with4Spaces.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
      dataObject = DataObject.find(FileUtil.toFileObject(file));
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  @After
  public void tearDown() {
    file.delete();
  }

  public IndentSizeOperationTest() {
  }

  @Test
  @Ignore
  public void itDetectsIfChangesAreNeeded() throws
          IOException, BadLocationException, BackingStoreException {
    String with2Spaces = "(function(){" + System.lineSeparator();
    with2Spaces += "  alert('Hello World!');" + System.lineSeparator();
    with2Spaces += "})();";

    boolean changeNeeded
            = new IndentSizeOperation().run(dataObject.getPrimaryFile(), 2);

    Preferences codeStyle = CodeStylePreferences.get(
            dataObject.getPrimaryFile(),
            dataObject.getPrimaryFile().getMIMEType()
    ).getPreferences();
    codeStyle.flush();

    EditorCookie cookie = dataObject.getLookup().lookup(EditorCookie.class);
    cookie.open();

    StyledDocument document = cookie.openDocument();

    NbDocument.runAtomicAsUser(document, () -> {
      try {
        // Save test file
        cookie.saveDocument();

        // Reformat test file
        Reformat reformat = Reformat.get(document);
        reformat.lock();

        try {
          reformat.reformat(0, document.getLength());
        } catch (BadLocationException ex) {
          Exceptions.printStackTrace(ex);
        } finally {
          reformat.unlock();
          // Save document after reformat
          try {
            cookie.saveDocument();
            System.out.println(document.getText(0, document.getLength()));
          } catch (IOException | BadLocationException ex) {
            Exceptions.printStackTrace(ex);
          }
        }
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    });

    assertEquals(true, changeNeeded);
    assertEquals(with2Spaces, dataObject.getPrimaryFile().asText());
  }

}
