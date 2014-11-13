package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

import com.welovecoding.netbeans.plugin.editorconfig.mapper.EditorConfigPropertyMapper;
import com.welovecoding.netbeans.plugin.editorconfig.processor.ReadFileTask;
import com.welovecoding.netbeans.plugin.editorconfig.processor.Tab;
import com.welovecoding.netbeans.plugin.editorconfig.processor.WriteFileTask;
import com.welovecoding.netbeans.plugin.editorconfig.util.NetBeansFileUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;

public class CharsetOperation {

  private static final Logger LOG = Logger.getLogger(CharsetOperation.class.getName());

  public static boolean doCharset(final DataObject dataObject, final String ecCharset, final String lineEnding) throws Exception {
    return new CharsetOperation().apply(dataObject, ecCharset, lineEnding).call();
  }

  public Callable<Boolean> apply(final DataObject dataObject, final String ecCharset, final String lineEnding) {
    return new ApplyCharsetTask(dataObject, ecCharset, lineEnding);
  }

  private class ApplyCharsetTask implements Callable<Boolean> {

    private final DataObject dataObject;
    private final String ecCharset;
    private final String lineEnding;

    public ApplyCharsetTask(final DataObject dataObject, final String ecCharset, final String lineEnding) {
      this.dataObject = dataObject;
      this.ecCharset = ecCharset;
      this.lineEnding = lineEnding;
    }

    @Override
    public Boolean call() throws Exception {
      Charset requestedCharset = EditorConfigPropertyMapper.mapCharset(ecCharset);
      boolean wasChanged = false;

      LOG.log(Level.INFO, "{0}Set encoding to: \"{1}\".", new Object[]{Tab.TWO, requestedCharset.name()});

      FileObject fo = dataObject.getPrimaryFile();
      Charset currentCharset = NetBeansFileUtil.getCharset(fo);

      if (currentCharset.name().equals(requestedCharset.name())) {
        LOG.log(Level.INFO, "{0}Action not needed: Encoding is already \"{1}\".",
                new Object[]{Tab.TWO, currentCharset.name()});
      } else {
        LOG.log(Level.INFO, "{0}Action: Rewriting file from encoding \"{1}\" to \"{2}\".",
                new Object[]{Tab.TWO, currentCharset.name(), requestedCharset.name()});

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
          LOG.log(Level.INFO, "{0}Action: Successfully changed encoding to \"{1}\".", new Object[]{Tab.TWO, requestedCharset.name()});
          wasChanged = true;
        }
      }

      return wasChanged;
    }

    private boolean writeFile(WriteFileTask task) {
      task.run();
      return true;
    }

  }
}
