package Entity;
import net.sf.json.JSONObject;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
//serverthread收到数据后，交给datamanager进行解析和打包，然后给回
//serverthread进行发�?�数�?

class DataMenager{
    public static ArrayList<ServerThread> serverthreads=new ArrayList<ServerThread>();


    public static JSONObject resolveData(ServerThread current,JSONObject data){
    	JSONObject newone=new JSONObject();
        switch((String)data.get("Type")){
            case "Require":
                //Require reqdata=(Require)data;
                newone=reqHandler(current,data);
                break;
            case "ServerMessage":
                //Message mesdata=(Message)data;
                newone=mesHandler(current,data);
                break;
            default:System.out.println("no type");
            	break;
        }
        return newone;
    }
    private static String[] getOnlineUser(){
		String[] userlist=new String[serverthreads.size()];
		for(int i=0;i<serverthreads.size();i++){
			userlist[i]=serverthreads.get(i).getUsername();
		}
		return userlist;
    }
    private static JSONObject resPackage(String type,String reqtype,String username,boolean state
    		,String[] usernamelist) {
    	JSONObject resdata=new JSONObject();
    	resdata.put("Type", type);
    	resdata.put("Reqtype", reqtype);
    	resdata.put("Username", username);
    	resdata.put("State", state);
    	resdata.put("Usernamelist", usernamelist);
    	return resdata;
    }
    private static JSONObject middlePackage(ArrayList<Integer> indexlist,JSONObject data,String mode) {
    	JSONObject newone=new JSONObject();
    	newone.put("Indexlist", indexlist);
    	newone.put("Data", data);
    	newone.put("Mode", mode);
    	return newone;
    }
    private static JSONObject reqHandler(ServerThread current,JSONObject reqdata){
    	JSONObject newone=new JSONObject();
        ArrayList<Integer> indexlist=new ArrayList<Integer>();
        String[] usernamelist=new String[0];
        User user=(User)reqdata.get("User");
        if(((String)reqdata.get("Reqtype")).equals("Signup")){
        	
            if(Server.storagemanager.addUser(user)){

                serverthreads.add(current);
                current.setUsername(user.getUsername());

                usernamelist=getOnlineUser();
                JSONObject resdata=resPackage("Response", "Signup", user.getUsername()
                , true, usernamelist);
                //Response temp=new Response("Response", "Signup", reqdata.getUser().getUsername()
                //, true, userlist);
                for(int i=0;i<serverthreads.size();i++){
                    indexlist.add(i);
                }
                newone = middlePackage(indexlist, resdata,"o");
            }else{
            	JSONObject resdata=resPackage("Response","Signup",user.getUsername()
                ,false,usernamelist);
                //Response temp=new Response("Response","Signup",reqdata.getUser().getUsername()
                //,false,userlist);
            	newone = middlePackage(indexlist,resdata,"q");
            }
        }else if(((String)reqdata.get("Reqtype")).equals("Signin")){
        	
            if(Server.storagemanager.checkUser(user)){

                serverthreads.add(current);
                current.setUsername(user.getUsername());

                usernamelist=getOnlineUser();
                JSONObject resdata=resPackage("Response", "Signin", user.getUsername()
                , true, usernamelist);
                //Response temp=new Response("Response", "Signin", reqdata.getUser().getUsername()
                //, true, userlist);
                for(int i=0;i<serverthreads.size();i++){
                    indexlist.add(i);
                }
                newone = middlePackage(indexlist, resdata,"o");
            }else{
            	JSONObject resdata=resPackage("Response","Signin",user.getUsername()
                ,false,usernamelist);
              //  Response temp=new Response("Response","Signin",reqdata.getUser().getUsername()
              //  ,false,userlist);
                newone = middlePackage(indexlist,resdata,"q");
            }
        }else if(((String)reqdata.get("Reqtype")).equals("Quit")){
            serverthreads.remove(current);
            JSONObject resdata=resPackage("Response","Quit",user.getUsername()
            ,true,usernamelist);
            //Response temp=new Response("Response","Quit",reqdata.getUser().getUsername()
            //,true,userlist);
            for(int i=0;i<serverthreads.size();i++){
                indexlist.add(i);
            }
            newone = middlePackage(indexlist, resdata,"oq");
        }

        return newone;
    }

