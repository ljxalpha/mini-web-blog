package observer.imp;

import common.CommonVariables;
import db.DBUtil;
import bean.imp.ArticleBean;
import observable.Observable;
import observer.Observer;

//观察者-阅读计数器

public class CountRead extends Observer{

	public CountRead(Observable subject) {
		super(subject);
		this.setName("阅读计数器");
		// 设置自己关注的状态码
		this.setSelfState(CommonVariables.STATE_READ);
	}
	
	@Override
	public void work() {
		ArticleBean article = (ArticleBean)this.subject.tasks.get(0);
		this.subject.tasks.remove(0);
		int articleId = article.getId();
		
		System.out.println("	阅读了ID为"+ articleId+ "的博文");
		
		this.updateReadCount(articleId);
		
	}
	
	public void updateReadCount(int articleId) {
		DBUtil dbu = new DBUtil();
		dbu.updateReadCount(articleId,"");
	}
	
	
	

}
