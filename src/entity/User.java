package entity;

public class User {
	
	private int id;
	private String nickname;
	private String phonenumber;
	private String password;
	
	public User(int id, String nickname, String phonenumber, String password) {
		super();
		this.id = id;
		this.nickname = nickname;
		this.phonenumber = phonenumber;
		this.password = password;
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
	@Override
	public String toString() {
		return "User [id=" + id + ", nickname=" + nickname + ", phonenumber="
				+ phonenumber + ", password=" + password + "]";
	}
	
}
