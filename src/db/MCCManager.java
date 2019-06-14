package db;
 
import com.danga.MemCached.MemCachedClient;  
import com.danga.MemCached.SockIOPool;
import bean.imp.ArticleBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.sql.ResultSet;

public class MCCManager {
	private static MemCachedClient client=new MemCachedClient();  
	private static boolean inited = false;
    private String [] addr ={"localhost:11211"};  
    private Integer [] weights = {3};  
    private static SockIOPool pool = SockIOPool.getInstance();  
    private static final int size = 20;
    private static MCCManager instance;
    public static MCCManager getInstance(){
        if(instance==null){
            instance=new MCCManager();
        }
        return instance;
    }
    //初始化MCC
    public MCCManager() {
        pool.setServers(addr);  
        pool.setWeights(weights);  
        pool.setInitConn(1);  
        pool.setMinConn(5);  
        pool.setMaxConn(200);  
        pool.setMaxIdle(1000*60*30);  
        pool.setMaintSleep(30);  
        pool.setNagle(false);  
        pool.setSocketTO(30);  
        pool.setSocketConnectTO(0);
        pool.setAliveCheck(true);
        pool.initialize(); 
        initMMC();
    }
    public void initMMC() {
    	Date date=new Date(System.currentTimeMillis()+1000*10*60*24*7); 
    	if (inited == true) return;
    	System.out.println("开始初始化MCC开始初始化MCC开始初始化MCC开始初始化MCC开始初始化MCC开始初始化MCC开始初始化MCC开始初始化MCC");
    	DBConnector dbc = new DBConnector();
    	ResultSet rs_user = null;
    	ResultSet rs_article = null;
    	ResultSet rs_article3 = null;
    	int count = 0;
    	List<String> ll = new ArrayList<String>();
    	String initsql1 = "select id from user order by click_count desc limit 0,1";
    	dbc.connect();
    	int ii=0;
    	try {
    		ArrayList<Integer> storedArticleId = new ArrayList<Integer>();
    		rs_user = dbc.select(initsql1);
    		while(rs_user.next()) {
    			String initsql2 ="select article.*,user.name from article left join user  on article.user_id = user.id where article.user_id = "+rs_user.getString("id")+
    						" order by article.read_count desc  limit 0, 100 ;";
    			rs_article = dbc.select(initsql2);
    			while(rs_article.next()) {
    				ii++;
    				if(storedArticleId.contains(Integer.parseInt(rs_article.getString("id")))) continue;
    				storedArticleId.add(Integer.parseInt(rs_article.getString("id")));
    				String title = rs_article.getString("title");
        			List<ArticleBean> sameTitleArticleList = null;
        			if(hasArticle(title)) {
        				sameTitleArticleList = fetchSameTitleArticleList(title);
        			}
        			else {
        				sameTitleArticleList = new ArrayList<ArticleBean> ();
        			}
        			sameTitleArticleList.add(new ArticleBean(rs_article.getInt("id"), rs_article.getInt("user_id"),
    						title, rs_article.getString("detail"), rs_article.getInt("read_count"), rs_article.getString("name")));
//        			System.out.println("正在往缓存中装入《"+title+"》"+rs_article.getInt("id"));
        			count++;
        			ll.add(title);
        			String initsql3 = "select article.*,user.name from article left join user  on article.user_id = user.id where article.title = '" + title + "';";
        			try {
        				rs_article3 = dbc.select(initsql3);
        				while(rs_article3.next()) {
        					if(storedArticleId.contains(Integer.parseInt(rs_article3.getString("id")))) continue;
        					storedArticleId.add(Integer.parseInt(rs_article3.getString("id")));
        					sameTitleArticleList.add(new ArticleBean(rs_article3.getInt("id"), rs_article3.getInt("user_id"),
            						title, rs_article3.getString("detail"), rs_article3.getInt("read_count"), rs_article3.getString("name")));
//        					System.out.println("正在往缓存中装入《"+title+"》-id="+rs_article3.getInt("id"));
        					count++;
        					ll.add(title);
        				}
        			}
        			catch(Exception e){
        				e.printStackTrace();
        			}
        			client.set(title, sameTitleArticleList,date);
    			}
    		}
			client.set("storedArticleId", storedArticleId,date);
    		System.out.println("IniMMC successfully!");
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	dbc.close();
    	inited = true;
    	System.out.println("一共装入缓存的博文数目_"+count);
    }
      public void storeArticle(ArticleBean article) {
    	  ArrayList<Integer> storedArticleId = (ArrayList<Integer>)client.get("storedArticleId");
    	  if(storedArticleId.contains(article.getId())) {
    		  System.out.println("题为《"+article.getTitle()+"》的博文已经在缓存中，无需再次存入");
    	  }
    	  else {
    		  List<ArticleBean> articleListTemp = null;
        	  if(hasArticle(article.getTitle())) {
        		  articleListTemp = fetchSameTitleArticleList(article.getTitle());
        	  }
        	  else {
        		  articleListTemp = new ArrayList<ArticleBean>();
        	  }
        	  try {
//        		  Date date=new Date(System.currentTimeMillis()+1000*10*60*24*7); 
        		  articleListTemp.add(article);
        		  client.set(article.getTitle(), articleListTemp);
          	    System.out.println(article.getTitle()+" is stored in memcache.");
        	  }
        	  catch(Exception e) {
        		  e.printStackTrace();
        	  }
        	  storedArticleId.add(article.getId());
        	  client.set("storedArticleId", storedArticleId);
        	  System.out.println("题为《"+article.getTitle()+"》的博文存入缓存");
    	  }
      }
      public void updateOneArticlList(String title, List<ArticleBean> articleList) {
    	  client.set(title, articleList);
    	  
      }
      public void updateStoredId(int articleId) {
    	  ArrayList<Integer> storedArticleId = (ArrayList<Integer>)client.get("storedArticleId");
    	  storedArticleId.add(articleId);
    	  client.set("storedArticleId", storedArticleId);
      }
      public void removeOne(int articleId, String articleTitle) {
    	  if(hasArticle(articleTitle)) {
    		  List<ArticleBean> articleListTemp = fetchSameTitleArticleList(articleTitle);
    		  for (int i=0;i<articleListTemp.size();i++) {
    			  if(articleListTemp.get(i).getId() == articleId) {
    				  articleListTemp.remove(i);
    				  if(articleListTemp.size() == 0) client.delete(articleTitle);
    				  else client.set(articleTitle, articleListTemp);
    				  ArrayList<Integer> storedArticleId = (ArrayList<Integer>) client.get("storedArticleId");
    				  storedArticleId.remove(articleId);
    				  client.set("storedArticleId", storedArticleId);
    				  System.out.println("成功删除一篇题为《"+articleTitle+"》的博文");
    				  break;
    			  }
    		  }
    	  }
    	  System.out.println("缓存中不存在题为《"+articleTitle+"》的博文，无需删除");
      }
      public ArticleBean fetchArticle(int  articleId, String articleTitle) {
    	  ArticleBean res =null;
    	  List<ArticleBean> articleList =(List<ArticleBean>) client.get(articleTitle);  
    	  for(int i=0;i<articleList.size();i++) {
    		  if(articleId == articleList.get(i).getId()) {
    			  res = articleList.get(i);
    			  System.out.println("从缓存中获取到了题为《"+articleTitle+"》的博文");
    			  break;
    		  }
    	  }
    	  return res;
      }
      public List<ArticleBean> fetchSameTitleArticleList(String articleTitle) {
    	  List<ArticleBean> res =(List<ArticleBean>) client.get(articleTitle);  
    	  System.out.println("从缓存中获取到了题为《"+articleTitle+"》的博文！");
    	    return res;
      }
      public void showSameTitleArticleList(String articleTitle) {
    	  List<ArticleBean> res =(List<ArticleBean>) client.get(articleTitle);  
    	  for (int i=0;i<res.size();i++) {
    		  System.out.println(res.get(i).getId());
    	  }
      }
      public boolean hasArticle(String title) {
    	  boolean res = false;
    	  if(client.keyExists(title)) {
    		  res = true;
    	  }
    	  return res;
      }
      public void showStats() {
    	  System.out.println(client.stats());
//    	  System.out.println(client.statsItems());
      }
	public List<String> getAllKeys() {
		System.out.println("开始获取没有挂掉服务器中所有的key.......");
		List<String> list = new ArrayList<String>();
		Map<String, Map<String, String>> items = client.statsItems();
		for (Iterator<String> itemIt = items.keySet().iterator(); itemIt.hasNext();) {
			String itemKey = itemIt.next();
			Map<String, String> maps = items.get(itemKey);
		    for (Iterator<String> mapsIt = maps.keySet().iterator(); mapsIt.hasNext();) {
		    	String mapsKey = mapsIt.next();
             String mapsValue = maps.get(mapsKey);
             if (mapsKey.endsWith("number")) {  
             	String[] arr = mapsKey.split(":");
                 int slabNumber = Integer.valueOf(arr[1].trim());
                 int limit = Integer.valueOf(mapsValue.trim());
                 Map<String, Map<String, String>> dumpMaps = client.statsCacheDump(slabNumber, limit);
                 for (Iterator<String> dumpIt = dumpMaps.keySet().iterator(); dumpIt.hasNext();) {
                     String dumpKey = dumpIt.next();
                     Map<String, String> allMap = dumpMaps.get(dumpKey);
                     for (Iterator<String> allIt = allMap.keySet().iterator(); allIt.hasNext();) {
                         String allKey = allIt.next();
                         list.add(allKey.trim());

                     }
                 }
             }
			}
		}
		System.out.println("获取没有挂掉服务器中所有的key完成.......");
		for(int i=0;i<list.size();i++) {
			System.out.println(list.get(i));
		}
		System.out.println(list.size());
		return list;
	}
	public void flush() {
		client.flushAll();
	}

     
}