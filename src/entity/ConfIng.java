package entity;

import java.sql.Timestamp;

public class ConfIng {
	private int id;
    private String title;
    private int type;
    private String password;
    private String channelId;
    private int capacity;
    private int online;
    private String member;
    private String creator;
    private Timestamp createTime;
    private String participator;
    private boolean hasfile;
    
	
	public ConfIng(int id, String title,int type, String password,
			String channelId, int capacity, int online, String member,
			String creator, Timestamp createTime, String participator,boolean hasfile) {
		this.id = id;
		this.title = title;
		this.type=type;
		this.password = password;
		this.channelId = channelId;
		this.capacity = capacity;
		this.online = online;
		this.member = member;
		this.creator = creator;
		this.createTime = createTime;
		this.participator = participator;
		this.hasfile=hasfile;
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
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public int getOnline() {
		return online;
	}
	public void setOnline(int online) {
		this.online = online;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
	public String getParticipator() {
		return participator;
	}
	public void setParticipator(String participator) {
		this.participator = participator;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public boolean isHasfile() {
		return hasfile;
	}
	public void setHasfile(boolean hasfile) {
		this.hasfile = hasfile;
	}
	
	
    
    

}
