<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src=" <%=basePath%>static/vue2.6.7/vue.js"></script>
<title>欢迎进入财务系统</title>
</head>
<body>
<a href="http://127.0.0.1:8080/Maven_Demo/user/test/">User Login</a>
用户名： ${user.userName}<br>
 密码：${user.userPassword}<br>
 <div id="app">
  <ul>
    <li v-for="todo in todos">
      {{ todo.text }}
    </li>
  </ul>
</div>
<!-- JavaScript 代码需要放在尾部（指定的HTML元素之后） -->
<script>
new Vue({
  el: '#app',
  data: {
    todos: [
      { text: '欢迎' },
      { text: '你好' },
      { text: 'good' }
    ]
  }
})
</script>
</body>
</html>