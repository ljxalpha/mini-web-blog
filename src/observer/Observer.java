package observer;
 
import observable.Observable;

//观察者类 父类
public abstract class Observer {
	protected Observable subject;
	private int selfState;	// 存储与我有关系的状态码
	private String name;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Observer(Observable subject) {
		this.subject = subject;	//接收到自己关心的subject
		// 把自己绑定给subject
		this.subject.attach(this);
	}
	
	public int getSelfState() {
		return selfState;
	}

	public void setSelfState(int selfState) {
		this.selfState = selfState;
	}

	public void update() {
		System.out.println();
		int state = this.subject.getState();
		String keyWord = this.subject.getKeyWord();
		System.out.println("!!"+this.name+"!!--------观察到了微博状态改变 ");
		System.out.println("	状态码："+state+" 状态关键字："+keyWord+"");
		if(state != this.selfState) {
			System.out.println("	这件事跟我没啥关系，我忽略了！");
			System.out.println();
		}else {
			System.out.println("    这件事应该我来干！");
			this.work();
		}
	}
	
	// 执行应该做的业务逻辑
	public abstract void work();
}

