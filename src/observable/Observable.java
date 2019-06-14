package observable;
 
import java.util.ArrayList;
import java.util.List;
import bean.Bean;
import observer.Observer;
	
//被观察者
public class Observable {
		
	// 观察者列表
	private List<Observer> observers = new ArrayList<Observer>();
	// 任务列表
	public List<Bean> tasks = new ArrayList<Bean>();
	
	// 自身状态
	private int state;
	// 存储新状态要做的事是什么
	private String keyWord;
	
	// 获取状态
	public int getState() {
		return state;
	}
	public String getKeyWord() {
		return this.keyWord;
	}
	// 修改自身状态时 通知所有观察者
	public void setState(int state, String keyWord) {
		this.state = state;
		this.keyWord = keyWord;
		notifyAllObservers();
	}
	
	// 添加观察者
	public void attach(Observer observer) {
		observers.add(observer);
	}
	
	// 实现通知所有观察者的方法 对观察者列表里所有的观察者更新
	public void notifyAllObservers() {
		for(Observer observer : observers) {
			observer.update();
		}
	}
}
