package com.welovecoding.netbeans.plugin.editorconfig.listener;

import com.welovecoding.netbeans.plugin.editorconfig.mapper.EditorConfigPropertyMapper;
import com.welovecoding.netbeans.plugin.editorconfig.model.EditorConfigConstant;
import com.welovecoding.netbeans.plugin.editorconfig.model.FileAttributeName;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.editorconfig.core.EditorConfig;
import org.editorconfig.core.EditorConfigException;
import org.netbeans.api.editor.settings.SimpleValueNames;
import org.netbeans.api.queries.FileEncodingQuery;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;

/**
 *
 * @author Michael Koppen
 */
public class EditorConfigProcessor {

  private static final Logger LOG = Logger.getLogger(EditorConfigProcessor.class.getName());

  private final String TAB_1 = "  ";
  private final String TAB_2 = "    ";
  private final String TAB_3 = "      ";

  private static final class InstanceHolder {

    static final EditorConfigProcessor INSTANCE = new EditorConfigProcessor();
  }

  private EditorConfigProcessor() {
  }

  public static EditorConfigProcessor getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public void applyEditorConfigRules(DataObject dataObject) {
    String filePath = dataObject.getPrimaryFile().getPath();

    LOG.log(Level.INFO, "Apply rules for: {0}", filePath);

    EditorConfig ec = new EditorConfig(".editorconfig", EditorConfig.VERSION);
    List<EditorConfig.OutPair> rules = new ArrayList<>();

    HashMap<String, String> keyedRules = new HashMap<>();
    for (EditorConfig.OutPair rule : rules) {
      keyedRules.put(rule.getKey().toLowerCase(), rule.getVal().toLowerCase());
    }

    try {
      rules = ec.getProperties(filePath);
    } catch (EditorConfigException ex) {
      LOG.log(Level.SEVERE, ex.getMessage());
    }

    FileObject primaryFile = dataObject.getPrimaryFile();
    boolean changedStyle = false;
    boolean changed = false;

    for (EditorConfig.OutPair rule : rules) {
      String key = rule.getKey().toLowerCase();
      String value = rule.getVal().toLowerCase();

      LOG.log(Level.INFO, "{0}Found rule \"{1}\" with value \"{2}\".", new Object[]{TAB_1, key, value});

      switch (key) {
        case EditorConfigConstant.CHARSET:
          String lineEnding = keyedRules.get(EditorConfigConstant.END_OF_LINE);
          lineEnding = EditorConfigPropertyMapper.normalizeLineEnding(lineEnding);
          changed = doCharset(dataObject, value, lineEnding);
          changedStyle = changedStyle || changed;
          break;
        case EditorConfigConstant.END_OF_LINE:
          changed = doEndOfLine(dataObject, value);
          changedStyle = changedStyle || changed;
          break;
        case EditorConfigConstant.INDENT_SIZE:
          changed = doIndentSize(primaryFile, value);
          changedStyle = changedStyle || changed;
          break;
        case EditorConfigConstant.INDENT_STYLE:
          changed = doIndentStyle(primaryFile, value);
          changedStyle = changedStyle || changed;
          break;
        case EditorConfigConstant.INSERT_FINAL_NEWLINE:
          changed = doInsertFinalNewLine(primaryFile);
          changedStyle = changedStyle || changed;
          break;
        case EditorConfigConstant.TAB_WIDTH:
          changed = doTabWidth(primaryFile, value);
          changedStyle = changedStyle || changed;
          break;
        default:
          LOG.log(Level.WARNING, "Unknown property: {0}", key);
      }
    }

    Preferences codeStyle = CodeStylePreferences.get(primaryFile, primaryFile.getMIMEType()).getPreferences();

    if (changedStyle) {
      try {
        codeStyle.flush();
      } catch (BackingStoreException ex) {
        LOG.log(Level.SEVERE, "Error applying code style: {0}", ex.getMessage());
      }
    }

  }

