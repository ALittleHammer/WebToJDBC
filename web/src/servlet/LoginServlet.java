package servlet;

import bean.User;
import dao.JDBCUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  {
//        //1.接收用户填写的用户名和密码封装在bean中
        String user = req.getParameter("username");
        String password = req.getParameter("password");
        System.out.println(user + password);
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(password);
        //2.查询数据库中是否存在相应用户

        String sql = "select user,password,balance from user_table where user=? and password=?";

        User returnUser = JDBCUtils.getInstance(User.class,sql,user,password);
        System.out.println(returnUser.getBalance());
        try {
            resp.getWriter().write(returnUser.getBalance());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
