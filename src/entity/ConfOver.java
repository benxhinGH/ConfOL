package entity;

import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Date;

public class ConfOver {
	private int id;
	private String title;
	private int type;
	private String creator;
	private long createTime;
	private int duration;
	private String participator;
	
	public ConfOver(){
		
	}
	
	public ConfOver(int id, String title, int type,String creator, long createTime,
			int duration, String participator) {
		this.id = id;
		this.title = title;
		this.type=type;
		this.creator = creator;
		this.createTime = createTime;
		this.duration = duration;
		this.participator = participator;
	}
	
	public ConfOver(String title, String creator, long createTime,
			int duration, String participator) {
		super();
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
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
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

	@Override
	public String toString() {
		return "ConfOver [id=" + id + ", title=" + title + ", type=" + type
				+ ", creator=" + creator + ", createTime=" + createTime
				+ ", duration=" + duration + ", participator=" + participator
				+ "]";
	}
	
	
	
	
	
	

}
