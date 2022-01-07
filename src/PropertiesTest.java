import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesTest {
    //Properties通常用来处理配置文件。key和value都是String类型
    public static void main(String[] args) throws IOException {
        Properties pro = new Properties();
        FileInputStream fis = new FileInputStream("jdbc.properties");
        pro.load(fis);
        String name = pro.getProperty("name");
        String password = pro.getProperty("password");
        System.out.println(name + password);

    }
}
