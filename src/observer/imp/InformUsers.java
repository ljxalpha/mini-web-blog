package observer.imp;
 
import java.sql.ResultSet;
import java.sql.SQLException;

import common.CommonVariables;
import db.DBUtil;
import bean.imp.ArticleBean;
import observable.Observable;
import observer.Observer;

//观察者-消息推送器

public class InformUsers extends Observer{

	public InformUsers(Observable subject) {
		super(subject);
		this.setName("消息推送器");
		// 设置自己关注的状态码
		this.setSelfState(CommonVariables.STATE_PUBLISH);
	}
	
	@Override
	public void work() {
		ArticleBean article = (ArticleBean)this.subject.tasks.get(0);
		this.subject.tasks.remove(0);
		int userId = article.getUser_id();
		String tittle = article.getTitle();
		String detail = article.getDetail();
		
		System.out.println("	这事归我干, id："+userId+"的用户发了一篇博文，我去把文章保存");
		
		// 把博文存到数据库
		this.publishArticle(userId, tittle, detail);
		
		// 获取关注该用户的所有用户id
		System.out.println("		我去通知所有粉丝来看");
		this.informAllFans(userId);
		
	}
	// 新发表博文保存到数据库
	public void publishArticle(int userId, String tittle, String detail) {
		DBUtil dbu = new DBUtil();
		dbu.publishArticle(userId, tittle, detail);
	}
	// 通知所有粉丝博主有新文章
	public void informAllFans(int userId) {
		DBUtil dbu = new DBUtil();
		ResultSet rs = dbu.getAllFans(userId);
		try {
			while(rs.next()) {
				String fansId = rs.getString("self_id");
				System.out.println("    -----通知用户"+fansId+", 你关注的用户"+userId+"发了一条新博文");
			}
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
