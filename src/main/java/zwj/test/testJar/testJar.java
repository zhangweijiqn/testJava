package zwj.test.testJar;

/**
 * Created by zhangwj on 16-3-8.
 */
public class testJar {
    /*
    *
    * 默认情况下，maven package可以自动打包成jar: clean package. 也可以配置到Configure中： Edit Configurations--> Add Maven-->Command Line: clean package
    *
    * 生成的jar包中无class文件，需要检查target文件夹下是否存在classes,若没有则可能为一下原因：
    * 生成classes：要求目录结构为 src/main/java，java marked as source package
    *
    * 添加maven插件可以定制生成的jar包：maven-assembly-plugin
    *
      * <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <artifactId>maven-assembly-plugin</artifactId>
                    <version>2.6</version>
                </plugin>
            </plugins>
        </pluginManagement>
        </build>
      *
      *
      *
      *
      *
    * */



}
