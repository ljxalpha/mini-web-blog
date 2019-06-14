package main;
 
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import controller.Controller;


@WebServlet("/")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    String xml_path;
    Map<String, Map<String, String>> mapping;
    public MainServlet() { 
        super();
    }
    
    public void init() throws ServletException {
    	this.xml_path = this.getServletConfig().getServletContext().getRealPath("/route/web.xml");
    	mapping = new HashMap<String, Map<String, String>>();
    	try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml_path);
            //获取所有map
            NodeList mapList = document.getElementsByTagName("map");
           
            for (int i = 0; i < mapList.getLength(); i++) {
            	Node map = mapList.item(i);
            	NodeList childNodes = map.getChildNodes();
            	String url = "";
            	String controller = "";
            	String goPage = "";
                for (int k = 0; k < childNodes.getLength(); k++) {
                    if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
                        String nodeName = childNodes.item(k).getNodeName();
                        String nodeValue = childNodes.item(k).getFirstChild().getNodeValue();
                        if(nodeName.equals("url")) {
                        	url = nodeValue;
                        }else if(nodeName.equals("controller")) {
                        	controller = nodeValue;
                        }else if(nodeName.equals("goPage")) {
                        	goPage = nodeValue;
                        }
                    }
                }
                Map<String, String> value = new HashMap<String, String>();
                value.put("controller", controller);
                value.put("goPage", goPage);
                value.put("url", url);
                mapping.put(url, value);
            }
        }catch(Exception e) {
        	e.printStackTrace();
        }
	}
    //处理get请求
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 当前请求路径
			String url = request.getRequestURI().replaceAll("/MyMVCdemo", "");
			System.out.println("当前请求:" + url);
			Map<String, String> cur_mapping = this.mapping.get(url);
			if(cur_mapping == null) {
				response.getWriter().append("Served at: ").append("404");
				return;
			}
			String controller_name = cur_mapping.get("controller");
			String goPage = cur_mapping.get("goPage");
			if(controller_name == null || goPage == null) {
				response.getWriter().append("Served at: ").append("404");
				return;
			}
			if(controller_name != null && !controller_name.equals("")) {
				Controller controller = (Controller) getClass().getClassLoader().loadClass("controller.imp."+controller_name).newInstance();
				controller.execute(request, response);
			}
			if(goPage!=null && !goPage.equals("")) {
				request.getRequestDispatcher(goPage).forward(request, response);
			}
			
		} catch (Exception e) {
			response.getWriter().append("Served at: ").append("404");
			e.printStackTrace();
		}	
	}
	//post请求
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		doGet(request, response);
	}

}
