package util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class AuthcodeHelper extends Thread{
	
	//��֤����Чʱ��,5����
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
		System.out.println("������֤�룺"+authcode+"�ֻ��ţ�"+phonenumber);
	}
	
	/**
	 * ����6λ��֤��
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
	 * �����ѹ��ڵ���֤��
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
				System.out.println("���������֤�룺"+authcode+"�ֻ��ţ�"+key);
			}
		}
	}
	
	
	
	
	

}
