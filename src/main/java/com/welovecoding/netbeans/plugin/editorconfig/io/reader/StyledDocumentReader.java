package com.welovecoding.netbeans.plugin.editorconfig.io.reader;

import com.welovecoding.netbeans.plugin.editorconfig.io.exception.FileAccessException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import org.openide.filesystems.FileObject;

public class StyledDocumentReader {

  public static ArrayList<String> readFileObjectIntoLines(FileObject fo, Charset charset, String lineEnding)
          throws FileAccessException {
    ArrayList<String> lines = new ArrayList<>();
    String line;

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(fo.getInputStream(), charset))) {
      while ((line = reader.readLine()) != null) {
        lines.add(line);
        lines.add(lineEnding);
      }

      // Remove last line-break
      lines.remove(lines.size() - 1);
    } catch (IOException ex) {
      throw new FileAccessException("Document could not be read: " + ex.getMessage());
    }

    return lines;
  }
}
