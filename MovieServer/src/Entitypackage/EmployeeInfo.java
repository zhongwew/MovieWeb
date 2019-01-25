package Entitypackage;

public class EmployeeInfo {
	private String email;
	private String password;
	private String fullname;
	public EmployeeInfo(String e,String f) {
		email = e;
		fullname = f;
	}
	public EmployeeInfo(String e) {
		email = e;
	}
	public String getEmail() {
		return email;
	}
	public String getName() {
		return fullname;
	}
}
