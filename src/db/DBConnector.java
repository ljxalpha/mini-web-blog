package db;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * 数据库连接及相关方法封装
 */
public class DBConnector {

	static String cur_url = "/home/kate/eclipse-workspace/MyMVCdemo";
	//用MySQL做测试
//	public static final String db_url = "jdbc:mysql://localhost:3306/db2";
	//用MyCat做分布式代理
	public static final String db_url = "jdbc:mysql://localhost:8066/BlogDB"; 
	//数据库用户名和密码
    public static final String username = "root";
    public static final String password = "mysql";
    
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    
    //插入
    public int insert(String sql) {
    	this.connect();
    	int res = this.updateSql(sql);
    	this.close();
    	return res;
    }
    //更新
    public int update(String sql) {
    	this.connect();
    	int res = this.updateSql(sql);
    	this.close();
    	return res;
    }
    //删除
    public int delete(String sql) {
    	this.connect();
    	int res = this.updateSql(sql);
    	this.close();
    	return res;
    }
    //查询
    public ResultSet select(String sql) {
    	this.connect();
    	ResultSet rs = null;
    	ResultSet rs0 = null;
    	try {
    		rs = this.stmt.executeQuery(sql);
    		this.stmt = conn.createStatement();
    		rs0 = this.stmt.executeQuery(sql);
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	try {
    		int count = 0;
    		while(rs.next()) {
    			count ++;
    		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			System.out.println("Select异常");
			e.printStackTrace();
		}
    	return rs0;
    }
    public void sql(String sql) {
    	this.connect();
    	try {
    		this.stmt.execute(sql);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	this.close();
    	   	
    }
    
    public int updateSql(String sql) {
    	int res = 0;
    	this.connect();
    	try {
    		res = this.stmt.executeUpdate(sql);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	this.close();
    	return res;
    }
    
    
    //连接数据库
    public void connect(){
    	try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection(db_url,username,password);
            this.stmt = conn.createStatement();
            
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    //释放连接资源
    public void close() {
    	try {
   		 	if(this.rs != null) {
   		 		this.rs.close();
   		 		this.rs = null;
   		 	}
   		 	if(this.stmt != null) {
   		 		this.stmt.close();
   		 		this.stmt = null;
   		 	}
   		 	if(this.conn != null) {
   		 		this.conn.close();
   		 		this.conn = null;
   		 	}
	   	}catch(Exception e) {
	   		e.printStackTrace();
	   	}
    }
    
}
