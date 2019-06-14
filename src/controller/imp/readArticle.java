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

public class readArticle implements Controller{
	final int STEP = 10;
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		String viewerName = request.getParameter("viewerName");
		HttpSession session = request.getSession();
		String s_targetArticleId = request.getParameter("targetArticleId");
		int targetArticleId = 0;
		String targetArticleTitle = request.getParameter("targetArticleTitle");
		if(null != s_targetArticleId && !"null".equals(s_targetArticleId)) {
			targetArticleId = Integer.parseInt(s_targetArticleId);
		}
		DBUtil dbu = new DBUtil();
		Map<String, Object> context = new HashMap<String, Object>();
		UserBean viewerBean = dbu.getUserInfoByName(viewerName);
//		UserBean viewerBean = (UserBean) session.getAttribute("curUser");
		ArticleBean articleBean = dbu.updateReadCount(targetArticleId, targetArticleTitle);
		if(articleBean == null) {
			context.put("isExist", false);
			request.setAttribute("context", context);
			System.out.println("博文好像不存在---后台");
			return;
		}
		System.out.println(viewerBean.getName()+"正在查看《"+targetArticleTitle+"》的信息");
		context.put("viewerBean", viewerBean);
		context.put("isExist", true);
		context.put("articleBean", articleBean);
		request.setAttribute("context", context);
	}

}
