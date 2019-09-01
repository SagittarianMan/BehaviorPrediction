package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Stream {
	public static String convertStreamToString(InputStream is) {      
        /*  
          * To convert the InputStream to String we use the BufferedReader.readLine()  
          * method. We iterate until the BufferedReader return null which means  
          * there's no more data to read. Each line will appended to a StringBuilder  
          * and returned as String.  
          */     
         BufferedReader reader = new BufferedReader(new InputStreamReader(is));      
         StringBuilder sb = new StringBuilder();      
     
         String line = null;      
        try {      
            while ((line = reader.readLine()) != null) {      
                 sb.append(line);      
             }      
         } catch (IOException e) {      
             e.printStackTrace();      
         } finally {      
            try {      
                 is.close();      
             } catch (IOException e) {      
                 e.printStackTrace();      
             }      
         }      
     
        return sb.toString();      
     }      
	
	
	public static String myMethod(InputStream fin){
		int n=0;		
		String rs="";
		String ss="";
		byte b[] =new byte[255];
		try {
			while((n=fin.read(b,0,255))!=-1){
				ss = new String(b,0,n);
				//ss = b.toString();
				rs += ss;
				System.out.println(n+"\t"+ss);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	//把流读取为字符串
	public static String streamToString(InputStream fin){
		String content = "";
		try{
			InputStreamReader read = new InputStreamReader(fin,"gbk");
			BufferedReader reader=new BufferedReader(read);       
	        String line;       
	        while ((line = reader.readLine()) != null)   
	        {        
	        	content += line;       
	        }         
	        read.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
        //System.out.println(content);
		return content;
	}
	//先将流转成字符串，再将字符串写入文件
	public static String writeStreamToFile(InputStream fin,File file){
		String fileContent = "";
		try{
			FileOutputStream fo = new FileOutputStream(file);
			OutputStreamWriter write = new OutputStreamWriter(fo,"gbk");
			BufferedWriter writer=new BufferedWriter(write); 
			
			InputStreamReader read = new InputStreamReader(fin,"utf-8");
			BufferedReader reader=new BufferedReader(read);       
	        String line;       
	        while ((line = reader.readLine()) != null)   
	        {        
	            fileContent += line;       
	        }         
	        //System.out.println(fileContent);
	        read.close(); 
			writer.write(fileContent);
			writer.close();
			fo.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		return fileContent;
	}
	//连接字符串（测试）
	public String cat(String s1,String s2){
		String rs=s1+s2;
		return rs;
	}
}
