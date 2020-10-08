package JDBC.utils;

import org.junit.Test;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Properties;

public class JDBCUtils {
/**@description 获取数据库的链接
 *
 *
 */
@Test
public static Connection getConnection() throws Exception {
    InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("JDBC1.properties");
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

    return conn;

    }
    /**
     *
     * @Description 关闭连接和Statement的操作
     * @author shkstart
     * @date 上午9:12:40
     * @param conn
     * @param ps
     */
    public static void closeResource(Connection conn, Statement ps, ResultSet rs){
        try {
            if(ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void closeResource(Connection conn, Statement ps){
        try {
            if(ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static <T> T getInstance(Class<T> clazz,String sql,Object...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //1.获取数据库连接
            conn = JDBCUtils.getConnection();

            //2.预编译sql语句，得到PreparedStatement对象
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for(int i = 0;i < args.length;i++){
                ps.setObject(i + 1,args[i]);
            }
            //4.执行executeQuery(),得到结果集：ResultSet
            rs = ps.executeQuery();
            //5.得到结果集的元数据：ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            //6.1通过ResultSetMetaData得到columnCount,columnLabel;
            int columnCount = rsmd.getColumnCount();
            if(rs.next()){
                T t = clazz.newInstance();
                for(int i = 0;i < columnCount; i++){//遍历每一个列
                    //获取列值
                    Object columnVal = rs.getObject(i + 1);
                    //获取列的别名：列的别名，使用类的属性名充当
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    //6.2使用反射，给对象的相应属性赋值
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnVal);

                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
    }
}
