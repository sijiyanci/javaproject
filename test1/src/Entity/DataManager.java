package Entity;

import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
//serverthread收到数据后，交给datamanager进行解析和打包，然后给回
//serverthread进行发�?�数�?

class DataMenager{
    public static ArrayList<ServerThread> serverthreads=new ArrayList<ServerThread>();


    public static Package resolveData(ServerThread current,Data data){
        Package newone=new Package();
        switch(data.getType()){
            case "Require":
                Require reqdata=(Require)data;
                newone=reqHandler(current,reqdata);
                break;
            case "Message":
                Message mesdata=(Message)data;
                newone=mesHandler(mesdata);
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
    
    private static Package reqHandler(ServerThread current,Require reqdata){
        Package newone=new Package();
        ArrayList<Integer> index=new ArrayList<Integer>();
        String[] userlist=new String[0];
        if(reqdata.getReqtype().equals("Signup")){
            if(Server.storagemanager.addUser(reqdata.getUser())){

                serverthreads.add(current);
                current.setUsername(reqdata.getUser().getUsername());

                userlist=getOnlineUser();
                Response temp=new Response("Response", "Signup", reqdata.getUser().getUsername()
                , true, userlist);
                for(int i=0;i<serverthreads.size();i++){
                    index.add(i);
                }
                newone = new Package(index, (Data)temp,"o");
            }else{
                Response temp=new Response("Response","Signup",reqdata.getUser().getUsername()
                ,false,userlist);
                newone = new Package(index,temp,"q");
            }
        }else if(reqdata.getReqtype().equals("Signin")){
            if(Server.storagemanager.checkUser(reqdata.getUser())){

                serverthreads.add(current);
                current.setUsername(reqdata.getUser().getUsername());

                userlist=getOnlineUser();
                Response temp=new Response("Response", "Signin", reqdata.getUser().getUsername()
                , true, userlist);
                for(int i=0;i<serverthreads.size();i++){
                    index.add(i);
                }
                newone = new Package(index, (Data)temp,"o");
            }else{
                Response temp=new Response("Response","Signin",reqdata.getUser().getUsername()
                ,false,userlist);
                newone = new Package(index,temp,"q");
            }
        }else if(reqdata.getReqtype().equals("Quit")){
            serverthreads.remove(current);
            Response temp=new Response("Response","Quit",reqdata.getUser().getUsername()
            ,true,userlist);
            for(int i=0;i<serverthreads.size();i++){
                index.add(i);
            }
            newone = new Package(index, (Data)temp,"oq");
        }

        return newone;
    }


    private static Package mesHandler(Message mesdata){
        ArrayList<Integer> index=new ArrayList<Integer>();
        if(mesdata.getUserto().equals("*")){
            for(int i=0;i<serverthreads.size();i++){
                index.add(i);
            }
        }else{
            for(ServerThread i:serverthreads){
                if(i.getUsername().equals(mesdata.getUserto())){
                    index.add(serverthreads.indexOf(i));
                }
            }
        }
        return new Package(index,(Data)mesdata,"o");
    }

    public static Data serverReceive(ServerThread current) throws Exception{
		ObjectInputStream ois = new ObjectInputStream(current.getClient().getInputStream());
		Data indata=(Data)ois.readObject();
		return indata;
    }
    
    public static boolean serverWrite(ServerThread current,Package packagedata) throws Exception{
		
            if(packagedata.getState().equals("q")||packagedata.getState().equals("oq")){
            	
                Socket temp=current.getClient();
                ObjectOutputStream oos=new ObjectOutputStream(temp.getOutputStream());
                oos.writeObject(packagedata.getData());
               
            }
            
            if(packagedata.getState().equals("o")||packagedata.getState().equals("oq")){
                for(Integer i:packagedata.getIndex()){
                	
                    Socket temp=serverthreads.get(i.intValue()).getClient();
                    ObjectOutputStream oos=new ObjectOutputStream(temp.getOutputStream());
                    oos.writeObject(packagedata.getData());
                }
                
            }
            
        String state=packagedata.getState();
		if(state.equals("q")||state.equals("oq"))
			return false;
		else if(state.equals("o"))
			return true;
		return true;
	}
}