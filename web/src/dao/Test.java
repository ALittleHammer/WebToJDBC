package dao;

import bean.User;

public class Test {
    @org.junit.Test
    public void testLogin(){
        String sql = "SELECT user,password,balance FROM user_table WHERE user = ? and password = ?";
        bean.User returnUser = JDBCUtils.getInstance(User.class,sql,"BB","654321");
        if(returnUser != null){
            System.out.println("登录成功");
            System.out.println(returnUser);
        }else{
            System.out.println("用户名不存在或密码错误");
        }
    }
}