  private boolean doIndentStyle(FileObject file, String value) {
    LOG.log(Level.INFO, "{0}Set indent style to \"{1}\".", new Object[]{TAB_2, value});
    boolean expandTabs = false;
    if (value.equals(EditorConfigConstant.INDENT_STYLE_SPACE)) {
      expandTabs = true;
    }

    Preferences codeStyle = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    boolean currentValue = codeStyle.getBoolean(SimpleValueNames.EXPAND_TABS, false);

    if (currentValue != expandTabs) {
      codeStyle.putBoolean(SimpleValueNames.EXPAND_TABS, expandTabs);
      LOG.log(Level.INFO, "{0}Action: Changed indent style to space? {1}", new Object[]{TAB_2, expandTabs});
      return true;
    } else {
      LOG.log(Level.INFO, "{0}Action not needed: Indent style is already set to spaces \"{1}\".", new Object[]{TAB_2, currentValue});
      return false;
    }
  }

  private boolean doIndentSize(FileObject file, String value) {
    int indentSize = Integer.valueOf(value);

    LOG.log(Level.INFO, "{0}Set indent size to \"{1}\".", new Object[]{TAB_2, indentSize});

    Preferences codeStyle = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    int currentValue = codeStyle.getInt(SimpleValueNames.INDENT_SHIFT_WIDTH, -1);

    if (currentValue != indentSize) {
      codeStyle.putInt(SimpleValueNames.INDENT_SHIFT_WIDTH, indentSize);
      LOG.log(Level.INFO, "{0}Action: Change indent size to \"{1}\".", new Object[]{TAB_2, indentSize});
      return true;
    } else {
      LOG.log(Level.INFO, "{0}Action not needed: Value is already \"{1}\".", new Object[]{TAB_2, currentValue});
      return false;
    }
  }

  private boolean doTabWidth(FileObject file, String value) {
    int desiredTabWidth = Integer.valueOf(value);
    LOG.log(Level.INFO, "{0}Set tab width to \"{1}\".", new Object[]{TAB_2, desiredTabWidth});

    Preferences codeStyle = CodeStylePreferences.get(file, file.getMIMEType()).getPreferences();
    int actualTabWidth = codeStyle.getInt(SimpleValueNames.TAB_SIZE, -1);

    if (actualTabWidth != desiredTabWidth) {
      codeStyle.putInt(SimpleValueNames.TAB_SIZE, desiredTabWidth);
      LOG.log(Level.INFO, "{0}Action: Changed tab width to \"{1}\".", new Object[]{TAB_2, desiredTabWidth});
      return true;
    } else {
      LOG.log(Level.INFO, "{0}Action not needed: Value is already \"{1}\".", new Object[]{TAB_2, desiredTabWidth});
      return false;
    }
  }

