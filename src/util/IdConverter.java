package util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class IdConverter {
	private Set<Integer> set;
	
	public IdConverter(String s){
		set=new HashSet();
		String[] arr=null;
		if(s!=null){
			arr=s.split(",");
			for(String s1:arr){
			set.add(Integer.valueOf(s1));
		}
		}
		
		
	}
	public boolean remove(int i){
		return set.remove(i);
	}
	public boolean add(int i){
		return set.add(i);
	}
	
	public boolean contains(int i){
		return set.contains(i);
	}
	
	
	public String toString(){
		
		String res="";
		for(int i:set){
			res+=i+",";
		}
		if(res.length()==0)return "";
		res=res.substring(0,res.length()-1);
		return res;
	}
	
}
