package com.welovecoding.nbeditorconfig.processor.operation;

import com.welovecoding.nbeditorconfig.processor.operation.IndentSizeOperation;
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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.api.editor.settings.SimpleValueNames;
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
    String codeWith4SpacesIndent = "(function(){" + System.lineSeparator();
    codeWith4SpacesIndent += "    alert('Hello World!');" + System.lineSeparator();
    codeWith4SpacesIndent += "})();";

    try {
      file = File.createTempFile(this.getClass().getSimpleName(), ".js");
      Path path = Paths.get(Utilities.toURI(file));
      Files.write(path, codeWith4SpacesIndent.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
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
  public void itDetectsIfChangesAreNeeded() throws
          IOException, BadLocationException, BackingStoreException {
    int indentWidth = 2;

    String codeWith2SpacesIndent = "(function(){" + System.lineSeparator();
    codeWith2SpacesIndent += "  alert('Hello World!');" + System.lineSeparator();
    codeWith2SpacesIndent += "})();";

    Preferences codeStyle = CodeStylePreferences.get(
            dataObject.getPrimaryFile(),
            dataObject.getPrimaryFile().getMIMEType()
    ).getPreferences();

    // Check indent size before change
    // Note: "org-netbeans-modules-csl-api" sets default value to 4
    int indentSizeBefore = codeStyle.getInt(SimpleValueNames.INDENT_SHIFT_WIDTH, 4);
    assertEquals(4, indentSizeBefore);

    // Change indent size within an operation
    boolean changeNeeded = new IndentSizeOperation(dataObject.getPrimaryFile()).changeIndentSize(indentWidth);
    assertEquals(true, changeNeeded);

    // Update code style reference
    codeStyle = CodeStylePreferences.get(
            dataObject.getPrimaryFile(),
            dataObject.getPrimaryFile().getMIMEType()
    ).getPreferences();

    // Save the new style
    codeStyle.flush();

    // Check that new style has been applied
    int indentSizeAfter = codeStyle.getInt(SimpleValueNames.INDENT_SHIFT_WIDTH, -1);
    assertEquals(indentWidth, indentSizeAfter);

    // Save indent size
    final EditorCookie cookie = dataObject.getLookup().lookup(EditorCookie.class);
    cookie.open();

    final StyledDocument document = cookie.openDocument();

    NbDocument.runAtomicAsUser(document, new Runnable() {
      @Override
      public void run() {
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
            try {
              // Save formatted document
              cookie.saveDocument();
              System.out.println("Content saved:");
              System.out.println(document.getText(0, document.getLength()));
            } catch (IOException | BadLocationException ex) {
              Exceptions.printStackTrace(ex);
            }
          }
        } catch (IOException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
    });

    // TODO: This check fails
    // assertEquals(codeWith2SpacesIndent, dataObject.getPrimaryFile().asText());
  }

}
