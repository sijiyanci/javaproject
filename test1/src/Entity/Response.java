package Entity;

public class Response extends Data{
    private String restype;
    private String username;
    private boolean state;
    private String[] userlist;
    
    public Response(){}
    public Response(String type,String restype,String username
        ,boolean state,String[] userlist){
            super(type);
            this.restype=restype;
            this.username=username;
            this.state=state;
            this.userlist=userlist;
    }
    
    public String getRestype(){
        return restype;
    }
    public String getUsername(){
        return username;
    }
    public boolean getState(){
        return state;
    }
    public String[] getUserlist(){
        return userlist;
    }
    public static String toString(String[] strarray) {
    	String str="";
    	for(int i=0;i<strarray.length;i++) {
    		if(i==0)
    			str+=strarray[i];
    		else
    			str+=", "+strarray[i];
    	}
    	return "["+str+"]";
    }
    public String toString() {
    	String str="[Response : Data = "+super.toString()+", restype = "+restype+", username = "+username+
    			", state = "+state+", userlist = "+toString(userlist)+"]";
    	return str;
    }
}