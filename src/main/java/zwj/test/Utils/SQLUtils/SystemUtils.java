package zwj.test.Utils.SQLUtils;

import java.io.File;

/**
 * 系统工具
 * Created by bjliuhongbin on 14-3-19.
 */
public class SystemUtils {
    public final static String LINE_SEPARETOR = System.getProperty("line.separator");
    public final static String TMP_DIR = System.getProperty("java.io.tmpdir");

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        deleteFile(file);
    }

    public static void deleteFile(File file) {
        if (!file.exists()) return ;

        if (file.isDirectory()) {
            File[] children = file.listFiles();
            for (File child : children) {
                deleteFile(child);
            }

            file.delete();
        } else {
            file.delete();
        }
    }



}
