package manager;

import java.util.ArrayList;
import java.util.List;

import tcp.ServerResponseTask;

public class SpeechRoom {
	
	private ServerResponseTask creator;
	private List<ServerResponseTask> participatorList=new ArrayList<>();
	
	public SpeechRoom(){
		
	}
	
	public void setCreator(ServerResponseTask creator){
		this.creator=creator;
	}
	
	public void addParticipator(ServerResponseTask participator){
		participatorList.add(participator);
	}
	
	public List<ServerResponseTask> getParticipatorList(){
		return participatorList;
	}

}
