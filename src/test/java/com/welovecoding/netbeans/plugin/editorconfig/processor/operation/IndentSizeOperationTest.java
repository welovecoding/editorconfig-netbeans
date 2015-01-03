package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import com.welovecoding.netbeans.plugin.editorconfig.processor.operation.tobedone.IndentSizeOperation;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.text.StyledDocument;
import static junit.framework.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
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

  /**
   * TODO: Test does not work because IndentSizeOperation doesn't work.
   * @throws Exception 
   */
  @Test
  @Ignore
  public void testApply() throws Exception {
    String contentWith4Spaces = "<html>" + System.lineSeparator()
            + "    <body>" + System.lineSeparator()
            + "    </body>" + System.lineSeparator()
            + "</html>";

    boolean changedIndentSize = IndentSizeOperation.doIndentSize(testDataObject, "4");
    FileObject fileWith2Spaces = testDataObject.getPrimaryFile();
    String mimeType = fileWith2Spaces.getMIMEType();
    Preferences codeStyle = CodeStylePreferences.get(fileWith2Spaces, mimeType).getPreferences();

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
        // Save with 4 spaces (instead of 2)
        cookie.saveDocument();
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    });

    String testFileContent = fileWith2Spaces.asText();
    String formattedContent = document.getText(0, document.getLength());

    System.out.println("Original file (with 2 spaces):");
    System.out.println(testFileContent);

    System.out.println("Rewritten file (with 4 spaces):");
    System.out.println(formattedContent);

    assertEquals(true, changedIndentSize);
    assertEquals(contentWith4Spaces, formattedContent);
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
