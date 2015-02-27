package com.welovecoding.nbeditorconfig.processor.operation.tobedone;

import com.welovecoding.nbeditorconfig.io.model.MappedCharset;
import com.welovecoding.nbeditorconfig.io.reader.FileInfoReader;
import com.welovecoding.nbeditorconfig.io.reader.FileObjectReader;
import static com.welovecoding.nbeditorconfig.processor.EditorConfigProcessor.OPERATION_LOG_LEVEL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

public class CharsetOperation {

  private static final Logger LOG = Logger.getLogger(CharsetOperation.class.getSimpleName());

  static {
    LOG.setLevel(OPERATION_LOG_LEVEL);
  }

  /**
   * TODO:<br>
   * 1. Check charset attribute<br>
   * 2. If no attribute exists, then take the default encoding from project
   * settings<br>
   * 3. Change charset (if needed) and save file &amp; charset attribute
   *
   * @param dataObject Object that represents the file
   * @param requestedCharset Charset which should be used to save the file
   *
   * @return true if the operation was successful
   */
  public boolean run(DataObject dataObject, MappedCharset requestedCharset) {
    boolean changedCharset = false;
    FileObject fo = dataObject.getPrimaryFile();
    MappedCharset currentCharset = FileInfoReader.readCharset(fo);

    LOG.log(Level.INFO, "\u00ac Current charset: {0}", currentCharset.getName());

    if (!currentCharset.getCharset().name().equals(requestedCharset.getCharset().name())) {
      LOG.log(Level.INFO, "\u00ac Changing charset from \"{0}\" to \"{1}\"",
              new Object[]{currentCharset.getName(), requestedCharset.getName()});

      String content = FileObjectReader.read(fo, currentCharset.getCharset());
      // FileObjectWriter.writeWithAtomicAction(dataObject, requestedCharset.getCharset(), content);

    } else {
      /*
       try {
       // TODO: A bit dangerous atm!
       // ConfigWriter.rewrite(dataObject, currentCharset, requestedCharset);
       } catch (IOException ex) {
       Exceptions.printStackTrace(ex);
       }
       */
      LOG.log(Level.INFO, "\u00ac No change needed");
    }

    return changedCharset;
  }
}
