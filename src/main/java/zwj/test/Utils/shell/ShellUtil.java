package zwj.test.Utils.shell;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;

public class ShellUtil {

  private static final Logger logger = LoggerFactory.getLogger(ShellUtil.class);

  /**
   * executor中都是daemon线程，当JVM中只剩下executor中的线程时，JVM会退出。
   */
  private static final ListeningExecutorService executor = MoreExecutors.listeningDecorator(
      Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).build()));

  /**
   * 同步执行shell命令，失败，或exit值不为0,抛异常。
   */
  public static ShellResult executeSync(final String command, final String threadNameSuffix) throws Exception {
    Preconditions.checkArgument(null != command && !command.trim().isEmpty());
    Preconditions.checkArgument(null != threadNameSuffix && !threadNameSuffix.trim().isEmpty());

    ShellWroker worker = new ShellWroker(command, threadNameSuffix);
    ListenableFuture<ShellResult> future = executor.submit(worker);
    return future.get();
  }

  /**
   * 异步执行shell命令，立刻返回一个ListenableFuture。在ListenableFuture调用get后，等待命令执行完，如果失败，
   * 或exit值不为0,抛异常。
   */
  public static ListenableFuture<ShellResult> executeAsync(final String command, final String threadNameSuffix)
      throws Exception {
    Preconditions.checkArgument(null != command && !command.trim().isEmpty());
    Preconditions.checkArgument(null != threadNameSuffix && !threadNameSuffix.trim().isEmpty());

    final ShellWroker worker = new ShellWroker(command, threadNameSuffix);
    return executor.submit(worker);
  }

  public static void main(String... args) throws Exception {
//    ShellUtil.executeSync("/tmp/abcd", "bad-shell");
    testSync();
//    testAsync();
  }

  private static void testSync() throws Exception {
    ShellResult ret = ShellUtil.executeSync("ls -R -l /tmp", "my-shell-executor1");
    ret = ShellUtil.executeAsync("ls -R -l /tmp", "my-shell-executor1").get();
    logger.info("ret={}", ret);
//    ShellUtil.executeSync("ls -al -h -R /tmp", "my-shell-executor2");
//    for (int i = 0; i < 10; i++) {
//      ShellUtil.executeSync("ls -al -h -R /tmp", "my-shell-executor" + i);
//    }
  }

  private static void testAsync() throws Exception {
    List<ListenableFuture<ShellResult>> fs = Lists.newArrayList();
    ListenableFuture<ShellResult> f = null;
    f = ShellUtil.executeAsync("ls /tmp", "my-shell-executor1");
    fs.add(f);
    f = ShellUtil.executeAsync("ls -al -h -R /tmp", "my-shell-executor2");
    fs.add(f);
    for (int i = 0; i < 10; i++) {
      f = ShellUtil.executeAsync("ls -al -h -R /tmp", "my-shell-executor" + i);
      fs.add(f);
    }
    Futures.successfulAsList(fs).get();
  }
}