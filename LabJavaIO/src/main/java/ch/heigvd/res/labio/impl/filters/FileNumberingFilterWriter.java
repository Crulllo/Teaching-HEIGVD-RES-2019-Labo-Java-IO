package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());
  private int lineNumber = 1;
  private boolean newLineFound;

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    for (int i = off; i < off + len; i++) {
      write(str.charAt(i));
    }
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    for (int i = off; i < off + len; i++) {
      write(cbuf[i]);
    }
  }

  @Override
  public void write(int c) throws IOException {
    if (lineNumber == 1) {
      out.write(lineNumber++ + "\t");
      out.write(c);
    } else if (!newLineFound) {
      out.write(c);
    } else if (c != '\n') {
      out.write(lineNumber++ + "\t" + (char)c);
      newLineFound = false;
    }

    if (c == '\r') {
      newLineFound = true;
    } else if (c == '\n') {
      if (newLineFound) {
        out.write("\n");
        newLineFound = false;
      }
      out.write(lineNumber++ + "\t");
    }
  }

}
