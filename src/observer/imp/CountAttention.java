package observer.imp;
 
import bean.imp.AttentionBean;
import common.CommonVariables;
import db.DBUtil;
import observable.Observable;
import observer.Observer;

//观察者-关注计数器

public class CountAttention extends Observer{

	public CountAttention(Observable subject) {
		super(subject);
		this.setName("关注计数器");
		// 设置自己关注的状态码
		this.setSelfState(CommonVariables.STATE_ATTENTION);
	}
	
	@Override
	public void work() {
		
		AttentionBean attention = (AttentionBean)this.subject.tasks.get(0);
		this.subject.tasks.remove(0);
		int id1 = attention.getId1();
		int id2 = attention.getId2();
		
		System.out.println("	这事归我干, id："+id1+"的用户发起关注了id:"+id2+"的用户");
		
		
		// 如果已经关注过则取消关注
		if(this.checkAttention(id1, id2)) {
			this.unFollow(id1, id2);
		}else {
			this.follow(id1, id2);
		}
	}
	// 检查是否关注过
	public boolean checkAttention(int id1, int id2) {
		DBUtil dbu = new DBUtil();
		return dbu.checkAttention(id1, id2);
	}
	// 关注
	public void follow(int id1, int id2) {
		DBUtil dbu = new DBUtil();
		dbu.follow(id1, id2);
		System.out.println(  "  id:"+id1+"关注了id:"+id2+"的用户");	
	}
	// 取消关注
	public void unFollow(int id1, int id2) {
		DBUtil dbu = new DBUtil();
		dbu.unFollow(id1, id2);
		System.out.println(  "  id:"+id1+"取消了关注id:"+id2+"的用户");
	}
}
