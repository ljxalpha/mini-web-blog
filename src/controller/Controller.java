package controller;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {
	// 执行核心业务逻辑
	public void execute(HttpServletRequest request, HttpServletResponse response);
}
