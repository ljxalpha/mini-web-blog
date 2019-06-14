package bean.imp;
 
import bean.Bean;
/*
 * 关注关系
 */
public class AttentionBean implements Bean {
	private int id;
	private int id1;
	private int id2;
	
	public AttentionBean(int id1, int id2) {
		super();
		this.id1 = id1;
		this.id2 = id2;
	}
	public int getId() {
		return id;
	}
	public void setId(int id1) {
		this.id1 = id;
	}
	public int getId1() {
		return id1;
	}
	public void setId1(int id1) {
		this.id1 = id1;
	}
	public int getId2() {
		return id2;
	}
	public void setId2(int id2) {
		this.id2 = id2;
	}
	
	
}
