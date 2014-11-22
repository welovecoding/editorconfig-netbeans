package com.welovecoding.netbeans.plugin.editorconfig.filetype;

import java.util.logging.Logger;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Michael Koppen
 */
@ServiceProvider(service = MIMEResolver.class, position = 3214328)
public class FilenameResolver extends MIMEResolver {

  private static final Logger LOG = Logger.getLogger(FilenameResolver.class.getSimpleName());
  private static final String mimetype = "text/plain+ec";

  public FilenameResolver() {
    super(mimetype);
  }

  @Override
  public String findMIMEType(FileObject fo) {
    String nameExt = fo.getNameExt();
//    LOG.log(Level.INFO, "Found file with nameExt: {0}", nameExt);
    if (".editorconfig".equalsIgnoreCase(nameExt)) {
      return mimetype;
    }

    return null;
  }

}
