package tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import util.FindXDB;
import util.HistoryItem;


public class Functions {
	private static final int BUFFER_SIZE=16*1024;
	public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	public static void copy(File src,File dst){
		InputStream inputStream=null;
		OutputStream outputStream =null;
		try{
			inputStream = new BufferedInputStream(new FileInputStream(src),BUFFER_SIZE);
			outputStream = new BufferedOutputStream(new FileOutputStream(dst),BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			int len=0;
			while ((len=inputStream.read(buffer))>0) {
				outputStream.write(buffer,0,len);
			}
			inputStream.close();
			outputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//毫秒时间转日期
	public static String mmToDate(double d){
		long mm = (long)d;
		print(d+"\t"+mm);
		//print(String.valueOf(d)+"\t"+String.valueOf(d).length());
		if(String.valueOf(mm).length()<11){
			mm = mm * 1000;
		}
		String dateString = "";
		Date date = new Date((long) mm);
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		dateString=format.format(date);
		return dateString;
	}
	//把文件内容插入数据库
	public static int insertHiiFromFile(String fileContent,String uid){
		int res = 0;
		FindXDB fxdb = new FindXDB();
		//先把该用户的历史记录删了再插入
		//fxdb.delItemsById(uid);
		
		JSONArray jArray = new JSONArray();
		jArray = JSONArray.fromObject(fileContent);
		System.out.println(jArray.size());
		List list = JSONArray.toList(jArray, HistoryItem.class);
		System.out.println(list.size());
		Iterator it = list.iterator();
		while(it.hasNext()){
			HistoryItem p = (HistoryItem)it.next();
			//System.out.println(p.getTitle());
			//筛选过滤数据
			if(filter(p,uid)==1){
				continue;
			}
			res = fxdb.insertHii(p.getId(),uid, p.getTitle(), p.getUrl(), p.getVisitCount(), Functions.mmToDate(p.getLastVisitTime()));
			//System.out.println(Functions.mmToDate(p.getLastVisitTime()));
		}
		return res;
	}
	//print
	public static void print(Object o){
		System.out.println(o.toString());
	}
	
	//过滤数据
	public static int filter(HistoryItem p,String uid){
		int rs=0;
		FindXDB fxdb = new FindXDB();
		String url = p.getUrl();
		String title = p.getTitle();
		int ck=fxdb.checkSameItem(p.getId(), uid, p.getVisitCount(), Functions.mmToDate(p.getLastVisitTime()));
		if(ck==1){
			rs=1;
			return rs;
		}
		if(title==""||title=="Google"||title.startsWith("我的首页")){
			rs = 1;
			return rs;
		}
		String[] url_exception={"://mail","user.qzone.qq.com/","account.","login.","dlmu.edu.cn","bt.neu6.edu.cn"};
		String[] public_sites={"www.baidu.com/","www.taobao.com/","bilibili.com/","www.zhihu.com/","bing.com/","www.jd.com/","qq.com/"};
		for(int i=0;i<public_sites.length;++i){
			if(url.endsWith(public_sites[i])){
				rs=1;
				return rs;
			}
		}
		for(int i=0;i<url_exception.length;++i){
			if(url.indexOf(url_exception[i])>0){
				rs = 1;
				return rs;
			}
		}
		if(!url.startsWith("http")  || url.indexOf("localhost")<8 || url.indexOf("127.0.0.1")<8){
			rs=1;
			return rs;
		}
		return rs;
	}
}
