package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tcp.ServerResponseTask;
import tcp.protocol.DataProtocol;

public class SpeechSyncManager {
	private static SpeechSyncManager INSTANCE;
	
	private Map<Integer,SpeechRoom> rooms=new HashMap<>();
	private Map<ServerResponseTask,SpeechRoom> creatorRoomMap=new HashMap<>();
	
	
	
	
	static{
		INSTANCE=new SpeechSyncManager();
	}
	private SpeechSyncManager(){
		
	}
	
	public void createRoom(int roomId){
		System.out.println("创建房间："+roomId);
		rooms.put(roomId, new SpeechRoom());
	}
	
	public void closeRoom(int roomId){
		rooms.remove(roomId);
	}
	
	public void registerCreator(int roomId,ServerResponseTask creator){
		System.out.println("注册创建者，roomId："+roomId);
		SpeechRoom room=rooms.get(roomId);
		room.setCreator(creator);
		creatorRoomMap.put(creator, rooms.get(roomId));
	}
	
	public void registerParticipator(int roomId,ServerResponseTask participator){
		System.out.println("注册参与者，roomId："+roomId+"task:"+participator.toString());
		SpeechRoom room=rooms.get(roomId);
		room.addParticipator(participator);
	}
	
	
	
	public void forwardEvent(ServerResponseTask creator,DataProtocol data){
		//System.out.println("转发消息："+data.toString());
		List<ServerResponseTask> participatorList=creatorRoomMap.get(creator).getParticipatorList();
		if(participatorList.size()==0){
			System.out.println("房间内成员数为0");
			return;
		}
		List<ServerResponseTask> invalid=new ArrayList<>();
		for(ServerResponseTask task:participatorList){
			if(!task.isConnected()){
				System.out.println("有task失去连接");
				invalid.add(task);
				continue;
			}
			//System.out.println("为["+task.toString()+"]转发");
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
