import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;

import com.mysql.jdbc.PreparedStatement;
 
public class DataGenerator {
 
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
    	//直接向MyCat中,使用批处理快速插入数据
        final String url = "jdbc:mysql://localhost:8066/BlogDB"; 
        final String name = "com.mysql.jdbc.Driver"; 
        final String user = "root"; 
        final String password = "mysql"; 
        Connection conn = null; 
        Class.forName(name);
        conn = DriverManager.getConnection(url, user, password);
        if (conn!=null) {
            System.out.println("获取连接成功");
            insert(conn);
        }else {
            System.out.println("连接数据失败");
        }
 
    }
    public static void insert(Connection conn) {
        Long begin = new Date().getTime();
        String prefix = "INSERT INTO user (id,name,click_count,fans_count) VALUES ";
        try {
            StringBuffer suffix = new StringBuffer();
            conn.setAutoCommit(false);
            PreparedStatement  pst = (PreparedStatement) conn.prepareStatement("");
            suffix = new StringBuffer();
            Random random = new Random();
            int max,min;
            for (int j = 0; j < 10000; j++) {
            	max = 1000;
            	min = 0;
           	    int click_count = random.nextInt(max)%(max-min+1) + min;
                suffix.append("("+(j+1)+",'user" + (j+1)+"',"+click_count+","+0+"),");
            }
            String sql = prefix + suffix.substring(0, suffix.length() - 1);
            pst.addBatch(sql);
            System.out.println(pst.isClosed());
            pst.executeBatch();
            conn.commit();
            conn.setAutoCommit(false);
            prefix = "INSERT INTO article (id,user_id,title,detail,read_count) VALUES ";
            pst = (PreparedStatement) conn.prepareStatement("");
            String titlebase = "博文";
            String detailbase = "这篇博文的编号是";
            int writer;
            int read_count;
            for(int i=0;i<300;i++) {
            	suffix = new StringBuffer();
                for (int j = 0; j < 10000; j++) {
                	max = 10000;
                	min = 1;
                	writer = random.nextInt(max)%(max-min+1) + min;
                	read_count = random.nextInt(max)%(max-min+1) + min;
                    suffix.append("("+((i*10000)+j+1)+"," +writer+",'"+titlebase+((i*10000)+j)+"','"+detailbase+((i*10000)+j)+".',"+read_count+"),");
                }
                sql = prefix + suffix.substring(0, suffix.length() - 1);
                pst.addBatch(sql);
                pst.executeBatch();
                conn.commit();
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Long end = new Date().getTime();
        System.out.println("插入总耗时: " + (end - begin) / 1000 + " s");
        System.out.println("插入完成");
    }
}