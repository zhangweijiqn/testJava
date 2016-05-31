package zwj.test.Utils.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.GuardedBy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamPrinter implements Runnable {

  private static final Logger logger = LoggerFactory.getLogger(StreamPrinter.class);
  private final String name;
  private final InputStream stream;
  private final StringBuffer out;
  @GuardedBy("this")
  private boolean done;

  public StreamPrinter(String name, InputStream stream, StringBuffer out) {
    this.name = name;
    this.stream = stream;
    this.out = out;
  }

  public void run() {
    try {
      Thread.currentThread().setName("StreamPrinter-" + name);
      BufferedReader br = new BufferedReader(new InputStreamReader(stream));
      String line;
      while ((line = br.readLine()) != null) {
        out.append(line);
        out.append("\n");
//        logger.info(line);
      }
    } catch (IOException e) {
      logger.error("fail to print stream", e);
    } finally {
      synchronized (this) {
//        logger.debug("notify done");
        this.done = true;
        this.notify();

      }

    }
  }

  public void waitForDone() throws InterruptedException {
    logger.debug("start waitForDone");
    synchronized (this) {
      while (!this.done) {
//        logger.debug("wair for done");
        this.wait();
      }
    }
  }
}
