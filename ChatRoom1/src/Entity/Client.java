package Entity;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
	public static String username;
	public static Socket socket;

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
			Require temp=new Require("Require",reqtype,new User(username,password));
			oos.writeObject(temp);
			ObjectInputStream ios = new ObjectInputStream(socket.getInputStream());
			Response receive=(Response)ios.readObject();
			/*System.out.println(receive.toString());*/
			if(receive.getState()==true) {
				System.out.println("you have enter the chatroom");
				Client.username=receive.getUsername();
				Client.socket=socket;
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
							Data data=(Data)ois.readObject();
							
							System.out.println("");
							if(data.getType().equals("Response")) {
								Response resdata=(Response)data;
								//signin & signup 是处理其他人收到某人的登录注册成功信息，失败的话不会收到；
								if(resdata.getRestype().equals("Signin")||resdata.getRestype().equals("Signup")) {
									System.out.println(resdata.getUsername()+" enters this chatroom");
									System.out.println("userlist = "+Response.toString(resdata.getUserlist()));
								}else if(resdata.getRestype().equals("Quit")){
									if(resdata.getUsername().equals(Client.username)) {
										System.out.println("you quit");
										break;
									}else {
										System.out.println(resdata.getUsername()+" exit the chatroom");
									}
									
								}
							}else if(data.getType().equals("Message")) {
								Message mesdata=(Message)data;
								System.out.println(mesdata.getUserfrom()+" say : "+mesdata.getWords());
							}
							
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
								Require reqdata=new Require("Require","Quit",new User(Client.username,"uuu"));
								oos.writeObject(reqdata);
								
								//break;
							}else if(type.equals("Message")) {
								String userto,words;
								System.out.print("userto : ");
								userto=br.readLine();
								System.out.print("words : ");
								words=br.readLine();
								Message mesdata=new Message("Message",Client.username,userto,words);
								oos.writeObject(mesdata);
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
