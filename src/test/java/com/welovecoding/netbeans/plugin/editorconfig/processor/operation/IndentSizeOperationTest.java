package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import static junit.framework.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;

public class IndentSizeOperationTest {

  private DataObject testDataObject = null;

  @Before
  public void setUp() throws URISyntaxException, DataObjectNotFoundException {
    String path = "files/IndentSize.html";
    URL url = Thread.currentThread().getContextClassLoader().getResource(path);
    Path testFilePath = Paths.get(url.toURI());
    testDataObject = DataObject.find(FileUtil.toFileObject(testFilePath.toFile()));
  }

  public IndentSizeOperationTest() {
  }

  @Test
  public void testApply() throws Exception {
    String expected = "<html>" + System.lineSeparator()
            + "  <body>" + System.lineSeparator()
            + "  </body>" + System.lineSeparator()
            + "</html>";

    boolean change = IndentSizeOperation.doIndentSize(testDataObject, "4");

    Preferences codeStyle = CodeStylePreferences.get(testDataObject.getPrimaryFile(), testDataObject.getPrimaryFile().getMIMEType()).getPreferences();
    try {
      codeStyle.flush();
    } catch (BackingStoreException ex) {
      Exceptions.printStackTrace(ex);
    }

    EditorCookie cookie = getEditorCookie(testDataObject);
    cookie.open();
    StyledDocument document = cookie.openDocument();
    NbDocument.runAtomicAsUser(document, () -> {
      try {
        System.out.println("Saving Document!");
        cookie.saveDocument();
        System.out.println(document.getText(0, document.getLength()));
      } catch (IOException | BadLocationException ex) {
        Exceptions.printStackTrace(ex);
      }
    });

    System.out.println(testDataObject.getPrimaryFile().asText());

    assertEquals(true, change);
    assertEquals(expected, testDataObject.getPrimaryFile().asText());
  }

  private EditorCookie getEditorCookie(FileObject fileObject) {
    try {
      return (EditorCookie) DataObject.find(fileObject).getLookup().lookup(EditorCookie.class);
    } catch (DataObjectNotFoundException ex) {
      Exceptions.printStackTrace(ex);
      return null;
    }
  }

  private EditorCookie getEditorCookie(DataObject dataObject) {
    return getEditorCookie(dataObject.getPrimaryFile());
  }

}
