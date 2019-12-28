package Entity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
	public static String username;
	public static Socket socket;
	public static JSONObject reqPackage(String type,String reqtype,User user) {
		JSONObject reqdata=new JSONObject();
		reqdata.put("Type", type);
		reqdata.put("Reqtype", reqtype);
		reqdata.put("User", user);
		return reqdata;
	}
	public static JSONObject mesPackage(String type,String way,ArrayList<String> fctype,String texttype
			,String userfrom,ArrayList<String> userto,String words) {
		JSONObject mesdata=new JSONObject();
		mesdata.put("Type", type);
		mesdata.put("Way", way);
		mesdata.put("Fctype", fctype);
		mesdata.put("Texttype", texttype);
		mesdata.put("Userfrom", userfrom);
		mesdata.put("Userto", userto);
		mesdata.put("Words", words);
		return mesdata;
	}
	public static JSONObject mesPackage(String type,String way,ArrayList<String> fctype,String texttype
			,String userfrom,ArrayList<String> userto) {
		JSONObject mesdata=new JSONObject();
		mesdata.put("Type", type);
		mesdata.put("Way", way);
		mesdata.put("Fctype", fctype);
		mesdata.put("Texttype", texttype);
		mesdata.put("Userfrom", userfrom);
		mesdata.put("Userto", userto);
		return mesdata;
	}
	private static JSONObject receiveBytes(Socket server,String texttype) throws Exception{
		ObjectInputStream dis = new ObjectInputStream(server.getInputStream());
    	String filename = dis.readUTF();
        long filelength = dis.readLong();
        File file=new File("./src/"+Client.username+"/"+filename+"."+texttype.toLowerCase());
        BufferedOutputStream fos=new BufferedOutputStream(new FileOutputStream(file,true));
        byte[] bytes = new byte[1024];    
        long times = (filelength%1024==0)?filelength/1024:filelength/1024+1;
        while(times!=0) {
        	System.out.println(times);
        	times--;
        	
        	int length=dis.read(bytes, 0, bytes.length);
        	fos.write(bytes, 0, length);

        	fos.flush();
        	}  
        fos.close();
        JSONObject temp=new JSONObject();
        temp.put("Filename", filename);
        temp.put("Filelength", filelength);

        System.out.print(filename+" ");
        System.out.println(filelength);
        return temp;
    }
	private static void writeBytes(Socket server,String texttype) throws Exception{
		ObjectOutputStream dos = new ObjectOutputStream(server.getOutputStream());
    	File file=new File("./src/user1/user1."+texttype.toLowerCase());
    	String filename=file.getName();
    	long filelength=file.length();
    	BufferedInputStream fis=new BufferedInputStream(new FileInputStream(file));
    	byte[] bytes = new byte[1024];    
        long times = (filelength%1024==0)?filelength/1024:filelength/1024+1;;
        
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
        
        
        
        System.out.println(new String(bytes));
        System.out.print(filename+" ");
        System.out.println(filelength);
        fis.close();
    }
	public static void main(String[] arg) throws Exception{
		
		System.out.println("start");
		while(true) {
			Socket socket = new Socket(InetAddress.getLocalHost().getHostName(), 9876);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String reqtype,username,password;
			System.out.println("[");
			System.out.print("reqtype : ");
			reqtype=br.readLine();
			System.out.print("username : ");
			username=br.readLine();
			System.out.print("password : ");
			password=br.readLine();
			System.out.println("]");
			//Require temp=new Require("Require",reqtype,new User(username,password));
			JSONObject temp=reqPackage("Require",reqtype,new User(username,password));

			oos.writeObject(temp.toString());
			ObjectInputStream ios = new ObjectInputStream(socket.getInputStream());
			String receive=(String)ios.readObject();
			JSONObject rdata=JSONObject.fromObject(receive);
			System.out.println(rdata.toString());
			if((Boolean)rdata.get("State")==true) {
				System.out.println("you have enter the chatroom");
				Client.username=(String)rdata.get("Username");
				Client.socket=socket;
				
				File file=new File("./src/"+Client.username,Client.username);
				File fileparent=file.getParentFile();
				if(!file.exists()) {
					System.out.println("exist");
					fileparent.mkdir();
				}

				break;
			}else {
				System.out.println("your username or password error");
			}
		}
		
			new Thread(new Runnable() {
				public void run() {
					while(true) {
						try {
							ObjectInputStream ois=new ObjectInputStream(Client.socket.getInputStream());
							String datastr=(String)ois.readObject();
							JSONObject data=JSONObject.fromObject(datastr);
							System.out.println("");
							if(data.get("Type").equals("Response")) {
								//Response resdata=(Response)data;
								//signin & signup 是处理其他人收到某人的登录注册成功信息，失败的话不会收到；
								if(data.get("Reqtype").equals("Signin")||data.get("Reqtype").equals("Signup")) {
									System.out.println(data.get("Username")+" enters this chatroom");
									JSONArray usernamelist=(JSONArray)data.get("Usernamelist");
									System.out.println("usernamelist = "+usernamelist.toString());//！！！
								
								}else if(data.get("Reqtype").equals("Quit")){
									if(data.get("Username").equals(Client.username)) {
										System.out.println("you quit");
										break;
									}else {
										System.out.println(data.get("Username")+" exit the chatroom");
									}
									
								}
							}else if(data.get("Type").equals("ClientMessage")) {
								//Message mesdata=(Message)data;
								if(data.get("Texttype").equals("Words")) {
									System.out.println(data.get("Userfrom")+" say : "+data.get("Words"));
								}else {
									receiveBytes(Client.socket,(String)data.get("Texttype"));
								}
								
							}
							
							System.out.println("[");
							System.out.print("type : ");
							//socket.shutdownInput();
							//ois.close();
						}catch(Exception e) {
							e.printStackTrace();
							break;
						}
					}
					
				}
			}).start();
			new Thread(new Runnable() {
				public void run() {
					while(true) {
						try {
							ObjectOutputStream oos = new ObjectOutputStream(Client.socket.getOutputStream());
							BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
							System.out.println("[");
							String type;
							System.out.print("type : ");
							type=br.readLine();
							if(type.equals("Require")) {
								//Require reqdata=new Require("Require","Quit",new User(Client.username,"uuu"));
								JSONObject reqdata=reqPackage("Require","Quit",new User(Client.username,"***"));
								oos.writeObject(reqdata.toString());
								
								break;
							}else if(type.equals("ServerMessage")) {
								String way,texttype="Words";
								String words;
								ArrayList<String> fctype=new ArrayList<String>();
								ArrayList<String> userto=new ArrayList<String>();
								System.out.print("way : ");
								way=br.readLine();
								System.out.println("fctype(end with !) : ");
								String temp;
								while(!(temp=br.readLine()).equals("!")) {
									fctype.add(temp);
									System.out.print("    ");
								}
								
								System.out.print("texttype : ");
								texttype=br.readLine();
								System.out.println("userfrom : "+Client.username);
								
								if(way.equals("Some")) {
									System.out.println("usertos(for Some,end with !) : ");
									
									System.out.print("    ");
									while(!(temp=br.readLine()).equals("!")) {
										userto.add(temp);
										System.out.print("    ");
									}
								}else if(way.equals("One")){
									System.out.print("userto(for One) : ");
									
									temp=br.readLine();
									userto.add(temp);
								}
								System.out.print("data : ");
								JSONObject mesdata;
								if(texttype.equals("Words")) {
									words=br.readLine();
									mesdata=mesPackage("ServerMessage",way,fctype,texttype,Client.username,userto,words);
								}else {
									mesdata=mesPackage("ServerMessage",way,fctype,texttype,Client.username,userto);
								}
								oos.writeObject(mesdata.toString());
								if(!texttype.equals("Words")) {
									writeBytes(Client.socket,texttype);
								}
							}
							System.out.println("]");
							
							
							//socket.shutdownOutput();
							//oos.close();
						}catch(Exception e) {
							e.printStackTrace();
							break;
						}
					}
				}
			}).start();
		
//		while(true) {
//			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//			
//			String username,password;
//			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//			username=br.readLine();
//			password=br.readLine();
//			Require temp=new Require("Require","Signin",new User(username,password));
//			oos.writeObject(temp);															//Require
//			/*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//			String data;
//			data=br.readLine();
//			Data temp=new Data(data);
//			oos.writeObject(temp);*/															//Data
//			/*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//			String userfrom,userto,words;
//			userfrom=br.readLine();
//			userto=br.readLine();
//			words=br.readLine();
//			Message temp=new Message("Message",userfrom,userto,words);
//			oos.writeObject(temp);	*/														//Message
//		}
	}
}
