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

public class UserView implements Controller{
	final int STEP = 10;
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		String viewerName = request.getParameter("viewerName");
		String username = request.getParameter("targetUserName");
		HttpSession session = request.getSession();
		DBUtil dbu = new DBUtil();
		Map<String, Object> context = new HashMap<String, Object>();
		UserBean viewerBean = dbu.getUserInfoByName(viewerName);
//		UserBean viewerBean = (UserBean) session.getAttribute("curUser");
		UserBean userBean = dbu.getUserInfoByName(username);
		//用户为空
		if(userBean == null) {
			context.put("isExist", false);
			request.setAttribute("context", context);
			return;
		}
		System.out.println(viewerBean.getName()+"正在查看"+userBean.getName()+"的信息");
		context.put("viewerBean", viewerBean);
		context.put("isExist", true);
		context.put("viewedUser", userBean);
		String userListType = request.getParameter("userListType");
		int viewerId = viewerBean.getId();
		//检测是否已经关注
		boolean followed = dbu.checkAttention(viewerBean.getId(), userBean.getId());
		//检测是否存在关注或者取关行为
		if(request.getParameter("attentionTargetId") != null) {
			int attentionTargetId = Integer.parseInt(request.getParameter("attentionTargetId"));
			if(followed) {
				dbu.unFollow(viewerId, attentionTargetId);
				System.out.println(viewerId+"号用户取关了"+attentionTargetId+"号用户");
				followed = false;
			}
			else {
				dbu.follow(viewerId, attentionTargetId);
				System.out.println(viewerId+"号用户关注了"+attentionTargetId+"号用户");
				followed = true;
			}
		}
		context.put("followed", followed);
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
		List<UserBean> userList = dbu.getAllUsersByIdSplitPage(userBean.getId(),start, step,userListType);
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
		
		// 博文分页
		String articlePage = request.getParameter("articlePage");
		int article_start = 0;
		int article_step = STEP;
		int curArticlePage = 1;	
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
		List<ArticleBean> articleList = dbu.getArticleByPageSplit(article_start, article_step,userBean.getId(),true);
		context.put("articleList", articleList);
		int articleCount = dbu.getArticleCount(userBean.getId(),true);
		int artile_page_num = articleCount /10;
		if(articleCount %10 != 0) {
			artile_page_num++;
		}
		List<Integer> articlePageRange = new ArrayList<>();
		for(int i=1; i<=artile_page_num;i++) {
			articlePageRange.add(i);
		}
		context.put("articlePageRange", articlePageRange);
		context.put("curArticlePage", curArticlePage);
		
		
		request.setAttribute("context", context);
	}

}
