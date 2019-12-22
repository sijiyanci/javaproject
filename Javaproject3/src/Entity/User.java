package Entity;

class User implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
    private String password;

    public User(){}
    public User(String username,String password){
        this.username=username;
        this.password=password;
    }

    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String toString() {
    	return "[User : username = "+username+", password = "+password+"]";
    }
}