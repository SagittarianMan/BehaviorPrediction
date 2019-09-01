package actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.FindXDB;

public class GetQW extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public GetQW() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 请求数据库中的用户社交信息
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		FindXDB fx = new FindXDB();
		String uid = request.getParameter("user_id");
		String rs=fx.getQW(uid);
//		response.setHeader(arg0, arg1)
		response.setContentType("text/json");		
		PrintWriter out = response.getWriter();
		out.print(rs);
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 更新用户社交信息
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		FindXDB fx = new FindXDB();
		String uid = request.getParameter("user_id");
		String qq=request.getParameter("qq");
		String wechat=request.getParameter("wechat");
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = response.getWriter();
		int rs = fx.modifyQW(uid, qq, wechat);
		out.println(rs);
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
