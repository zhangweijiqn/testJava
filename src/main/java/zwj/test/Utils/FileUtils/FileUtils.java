package zwj.test.Utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * file工具类
 * 
 * @author wangneng
 */
public class FileUtils {
	private static String docBase;
	
	private FileUtils() {
	}
	
	/**
	 * 返回Web应用程序在文件系统中的根路径
	 * @return
	 */
	public static String getDocBase() {
		if ( docBase != null ) {
			return docBase;
		}
		
		String path = null; 
		try {  
			path = FileUtils.class.getResource("").getPath();
			if ( path.startsWith("file:") ) {
				path = path.substring(5);
			}
			
			if ( File.separator.equals("\\") && path.startsWith("/") ) {
				path = path.substring(1);
			}
			
			docBase = path.substring(0, path.indexOf("WEB-INF"));
         }  catch(Exception e){
            throw new RuntimeException("获取应用路径时发生错误！", e);
         }
		 
		 return docBase;
	}
	
	/**
	 * 替换文件路径的分隔符，以适应不同的操作系统，window："\"，Linux：“/”
	 * @param dir
	 * @return
	 */
	public static String handleDir(String dir) {
		return handleDir(dir, false);
	}
	
	/**
	 * 替换文件路径的分隔符，以适应不同的操作系统，window："\"，Linux：“/”
	 * @param dir
	 * @param isFillSep 如果路径的末尾没有路径分隔符，是否补充路径分隔符，比如：补充前为：d:\dir，补充后为d:\dir\
	 * @return
	 */
	public static String handleDir(String dir, boolean isFillSep) {
		if ( dir == null || dir.length() == 0 ) {
			return dir;
		}
		
		if ( File.separator.equals("\\") ) {
			dir = dir.replace("/", "\\");
		} else {
			dir = dir.replace("\\", "/");
		}
		
		if ( isFillSep ) {
			if ( dir.charAt(dir.length()-1) != File.separatorChar ) {
				dir = dir + File.separator;
			}
		}
	
		return dir;
	}
	
	/**
	 * 加载properties文件
	 * @param path 类路径，比如：org/jd/xx.properties，或者文件路径，比如：d:/file/xx.properties
	 * @return
	 */
	public static Properties loadProperties(String path) {
		if ( path == null || path.lastIndexOf(".properties") != (path.length()-11) ) {
			throw new IllegalArgumentException("请传入正确的properties文件路径!，path=" + path);
		}
		
		Properties p = new Properties();
        InputStream in = null;
        try {
        	in = FileUtils.class.getClassLoader().getResourceAsStream(path);
        	if ( in == null ) {
        		in = new FileInputStream(path);
        	}
        	
            if (in != null) {
                p.load(in);
            }
        } catch ( IOException e ) {
        	throw new RuntimeException(e);
        }  finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        
        return p;
	}

}
