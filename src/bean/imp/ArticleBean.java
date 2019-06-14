package bean.imp;
 
import java.io.Serializable;

import bean.Bean;
/*
 * 博文的实体类
 */
public class ArticleBean implements Bean,Serializable {
	private int id;
	private int user_id;
	private String title;
	private String detail;
	private int read_count;
	private String username;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ArticleBean(int id, int user_id, String title, String detail, int read_count, String username) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.title = title;
		this.detail = detail;
		this.read_count = read_count;
		this.username = username;
	}
	
	public int getRead_count() {
		return read_count;
	}
	public void setRead_count(int read_count) {
		this.read_count = read_count;
	}
	public ArticleBean(int user_id, String title, String detail) {
		super();
		this.user_id = user_id;
		this.title = title;
		this.detail = detail;
	}
	public ArticleBean(int id) {
		super();
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
}
