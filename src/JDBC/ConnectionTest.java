package JDBC;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {
    @Test
    public void testConnection1() throws SQLException{
        Driver driver = new com.mysql.cj.jdbc.Driver();
        String url = "jdbc:mysql://localhost:3306/test?serverTimezone=CTT";
        //将用户名密码封装在Properties中；
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","47030118");

        Connection conn = driver.connect(url,info);
        System.out.println(conn);
    }
    //迭代使用反射
    @Test
    public void testConnection2() throws Exception{
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver)clazz.newInstance();
        String url = "jdbc:mysql://localhost:3306/test?serverTimezone=CTT";
        //将用户名密码封装在Properties中；
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","47030118");

        Connection conn = driver.connect(url,info);
        System.out.println(conn);
    }
    //迭代使用DriverManager
    @Test
    public void testConnection3() throws Exception{
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver)clazz.newInstance();
        String url = "jdbc:mysql://localhost:3306/test?serverTimezone=CTT";
//        String user = "root";
//        String password= "47030118";
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","47030118");
        DriverManager.registerDriver(driver);
        Connection conn = DriverManager.getConnection(url,info);
        System.out.println(conn);

    }
    //迭代省略注册driver过程，其实在forName引用已经实现
    @Test
    public void testConnection4() throws Exception{
        String url = "jdbc:mysql://localhost:3306/test?serverTimezone=CTT";
//        String user = "root";
//        String password= "47030118";
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","47030118");

        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url,info);
        System.out.println(conn);
    }
    @Test
    //将连接数据库所需要的的信息注册在配置文件中，通过读取配置文件来建立数据库连接
    public void testConnection5() throws Exception{
        //1.读取配置文件中的4个基本信息
        InputStream is = ConnectionTest.class.getResourceAsStream("JDBC1.properties");
        Properties pros = new Properties();
        pros.load(is);

//        String user = pros.getProperty("user");
//        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");
        //2.加载驱动
        Class.forName(driverClass);

        //3.获取连接
        Connection conn = DriverManager.getConnection(url,pros);
        System.out.println(conn);
    }
}
