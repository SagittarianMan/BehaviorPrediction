package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;
import tools.Functions;
import tools.ModifyTable;

public class FindXDB {
	public static final String url="jdbc:mysql://localhost:3306/FindX?useUnicode=true&characterEncoding=utf8";//202.118.83.209:51118/FindX,zsh,zsh
	public static final String userName="root";
	public static final String psd="";
	public Connection connection=null;
	
	Statement statement;
	ResultSet rs;
	String sql;
	
	public FindXDB(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("**");
		} catch (ClassNotFoundException e) {
			System.out.println("找不到数据库驱动");
			System.out.println(e);
		}
		try {
			connection = DriverManager.getConnection(url,userName,psd);
			System.out.println("***");
		} catch (Exception ee) {
			System.out.println("连接出错");
			System.out.println(ee);
		}
	}
	
	//登录验证
	public int validate(String name, String psd){
		int res=0;
		psd = Functions.MD5(psd);
		sql = "select id from users where name='"+name+"' and password='"+psd+"'";
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			rs = statement.executeQuery(sql);
			if(rs.next()){
				res = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}
	// 从数据库获取社交信息QQ
		public String getQQ(String user_id){
			String qq = null; 
			try {
				statement = connection.createStatement();
				sql = "select qq from users where id = "+user_id;
				rs = statement.executeQuery(sql);
				while (rs.next()) {
					qq = rs.getString(1);				
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return qq;
		}
		
		// 从数据库获取社交信息Wechat
			public String getWechat(String user_id){
				String wechat = null; 
				try {
					statement = connection.createStatement();
					sql = "select wechat from users where id = "+user_id;
					rs = statement.executeQuery(sql);
					while (rs.next()) {
						wechat = rs.getString(1);				
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return wechat;
			}
			
			public String getQW(String uid){
				String rs=null;
				String qq = getQQ(uid);
				String wechat = getWechat(uid);
				JSONObject object = new JSONObject();
				object.put("qq", qq);
				object.put("wechat", wechat);
				System.out.println(object.toString());
				rs = object.toString();
				return rs;
			}
			
			//修改用户QW
			public int modifyQW(String uid,String qq,String wechat){
				sql = "update users set ";
				if(qq!=""){
					sql += "qq="+qq;
					if(wechat!=""){
						sql += ", wechat='"+wechat+"' ";
					}
				}
				else{
					sql += " wechat='"+wechat+"' ";
				}
				sql += " where id="+uid;
				System.out.println(sql);
				try {
					statement = connection.createStatement();
					statement.execute(sql);
					connection.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.toString());
					return 0;
				}
				return 1;
			}
			
			//添加用户名、密码到数据库，返回新用户的ID
			public int insert(String userName,String psd){
				Date date=new Date(); 
				DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				String time=format.format(date);
				ModifyTable mt = new ModifyTable();
				mt.setDatasourceName("202.118.83.209:51118/FindX");
				System.out.println(time);
				mt.setSQL("insert into users(name,create_date,password) values('"+userName+"','"+time+"','"+psd+"')");
				String msg = mt.modifyRecord();
				System.out.println("FindXDB.insert:"+msg);//操作成功
				int id = mt.queryIdByName(userName);
				return id;
			}
			
			public int queryIdByName(String name){
				int id=0;
				try {
					statement = connection.createStatement();
					sql = "select id from users where name='"+name+"'";
					rs = statement.executeQuery(sql);
					if (rs.next()) {
						id = rs.getInt(1);
					}
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return id;
			}
			//从数据库获取历史记录文件的路径By用户ID
			public String getHisFileByUid(String uid){
				String res="";
				try{
					statement = connection.createStatement();
					sql = "select history_list from history where user_id='"+uid+"' order by id desc limit 1";
					rs = statement.executeQuery(sql);
					if(rs.next()){
						res = rs.getString(1);
					}
				}
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return res;
			}
			//插入history_items
			public int insertHii(String hid,String userid,String webTitle,String url,int visitCount,String lastDate){
				int res = 0;
				try{
					statement = connection.createStatement();
					sql = "insert into history_items(hid,userid,web_title,web_url,visit_count,last_visit_date) values("+hid+","+userid+",'"+webTitle+"','"+url+"',"+visitCount+",'"+lastDate+"')";
					statement.execute(sql);
					res = 1;
				}
				catch(Exception e){
					System.out.println(e.toString()+"8888888888888888");
				}
				return res;
			}
			//更新记录
			public int updateSameItem(int id, int visitCount, String lastDate){
				int res=0;
				sql = "update table history_items set visit_count="+visitCount+", last_visit_date="+lastDate+" where id="+id;
				try{
					statement = connection.createStatement();
					statement.execute(sql);
					res=1;
				}
				catch(Exception e){
					Functions.print(e);
				}
				return res;
			}
			//检查是否有重复记录
			public int checkSameItem(String hid,String userid,int visitCount,String lastDate){
				int res=0;
				sql = "select visit_count,id from history_items where hid="+hid+" and userid="+userid;
				try{
					statement = connection.createStatement();
					rs = statement.executeQuery(sql);
					if(rs.next()){
						int visitCount0=rs.getInt(1);
						int id = rs.getInt(2);
						if(visitCount!=visitCount0){
							updateSameItem(id, visitCount0, lastDate);
						}
						res=1;
					}
					else{
						res=-1;
					}					
				}
				catch(Exception e){
					Functions.print(e);
				}				
				return res;
				
			}
			//从history_items表中删除某用户的记录
			public int delItemsById(String uid){
				int res = 0;
				try{
					statement = connection.createStatement();
					sql = "delete from history_items where userid="+uid;
					statement.execute(sql);
					res = 1;
				}
				catch(Exception e){
					System.out.println(e.toString());
				}
				return res;
			}
}
