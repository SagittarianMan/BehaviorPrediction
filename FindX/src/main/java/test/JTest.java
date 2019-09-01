package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;

import org.junit.Test;
import org.python.core.Py;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import tools.Functions;
import tools.Stream;
import util.FindXDB;
import util.HistoryItem;

public class JTest {
	
	@Test
	public void testJython(){
			PythonInterpreter interpreter = new PythonInterpreter();  
			PySystemState sys = Py.getSystemState(); 
//			sys.setdefaultencoding("UTF-8");
			sys.path.add("D:\\Program Files\\Python35\\Lib\\site-packages");
			sys.path.add("D:\\Program Files\\Python35\\Lib\\idlelib");
			sys.path.add("D:\\Program Files\\Python35\\python35.zip");
			sys.path.add("D:\\Program Files\\Python35\\DLLs");
			sys.path.add("D:\\Program Files\\Python35\\lib");
			sys.path.add("D:\\Program Files\\Python35");
			sys.path.add("E:\\学习\\大四\\Python\\Test");
		  interpreter.exec("import sys;");  
		  interpreter.exec("sys.setdefaultencoding('utf-8');");  
			
		  interpreter.exec("import jieba;");  
//		  interpreter.exec("print(sys.path)");  
	}

	/**
	 * @param args
	 */
	// {"id":"622","lastVisitTime":1488430705718.574,"title":"Magic Actions | Change Your Options","typedCount":0,"url":"https://www.chromeactions.com/magic-options.html","visitCount":1}
	// public static void main(String[] args) {
	// // TODO Auto-generated method stub
	// //readFile();
	// writeFile();
	// }
	//@Test
	public void test() {
		FindXDB fx = new FindXDB();
		fx.getQW("6");
	}

	//@Test
	public void testDate() {
		System.out.print(Functions.mmToDate(1493775754000.00));
	}

	//@Test
	public void readFile() {
		// System.out.println(Class.class.getClass().getResource("/").getPath()
		// );
		System.out.println(System.getProperty("user.dir"));
		FindXDB fxdb = new FindXDB();
		String filePath = null;
		String uid = "25";
		filePath = fxdb.getHisFileByUid(uid);
		filePath = System.getProperty("user.dir") + filePath;
		System.out.println(filePath);
		
		//先把该用户的历史记录删了再插入
		fxdb.delItemsById(uid);
		// 读出文件内容
		File file = new File(filePath);
		FileInputStream fin = null;
		String fileContent = null;
		try {
			fin = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileContent = Stream.streamToString(fin);
		System.out.println(fileContent);
		JSONArray jArray = new JSONArray();
		jArray = JSONArray.fromObject(fileContent);
		System.out.println(jArray.size());
		List list = JSONArray.toList(jArray, HistoryItem.class);
		System.out.println(list.size());
		Iterator it = list.iterator();
		int i=0;
		while(it.hasNext()&&i<3){
			HistoryItem p = (HistoryItem)it.next();
			//System.out.println(p.getTitle());
			int res = fxdb.insertHii(p.getId(),uid, p.getTitle(), p.getUrl(), p.getVisitCount(), Functions.mmToDate(p.getLastVisitTime()));
			//System.out.println(Functions.mmToDate(p.getLastVisitTime()));
			i++;
		}

	}

	// 写文件
	public static void writeFile() {
		String path = "E://MyWeb//FindX//history//啧啧啧.txt";
		String s = "llll啦啦啦啦啦石石石assfdas";
		System.out.println(path);
		try {
			File file = new File(path);
			System.out.println(1);
			FileOutputStream fo = new FileOutputStream(file);
			OutputStreamWriter write = new OutputStreamWriter(fo, "utf-8");
			BufferedWriter writer = new BufferedWriter(write);
			System.out.println(2);
			writer.write(s);
			System.out.println(3);
			writer.close();
			fo.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}
