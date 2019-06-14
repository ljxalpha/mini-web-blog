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
&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;
<a href="./login" >
	<button>注销登录</button>
</a>
<hr/>
<div style="border:1px solid black; 
			width:100%; 
			height:900px; 
			margin: 10px auto;">
	<% int curUserPage = (Integer)context.get("curUserPage"); %>
	<% List<Integer> pageRange = (List)context.get("pageRange"); %>
	<% int curArticlePage = (Integer)context.get("curArticlePage"); %>
	<% List<Integer> articlePageRange = (List<Integer>)context.get("articlePageRange"); %>
	<% int curMyArticlePage = (Integer)context.get("curMyArticlePage"); %>
	<% List <Integer> myArticlePageRange = (List<Integer>)context.get("myArticlePageRange"); %>
	<% String curUserListType = (String)context.get("userListType"); %>
	<div style="border:1px solid black; 
			width:25%; 
			height:97%; 
			float:left;
			margin: 10px;
			position:relative;">
	<a href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>
				&userListType=fan&myArticlePage=<% out.println(curMyArticlePage); %>" >
		<button>我的粉丝</button>
	</a>
	<a href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>
				&userListType=fanof&myArticlePage=<% out.println(curMyArticlePage); %>" >
		<button>我的关注</button>
	</a>
	<% List<UserBean> userList = (List<UserBean>)context.get("userList"); %>
	<% for(int i=0;i<userList.size();i++){ 
		UserBean cur = userList.get(i);
	%>
		<div style="border:1px solid black; 
			height:8%; ">
			<% if ("fanof".equals(curUserListType)){ %>
				关注用户:
			<% }else{ %>
				粉丝用户:
			<% } %>
			<a href="./userview?viewerName=<% out.println(userBean.getName()); %>&targetUserName=<% out.println(cur.getName());  %>" target="_blank">
				<% out.println(cur.getName());  %>
			</a>
			 &nbsp; &nbsp;
			点击数:<% out.println(cur.getClick_count()); %> 
			 &nbsp; &nbsp;
			粉丝数:<% out.println(cur.getFans_count()); %> 
			<% if ("fanof".equals(curUserListType)){ %>
				<a href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>
				&userPage=<% out.println(curUserPage); %>&userListType=fanof&unFollowTargetId=<% out.println(cur.getId()); %>&myArticlePage=<% out.println(curMyArticlePage); %>">
					<button>取关</button>
				</a>
			<% } %>
			
		</div>
	<% } %>
		<!-- 构造分页 -->
		<div style=" 
				height:8%;position:absolute;
				bottom:0px; ">
			<% if (pageRange.size() > 0){ %>
				<a <% if(curUserPage>1){ %> href = "./login.do?username=<% out.println(userBean.getName());%>
								&userPage=<% out.println(curUserPage-1); %>&articlePage=<% out.println(curArticlePage);%>
								&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>" <% } %>>
					上一页
				</a> &nbsp;
				<% 
				//截取显示页码，显示以当前页码为中心的5个页码加上收尾页码
					int userPageStart = 0;
					int userPageEnd = pageRange.size() - 1;
					if(pageRange.size() > 5){
						if(curUserPage <= 3){
							userPageStart = 0;
							userPageEnd = 4;
						}
						else if(curUserPage >=pageRange.size() - 2){
							userPageStart = pageRange.size() - 5;
							userPageEnd = pageRange.size() - 1;
						}
						else{
							userPageStart = curUserPage - 3;
							userPageEnd = curUserPage + 1;
						}
						//判断是否需要首页码是否已经被显示
						if (userPageStart == 1) {%> 
							<a <% if(0 != curUserPage) {%> href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); 
								%>&userPage=<% out.println(pageRange.get(0)); 
								%>&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>" <% } %>>
								<% out.println(pageRange.get(0));  %>
							</a>
							&nbsp;
						 <%}
							//判断是否需要在首页码之后显示一个省略号表示隐藏的页码
						else if(userPageStart > 1){%> 
							<a <% if(0 != curUserPage) {%> href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<%
								out.println(curArticlePage); %>&userPage=<% out.println(pageRange.get(0)); 
								%>&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>" <% } %>>
								<% out.println(pageRange.get(0));  %>
							</a>
							&nbsp;…&nbsp;
						<%}
					}
				%>
				<% for(int i=userPageStart;i<=userPageEnd;i++){ 
					int cur_num = pageRange.get(i);
				%>
					<a <% if(curUserPage != cur_num){ %>href = "./login.do?username=<% out.println(userBean.getName());%>&articlePage=<% out.println(curArticlePage); %>
						&userPage=<% out.println(cur_num); %>&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>" <% } %>>
						<% out.println(cur_num); %>
					</a>
					&nbsp; 
				<% } %>
				<% 
					//判断尾页码是否已经被显示
					if (userPageEnd == pageRange.size() - 2) {%> 
						<a <% if(pageRange.size()-1 != curUserPage) {%> href="./login.do?username=<% out.println(userBean.getName()); 
						%>&articlePage=<% out.println(curArticlePage); %>&userPage=<% out.println(pageRange.get(pageRange.size()-1)); 
							%>&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>" <% } %>>
							<% out.println(pageRange.get(pageRange.size()-1));  %>
						</a>
					<%}
					//判断是否需要在尾页码之前显示一个省略号表示隐藏的页码
					else if(userPageEnd < pageRange.size() - 2){%> 
						…&nbsp;
						<a <% if(pageRange.size()-1 != curUserPage) {%> href="./login.do?username=<% out.println(userBean.getName()); 
						%>&articlePage=<% out.println(curArticlePage); %>&userPage=<% out.println(pageRange.get(pageRange.size()-1)); 
							%>&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>" <% } %>>
							<% out.println(pageRange.get(pageRange.size()-1)); %>
						</a>
					<%}
				%>
				<% int max_index = pageRange.get(pageRange.size()-1); %>
				<a <% if(curUserPage<max_index){ %> href = "./login.do?username=<% out.println(userBean.getName());%>
								&userPage=<% out.println(curUserPage+1); %>&articlePage=<% out.println(curArticlePage); %>
								&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>"" <% } %>>
					下一页
				</a>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<form action="./login.do?username=<% out.println(userBean.getName());%>
								&userPage=<% out.println(curUserPage); %>&articlePage=<% out.println(curArticlePage); %>
								&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>"
								method="post" style="float:right;">
					前往第
					<input type="number" min=1 max=<%out.println(pageRange.size()); %> name="userPageJumpto" style="width:25px"  required>
					页
					&nbsp;
					<input type="submit" value="跳转">
				</form>
			<% } %>
		</div>
	</div>
	
	<!-- 博文部分 -->
	<div style="border:1px solid black; 
			width:70%; 
			height:5%; 
			float:right;
			margin: 10px;
			position:relative;">
		<div style="width:85%;float:left;">
			<form action="./login.do?username=<% out.println(userBean.getName()); %>&userPage=<% out.println(curUserPage); 
					 		%>&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>"
					 		style="margin-top:10px;margin-left:30px" method="post">
				<span style="float:left">按标题搜索博文：</span>
				<textarea name="searchArticleTitle" rows="1" style="width:80%;float:left;" onFocus="" onBlur="" required></textarea>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="submit" value="搜索" style=""/>
			</form>
		</div>
		<div style="width:13%;float:right;">
			<form action="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=1&userPage=<% out.println(curUserPage); 
					 		%>&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>" 
					 		style="margin-top:10px;float:right;position:absolute;" method="post">
				<input type="submit" value="不搜了，随便看看" style=""/>
			</form>
		</div>
	</div>
	<div style="border:1px solid black; 
			width:70%; 
			height:89.5%; 
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
			height:8%; 
			overflow:scroll;">
			博文id:<% out.println(cur_article.getId()); %>
			&nbsp;&nbsp;
			博文title:
			<a href="./read?viewerName=<% out.println(userBean.getName()); %>&targetArticleId=<% out.println(cur_article.getId()); 
			%>&targetArticleTitle=<% out.println(cur_article.getTitle()); %>" target="_blank">
				<% out.println(cur_article.getTitle()); %>
			</a>
			&nbsp;&nbsp;
			作者:
			<a href="./userview?viewerName=<% out.println(userBean.getName()); %>&targetUserName=<% out.println(cur_article.getUsername()); %>" target="_blank">
				<% out.println(cur_article.getUsername()); %>
			</a>
			&nbsp;&nbsp;
			阅读量:<% out.println(cur_article.getRead_count()); %>
			
			<br/>

		</div>
	<% } %>
	
	<!-- 博文的页码部分 -->
		<div style=" 
					height:8%;position:absolute;
					bottom:0px; ">
			<% if(articlePageRange.size() > 0){ %>
				<a
					<% if(curArticlePage!=1){ %>
					href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage-1); %>&userPage=<% out.println(curUserPage); %>
					&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>"
					<% } %>
				>上一页</a> &nbsp;
				<% 
				//截取显示页码，显示以当前页码为中心的5个页码加上收尾页码
					int articlePageStart = 0;
					int articlePageEnd = articlePageRange.size() - 1;
					if(articlePageRange.size() > 5){
						if(curArticlePage <= 3){
							articlePageStart = 0;
							articlePageEnd = 4;
						}
						else if(curArticlePage >=articlePageRange.size() - 2){
							articlePageStart = articlePageRange.size() - 5;
							articlePageEnd = articlePageRange.size() - 1;
						}
						else{
							articlePageStart = curArticlePage - 3;
							articlePageEnd = curArticlePage + 1;
						}
						//判断是否需要首页码是否已经被显示
						if (articlePageStart == 1) {%> 
							<a <% if(0 != curArticlePage) {%> href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(articlePageRange.get(0)); %>&userPage=<% out.println(curUserPage); 
								%>&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>" <% } %>>
								<% out.println(articlePageRange.get(0)); %>
							</a>
							&nbsp;
						 <%}
							//判断是否需要在首页码之后显示一个省略号表示隐藏的页码
						else if(articlePageStart > 1){%> 
							<a <% if(0 != curArticlePage) {%> href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(articlePageRange.get(0)); %>&userPage=<% out.println(curUserPage); 
								%>&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>" <% } %>>
								<% out.println(articlePageRange.get(0)); %>
							</a>
							&nbsp;…&nbsp;
						<%}
					}
				%>
				<!-- 循环显示以当前页码为中心的5个页码 -->
				<% for(int i=articlePageStart;i<=articlePageEnd;i++){
					int curNum = articlePageRange.get(i);	
				%>
					
					<a <% if(curArticlePage != curNum){ %>
					 		href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curNum); %>&userPage=<% out.println(curUserPage); 
					 		%>&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>" 
					   <% } %>
					 >
						<% out.println(curNum); %>
					</a>
				&nbsp;
				<% } %>
				<% 
					//判断尾页码是否已经被显示
					if (articlePageEnd == articlePageRange.size() - 2) {%> 
						<a <% if(articlePageRange.size()-1 != curArticlePage) {%> href="./login.do?username=<% out.println(userBean.getName()); 
						%>&articlePage=<% out.println(articlePageRange.get(articlePageRange.size()-1)); %>&userPage=<% out.println(curUserPage); 
							%>&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>" <% } %>>
							<% out.println(articlePageRange.get(articlePageRange.size()-1)); %>
						</a>
					<%}
					//判断是否需要在尾页码之前显示一个省略号表示隐藏的页码
					else if(articlePageEnd < articlePageRange.size() - 2){%> 
						…&nbsp;
						<a <% if(articlePageRange.size()-1 != curArticlePage) {%> href="./login.do?username=<% out.println(userBean.getName()); 
						%>&articlePage=<% out.println(articlePageRange.get(articlePageRange.size()-1)); %>&userPage=<% out.println(curUserPage); 
							%>&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>" <% } %>>
							<% out.println(articlePageRange.get(articlePageRange.size()-1)); %>
						</a>
					<%}
				%>
				<a  <% if(curArticlePage!= articlePageRange.get(articlePageRange.size()-1)){ %>
					href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage+1); %>&userPage=<% out.println(curUserPage); %>
					&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>"
					<% } %>>
				下一页</a>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<form action="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>&userPage=<% out.println(curUserPage); %>
					&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>" method="post" style="float:right;">
					前往第
					<input type="number" min=1 max=<%out.println(articlePageRange.size()); %> name="articlePageJumpto" style="width:25px"  required>
					页
					&nbsp;
					<input type="submit" value="跳转">
				</form>
			<% } %>
		</div>
	</div>
</div>
<div style="border:1px solid black; 
			width:100%; 
			height:800px; 
			margin: 10px auto;">
			
	<div style="border:1px solid black; 
			width:98.7%; 
			height:55%; 
			float:left;
			margin: 10px;
			position:relative;">
		我的博文：
		<hr>
		<% List <ArticleBean> myArticleList = (List<ArticleBean>)context.get("myArticleList"); %>
		<% for (int i=0; i < myArticleList.size(); i++){ 
				ArticleBean myArticle = myArticleList.get(i);
		%>
			<div style="border:1px solid black; 
				height:16%;overflow:scroll;">
				博文id:<% out.println(myArticle.getId()); %>
				&nbsp;&nbsp;
				博文title:
				<a href="./read?viewerName=<% out.println(userBean.getName()); %>&targetArticleId=<% out.println(myArticle.getId()); 
			%>&targetArticleTitle=<% out.println(myArticle.getTitle()); %>" target="_blank">
					<% out.println(myArticle.getTitle()); %>
				</a>
				&nbsp;&nbsp;
				阅读量：<% out.println(myArticle.getRead_count()); %>
				&nbsp;&nbsp;
				<a href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>
					&userPage=<% out.println(curUserPage); %>&deleteArticleId=<% out.println(myArticle.getId()); 
					%>&myArticlePage=<% out.println(curMyArticlePage); %>&userListType=<% out.println(curUserListType); %>&deleteArticleTitle=<% out.println(myArticle.getTitle()); %>">
					<button>删除</button>
				</a>
				<br/>

			</div>
		<% } %>
		<!--我的博文页码-->
			<div style=" 
					height:8%;position:absolute;
					bottom:0px; ">
				<% if(myArticlePageRange.size() > 0) {%>
					<a <% if(curMyArticlePage > 1) { %> 
						href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>&userPage=<% out.println(curUserPage); %>
						&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage-1); %>" 
						<% } %>>
						上一页
					</a>
					&nbsp;
					<% 
					//截取显示页码，显示以当前页码为中心的5个页码加上收尾页码
						int myArticlePageStart = 0;
						int myArticlePageEnd = myArticlePageRange.size() - 1;
						if(myArticlePageRange.size() > 5){
							if(curMyArticlePage <= 3){
								myArticlePageStart = 0;
								myArticlePageEnd = 4;
							}
							else if(curMyArticlePage >=myArticlePageRange.size() - 2){
								myArticlePageStart = myArticlePageRange.size() - 5;
								myArticlePageEnd = myArticlePageRange.size() - 1;
							}
							else{
								myArticlePageStart = curMyArticlePage - 3;
								myArticlePageEnd = curMyArticlePage + 1;
							}
							//判断是否需要首页码是否已经被显示
							if (myArticlePageStart == 1) {%> 
								<a <% if(0 != curMyArticlePage) {%> href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>&userPage=<% out.println(curUserPage); 
									%>&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(myArticlePageRange.get(0)); %>" <% } %>>
									<% out.println(myArticlePageRange.get(0)); %>
								</a>
								&nbsp;
							 <%}
								//判断是否需要在首页码之后显示一个省略号表示隐藏的页码
							else if(myArticlePageStart > 1){%> 
								<a <% if(0 != curMyArticlePage) {%> href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>&userPage=<% out.println(curUserPage); 
									%>&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(myArticlePageRange.get(0)); %>" <% } %>>
									<% out.println(myArticlePageRange.get(0)); %>
								</a>
								&nbsp;…&nbsp;
							<%}
						}
					%>
					<!-- 循环显示以当前页码为中心的5个页码 -->
					<% for (int i = myArticlePageStart;i <=myArticlePageEnd;i++) {%>
						<% int curPage = myArticlePageRange.get(i); %>
						<a <% if(curPage != curMyArticlePage) {%> href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>&userPage=<% out.println(curUserPage); %>
						&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curPage); %>" <% } %>>
							<% out.println(curPage); %>
						</a>
						&nbsp;
					<% } %>
					<% 
						//判断尾页码是否已经被显示
						if (myArticlePageEnd == myArticlePageRange.size() - 2) {%> 
							<a <% if(myArticlePageRange.size()-1 != curMyArticlePage) {%> href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>&userPage=<% out.println(curUserPage); 
								%>&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(myArticlePageRange.get(myArticlePageRange.size()-1)); %>" <% } %>>
								<% out.println(myArticlePageRange.get(myArticlePageRange.size()-1)); %>
							</a>
						<%}
						//判断是否需要在尾页码之前显示一个省略号表示隐藏的页码
						else if(myArticlePageEnd < myArticlePageRange.size() - 2){%> 
							…&nbsp;
							<a <% if(myArticlePageRange.size()-1 != curMyArticlePage) {%> href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>&userPage=<% out.println(curUserPage); 
								%>&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(myArticlePageRange.get(myArticlePageRange.size()-1)); %>" <% } %>>
								<% out.println(myArticlePageRange.get(myArticlePageRange.size()-1)); %>
							</a>
						<%}
					%>
					<a <% if(curMyArticlePage <myArticlePageRange.size()) { %> 
						href="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>&userPage=<% out.println(curUserPage); %>
						&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage+1); %>" 
						<% } %>>
						下一页
					</a>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<form action="./login.do?username=<% out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>&userPage=<% out.println(curUserPage); %>
						&userListType=<% out.println(curUserListType); %>&myArticlePage=<% out.println(curMyArticlePage); %>" method="post" style="float:right;">
						前往第
						<input type="number" min=1 max=<%out.println(myArticlePageRange.size()); %> name="myArticlePageJumpto" style="width:25px" required>
						页
						&nbsp;
						<input type="submit" value="跳转">
					</form>
				<% } %>
				
				
			</div>
	</div>

	<div style="border:1px solid black; 
			width:98.7%; 
			height:39%; 
			float:left;
			margin: 10px;
			position:relative;">
		发布新的博文：
		<hr>
		<div align="center">
			<form action="./login.do?username=<% out.println(userBean.getName()); %>" method="post">
				<span style="float:left">博文标题：</span>
				<textarea name="newArticleTitle" rows="1" style="width:98%" required></textarea>
				<br>
				<span style="float:left">博文正文：</span>
				<textarea name="newArticleDetail" rows="10" style="width:98%" required></textarea>
				<br>
				<input type="submit" value="发布" style="float:right;"/>
			</form>
		</div>
	</div>
</div>
</body>
<%	} %>
<script type= "text/javascript">

</script>
</html>