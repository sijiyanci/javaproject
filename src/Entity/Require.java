package Entity;

public class Require extends Data{
    private String reqtype;
    private User user;
    public Require(){}
    public Require(String type,String reqtype,User user){
        super(type);
        this.reqtype=reqtype;
        this.user=user;
    }
    public Require(String type,String reqtype){
        super(type);
        this.reqtype=reqtype;
    }

    public String getReqtype(){
        return reqtype;
    }
    public User getUser(){
        return user;
    }
}