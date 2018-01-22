package util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class AuthcodeHelper extends Thread{
	
	//验证码有效时间,5分钟
	public static final int authPeriod=5*60000;
	public static final int SIZE=100;
	private HashMap<String,String> authcodeMap;
	private HashMap<String,Long> codetimeMap;
	private Random random;
	
	
	
	
	
	public AuthcodeHelper(){
		authcodeMap=new HashMap<String, String>();
		codetimeMap=new HashMap<String,Long>();
		random=new Random();
	}
	
	public void put(String phonenumber,String authcode){
		authcodeMap.put(phonenumber, authcode);
		codetimeMap.put(authcode, System.currentTimeMillis());
		System.out.println("存入验证码："+authcode+"手机号："+phonenumber);
	}
	
	/**
	 * 生成6位验证码
	 * @return
	 */
	public String generateAuthcode(){
		String res="";
		for(int i=0;i<6;++i){
			res+=random.nextInt(10);
		}
		return res;
	}
	
	
	public boolean verifyAuthcode(String phonenumber,String authcode){
		String real=authcodeMap.get(phonenumber);
		if(real.equals(authcode))return true;
		return false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clearPool();
	}
	
	/**
	 * 清理已过期的验证码
	 */
	private void clearPool(){
		Iterator<String> it=authcodeMap.keySet().iterator();
		while(it.hasNext()){
			String key=it.next();
			String authcode=authcodeMap.get(key);
			long time=codetimeMap.get(authcode);
			if(System.currentTimeMillis()-time>=authPeriod){
				codetimeMap.remove(authcode);
				it.remove();
				System.out.println("清理过期验证码："+authcode+"手机号："+key);
			}
		}
	}
	
	
	
	
	

}
