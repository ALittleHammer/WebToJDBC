package JDBC.statementCRUD;

import JDBC.utils.JDBCUtils;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.spec.PSSParameterSpec;
import java.sql.*;
import java.util.Scanner;

public class PreparedStatementUpdateTest {
    @Test
    public void testCommonUpdate(){
        String sql = "update `order` set order_name = ? where order_id = ?";
        update(sql,"CC",1);
    }
    @Test
    public void testLogin() {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.print("请输入用户名：");
//        String user = scanner.nextLine();
//        System.out.print("请输入密码：");
//        String password = scanner.nextLine();
        //SELECT user,password FROM user_table WHERE user = '1' or ' AND password = '=1 or '1' = '1'
        String sql = "SELECT user,password,balance FROM user_table WHERE user = ? and password = ?";
        User returnUser = getInstance(User.class,sql,"BB","654321");
        if(returnUser != null){
            System.out.println("登录成功");
            System.out.println(returnUser);
        }else{
            System.out.println("用户名不存在或密码错误");
        }
    }
    //通用的增删改操作
    public static void update(String sql,Object ...args)  {//sql中占位符的个数与可变形参的长度相同！
        Connection conn = null;
        PreparedStatement  ps =null;

        //1.获取数据库的连接
        try {
           conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement的实例
           ps = conn.prepareStatement(sql);
            //3.填充占位符
            for(int i = 0;i < args.length;i++){
                ps.setObject(i+1,args[i]);

            }
            //4.执行
            ps.execute();


        } catch (Exception e) {
            e.printStackTrace();

            //5.资源的关闭
        }finally {

            JDBCUtils.closeResource(conn,ps);
        }

    }

    //通过的针对不同表的查询：返回一个对象（version 1.0)
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
    public void update(Connection conn,String sql,Object...args) throws Exception {
        PreparedStatement ps = null;
        //1.获取PreparedStatement的实例（或：预编译sql语句）
        try {
            ps = conn.prepareStatement(sql);
            //2.填充占位符
            for(int i = 0;i < args.length;i++){
                ps.setObject(i+1,args[i]);

            }
            //3.执行sql语句
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //4.关闭资源
            JDBCUtils.closeResource(null,ps);
        }

    }
    @Test
    public void testJDBCTransaction() throws Exception {
        Connection conn = null;
        //1.获取数据库连接
        try {
            conn = JDBCUtils.getConnection();
            //2.开启事务
            conn.setAutoCommit(false);
            //3.进行数据库操作
            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            update(conn,sql1,"AA");
            //模拟网络异常
            System.out.println(10/0);
            String sql2 = "update user_table set balance = balance + 100 where user =?";
            update(conn,sql2,"BB");
            //4.若没有异常，则提交事务
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //5.若有异常，则回滚事务
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            //6.恢复每次DML操作的自动提交功能；
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //7.关闭连接
            JDBCUtils.closeResource(conn,null,null);
        }
    }
}
