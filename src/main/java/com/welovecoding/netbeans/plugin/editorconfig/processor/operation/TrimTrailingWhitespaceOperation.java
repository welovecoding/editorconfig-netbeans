package com.welovecoding.netbeans.plugin.editorconfig.processor.operation;

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

public class TrimTrailingWhitespaceOperation {

  private static final Logger LOG = Logger.getLogger(TrimTrailingWhitespaceOperation.class.getName());

  public static boolean doTrimTrailingWhitespaces(final DataObject dataObject, final Charset ecCharset, final String lineEnding) throws Exception {
    return new TrimTrailingWhitespaceOperation().apply(dataObject, ecCharset, lineEnding).call();
  }

  public Callable<Boolean> apply(final DataObject dataObject, final Charset ecCharset, final String lineEnding) {
    return new ApplyTrailingWhitespacesTask(dataObject, ecCharset, lineEnding);
  }

  private class ApplyTrailingWhitespacesTask implements Callable<Boolean> {

    private final DataObject dataObject;
    private final Charset ecCharset;
    private final String lineEnding;

    public ApplyTrailingWhitespacesTask(final DataObject dataObject, final Charset ecCharset, final String lineEnding) {
      LOG.log(Level.INFO, "Created new ApplyTrailingWhitespacesTask for File {0}", dataObject.getPrimaryFile().getPath());
      this.dataObject = dataObject;
      this.ecCharset = ecCharset;
      this.lineEnding = lineEnding;
    }

    @Override
    public Boolean call() throws Exception {
      LOG.log(Level.INFO, "Executing ApplyTrailingWhitespacesTask");
      boolean wasChanged = false;

      FileObject fo = dataObject.getPrimaryFile();

      final String content = new ReadFileTask(fo) {

        @Override
        public String apply(BufferedReader reader) {
          return NetBeansFileUtil.trimTrailingWhitespace(reader.lines(), lineEnding);
        }
      }.call();

      if (content.equals(dataObject.getPrimaryFile().asText())) {
        LOG.log(Level.INFO, "File has NO trailing whitespaces");
      } else {
        LOG.log(Level.INFO, "File has trailing whitespaces");
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
          LOG.log(Level.INFO, "{0}Action: Successfully changed encoding to \"{1}\".", new Object[]{Tab.TWO, ecCharset.name()});
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
