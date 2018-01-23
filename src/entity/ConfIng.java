package entity;

import java.sql.Timestamp;

public class ConfIng {
	private int id;
    private String title;
    private String password;
    private String roomId;
    private int capacity;
    private String creator;
    private Timestamp createTime;
    
	public ConfIng(int id, String title, String password, String roomId,
			int capacity, String creator, Timestamp createTime) {
		this.id = id;
		this.title = title;
		this.password = password;
		this.roomId = roomId;
		this.capacity = capacity;
		this.creator = creator;
		this.createTime = createTime;
	}
	public ConfIng() {
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
    
    

}
