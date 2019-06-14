package main;
 
import db.DBUtil;
import db.MCCManager;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import db.DBConnector;
import observable.imp.Blogs;
import observer.imp.CountAttention;
import observer.imp.CountRead;
import observer.imp.InformUsers;
import observer.imp.RegisterUser;
/*
 * 用来插入数据和做一些简单测试的主类,项目运行的时候并没有什么用处.............
 */
public class Main {
	public static void main(String[] arga) {
		new Main().run();
	}
	public void run() {

//		// 1. 初始化一下数据库表
//		init_database();
		
		
		// 被观察者 博客对象
		Blogs blogs = new Blogs();	
		// 初始化几个观察者并绑定
		new CountRead(blogs);
		new CountAttention(blogs);
		new InformUsers(blogs);
		new RegisterUser(blogs);
//		DBConnector dbc = new DBConnector();
//		dbc.connect();
//		dbc.update("insert into attention (self_id, object_id)values(39,40);");
//		dbc.close();
		
		// 2. 注册10个用户
//		for(int i = 0; i <= 20; i++) {
//			blogs.registerUser("user" + i);
//		}
		
		
		
		// 3. id为5到20的用户 对 id为1的用户进行关注
//		for(int i=1; i<=20; i++) {
//			blogs.followUser(2, i);
//			blogs.followUser(i, 2);
//			blogs.followUser(i, 3);
//		}
		
		
		// 4. id为1的用户 发了一篇新的博文 
//		for(int i=0;i<40;i++) {
//			int userId = 2;
//			String title = "article" + i;
//			String detail = "真的是篇新博文" + i;
//			blogs.publishArticle(userId, title, detail);
//			blogs.publishArticle(1, title, detail);
//			blogs.publishArticle(3, title, detail);
//			blogs.publishArticle(5, title, detail);
//		}
		
		// 5. 有10个人阅读了id为1的博文 
//		for(int i = 0; i < 10; i++) {
//			int articleId = 1;
//			blogs.readArticle(articleId);
//		}

		// 6. id为2 3 的用户取消了对id为1用户的关注
//		blogs.unFollowUser(2, 1);
//		blogs.unFollowUser(3, 1);
		
//		// 7 id为1的用户又发了新博文
//		int userId = 1;
//		String title = "再发一篇";
//		String detail = "多发几篇也没关系";
//		blogs.publishArticle(userId, title, detail);
//		ArrayList <Integer> aa = new ArrayList<Integer>();
//		aa.add(1);
//		aa.add(2);
//		aa.add(3);
		MCCManager mcc = new MCCManager();
		mcc.flush();
	
	}
	
	public void init_database() {
		DBUtil dbu = new DBUtil();
		dbu.init_table();
	}
}