  private boolean doInsertFinalNewLine(FileObject fo) {

    try {
      final String content = fo.asText();
      if (content.endsWith("\n") || content.endsWith("\r")) {
        return false;
      }
      final String newContent = content + System.lineSeparator();
      FileLock lock = FileLock.NONE;
      if (!fo.isLocked()) {
        BufferedOutputStream os = new BufferedOutputStream(fo.getOutputStream(lock));
        os.write(newContent.getBytes("ASCII"));
        os.flush();
        os.close();
        lock.releaseLock();
        fo.refresh(true);
      } else {
        DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message("Couldn't apply newline at the end of file \"" + fo.getName() + "." + fo.getExt() + "\"", NotifyDescriptor.WARNING_MESSAGE));
        return false;
      }

    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
      return false;
    }

    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        try {
          EditorCookie cookie = (EditorCookie) DataObject.find(fo).getCookie(EditorCookie.class);
          System.out.println("Cookie: " + cookie);
          if (cookie != null) {
//            cookie.prepareDocument().waitFinished();
            StyledDocument document = cookie.openDocument();
//            cookie.saveDocument();
            System.out.println("Document: " + document);
            for (JEditorPane pane : cookie.getOpenedPanes()) {
              JTextComponent comp = (JTextComponent) pane;
//              comp.updateUI();
//              comp.validate();
//              comp.updateUI();
//            pane.setDocument(document);
              NbDocument.runAtomicAsUser(document, new Runnable() {

                @Override
                public void run() {
                  try {
                    document.insertString(document.getEndPosition().getOffset() - 1, "\n", null);
                    cookie.saveDocument();
                  } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                  } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                  }
                }
              });

            }
//            cookie.open();

          }
        } catch (BadLocationException ex) {
          Exceptions.printStackTrace(ex);
        } catch (DataObjectNotFoundException ex) {
          Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
    }
    );

    return true;

  }

  private boolean doEndOfLine(DataObject dataObject, String value) {
    LOG.log(Level.INFO, "{0}Change line endings to \"{1}\".", new Object[]{TAB_2, value});

    String normalizedLineEnding = EditorConfigPropertyMapper.normalizeLineEnding(value);
    StyledDocument document = NbDocument.getDocument(dataObject);

    if (document != null) {
      if (!document.getProperty(BaseDocument.READ_LINE_SEPARATOR_PROP).equals(normalizedLineEnding)) {
        document.putProperty(BaseDocument.READ_LINE_SEPARATOR_PROP, normalizedLineEnding);
        LOG.log(Level.INFO, "{0}Action: Changed line endings to \"{1}\".", new Object[]{TAB_2, value});
        return true;
      } else {
        LOG.log(Level.INFO, "{0}Action not needed: Line endings are already \"{1}\".", new Object[]{TAB_2, value});
        return false;
      }
    }
    return false;
  }

  private boolean doCharset(DataObject dataObject, String ecCharset, final String lineEnding) {
    Charset requestedCharset = EditorConfigPropertyMapper.mapCharset(ecCharset);
    boolean wasChanged = false;

    LOG.log(Level.INFO, "{0}Set encoding to: \"{1}\".", new Object[]{TAB_2, requestedCharset.name()});

    FileObject fo = dataObject.getPrimaryFile();
    Charset currentCharset = getCharset(fo);

    if (currentCharset.name().equals(requestedCharset.name())) {
      LOG.log(Level.INFO, "{0}Action not needed: Encoding is already \"{1}\".",
              new Object[]{TAB_2, currentCharset.name()});
    } else {
      LOG.log(Level.INFO, "{0}Action: Rewriting file from encoding \"{1}\" to \"{2}\".",
              new Object[]{TAB_2, currentCharset.name(), requestedCharset.name()});

      final String content = new ReadFileTask(fo) {

        @Override
        public String apply(BufferedReader reader) {
          return reader.lines().collect(Collectors.joining(lineEnding));
        }
      }.call();

      boolean wasWritten = writeFile(new WriteFileTask(fo) {

        @Override
        public void apply(OutputStreamWriter writer) {
          try {
            writer.write(content);
          } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
          }
        }
      });

      if (wasWritten) {
        LOG.log(Level.INFO, "{0}Action: Successfully changed encoding to \"{1}\".", new Object[]{TAB_2, requestedCharset.name()});
        setFileAttribute(fo, FileAttributeName.ENCODING, requestedCharset.name());
        wasChanged = true;
      }
    }

    return wasChanged;
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

  private boolean writeFile(WriteFileTask task) {
    task.run();
    return true;
  }

  private void setFileAttribute(FileObject fo, String key, String value) {
    try {
      fo.setAttribute(key, value);
    } catch (IOException ex) {
      LOG.log(Level.SEVERE, "Error setting file attribute \"{0}\" with value \"{1}\" for {2}. {3}",
              new Object[]{
                key,
                value,
                fo.getPath(),
                ex.getMessage()
              });
    }
  }
}
