<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>用户登录</title>
</head>
<body>
请登录微博系统
<br/>
<br/>
<br/>
<form action="./login.do?userListType=fan" method="post">
	用户名:<input type="text" name="username"/>
	<br/>
	密码: <input type="password" name="password" />
	<br/>
	<input type="submit" value="登录" />
</form>

</body>
</html>