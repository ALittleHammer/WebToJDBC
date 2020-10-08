<%--
  Created by IntelliJ IDEA.
  User: pangbo
  Date: 2020/10/8
  Time: 8:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
    <script type="text/javascript" src="./jquery-1.7.2.js"></script>
    <script>
      $(function () {
        $("#btn01").click(function () {

          window.location.href ="register.html"
        });
        $("#btn02").click(function () {

          window.location.href ="login.html"
        });
      })

    </script>
  </head>
  <body>
  <button id="btn01" >注册用户</button>
  <button id="btn02" >用户登录</button>

  </body>
</html>
