package servlet;

import dao.JDBCUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = req.getParameter("username");
        String password = req.getParameter("password");
        String balance = req.getParameter("balance");
        String sql = "insert into user_table(user,password,balance)value(?,?,?)";
        JDBCUtils.update(sql,user,password,balance);
        resp.getWriter().write("注册成功");
    }
}
