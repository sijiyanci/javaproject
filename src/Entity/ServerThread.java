package Entity;
import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
public class ServerThread extends Thread{
	private Socket client;
	private static ArrayList<Socket> serversockets=new ArrayList<Socket>();
	
	public ServerThread(Socket client) {
		this.client=client;
	}
	public void run() {
		while(true) {
			try {
				Data indata=serverReceive();
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Data serverReceive() throws Exception{
		ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
		Data indata=(Data)ois.readObject();
		return indata;
	}
	
}
