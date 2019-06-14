package controller.imp;

 
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.imp.ArticleBean;
import bean.imp.UserBean;
import controller.Controller;
import db.DBUtil;

/**
 * 返回登录界面
 */
public class Login implements Controller{
	final int STEP = 10;
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String userListType = request.getParameter("userListType");
		System.out.println("用户请求登录:");
		System.out.println("   用户名："+username);
		System.out.println("   密码："+password);
		/*
		 * 此处应该调用数据库方法 比对改用户名与密码是否匹配。
		 * 数据库没有存密码字段 假设 存在用户名 就能登录成功(简单模拟登陆,略去密码正确性检测步骤)
		 */
		DBUtil dbu = new DBUtil();
		Map<String, Object> context = new HashMap<String, Object>();
		UserBean userBean = dbu.getUserInfoByName(username);
		//登陆失败
		if(userBean == null) {
			context.put("isLogin", false);
			request.setAttribute("context", context);
			return;
		}
		// 登录成功
		HttpSession session = request.getSession();
		session.setAttribute("curUser", userBean);
		context.put("isLogin", true);
		context.put("curUser", userBean);
		int id = userBean.getId();
		//检测是否存在取关行为
		if(request.getParameter("unFollowTargetId") != null) {
			int unFollowTargetId = Integer.parseInt(request.getParameter("unFollowTargetId"));
			dbu.unFollow(id, unFollowTargetId);
		}
		//检测是否存在删除文章的行为
		if(request.getParameter("deleteArticleId") != null) {
			int deleteArticleid = Integer.parseInt(request.getParameter("deleteArticleId"));
			dbu.deleteArticleById(deleteArticleid,request.getParameter("deleteArticleTitle"));
			System.out.println("ID为"+deleteArticleid+"的微博删除成功！");
		}
		//检测是否存在发布新文章的行为
		if(request.getParameter("newArticleTitle") != null && request.getParameter("newArticleDetail") != null) {
			String newArticleTitle = request.getParameter("newArticleTitle");
			String newArticleDetail = request.getParameter("newArticleDetail");
			newArticleTitle = newArticleTitle.replaceAll(" ", "");
			newArticleDetail = newArticleDetail.replaceAll(" ", "");
			if("".equals(newArticleTitle) || "".equals(newArticleDetail)) 
				System.out.println("都不知道说啥你发啥玩意儿啊？");
			else {
				dbu.publishArticle(userBean.getId(), newArticleTitle, newArticleDetail);
				System.out.println("标题为《"+newArticleTitle+"》的新微博发布成功！");
			}
//			int deleteArticleid = Integer.parseInt(request.getParameter("deleteArticleId"));
//			dbu.deleteArticleById(deleteArticleid);
		}
		// 获取该用户的所有粉丝或者关注
		int start = 0;
		int step = STEP;
		int curUserPage = 1;
		String userPage = request.getParameter("userPage");
		if(userPage == null) {
			start = 0;
		}else {
			curUserPage = Integer.parseInt(userPage);
			String s_userPageJumpto = request.getParameter("userPageJumpto");
			if(null != s_userPageJumpto && !"".equals(s_userPageJumpto)) {
				try {
					int userPageJumpto = Integer.parseInt(s_userPageJumpto);
					curUserPage = userPageJumpto;
				}
				catch(Exception e) {
					System.out.println("userPageJumpto is NAN.");
				}
			}
			start = (curUserPage-1) * 10;
		}
		List<UserBean> userList = dbu.getAllUsersByIdSplitPage(id,start, step,userListType);
		context.put("userListType", userListType);
		context.put("userList", userList);
		// 根据显示粉丝还是关注来构造页码标
		int user_count = 0;
		if("fan".equals(userListType) || null == userListType || "null".equals(userListType)) user_count = userBean.getFans_count();
		else user_count = dbu.getAllFanOfUsers(userBean.getId());
		int page_count = user_count / 10;
		if(user_count % 10 != 0) {
			page_count ++;
		}
		List<Integer> pageRange = new ArrayList<>();
		for(int i=1;i<=page_count;i++) {
			pageRange.add(i);
		}
		context.put("pageRange", pageRange);
		context.put("curUserPage", curUserPage);
		
