package com.welovecoding.nbeditorconfig.netbeans;

import java.util.HashMap;
import java.util.Map;
import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.parsing.api.Source;
import org.netbeans.spi.editor.document.OnSaveTask;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ChangeLE.class)
public class ChangeLEProvider implements OnSaveTask, ChangeLE {

  public static final String LF = "LF"; // NOI18N
  public static final String CR = "CR"; // NOI18N
  public static final String CRLF = "CRLF"; // NOI18N
  private static final Map<String, String> LE_TYPES = new HashMap<>();
  private Document document;

  static {
    LE_TYPES.put(LF, BaseDocument.LS_LF);
    LE_TYPES.put(CR, BaseDocument.LS_CR);
    LE_TYPES.put(CRLF, BaseDocument.LS_CRLF);
  }

  public ChangeLEProvider() {
    this.document = null;
  }

  private ChangeLEProvider(Document document) {
    this.document = document;
  }

  @Override
  public void performTask() {
    String le = (String) document.getProperty(BaseDocument.READ_LINE_SEPARATOR_PROP);
    for (Map.Entry<String, String> entry : LE_TYPES.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (value.equals(le)) {
        System.out.println("LE: " + key);
      }
    }
    document.putProperty(BaseDocument.READ_LINE_SEPARATOR_PROP, ECProjectPreferences.getLineEnding(getProject()));
  }

  @Override
  public void runLocked(Runnable r) {
    r.run();
  }

  @Override
  public boolean cancel() {
    return true;
  }

  private Project getProject() {
    Source source = Source.create(document);
    FileObject fileObject = source.getFileObject();
    return FileOwnerQuery.getOwner(fileObject);
  }

  @MimeRegistration(mimeType = "", service = OnSaveTask.Factory.class, position = 1500)
  public static final class FactoryImpl implements Factory {

    @Override
    public OnSaveTask createTask(Context context) {
      return new ChangeLEProvider(context.getDocument());
    }
  }

}
