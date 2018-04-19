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
		System.out.println("�������䣺"+roomId);
		rooms.put(roomId, new SpeechRoom());
	}
	
	public void closeRoom(int roomId){
		rooms.remove(roomId);
	}
	
	public void registerCreator(int roomId,ServerResponseTask creator){
		System.out.println("ע�ᴴ���ߣ�roomId��"+roomId);
		SpeechRoom room=rooms.get(roomId);
		room.setCreator(creator);
		creatorRoomMap.put(creator, rooms.get(roomId));
	}
	
	public void registerParticipator(int roomId,ServerResponseTask participator){
		System.out.println("ע������ߣ�roomId��"+roomId+"task:"+participator.toString());
		SpeechRoom room=rooms.get(roomId);
		room.addParticipator(participator);
	}
	
	
	
	public void forwardEvent(ServerResponseTask creator,DataProtocol data){
		//System.out.println("ת����Ϣ��"+data.toString());
		List<ServerResponseTask> participatorList=creatorRoomMap.get(creator).getParticipatorList();
		if(participatorList.size()==0){
			System.out.println("�����ڳ�Ա��Ϊ0");
			return;
		}
		List<ServerResponseTask> invalid=new ArrayList<>();
		for(ServerResponseTask task:participatorList){
			if(!task.isConnected()){
				System.out.println("��taskʧȥ����");
				invalid.add(task);
				continue;
			}
			//System.out.println("Ϊ["+task.toString()+"]ת��");
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
