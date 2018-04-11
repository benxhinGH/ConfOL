package tcp.event;

public class AuthEvent {
	
	public static final String AUTH_SUCCESS="identity verification";
	
	private int roomId;
	/**
	 * 0Ϊ�����ˣ�1Ϊ�λ���
	 */
	private int identity;
	
	
	public AuthEvent(int roomId, int identity) {
		this.roomId = roomId;
		this.identity = identity;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public int getIdentity() {
		return identity;
	}
	public void setIdentity(int identity) {
		this.identity = identity;
	}
	

}
