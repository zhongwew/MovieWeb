package Entitypackage;

public class UserInfo {
	private String user_name;
	private String id;
	public UserInfo(String u,String i){
		user_name = u;
		id = i;
	}
	public String getName() {
		return this.user_name;
	}
	public String getId() {
		return this.id;
	}
}
