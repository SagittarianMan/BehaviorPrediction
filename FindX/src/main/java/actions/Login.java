package actions;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.Functions;
import tools.ModifyTable;
import tools.Stream;
import util.FindXDB;

public class Login extends HttpServlet {
	Connection connection;
	Statement statement;
	ResultSet rs;
	String sql;
	
	public String uploadPhotosContentTypeString;
	public String uploadPhotosFileName;
	public String savePath;
	
	public String getUploadPhotosContentTypeString() {
		return uploadPhotosContentTypeString;
	}

	public void setUploadPhotosContentTypeString(
			String uploadPhotosContentTypeString) {
		this.uploadPhotosContentTypeString = uploadPhotosContentTypeString;
	}

	public String getUploadPhotosFileName() {
		return uploadPhotosFileName;
	}

	public void setUploadPhotosFileName(String uploadPhotosFileName) {
		this.uploadPhotosFileName = uploadPhotosFileName;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	/**
	 * Constructor of the object.
	 */
	public Login() {
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
		String name = request.getParameter("name");
		String psd = request.getParameter("psd");
		FindXDB fdb = new FindXDB();
		System.out.println(name);

		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(fdb.validate(name, psd));
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
	//获取历史记录
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("start post");
		String userName =request.getParameter("userName");
		System.out.println(userName);
//		userName = new String(userName.getBytes("iso-8859-1"),"gbk");
//		System.out.println(userName);
		String userId = request.getParameter("userId");
		if(request.getCharacterEncoding()==null){
			request.setCharacterEncoding("UTF-8");
		}
				
		PrintWriter out = response.getWriter();
		try{
			System.out.println("ssssssssssss");
			System.out.println(request.getContentType());
			response.setContentType("text/plain;charset=UTF-8");
			out.print("hello Post\n");
			
			String uri = this.getServletContext().getRealPath("/");
			System.out.println("uri:"+uri);
			
			String filePath = uri+"//history//"+userId+".txt";//设置保存路径
			
			File dstFile = new File(uri+"//history//"); //创建目录
			boolean exist=dstFile.exists();
			if(!exist){
				   dstFile.mkdir();
			}
			exist=dstFile.exists();
			System.out.println(exist);
			File txt=new File(filePath);
			//先将流转成字符串，再将字符串写入文件
			ServletInputStream fin = request.getInputStream();
			String fileContent = Stream.writeStreamToFile(fin, txt);//获取文件内容
			System.out.println(fileContent);//*******************************
			//将文件内容（历史记录条目）插入表
			int res = Functions.insertHiiFromFile(fileContent, userId);
			System.out.println(res);
			System.out.println("path:"+txt.getPath());//*******************************
			//将路径存入数据库
			filePath = "/history/"+userId+".txt";
//			filePath = filePath.replace("/", "\\");
			if(updateHistory(filePath, userId)==1){
				System.out.println(userName+"保存成功");
			}
			else {
				System.out.println(userName+"保存失败");
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		finally{
			out.flush();
			out.close();
		}
		//System.out.println(userId);
		System.out.println(userName);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		System.out.println("1");
		FindXDB db=new FindXDB() ;
		this.connection = db.connection;
		// Put your code here
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			System.out.println("**");
//		} catch (ClassNotFoundException e) {
//			System.out.println("找不到数据库驱动");
//			System.out.println(e);
//		}
//		try {
//			connection = DriverManager.getConnection(
//					"jdbc:mysql://localhost:3306/FindX", "root", "");
//			System.out.println("***");
//		} catch (Exception ee) {
//			System.out.println("连接出错");
//			System.out.println(ee);
//		}
	}
	
	
	//添加历史记录到数据库
	public static int updateHistory(String path,String userId){
		Date date=new Date(); 
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String time=format.format(date);
		ModifyTable mt = new ModifyTable();
		mt.setDatasourceName("202.118.83.209:51118/FindX");
		System.out.println(time);
		mt.setSQL("insert into history(history_list,user_id,upload_date) values('"+path+"','"+userId+"','"+time+"')");
		String msg = mt.modifyRecord();
		System.out.println(msg);
		if(msg=="操作成功"){
			return 1;
		}
		else{
			return 0;
		}
	}
}
