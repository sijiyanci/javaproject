package Entity;

public class Require extends Data{
    private String reqtype;
    private String username;
    private String password;

    public Require(String type,String reqtype,String username,String password){
        super(type);
        this.reqtype=reqtype;
        this.username=username;
        this.password=password;
    }
    public Require(String type,String reqtype,String username){
        super(type);
        this.reqtype=reqtype;
        this.username=username;
    }

    public String getReqtype(){
        return reqtype;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
}