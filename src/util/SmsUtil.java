package util;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import entity.HttpCallback;

public class SmsUtil {
	
	private static String uid="lyzinSMS";
	private static String key="60a7b4108b2a58a6956f";

	public static void sendMsg(String phonenumber,String content,HttpCallback callback) throws HttpException, IOException{
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://gbk.api.smschinese.cn"); 
		post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
		NameValuePair[] data ={ new NameValuePair("Uid", uid),new NameValuePair("Key", key),new NameValuePair("smsMob",phonenumber),new NameValuePair("smsText",content)};
		post.setRequestBody(data);

		client.executeMethod(post);
		Header[] headers = post.getResponseHeaders();
		int statusCode = post.getStatusCode();
		System.out.println("statusCode:"+statusCode);
		
		if(statusCode>0)callback.onSuccess();
		else callback.onError(statusCode, "unkown");
		
		
		for(Header h : headers)
		{
		System.out.println(h.toString());
		}
		String result = new String(post.getResponseBodyAsString().getBytes("gbk")); 
		System.out.println(result); //打印返回消息状态


		post.releaseConnection();
		
		callback.onComplete();
		
	}
}
