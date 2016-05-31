package zwj.hadoop.testHdfs;

/**
 * Created by zhangwj on 16-3-8.
 *
 *  不使用uri版本，将hadoop配置加载到conf中
 *  需要 hadoop core-site.xml和 hdfs-site.xml 配置到resources路径下
 *
 *  主要使用的是  FileSystem 类来对 hdfs文件/目录进行操作。
 *   查看 FileSystem class可以调用相关的方法
 *
 *   源码中查看实现方式及用法
 *
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import java.io.FileOutputStream;
import java.io.IOException;


public class testHDFS2 {

    /**
     * 上传文件
     * @param conf
     * @param local
     * @param remote
     * @throws IOException
     */
    public static void copyFile(Configuration conf  , String local, String remote) throws IOException {
        FileSystem fs = FileSystem.get(conf);
        fs.copyFromLocalFile(new Path(local), new Path(remote));    // 这里会overwrite已存在的同名文件
        System.out.println("copy from: " + local + " to " + remote);
        fs.close();
    }

    /**
     * 获取hdfs上文件流
     * @param conf
     * 
     * @param local
     * @param remote
     * @throws IOException
     */
    public static  void getFileStream(Configuration conf  , String local, String remote) throws IOException{
        FileSystem fs = FileSystem.get(conf);
        Path path= new Path(remote);
        FSDataInputStream in = fs.open(path);//获取文件流
        FileOutputStream fos = new FileOutputStream("C:/Users/Administrator/Desktop/b.txt");//输出流
        int ch = 0;
        while((ch=in.read()) != -1){
            fos.write(ch);
        }
        System.out.println("-----");
        in.close();
        fos.close();
    }

    /**
     * 创建文件夹
     * @param conf
     * 
     * @param remoteFile
     * @throws IOException
     */
    public static void markDir(Configuration conf  , String remoteFile ) throws IOException{
        FileSystem fs = FileSystem.get(conf);
        Path path = new Path(remoteFile);

        fs.mkdirs(path);
        System.out.println("创建文件夹"+remoteFile);

    }
    /**
     * 查看文件
     * @param conf
     * 
     * @param remoteFile
     * @throws IOException
     */
    public static void cat(Configuration conf  ,String remoteFile) throws IOException {
        Path path = new Path(remoteFile);
        FileSystem fs = FileSystem.get(conf);
        FSDataInputStream fsdis = null;
        System.out.println("cat: " + remoteFile);
        try {
            fsdis = fs.open(path);
            IOUtils.copyBytes(fsdis, System.out, 4096, false);
        } finally {
            IOUtils.closeStream(fsdis);
            fs.close();
        }
    }
    /**
     * 下载 hdfs上的文件
     * @param conf
     * 
     * @param remote
     * @param local
     * @throws IOException
     */
    public static void download(Configuration conf  ,String remote, String local) throws IOException {
        Path path = new Path(remote);
        FileSystem fs = FileSystem.get(conf);
        fs.copyToLocalFile(path, new Path(local));
        System.out.println("download: from" + remote + " to " + local);
        fs.close();
    }
    /**
     * 删除文件或者文件夹
     * @param conf
     * 
     * @param filePath
     * @throws IOException
     */
    public static void delete(Configuration conf ,String filePath) throws IOException {
        Path path = new Path(filePath);
        FileSystem fs = FileSystem.get(conf);
        fs.deleteOnExit(path);
        System.out.println("Delete: " + filePath);
        fs.close();
    }
    /**
     * 查看目录下面的文件
     * @param conf
     *
     * @param folder
     * @throws IOException
     */
    public static  void ls(Configuration conf  , String folder) throws IOException {
        Path path = new Path(folder);
        FileSystem fs = FileSystem.get(conf);
        FileStatus[] list = fs.listStatus(path);
        System.out.println("ls: " + folder);
        System.out.println("==========================================================");
        for (FileStatus f : list) {
            System.out.printf("name: %s, folder: %s, size: %d\n", f.getPath(),f.isDirectory() , f.getLen());
        }
        System.out
                .println("==========================================================");
        fs.close();
    }

    /**
     *
     * @param parentName 绝对路径地址
     * @throws Exception
     */
    /*public static void checkDir(String uri,String parentName)	throws Exception{
        //D:\file
        Configuration conf = new Configuration();
        File file = new File(parentName);
        boolean flag = true;
        while (flag)	{
            //查出parentName下的所有文件
            File[] fileNames = file.listFiles(new FileFilter());
            if(fileNames != null)	{
                for (int i = 0; i < fileNames.length; i++) {
                    File f = fileNames[i];
                    //System.out.println("parent directory:"+f.getParent()+",file name:"+f.getName());
                    System.out.println("parent directory:"+f.getParent().replace("\\", "/").substring(2)+",file name:"+f.getName());
                    String remoteFolrd= "hdfs://192.168.0.173:8020/Workspace/houlinlin"+f.getParent().replace("\\", "/").substring(2);
                    markDir(conf ,remoteFolrd);
                    copyFile(conf ,f.getParent()+"\\"+f.getName(),remoteFolrd);
                }
            }
            //查出parentName下的所有目录
            File[] directories = file.listFiles(new DirectortyFilter());
            if(directories != null)	{
                for (int i = 0; i < directories.length; i++) {
                    File dir = directories[i];
                    //绝对路径
                    String path =  dir.getAbsolutePath();
                    //递归
                    checkDir(uri,path);
                }
            }
            flag = false;
        }
    }*/

    public static void main(String[] args) throws Exception  {
        String local="file:///home/zhangwj/Applications/logs/";  // 本地路径，使用conf后默认文件系统为hdfs，本地路径文件需要指定 file:///
        String remote="/hdfs/";    // hdfs文件路径
        Configuration conf = new Configuration();   //实例化的时候会自动去读取hadoop配置文件：core-site.xml,hdfs-site.xml(里面包含了fs.defaultFS)

        /* 查看目录内容 */
           ls(conf ,"/hdfs");

        /* 显示文件内容 */
        //  cat(conf ,remote);

        /* 下载 hdfs 文件 */
        //  download(conf ,remote+"hadoop-zhangwj-namenode-zhangwj-OptiPlex-3020.log",local);

        /* 创建 hdfs 文件夹 */
        //  markDir(conf ,remote+"test");

        /* 本地文件上传到hdfs */
        // copyFile(conf,local+"hadoop-zhangwj-namenode-zhangwj-OptiPlex-3020.log",remote);

        /* 删除 hdfs 文件 */
        //    delete(conf ,remote+"hadoop-zhangwj-namenode-zhangwj-OptiPlex-3020.log");

    }

}