		// 拿到全系统所有博文(非自己的) 按照分页
		String articlePage = request.getParameter("articlePage");
		int article_start = 0;
		int article_step = STEP;
		int curArticlePage = 1;// 默认当前在第一页
		if(articlePage != null) {
			curArticlePage = Integer.parseInt(articlePage);
			String s_articlePageJumpto = request.getParameter("articlePageJumpto");
			System.out.println(s_articlePageJumpto);
			if(null != s_articlePageJumpto && !"".equals(s_articlePageJumpto)) {
				try {
					int articlePageJumpto = Integer.parseInt(s_articlePageJumpto);
					curArticlePage = articlePageJumpto;
				}
				catch(Exception e) {
					e.printStackTrace();
					System.out.println("articlePageJumpto is NAN.");
				}
			}
			article_start = (curArticlePage-1) * 10;
		}
		String searchArticleTitle = request.getParameter("searchArticleTitle");
		List<ArticleBean> articleList =  null;
		int articleCount = 0;
		if(null != searchArticleTitle && !"null".equals(searchArticleTitle) && !"".equals(searchArticleTitle.replaceAll(" ", ""))) {
			searchArticleTitle = searchArticleTitle.replaceAll(" ", "");
			System.out.println(searchArticleTitle);
			article_start = 0;
			articleList = dbu.getArticleByTitle(article_start, article_step,searchArticleTitle);
			// 构造页码生成
			//1 一共有多少博文
			articleCount = dbu.getArticleCountByTitle(searchArticleTitle);
		}
		else {
			articleList = dbu.getArticleByPageSplit(article_start, article_step,id,false);
			articleCount = dbu.getArticleCount(id,false);
		}
		context.put("articleList", articleList);
		// 2 一共有多少页
		int artile_page_num = articleCount /10;
		if(articleCount %10 != 0) {
			artile_page_num++;
		}
		// 3 构造一个页码器对象
		List<Integer> articlePageRange = new ArrayList<>();
		for(int i=1; i<=artile_page_num;i++) {
			articlePageRange.add(i);
		}
		context.put("articlePageRange", articlePageRange);
		context.put("curArticlePage", curArticlePage);
		
		// 拿到自己发布过的所有博文 按照分页
		String myArticlePage = request.getParameter("myArticlePage");
		int my_article_start = 0;
		int my_article_step = STEP/2;
		int curMyArticlePage = 1;
		if(myArticlePage != null) {
			curMyArticlePage = Integer.parseInt(myArticlePage);
			String s_myArticlePageJumpto = request.getParameter("myArticlePageJumpto");
			if(null != s_myArticlePageJumpto && !"".equals(s_myArticlePageJumpto)) {
				int myArticlePageJumpto = Integer.parseInt(s_myArticlePageJumpto);
				curMyArticlePage = myArticlePageJumpto;
			}
			my_article_start = (curMyArticlePage - 1) * 5;
		}
		//获取到了登录用户的当前页码下所有的博文
		List <ArticleBean> myArticleList = dbu.getArticleByPageSplit(my_article_start, my_article_step, id, true);
		context.put("myArticleList", myArticleList);
		//实现博文分页
		List <Integer> myArticlePageRange = new ArrayList<>();
		int myArticleCount = dbu.getArticleCount(id, true);
		int my_article_page_num = myArticleCount / 5;
		if(myArticleCount % 5 != 0) my_article_page_num++;
		for (int i=0;i<my_article_page_num;i++) {
			myArticlePageRange.add(i+1);
		}
		context.put("myArticlePageRange", myArticlePageRange);
		context.put("curMyArticlePage", curMyArticlePage);
		
		request.setAttribute("context", context);
	}

}
