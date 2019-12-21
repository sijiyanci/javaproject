package Entity;
import java.net.Socket;

public class ServerThread extends Thread{
	private Socket client;
	private String username;
	
	
	public ServerThread(Socket client) {
		this.client=client;
	} 
	public Socket getClient(){
		return client;
	}
	public String getUsername(){
		return username;
	}
	public void setUsername(String username){
		this.username=username;
	}

	public void run() {
		while(true) {
			try {
				Data indata=DataMenager.serverReceive(this);
				Package packagedata=DataMenager.resolveData(this, indata);
				if(!DataMenager.serverWrite(this, packagedata)){
					client.close();
					break;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	

	
	
}
