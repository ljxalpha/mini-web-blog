package observable.imp;
 
import common.CommonVariables;
import bean.Bean;
import bean.imp.ArticleBean;
import bean.imp.AttentionBean;
import bean.imp.UserBean;
import observable.Observable;

//微博Blog作为被观察者

public class Blogs extends Observable {
	
	// 注册一个新的微博用户
	public void registerUser(String name) {
		// 创建一个任务对象丢到任务队列里
		Bean user = new UserBean(name);
		this.tasks.add(user);
		
		// 修改状态码和关键字 用户注册
		this.setState(CommonVariables.STATE_REGISTER, CommonVariables.REGISTER); 	
		
	}
	
	// id为id1的用户 发起关注id为id2的用户
	public void followUser(int id1, int id2) {
		// 创建一个任务丢到任务队列
		Bean attention = new AttentionBean(id1, id2);
		this.tasks.add(attention);
		// 修改状态码和关键字 用户注册
		this.setState(CommonVariables.STATE_ATTENTION, CommonVariables.ATTENTION); 
	}

	// id1 对id2进行取消关注
	public void unFollowUser(int id1, int id2) {
		this.followUser(id1, id2);
	}
	
	// id为id的用户发了一篇新博文 标题title 内容detail
	public void publishArticle(int id, String title, String detail) {
		// 创建一个任务丢到任务队列
		Bean article = new ArticleBean(id, title, detail);
		this.tasks.add(article);
		// 修改状态码和关键字 用户注册
		this.setState(CommonVariables.STATE_PUBLISH, CommonVariables.PUBLISH); 
	
	}

	// id为id的博文被阅读了
	public void readArticle(int articleId) {
		// 创建一个任务丢到任务队列
		Bean article = new ArticleBean(articleId);
		this.tasks.add(article);
		// 修改状态码和关键字 用户注册
		this.setState(CommonVariables.STATE_READ, CommonVariables.READ); 
		
	}
}

