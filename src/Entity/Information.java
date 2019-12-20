package Entity;

public class Information extends Data{
	private String username;
	private String password;
	
	public Information(String type,String username,String password) {
		super(type);
		this.username=username;
		this.password=password;
	}
	
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
}
