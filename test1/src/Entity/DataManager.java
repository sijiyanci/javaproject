package Entity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.util.ArrayList;
import java.io.*;

import java.net.Socket;
//serverthread收到数据后，交给datamanager进行解析和打包，然后给回
//serverthread进行发�?�数�?

class DataMenager{
    public static ArrayList<ServerThread> serverthreads=new ArrayList<ServerThread>();


    public static JSONObject resolveData(ServerThread current,JSONObject data)throws Exception{
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
    private static ArrayList<String> getOnlineUser(){
    	ArrayList<String> usernamelist=new ArrayList<String>();
		for(int i=0;i<serverthreads.size();i++){
			usernamelist.add(serverthreads.get(i).getUsername());
		}
		return usernamelist;
    }
    private static JSONObject resPackage(String type,String reqtype,String username,boolean state
    		,ArrayList<String> usernamelist) {
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
    private static JSONObject middlePackage(ArrayList<Integer> indexlist,JSONObject data,String mode,JSONObject datahead) {
    	JSONObject newone=new JSONObject();
    	newone.put("Indexlist", indexlist);
    	newone.put("Data", data);
    	newone.put("Mode", mode);
    	newone.put("Datahead", datahead);
    	return newone;
    }
    
    private static JSONObject reqHandler(ServerThread current,JSONObject reqdata){
    	JSONObject newone=new JSONObject();
        ArrayList<Integer> indexlist=new ArrayList<Integer>();
        ArrayList<String> usernamelist=new ArrayList<String>();
        //System.out.println(reqdata.get("User").toString());

        User user=(User)JSONObject.toBean((JSONObject)reqdata.get("User"),User.class);

       // User user=(User)(reqdata.get("User"));
        if(((String)reqdata.get("Reqtype")).equals("Signup")){
        	
            if(Server.storagemanager.addUser(user)){
            	System.out.println(user.getUsername()+" client connects!");
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
            	System.out.println(user.getUsername()+" client connects!");
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
    	ArrayList<String> usernamelist=getOnlineUser();
    	if(way.equals("All")) {
    		for(int i=0;i<usernamelist.size();i++){
                if(!usernamelist.get(i).equals((String)mesdata.get("Userfrom"))) {
                	indexlist.add(i);
                }
            }
    	}else if(way.equals("Some")||way.equals("One")) {
    		JSONArray userto=(JSONArray)mesdata.get("Userto");
    		//for(int i=0;i<userto.length();i++) {
    		for(int i=0;i<userto.length();i++) {
    			for(int j=0;j<usernamelist.size();j++){
    				if(userto.get(i).equals(usernamelist.get(j)))
    					indexlist.add(j);
                }
    		}
//    		for(int i=0;i<usernamelist.size();i++)
//    			if(usernamelist.get(i).equals((String)mesdata.get("Userfrom"))){
//                    indexlist.add(i);
//                }
    	}
    	return indexlist;
    }
    
    private static JSONObject mesPackage(String type,String way,String texttype,String userfrom,ArrayList<String> usernamelist,String words) {
    	JSONObject resmes=new JSONObject();
    	resmes.put("Type", type);
    	resmes.put("Way", way);
    	resmes.put("Texttype", texttype);
    	resmes.put("Userfrom", userfrom);
    	resmes.put("Usernamelist", usernamelist);
    	resmes.put("Words", words);
    	return resmes;
    }
    private static JSONObject mesPackage(String type,String way,String texttype,String userfrom,ArrayList<String> usernamelist) {
    	JSONObject resmes=new JSONObject();
    	resmes.put("Type", type);
    	resmes.put("Way", way);
    	resmes.put("Texttype", texttype);
    	resmes.put("Userfrom", userfrom);
    	resmes.put("Usernamelist", usernamelist);
    	return resmes;
    }
    
    private static void anonymous_handle(JSONObject resmes) {
    	resmes.remove("Userfrom");
    	resmes.put("Userfrom","XXX");
    }
    
    private static JSONObject mesHandler(ServerThread current,JSONObject mesdata)throws Exception{
    	ArrayList<Integer> indexlist=mesWayResolve(mesdata);
    	JSONObject resmes;
    	JSONObject datahead=new JSONObject();
    	ArrayList<String> usernamelist=new ArrayList<String>();
    	for(int i:indexlist) {
    		usernamelist.add(serverthreads.get(i).getUsername());
    	}
    	usernamelist.add(current.getUsername());
    	if(mesdata.get("Texttype").equals("Words")) {
    		resmes=mesPackage("ClientMessage",(String)mesdata.get("Way"),
    				(String)mesdata.get("Texttype"),(String)mesdata.get("Userfrom"),usernamelist,(String)mesdata.getString("Words"));
    	}else {
    		datahead=receiveBytes(current);
    		
    		resmes=mesPackage("ClientMessage",(String)mesdata.get("Way"),
    				(String)mesdata.get("Texttype"),(String)mesdata.get("Userfrom"),usernamelist);
    	}
    	
        JSONArray fctype=(JSONArray)mesdata.get("Fctype");
       // for(int i=0;i<fctype.length();i++) {
        for(int i=0;i<fctype.length();i++) {
        	switch(fctype.getString(i)) {
        	case "Anonymous":
        		anonymous_handle(resmes);
        		break;
        	default:
        		System.out.println("no function");
        		break;
        }
        }
        if(mesdata.get("Texttype").equals("Words")) {
        	return middlePackage(indexlist,resmes,"o");
        }else {
        	return middlePackage(indexlist,resmes,"o",datahead);
        }
    }

    public static JSONObject serverReceive(ServerThread current) throws Exception{
		ObjectInputStream ois = new ObjectInputStream(current.getClient().getInputStream());
		String indata=(String)ois.readObject();
		JSONObject data=JSONObject.fromObject(indata);
		return data;
    }
    
    private static JSONObject receiveBytes(ServerThread current) throws Exception{
    	ObjectInputStream dis = new ObjectInputStream(current.getClient().getInputStream());
    	String filename = dis.readUTF();
        long filelength = dis.readLong();
        File file=new File("./src/cache/cache");
        BufferedOutputStream fos=new BufferedOutputStream(new FileOutputStream(file));
        byte[] bytes = new byte[1024];    
        long times = (filelength%1024==0)?filelength/1024:filelength/1024+1;
        while(times!=0) {
        	times--;
        	int length=dis.read(bytes, 0, bytes.length);
        	fos.write(bytes, 0, length);
        	fos.flush();
        	}  
        fos.close();
//        ObjectOutputStream ois = new ObjectOutputStream(current.getClient().getOutputStream());
//        ois.writeObject(new String("End"));
        
        JSONObject temp=new JSONObject();
        temp.put("Filename", filename);
        temp.put("Filelength", filelength);
        return temp;
    }
    
    private static void writeBytes(ServerThread other,JSONObject datahead) throws Exception{
    	ObjectOutputStream dos = new ObjectOutputStream(other.getClient().getOutputStream());
    	String filename=(String)datahead.get("Filename");
    	long filelength=datahead.getLong("Filelength");
    	File file=new File("./src/cache/cache");
    	BufferedInputStream fis=new BufferedInputStream(new FileInputStream(file));
    	byte[] bytes = new byte[1024];    
    	long times = (filelength%1024==0)?filelength/1024:filelength/1024+1;
        
        dos.writeUTF(filename);
        dos.flush();
        dos.writeLong(filelength);
        dos.flush();
        
        while(times!=0) {
        	times--;
        	int length=fis.read(bytes, 0, bytes.length);
        	dos.write(bytes, 0, length);
        	dos.flush();
        }
        fis.close();
    }
    
    @SuppressWarnings("unchecked")
	public static boolean serverWrite(ServerThread current,JSONObject packagedata) throws Exception{
		
            if(((String)packagedata.get("Mode")).equals("q")||((String)packagedata.get("Mode")).equals("oq")){
            	
                Socket temp=current.getClient();
                ObjectOutputStream oos=new ObjectOutputStream(temp.getOutputStream());
                oos.writeObject(packagedata.get("Data").toString());
               
            }
            
            if(((String)packagedata.get("Mode")).equals("o")||((String)packagedata.get("Mode")).equals("oq")){
            	//ArrayList<Integer> array=(ArrayList<Integer>)JSONObject.toBean(packagedata, ArrayList<Integer>.class)
            	JSONArray array=(JSONArray)(packagedata.get("Indexlist"));
            	//for(int i=0;i<array.length();i++) {
            	for(int i=0;i<array.length();i++) {
            		Socket temp=serverthreads.get(array.getInt(i)).getClient();
                    ObjectOutputStream oos=new ObjectOutputStream(temp.getOutputStream());
                     oos.writeObject(packagedata.get("Data").toString());
            	}
                
            }
            
            if(packagedata.has("Datahead")) {
            	JSONArray array=(JSONArray)(packagedata.get("Indexlist"));
            	//for(int i=0;i<array.length();i++) {
            	for(int i=0;i<array.length();i++) {
            		writeBytes(serverthreads.get(array.getInt(i)),(JSONObject)packagedata.getJSONObject("Datahead"));
            	}
            }
            
        String state=(String)packagedata.get("Mode");
		if(state.equals("q")||state.equals("oq"))
			return false;
		else if(state.equals("o"))
			return true;
		return true;
	}
    
    public static void forcedReturn(ServerThread current)throws Exception {
    	JSONObject resdata=resPackage("Response","Quit",current.getUsername()
                ,false,new ArrayList<String>());
                //Response temp=new Response("Response","Quit",reqdata.getUser().getUsername()
                //,true,userlist);
    	ArrayList<Integer> indexlist=new ArrayList<Integer>();
                for(int i=0;i<serverthreads.size();i++){
                    indexlist.add(i);
                }
        JSONObject packagedata = middlePackage(indexlist, resdata,"o");
        serverWrite(current,packagedata);
    }
}