package zwj.test.Utils.shell;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShellWroker implements Callable<ShellResult> {

  private static final Logger logger = LoggerFactory.getLogger(ShellWroker.class);
  /**
   * executor中都是daemon线程，当JVM中只剩下executor中的线程时，JVM会退出。
   */
  private static final ExecutorService executor = Executors.newCachedThreadPool(
      new ThreadFactoryBuilder().setDaemon(true).build());

  private final String command;
  private final String threadNameSuffix;

  private final StringBuffer output = new StringBuffer();

  public ShellWroker(String command, String threadNameSuffix) {
    this.command = command;
    this.threadNameSuffix = threadNameSuffix;
  }

  public ShellResult call() throws Exception {
    Thread.currentThread().setName("ShellWroker-" + threadNameSuffix);
    Process proc = Runtime.getRuntime().exec(command);

    StreamPrinter out = new StreamPrinter(threadNameSuffix, proc.getInputStream(), output);
    executor.submit(out);
    StreamPrinter err = new StreamPrinter(threadNameSuffix, proc.getErrorStream(), output);
    executor.submit(err);

    int exit = proc.waitFor();
    out.waitForDone();
    err.waitForDone();
    ShellResult ret = new ShellResult();
    ret.code = exit;
    ret.output = output.toString();

    return ret;
  }
}

