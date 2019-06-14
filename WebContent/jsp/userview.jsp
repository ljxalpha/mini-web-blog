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
<% UserBean userBean = (UserBean)context.get("viewedUser"); %>
<% if(null != userBean){ %>
	<title>查看用户<% out.println(userBean.getName()); %></title>
<% }else{ %>
	<title>用户不存在</title>
<% } %>
</head>
<% boolean isExist = (boolean)context.get("isExist");%>
<!-- 用户不存在的时候 -->
<% if(isExist == false){ %>
<body>
	用户不存在。
	<br/>
</body>
<% 
	return;
	}else{  %>
<body>
<!-- 要查看的用户存在的时候 -->
<% int curUserPage = (Integer)context.get("curUserPage"); %>
<% List<Integer> pageRange = (List)context.get("pageRange"); %>
<% int curArticlePage = (Integer)context.get("curArticlePage"); %>
<% List<Integer> articlePageRange = (List<Integer>)context.get("articlePageRange"); %>
<% String curUserListType = (String)context.get("userListType"); %>
<hr style="margin:10px auto;"/>
用户&nbsp;&nbsp;<% out.println(userBean.getName()); %>&nbsp;&nbsp;的详细信息页面
 <hr style="margin:10px auto;"/>
<div style="float:left;">
他的点击量:<% out.println(userBean.getClick_count()); %>
 &nbsp; &nbsp;
他的粉丝量:<% out.println(userBean.getFans_count()); %>
&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;
</div>
<% boolean followed = (boolean)context.get("followed"); %>
<% if(followed){ %>
	<form method="post" style="float:left;">
		<input type="number" value=<% out.println(userBean.getId()); %> name="attentionTargetId" style="display:none">
		&nbsp;
		<input type="submit" value="取关">
	</form>
<% }else{ %>
	<form method="post" style="float:left;">
		<input type="number" value=<% out.println(userBean.getId()); %> name="attentionTargetId" style="display:none">
		&nbsp;
		<input type="submit" value="关注">
	</form>
<% } %>
<br>
<hr/>
<div style="border:1px solid black; 
			width:100%; 
			height:900px; 
			margin: 10px auto;">
	<div style="border:1px solid black; 
			width:25%; 
			height:97%; 
			float:left;
			margin: 10px;
			position:relative;">
	<a href="./userview?viewerName=<% out.println(viewerBean.getName()); %>&targetUserName=<%out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>
				&userListType=fan" >
		<button>他的粉丝</button>
	</a>
	<a href="./userview?viewerName=<% out.println(viewerBean.getName()); %>&targetUserName=<%out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>
				&userListType=fanof" >
		<button>他的关注</button>
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
			<a <% if(!viewerBean.getName().equals(cur.getName())){ %>
					href="./userview?viewerName=<% out.println(viewerBean.getName()); %>&targetUserName=<% out.println(cur.getName());  %>" target="_blank"
				<% } %>>
				<% out.println(cur.getName());  %>
			</a>
			 &nbsp; &nbsp;
			点击数:<% out.println(cur.getClick_count()); %> 
			 &nbsp; &nbsp;
			粉丝数:<% out.println(cur.getFans_count()); %> 
		</div>
	<% } %>
		<!-- 构造分页 -->
		<div style=" 
				height:8%;position:absolute;
				bottom:0px; ">
			<% if (pageRange.size() > 0){ %>
				<a <% if(curUserPage>1){ %> href = "./userview?viewerName=<% out.println(viewerBean.getName()); 
					%>&targetUserName=<%out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage);%>&userPage=<% out.println(curUserPage-1); %>
								&userListType=<% out.println(curUserListType); %>" <% } %>>
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
							<a <% if(0 != curUserPage) {%> href="./userview?viewerName=<% out.println(viewerBean.getName()); 
								%>&targetUserName=<%out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); 
								%>&userPage=<% out.println(pageRange.get(0)); 
								%>&userListType=<% out.println(curUserListType); %>" <% } %>>
								<% out.println(pageRange.get(0));  %>
							</a>
							&nbsp;
						 <%}
							//判断是否需要在首页码之后显示一个省略号表示隐藏的页码
						else if(userPageStart > 1){%> 
							<a <% if(0 != curUserPage) {%> href="./userview?viewerName=<% out.println(viewerBean.getName()); 
								%>&targetUserName=<%out.println(userBean.getName()); %>&articlePage=<%
								out.println(curArticlePage); %>&userPage=<% out.println(pageRange.get(0)); 
								%>&userListType=<% out.println(curUserListType); %>" <% } %>>
								<% out.println(pageRange.get(0));  %>
							</a>
							&nbsp;…&nbsp;
						<%}
					}
				%>
				<% for(int i=userPageStart;i<=userPageEnd;i++){ 
					int cur_num = pageRange.get(i);
				%>
					<a <% if(curUserPage != cur_num){ %>href = "./userview?viewerName=<% out.println(viewerBean.getName()); 
						%>&targetUserName=<%out.println(userBean.getName()); %>&articlePage=<% out.println(curArticlePage); %>
						&userPage=<% out.println(cur_num); %>&userListType=<% out.println(curUserListType); %>" <% } %>>
						<% out.println(cur_num); %>
					</a>
					&nbsp; 
				<% } %>
				<% 
					//判断尾页码是否已经被显示
					if (userPageEnd == pageRange.size() - 2) {%> 
						<a <% if(pageRange.size()-1 != curUserPage) {%> href="./userview?viewerName=<% out.println(viewerBean.getName()); %>&targetUserName=<%out.println(userBean.getName()); 
							%>&articlePage=<% out.println(curArticlePage); %>&userPage=<% out.println(pageRange.get(pageRange.size()-1)); 
							%>&userListType=<% out.println(curUserListType); %>" <% } %>>
							<% out.println(pageRange.get(pageRange.size()-1));  %>
						</a>
					<%}
					//判断是否需要在尾页码之前显示一个省略号表示隐藏的页码
					else if(userPageEnd < pageRange.size() - 2){%> 
						…&nbsp;
						<a <% if(pageRange.size()-1 != curUserPage) {%> href="./userview?viewerName=<% out.println(viewerBean.getName()); %>&targetUserName=<%out.println(userBean.getName()); 
							%>&articlePage=<% out.println(curArticlePage); %>&userPage=<% out.println(pageRange.get(pageRange.size()-1)); 
							%>&userListType=<% out.println(curUserListType); %>" <% } %>>
							<% out.println(pageRange.get(pageRange.size()-1)); %>
						</a>
					<%}
				%>
				<% int max_index = pageRange.get(pageRange.size()-1); %>
				<a <% if(curUserPage<max_index){ %> href = "./userview?viewerName=<% out.println(viewerBean.getName()); %>&targetUserName=<%out.println(userBean.getName()); %>
								&userPage=<% out.println(curUserPage+1); %>&articlePage=<% out.println(curArticlePage); %>
								&userListType=<% out.println(curUserListType); %>" <% } %>>
					下一页
				</a>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<form action="./userview?viewerName=<% out.println(viewerBean.getName()); %>&targetUserName=<%out.println(userBean.getName()); %>
								&userPage=<% out.println(curUserPage); %>&articlePage=<% out.println(curArticlePage); %>
								&userListType=<% out.println(curUserListType); %>"
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
			height:97%; 
			float:right;
			margin: 10px;
			position:relative;">
	他的博文:
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
			<a href="./read?viewerName=<% out.println(viewerBean.getName()); %>&targetArticleId=<% out.println(cur_article.getId()); 
			%>&targetArticleTitle=<% out.println(cur_article.getTitle()); %>" target="_blank">
				<% out.println(cur_article.getTitle()); %>
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
					href="./userview?viewerName=<% out.println(viewerBean.getName()); %>&targetUserName=<%out.println(userBean.getName()); 
					%>&articlePage=<% out.println(curArticlePage-1); %>&userPage=<% out.println(curUserPage); %>
					&userListType=<% out.println(curUserListType); %>"
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
							<a <% if(0 != curArticlePage) {%> href="./userview?viewerName=<% out.println(viewerBean.getName()); %>&targetUserName=<%out.println(userBean.getName()); 
								%>&articlePage=<% out.println(articlePageRange.get(0)); %>&userPage=<% out.println(curUserPage); 
								%>&userListType=<% out.println(curUserListType); %>" <% } %>>
								<% out.println(articlePageRange.get(0)); %>
							</a>
							&nbsp;
						 <%}
							//判断是否需要在首页码之后显示一个省略号表示隐藏的页码
						else if(articlePageStart > 1){%> 
							<a <% if(0 != curArticlePage) {%> href="./userview?viewerName=<% out.println(viewerBean.getName()); %>&targetUserName=<%out.println(userBean.getName()); 
								%>&articlePage=<% out.println(articlePageRange.get(0)); %>&userPage=<% out.println(curUserPage); 
								%>&userListType=<% out.println(curUserListType); %>" <% } %>>
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
					 		href="./userview?viewerName=<% out.println(viewerBean.getName()); %>&targetUserName=<%out.println(userBean.getName()); 
					 		%>&articlePage=<% out.println(curNum); %>&userPage=<% out.println(curUserPage); 
					 		%>&userListType=<% out.println(curUserListType); %>" 
					   <% } %>
					 >
						<% out.println(curNum); %>
					</a>
				&nbsp;
				<% } %>
				<% 
					//判断尾页码是否已经被显示
					if (articlePageEnd == articlePageRange.size() - 2) {%> 
						<a <% if(articlePageRange.size()-1 != curArticlePage) {%> href="./userview?viewerName=<% out.println(viewerBean.getName()); %>&targetUserName=<%out.println(userBean.getName()); 
							%>&articlePage=<% out.println(articlePageRange.get(articlePageRange.size()-1)); %>&userPage=<% out.println(curUserPage); 
							%>&userListType=<% out.println(curUserListType); %>" <% } %>>
							<% out.println(articlePageRange.get(articlePageRange.size()-1)); %>
						</a>
					<%}
					//判断是否需要在尾页码之前显示一个省略号表示隐藏的页码
					else if(articlePageEnd < articlePageRange.size() - 2){%> 
						…&nbsp;
						<a <% if(articlePageRange.size()-1 != curArticlePage) {%> 
							href="./userview?viewerName=<% out.println(viewerBean.getName()); %>&targetUserName=<%out.println(userBean.getName()); 
							%>&articlePage=<% out.println(articlePageRange.get(articlePageRange.size()-1)); %>&userPage=<% out.println(curUserPage); 
							%>&userListType=<% out.println(curUserListType); %>" <% } %>>
							<% out.println(articlePageRange.get(articlePageRange.size()-1)); %>
						</a>
					<%}
				%>
				<a  <% if(curArticlePage!= articlePageRange.get(articlePageRange.size()-1)){ %>
					href="./userview?viewerName=<% out.println(viewerBean.getName()); %>&targetUserName=<%out.println(userBean.getName()); 
					%>&articlePage=<% out.println(curArticlePage+1); %>&userPage=<% out.println(curUserPage); %>
					&userListType=<% out.println(curUserListType); %>"
					<% } %>>
				下一页</a>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<form action="./userview?viewerName=<% out.println(viewerBean.getName()); %>&targetUserName=<%out.println(userBean.getName()); 
					%>&articlePage=<% out.println(curArticlePage); %>&userPage=<% out.println(curUserPage); %>
					&userListType=<% out.println(curUserListType); %>" method="post" style="float:right;">
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
</div>
</body>
<%	} %>
<script type= "text/javascript">

</script>
</html>