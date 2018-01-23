package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

public class Util {
	
	/**
	 * ¶ÁÈ¡postÊý¾Ý
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getPostData(HttpServletRequest request) throws IOException{  
        BufferedReader reader = request.getReader();  
        char[] buf = new char[512];  
        int len = 0;  
        StringBuffer contentBuffer = new StringBuffer();  
        while ((len = reader.read(buf)) != -1) {  
            contentBuffer.append(buf, 0, len);  
        }  
          
        String content = contentBuffer.toString();  
          
        if(content == null){  
            content = "";  
        }  
          
        return content;  
    } 

}
