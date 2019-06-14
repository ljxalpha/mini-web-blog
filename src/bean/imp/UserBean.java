package bean.imp;
 
import bean.Bean;

public class UserBean implements Bean{
	private int id;
	private String name;
	private int click_count;
	private int fans_count;
	public UserBean(String name) {
		super();
		this.name = name;
	}
	public UserBean(int id, String name, int click_count, int fans_count) {
		super();
		this.id = id;
		this.name = name;
		this.click_count = click_count;
		this.fans_count = fans_count;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getClick_count() {
		return click_count;
	}
	public void setClick_count(int click_count) {
		this.click_count = click_count;
	}
	public int getFans_count() {
		return fans_count;
	}
	public void setFans_count(int fans_count) {
		this.fans_count = fans_count;
	}
	
	
}
