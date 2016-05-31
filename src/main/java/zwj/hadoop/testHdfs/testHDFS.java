package zwj.hadoop.testHdfs;

/**
 * Created by zhangwj on 16-3-8.
 *
 *   使用uri指定hdfs目录
 *   conf配置见 testHDFS2
 *
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class testHDFS {

    /**
     * 上传文件
     * @param conf
     * @param local
     * @param remote
     * @throws IOException
     */
    public static void copyFile(Configuration conf , String uri , String local, String remote) throws IOException {
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        fs.copyFromLocalFile(new Path(local), new Path(remote));
        System.out.println("copy from: " + local + " to " + remote);
        fs.close();
    }

    /**
     * 获取hdfs上文件流
     * @param conf
     * @param uri
     * @param local
     * @param remote
     * @throws IOException
     */
    public static  void getFileStream(Configuration conf , String uri , String local, String remote) throws IOException{
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
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
     * @param uri
     * @param remoteFile
     * @throws IOException
     */
    public static void markDir(Configuration conf , String uri , String remoteFile ) throws IOException{
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        Path path = new Path(remoteFile);

        fs.mkdirs(path);
        System.out.println("创建文件夹"+remoteFile);

    }
    /**
     * 查看文件
     * @param conf
     * @param uri
     * @param remoteFile
     * @throws IOException
     */
    public static void cat(Configuration conf , String uri ,String remoteFile) throws IOException {
        Path path = new Path(remoteFile);
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
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
     * @param uri
     * @param remote
     * @param local
     * @throws IOException
     */
    public static void download(Configuration conf , String uri ,String remote, String local) throws IOException {
        Path path = new Path(remote);
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        fs.copyToLocalFile(path, new Path(local));
        System.out.println("download: from" + remote + " to " + local);
        fs.close();
    }
    /**
     * 删除文件或者文件夹
     * @param conf
     * @param uri
     * @param filePath
     * @throws IOException
     */
    public static void delete(Configuration conf , String uri,String filePath) throws IOException {
        Path path = new Path(filePath);
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        fs.deleteOnExit(path);
        System.out.println("Delete: " + filePath);
        fs.close();
    }
    /**
     * 查看目录下面的文件
     * @param conf
     * @param uri
     * @param folder
     * @throws IOException
     */
    public static  void ls(Configuration conf , String uri , String folder) throws IOException {
        Path path = new Path(folder);
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
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
                    markDir(conf ,uri,remoteFolrd);
                    copyFile(conf ,uri,f.getParent()+"\\"+f.getName(),remoteFolrd);
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
        String uri="hdfs://localhost:9000/";   // hdfs 地址
        String local="/home/zhangwj/Applications/logs";  // 本地路径
        String remote="/hdfs/";    // hdfs文件路径
        Configuration conf = new Configuration();

        /* 查看目录内容 */
        //   ls(conf ,uri,"/hdfs");

        /* 显示文件内容 */
        //  cat(conf ,uri,remote);

        /* 下载 hdfs 文件 */
        //  download(conf ,uri,remote,local);

        /* 创建 hdfs 文件夹 */
        //  markDir(conf ,uri,"/hdfs/test");

        /* 本地文件上传到hdfs */
        copyFile(conf,uri,local,remote+"hadoop-zhangwj-namenode-zhangwj-OptiPlex-3020.log");    //默认 overwrite方式

        /* 删除 hdfs 文件 */
        //    delete(conf ,uri,remote);

        // checkDir(uri,"d:/file");
        // getFile(conf ,uri,"","hdfs://192.168.0.151:8020/Workspace/houlinlin/a.txt");

    }

}
