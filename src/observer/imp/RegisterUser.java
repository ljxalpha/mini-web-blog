package observer.imp;
 
import common.CommonVariables;
import db.DBUtil;
import bean.imp.UserBean;
import observable.Observable;
import observer.Observer;

//观察者-注册器

public class RegisterUser extends Observer{

	public RegisterUser(Observable subject) {
		super(subject);
		this.setName("用户注册器");
		// 设置自己关注的状态码
		this.setSelfState(CommonVariables.STATE_REGISTER);
	}
	
	@Override
	public void work() {
		
		UserBean user = (UserBean)this.subject.tasks.get(0);
		this.subject.tasks.remove(0);
		String name = user.getName();
		
		System.out.println("	这事归我干！注册新用户，用户名为：" + name);
		this.registerUser(name);	
	}
	
	public void registerUser(String name) {
		DBUtil dbu = new DBUtil();
		dbu.registerUser(name);
	}
}