    private static ArrayList<Integer> mesWayResolve(JSONObject mesdata){
    	ArrayList<Integer> indexlist=new ArrayList<Integer>();
    	String way=(String)mesdata.get("Way");
    	String[] usernamelist=getOnlineUser();
    	if(way.equals("All")) {
    		for(int i=0;i<usernamelist.length;i++){
                indexlist.add(i);
            }
    	}else if(way.equals("Some")) {
    		String[] userto=(String[])mesdata.get("Userto");
    		for(String i:userto) {
    			for(int j=0;j<usernamelist.length;j++){
    				if(i.equals(usernamelist[j]))
    					indexlist.add(j);
                }
    		}
    		for(int i=0;i<usernamelist.length;i++)
    			if(usernamelist[i].equals((String)mesdata.get("Userfrom"))){
                    indexlist.add(i);
                }
    	}else if(way.equals("One")) {
    		for(int i=0;i<usernamelist.length;i++){
                if(usernamelist[i].equals((String)mesdata.get("Userfrom"))){
                    indexlist.add(i);
                }
                if(usernamelist[i].equals((String)mesdata.get("Userto"))){
                    indexlist.add(i);
                }
            }
    	}
    	return indexlist;
    }
    private static JSONObject mesPackage(String texttype,String userfrom,JSONObject data) {
    	JSONObject resmes=new JSONObject();
    	resmes.put("Texttype", texttype);
    	resmes.put("Userform", userfrom);
    	resmes.put("Data", data);
    	return resmes;
    }
    private static void anonymous_handle(JSONObject resmes) {
    	resmes.remove("Userfrom");
    	resmes.put("Userfrom","XXX");
    }
    private static JSONObject mesHandler(ServerThread current,JSONObject mesdata){
        ArrayList<Integer> indexlist=mesWayResolve(mesdata);
        JSONObject resmes=mesPackage((String)mesdata.get("Texttype"),(String)mesdata.get("Userfrom"),
        		(JSONObject)mesdata.get("Data"));
        switch((String)mesdata.get("Fctype")) {
        	case "Anonymous":
        		anonymous_handle(resmes);
        		break;
        	default:
        		System.out.println("no function");
        		break;
        }
        
        return middlePackage(indexlist,resmes,"o");
    }

    public static JSONObject serverReceive(ServerThread current) throws Exception{
		ObjectInputStream ois = new ObjectInputStream(current.getClient().getInputStream());
		String indata=(String)ois.readObject();
		JSONObject data=JSONObject.fromObject(indata);
		return data;
    }
    
    @SuppressWarnings("unchecked")
	public static boolean serverWrite(ServerThread current,JSONObject packagedata) throws Exception{
		
            if(((String)packagedata.get("Mode")).equals("q")||((String)packagedata.get("Mode")).equals("oq")){
            	
                Socket temp=current.getClient();
                ObjectOutputStream oos=new ObjectOutputStream(temp.getOutputStream());
                oos.writeObject(packagedata.get("Data").toString());
               
            }
            
            if(((String)packagedata.get("Mode")).equals("o")||((String)packagedata.get("Mode")).equals("oq")){
                for(Integer i:((ArrayList<Integer>)packagedata.get("Indexlist"))){
                	
                    Socket temp=serverthreads.get(i.intValue()).getClient();
                    ObjectOutputStream oos=new ObjectOutputStream(temp.getOutputStream());
                    oos.writeObject(packagedata.get("Data").toString());
                }
                
            }
            
        String state=(String)packagedata.get("Mode");
		if(state.equals("q")||state.equals("oq"))
			return false;
		else if(state.equals("o"))
			return true;
		return true;
	}
}