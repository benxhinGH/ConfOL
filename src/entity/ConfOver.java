package entity;

import java.sql.Time;
import java.sql.Timestamp;

public class ConfOver {
	private int id;
	private String title;
	private String creator;
	private Timestamp createTime;
	private Time duration;
	private String participator;
	public ConfOver(int id, String title, String creator, Timestamp createTime,
			Time duration, String participator) {
		this.id = id;
		this.title = title;
		this.creator = creator;
		this.createTime = createTime;
		this.duration = duration;
		this.participator = participator;
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
	public Time getDuration() {
		return duration;
	}
	public void setDuration(Time duration) {
		this.duration = duration;
	}
	public String getParticipator() {
		return participator;
	}
	public void setParticipator(String participator) {
		this.participator = participator;
	}
	
	
	
	

}
