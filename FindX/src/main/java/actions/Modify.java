package actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.ModifyTable;

/**
 * 
 * @author Sarketch
 * doGet  修改昵称
 * doPost 完善社交信息
 */
public class Modify extends HttpServlet {
	Connection connection;
	Statement statement;
	ResultSet rs;
	String sql;

	/**
	 * Constructor of the object.
	 */
	public Modify() {
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
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String old_name = request.getParameter("old_name");
		String new_name = request.getParameter("new_name");
		int response_code;
		//检查新昵称是否冲突
		try{
			statement = connection.createStatement();
	
			sql = "select * from users where name = '" + new_name + "'";
			rs = statement.executeQuery(sql);
			if (rs.next()) {
				response_code=0;
			}
			else{
				response_code = modifyName(old_name, new_name);
			}
		}
		catch(SQLException e){
			System.out.println("查询出错！");
			System.out.println(e.toString());
			response_code = -1;
		}		
		response.setContentType("text/plain;charset=UTF-8");
		out.print(response_code);
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
		System.out.println("Modify");
		// Put your code here
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("**");
		} catch (ClassNotFoundException e) {
			System.out.println("找不到数据库驱动");
			System.out.println(e);
		}
		try {
			connection = DriverManager.getConnection(
					"jdbc:mysql://202.118.83.209:51118/FindX", "zsh", "zsh");
			System.out.println("***");
		} catch (Exception ee) {
			System.out.println("连接出错");
			System.out.println(ee);
		}
	}

	// 修改昵称
	public int modifyName(String old_name, String new_name) {
		ModifyTable mt = new ModifyTable();
		mt.setDatasourceName("202.118.83.209:51118/FindX");
		mt.setSQL("update users set name = '" + new_name + "' where name = '"
				+ old_name + "'");
		String msg = mt.modifyRecord();
		System.out.println(msg);
		return 1;
	}
	
	

}
