<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%> 
<%@ page import="bean.imp.UserBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="bean.imp.ArticleBean" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<% Map context = (Map)request.getAttribute("context"); %>
<% UserBean viewerBean = (UserBean)context.get("viewerBean"); %>
<% ArticleBean targetArticle = (ArticleBean)context.get("articleBean"); %>
<% if(null != targetArticle){ %>
	<title>查看博文<% out.println(targetArticle.getTitle()); %></title>
<% }else{ %>
	<title>博文不存在</title>
<% } %>
</head>
<% boolean isExist = (boolean)context.get("isExist");%>
<!-- 用户不存在的时候 -->
<% if(isExist == false){ %>
<body>
	博文不存在。
	<br/>
</body>
<% 
	return;
	}else{  %>
<body>
<hr style="margin:10px auto;"/>
博文&nbsp;&nbsp;<% out.println(targetArticle.getTitle()); %>&nbsp;&nbsp;的详细信息页面
 <hr style="margin:10px auto;"/>
<div style="border:1px solid black; 
			width:100%; 
			height:200px; 
			margin: 10px auto;">
	
	<!-- 博文部分 -->
	<div style="border:1px solid black; 
			width:98.5%; 
			height:90%; 
			margin: 10px;
			position:relative;">
	博文详细:
	<% List<ArticleBean> articleList = (List<ArticleBean>)context.get("articleList"); %>
	<hr>
		<div style="border:1px solid black; 
			height:70%; 
			overflow:scroll;">
			博文id:<% out.println(targetArticle.getId()); %>
			&nbsp;&nbsp;
			博文title:<% out.println(targetArticle.getTitle()); %>
			&nbsp;&nbsp;
			阅读量:<% out.println(targetArticle.getRead_count()); %>
			
			<br/>
			内容: <% out.println(targetArticle.getDetail()); %>
		</div>
	</div>
</div>
</div>
</body>
<%	} %>
</html>