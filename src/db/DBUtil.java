package db;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.MCCManager;
import bean.imp.ArticleBean;
import bean.imp.UserBean;

public class DBUtil {
	MCCManager mcc = MCCManager.getInstance();
	//建表
	public void init_table() {
		DBConnector dbc = new DBConnector();
		//用户表
		String sql = "create table user ("
				+ " ID INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0 , "
				+ " NAME TEXT NOT NULL,"
				+ " click_count INTEGER default 0,"
				+ " fans_count INTEGER default 0) ";
		dbc.sql(sql);
		
		//关注信息表
		String sql1 = "create table attention("
				+ " ID INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0 ,"
				+ "self_id integer not null,"
				+ "object_id integer not null "
				+ ")";
		dbc.sql(sql1);
		
		//博文表
		String sql2 = "create table article("
				+ " ID INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0 ,"
				+ " user_id integer not null,"
				+ " title text not null,"
				+ " detail text not null,"
				+ " read_count integer default 0 "
				+ ")";
		dbc.sql(sql2);
		
	}
/*
 * 获取博文数量
 */
	public int getArticleCount(int userid, boolean showArticleOfThisUser) {
		int count = 0;
		DBConnector dbc = new DBConnector();
		String sql = "";
		if(showArticleOfThisUser) {
			sql = " select count(*) from article "
					+ "where user_id = " + userid 
					+ ";";
		}
		else {
			sql = " select count(*) from article "
					+ "where user_id != " + userid 
					+ ";";
		}
		try {
			ResultSet rs = dbc.select(sql);
			if(rs.next()) {
				count = rs.getInt("count(*)");
			}
			dbc.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return count;
	}
/*
 * 标题获取博文
 */
	public int getArticleCountByTitle(String searchArticleTitle) {
		int count = 0;
		if(mcc.hasArticle(searchArticleTitle)) {
			count = ((List<ArticleBean>) mcc.fetchSameTitleArticleList(searchArticleTitle)).size();
		}
		else {
			DBConnector dbc = new DBConnector();
			String sql = " select count(*) from article "
						+ "where title = '" + searchArticleTitle 
						+ "';";
			try {
				ResultSet rs = dbc.select(sql);
				if(rs.next()) {
					count = rs.getInt("count(*)");
				}
				dbc.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return count;
	}
/*
 * 分页获取博文
 */
	public List<ArticleBean> getArticleByPageSplit(int start, int step, int userid, boolean showArticleOfThisUser){
		List<ArticleBean> articleList = new ArrayList<>();
		try {
			DBConnector dbc = new DBConnector();
			String sql = "";
			if(showArticleOfThisUser) {
				sql = " select article.*,user.name"
						+ " from article left join user "
						+ " on article.user_id = user.id where"
						+ " article.user_id = " + userid
						+ " order by article.id desc "
						+ " limit "+ start +", " +step
						+ " ;";
			}
			else {
				sql = " select article.*,user.name"
						+ " from article left join user "
						+ " on article.user_id = user.id where"
						+ " article.user_id != " + userid
						+ " order by article.id desc "
						+ " limit "+ start +", " +step
						+ " ;";
			}
			ResultSet rs = dbc.select(sql);
			while(rs.next()) {
				int id = rs.getInt("id");
				int user_id = rs.getInt("user_id");
				String title = rs.getString("title");
				String detail = rs.getString("detail");
				int read_count = rs.getInt("read_count");
				String username = rs.getString("name");
				articleList.add(new ArticleBean(id, user_id, title, detail, read_count, username));
			}
			dbc.close();
		}catch(Exception e) {
			e.printStackTrace();
		}		
		return articleList;
	}

	/*
	 * 
	 */
	public List<ArticleBean> getArticleByTitle(int start, int step, String searchArticleTitle) {
		List<ArticleBean> articleList = new ArrayList<>();
		System.out.println("开始按题目搜索博文"+mcc.hasArticle(searchArticleTitle));
//		mcc.showStats();
//		mcc.showSameTitleArticleList(searchArticleTitle);
//		mcc.getAllKeys();
//		mcc.hahaha(searchArticleTitle);
		if(mcc.hasArticle(searchArticleTitle)) {
			articleList = mcc.fetchSameTitleArticleList(searchArticleTitle);
//			System.out.println("articleList.size()"+articleList.size());
			if(articleList.size()-start<step) step = articleList.size()-start;
			articleList = (List<ArticleBean>)articleList.subList(start, start + step);
			System.out.println("完成了在缓存中的搜索");
			return articleList;
		}
		try {
			DBConnector dbc = new DBConnector();
			String sql = " select article.*,user.name"
						+ " from article left join user "
						+ " on article.user_id = user.id where"
						+ " article.title = '" + searchArticleTitle
						+ "' order by article.id desc "
						+ " limit "+ start +", " +step
						+ " ;";
			ResultSet rs = dbc.select(sql);
			while(rs.next()) {
				int id = rs.getInt("id");
				int user_id = rs.getInt("user_id");
				String title = rs.getString("title");
				String detail = rs.getString("detail");
				int read_count = rs.getInt("read_count");
				String username = rs.getString("name");
				articleList.add(new ArticleBean(id, user_id, title, detail, read_count, username));
				mcc.storeArticle(new ArticleBean(id, user_id, title, detail, read_count, username));
			}
			dbc.close();
			System.out.println("完成了在数据库中的搜索");
		}catch(Exception e) {
			e.printStackTrace();
		}		
		return articleList;
	}

	//注册用户
	public void registerUser(String name) {
		DBConnector dbc = new DBConnector();
		String sql = "insert into user (name) values('"+ name +"')";
		dbc.insert(sql);
		dbc.close();
	}
	// 按照用户名获取用户信息
	public UserBean getUserInfoByName(String name) {
		UserBean userbean = null;
		try {
			DBConnector dbc = new DBConnector();
			String sql = " select * from user"
					+ " where name='"+name + "' " 
					+ " ;";
			ResultSet rs = dbc.select(sql);
			if(rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("name");
				int click_count = rs.getInt("click_count");
				int fans_count = rs.getInt("fans_count");
				userbean = new UserBean(id, username, click_count, fans_count);
			}
			dbc.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return userbean;
	}
	
	// 检查id1是否关注过id2
	public boolean checkAttention(int id1, int id2) {
		DBConnector dbc = new DBConnector();
		boolean res = false;
		String sql = "select * from attention"
				+ " where self_id="+id1
				+ " and object_id="+id2 
				+ " ;";
		ResultSet rs = dbc.select(sql);
		try {
			if(rs.next()) {
				res = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbc.close();
		return res;
	}
	
	// id1 用户对id2 取消关注
	public void unFollow(int id1, int id2) {
		try {
			DBConnector dbc1 = new DBConnector();
			String sql1 = "delete from attention "
					+ " where self_id=" + id1
					+ " and object_id=" + id2
					+ " ;";
			int deletecount = dbc1.delete(sql1);
			dbc1.close();
			if(deletecount != 0) {
				dbc1 = new DBConnector();
				String sql2 = " select fans_count from user where id="+ id2 +"; ";
				ResultSet rs = dbc1.select(sql2);
				int count = 0;
				if(rs.next()) {
					count = rs.getInt("fans_count") - 1;
				}
				dbc1.close();
				dbc1 = new DBConnector();
				String sql3 = " update user set fans_count="+count+"  where id = "+id2+"; ";
				dbc1.update(sql3);
				dbc1.close();
			}
			else {
				System.out.println("本来就没关注，不用取关");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	// id1用户 关注id2用户
	public void follow(int id1, int id2) {
		if (id1 == id2) return;
		try {
			DBConnector dbc1 = new DBConnector();
			String sql1 = "insert into attention "
					+ "(self_id, object_id) "
					+ "values("+id1+", "+id2+")";
			int insertcount = dbc1.insert(sql1);
			dbc1.close();
			if(insertcount != 0) {
				dbc1 = new DBConnector();
				String sql2 = " select fans_count from user where id="+ id2 +"; ";
				ResultSet rs = dbc1.select(sql2);
				int count = 0;
				if(rs.next()) {
					count = rs.getInt("fans_count") + 1;
				}
				dbc1.close();
				dbc1 = new DBConnector();
				String sql3 = " update user set fans_count="+count+"  where id = "+id2+"; ";
				dbc1.update(sql3);
				dbc1.close();
			}
			else {
				System.out.println("关注失败");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 用户userId 新发了一条博文
	public void publishArticle(int userId, String title, String detail) {
		DBConnector dbc = new DBConnector();
		String sql = "insert into article"
				+ " (user_id, title, detail)"
				+ " values"
				+ " ("+userId+",'"+title+"','"+detail+"')"
				+ " ;";
		dbc.insert(sql);
		dbc.close();
		if(mcc.hasArticle(title)) {
			String sql1 = " select article.*,user.name"
					+ " from article left join user "
					+ " on article.user_id = user.id where"
					+ " article.title = '" + title
					+ "' order by article.id desc "
					+ " limit "+ 0 +", " +1
					+ " ;";
			dbc.connect();
			ResultSet rs = dbc.select(sql1);
			dbc.close();
			try {
				while(rs.next()) {
					mcc.storeArticle(new ArticleBean(rs.getInt("id"), rs.getInt("user_id"), rs.getString("title"), rs.getString("detail"), rs.getInt("read_count"), rs.getString("name")));
					mcc.updateStoredId(rs.getInt("id"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//根据id删除博文
	public void deleteArticleById(int articleId, String articleTitle) {
		DBConnector dbc = new DBConnector();
		String sql = "delete from article "
				+ "where id="+articleId
				+ ";";
		dbc.delete(sql);
		dbc.close();
		mcc.removeOne(articleId, articleTitle);
	}
	// 通过用户id 获取该用户的所有粉丝 返回beanList
		public List<UserBean> getAllUsersByIdSplitPage(int id, int start, int step ,String userListType){
			List<UserBean> userList = new ArrayList<>();
			try {
				DBConnector dbc = new DBConnector();
				String sql = null;
				if("fan".equals(userListType) || userListType == null || "null".equals(userListType)) {
					sql =  " select user.* "
							+ " from user left join attention "
							+ " on self_id=user.id "
							+ "where object_id=" + id 
							+ " limit " + start + ", " + step
							+ " ;";
				}
				else if("fanof".equals(userListType)) {
					sql = " select user.* "
							+ " from user left join attention "
							+ " on object_id=user.id "
							+ "where self_id=" + id
							+ " limit " + start + ", " + step
							+ " ;";
				}
				ResultSet rs = dbc.select(sql);
				while(rs.next()) {
					int user_id = rs.getInt("id");
					String name = rs.getString("name");
					int click_count = rs.getInt("click_count");
					int fans_count = rs.getInt("fans_count");
					userList.add(new UserBean(user_id, name, click_count, fans_count));
				}
				dbc.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			return userList;
		}
		
		// 获取id用户的所有粉丝
		public ResultSet getAllFans(int userId) {
			ResultSet rs = null;
			try {
				DBConnector dbc = new DBConnector();
				String sql = "select self_id "
						+ " from attention "
						+ " where object_id="+userId
						+ " ;";
				rs = dbc.select(sql);
				dbc.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			return rs;
		}
		
		//获取用户的关注数量
		public int getAllFanOfUsers(int selfid) {
			int fan_of_count = 0;
			String sql = "select count(*) "
					+ "from attention "
					+ "where self_id = " + selfid
					+ ";";
			System.out.println(sql);
			ResultSet rs = null;
			DBConnector dbc = new DBConnector();
			try {
				rs = dbc.select(sql);
				rs.next();
				fan_of_count = rs.getInt("count(*)");
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			return fan_of_count;
		}
		
		// articleId的文章被阅读了 增加一下阅读数量
		public ArticleBean updateReadCount(int articleId, String title) {
			ArticleBean res = null;
			String sql = null;
			DBConnector dbc = new DBConnector();
			int read_count = 0;
			if(mcc.hasArticle(title)) {
				List<ArticleBean> articleList = mcc.fetchSameTitleArticleList(title);
				for (int i=0;i<articleList.size();i++) {
					if(articleList.get(i).getId() == articleId) {
						ArticleBean article = articleList.get(i);
						read_count = article.getRead_count()+1;
						article = new ArticleBean(article.getId(), article.getUser_id(), title, article.getDetail(),read_count, article.getUsername());
						res = article;
						articleList.set(i, article);
						mcc.updateOneArticlList(title, articleList);
					}
				}
			}
			else {
				try {	
					// 先查询一下这篇文章之前的阅读数是多少
					dbc = new DBConnector();
					dbc.connect();
					sql = " select article.*,user.name"
							+ " from article left join user "
							+ " on article.user_id = user.id where"
							+ " article.id = " + articleId
							+ " ;";
					ResultSet rs = dbc.select(sql);
					rs.next();
					int id = rs.getInt("id");
					int user_id = rs.getInt("user_id");
					String detail = rs.getString("detail");
					read_count = rs.getInt("read_count")+1;
					String username = rs.getString("name");
					res = new ArticleBean(id, user_id, title, detail, read_count, username);
					dbc.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			ThreadReadCount trc = new ThreadReadCount(articleId, read_count);
			trc.start();
		return res;
		}
		
	}
