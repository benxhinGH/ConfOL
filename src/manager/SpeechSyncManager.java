package manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tcp.ServerResponseTask;
import tcp.protocol.DataProtocol;

public class SpeechSyncManager {
	private static SpeechSyncManager INSTANCE;
	
	private Map<Integer,SpeechRoom> rooms;
	private Map<ServerResponseTask,SpeechRoom> creatorRoomMap;
	
	
	
	
	static{
		INSTANCE=new SpeechSyncManager();
	}
	private SpeechSyncManager(){
		
	}
	
	public void createRoom(int roomId){
		rooms.put(roomId, new SpeechRoom());
	}
	
	public void closeRoom(int roomId){
		rooms.remove(roomId);
	}
	
	public void registerCreator(int roomId,ServerResponseTask creator){
		SpeechRoom room=rooms.get(roomId);
		room.setCreator(creator);
	}
	
	public void registerParticipator(int roomId,ServerResponseTask participator){
		SpeechRoom room=rooms.get(roomId);
		room.addParticipator(participator);
	}
	
	
	
	public void forwardEvent(ServerResponseTask creator,DataProtocol data){
		List<ServerResponseTask> participatorList=creatorRoomMap.get(creator).getParticipatorList();
		List<ServerResponseTask> invalid=new ArrayList<>();
		for(ServerResponseTask task:participatorList){
			if(!task.isConnected())invalid.add(task);
			task.addMessage(data);
		}
		
		participatorList.removeAll(invalid);
		for(ServerResponseTask task:invalid){
			task=null;
		}
	}
	
	
	
	
	public static SpeechSyncManager getIntance(){
		return INSTANCE;
	}
}
