package zwj.test.Utils.shell;

/**
 */
public class ShellResult {

  public int code = -1;
  public String output;

  @Override
  public String toString() {
    return "ShellResult{" +
           "code=" + code +
           ", output='" + output + '\'' +
           '}';
  }
}
