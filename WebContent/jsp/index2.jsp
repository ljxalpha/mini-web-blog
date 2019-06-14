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
<title>用户主页</title>
</head>
<% Map context = (Map)request.getAttribute("context"); %>
<% boolean isLogin = (boolean)context.get("isLogin");%>
<!-- 用户名或密码错误 登录失败的时候 -->
<% if(isLogin == false){ %>
<body>
	用户不存在或密码错误。
	<br/>
	<a href="./login">返回重新登录</a>
</body>
<% 
	return;
	}else{  %>
<body>
<!-- 登录成功的页面 -->
<% UserBean userBean = (UserBean)context.get("curUser"); %>
<hr style="margin:10px auto;"/>
欢迎:<% out.println(userBean.getName()); %>
 &nbsp; &nbsp;
你的点击量:<% out.println(userBean.getClick_count()); %>
 &nbsp; &nbsp;
你的粉丝量:<% out.println(userBean.getFans_count()); %>
<hr/>
<div style="border:1px solid black; 
			width:100%; 
			height:800px; 
			margin: 10px auto;">
	<% int curUserPage = (Integer)context.get("curUserPage"); %>
	<% int curArticlePage = (Integer)context.get("curArticlePage"); %>
	<% String curUserListType = (String)context.get("userListType"); %>
	<div style="border:1px solid black; 
			width:25%; 
			height:97%; 
			float:left;
			margin: 10px;
			position:relative;">
	<a href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>
				&userListType=fan" >
		<button>我的粉丝</button>
	</a>
	<a href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>
				&userListType=fanof" >
		<button>我的关注</button>
	</a>
	<% List<UserBean> userList = (List<UserBean>)context.get("userList"); %>
	<% for(int i=0;i<userList.size();i++){ 
		UserBean cur = userList.get(i);
	%>
		<div style="border:1px solid black; 
			height:8%; ">
			<% if ("fanof".equals(curUserListType)){ %>
				关注用户:<% out.println(cur.getName()); %> 
			<% }else{ %>
				粉丝用户:<% out.println(cur.getName()); %> 
			<% } %>
			 &nbsp; &nbsp;
			点击数:<% out.println(cur.getClick_count()); %> 
			 &nbsp; &nbsp;
			粉丝数:<% out.println(cur.getFans_count()); %> 
			<% if ("fanof".equals(curUserListType)){ %>
				<a href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>
				&userPage=<% out.println(curUserPage); %>&userListType=fanof&unFollowTargetId=<% out.println(cur.getId()); %>">
					<button>取关</button>
				</a>
			<% } %>
			
		</div>
	<% } %>
		<!-- 构造分页 -->
		<div style=" 
				height:8%;position:absolute;
				bottom:0px; ">
			<% List<Integer> pageRange = (List)context.get("pageRange"); %>
			<% if (pageRange.size() > 0){ %>
				<a <% if(curUserPage>1){ %> href = "./login.do?username=<% out.println(userBean.getName());%>
								&userPage=<% out.println(curUserPage-1); %>&articlePage=<% out.println(curArticlePage);%>
								&userListType=<% out.println(curUserListType); %>" <% } %>>
					上一页
				</a> &nbsp;
				<% for(int i=0;i<pageRange.size();i++){ 
					int cur_num = pageRange.get(i);
				%>
					<a <% if(curUserPage != cur_num){ %>href = "./login.do?username=<% out.println(userBean.getName());%>&articlePage=<% out.println(curArticlePage); %>
						&userPage=<% out.println(cur_num); %>&userListType=<% out.println(curUserListType); %>" <% } %>>
						<% out.println(cur_num); %>
					</a>
					&nbsp; 
				<% } %>
				<% int max_index = pageRange.get(pageRange.size()-1); %>
				<a <% if(curUserPage<max_index){ %> href = "./login.do?username=<% out.println(userBean.getName());%>
								&userPage=<% out.println(curUserPage+1); %>&articlePage=<% out.println(curArticlePage); %>
								&userListType=<% out.println(curUserListType); %>" <% } %>>
					下一页
				</a>
			<% } %>
		</div>
	</div>
	
	<!-- 博文部分 -->
	<div style="border:1px solid black; 
			width:70%; 
			height:97%; 
			float:right;
			margin: 10px;
			position:relative;">
	博文列表:
	<% List<ArticleBean> articleList = (List<ArticleBean>)context.get("articleList"); %>
	<hr>
	<% for(int i=0;i<articleList.size();i++){ 
		ArticleBean cur_article = articleList.get(i);
	%>
	
		<div style="border:1px solid black; 
			height:8%; ">
			博文id:<% out.println(cur_article.getId()); %>
			&nbsp;&nbsp;
			博文title:<% out.println(cur_article.getTitle()); %>
			&nbsp;&nbsp;
			作者:<% out.println(cur_article.getUsername()); %>
			&nbsp;&nbsp;
			阅读量:<% out.println(cur_article.getRead_count()); %>
			
			<br/>
			内容: <% out.println(cur_article.getDetail()); %>
		</div>
	<% } %>
	
	<!-- 博文的页码部分 -->
		<div style=" 
					height:8%;position:absolute;
					bottom:0px; ">
			<% List<Integer> articlePageRange = (List<Integer>)context.get("articlePageRange"); %>
			<% if(articlePageRange.size() > 0){ %>
				<a
					<% if(curArticlePage!=1){ %>
					href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage-1); %>&userPage=<% out.println(curUserPage); %>
					&userListType=<% out.println(curUserListType); %>" 
					<% } %>
				>上一页</a> &nbsp;
				
				<% for(int i=0;i<articlePageRange.size();i++){
					int curNum = articlePageRange.get(i);	
				%>
					
					<a <% if(curArticlePage != curNum){ %>
					 		href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curNum); %>&userPage=<% out.println(curUserPage); %>
					 		&userListType=<% out.println(curUserListType); %>" 
					   <% } %>
					 >
						<% out.println(curNum); %>
					</a>
				&nbsp;
				<% } %>
				<a  <% if(curArticlePage!= articlePageRange.get(articlePageRange.size()-1)){ %>
					href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage+1); %>&userPage=<% out.println(curUserPage); %>
					&userListType=<% out.println(curUserListType); %>"
					<% } %>>
				下一页</a>
			<% } %>
		</div>
	</div>
	<div style="border:1px solid black; 
			width:70%; 
			height:97%; 
			float:inherit;
			margin: 10px;
			position:relative;">
		
	</div>
</div>
	

</body>
<%	} %>

</html>