package db;
//这个小弟专门用来更新数据库中的博文阅读量
public class ThreadReadCount extends Thread {
 
    private int articleId; 
    private int read_count;

    public ThreadReadCount(int articleId ,int read_count){
        this. articleId = articleId;
        this. read_count = read_count;
    }
    public void run (){
    	DBConnector dbc = new DBConnector();
    	dbc.connect();
    	String sql = "update article"
				+ " set read_count="+this.read_count
				+ " where id=" + this.articleId
				+ " ;";
    	dbc.update(sql);
    	dbc.close();
        System.out.println("更新博文的阅读量成功");
        this.interrupt();
    }
}