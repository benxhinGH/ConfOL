package entity;

public class User {
	
	private int id;
	private String nickname;
	private String phonenumber;
	private String password;
	private String headImageUrl;
	
	public User(int id, String nickname, String phonenumber, String password,String headImageUrl) {
		this.id = id;
		this.nickname = nickname;
		this.phonenumber = phonenumber;
		this.password = password;
		this.headImageUrl=headImageUrl;
	}
	public User(String phonenumber, String password) {
		this.phonenumber = phonenumber;
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHeadImageUrl() {
		return headImageUrl;
	}
	public void setHeadImageUrl(String headImageUrl) {
		this.headImageUrl = headImageUrl;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", nickname=" + nickname + ", phonenumber="
				+ phonenumber + ", password=" + password + ", headImageUrl="
				+ headImageUrl + "]";
	}
	
	
}
