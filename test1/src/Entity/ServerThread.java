package Entity;
import java.io.ObjectInputStream;
import java.net.Socket;
import net.sf.json.JSONObject;
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
	
	public void run(){
		while(true) {
			try {
				/*ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
				Data indata=(Data)ois.readObject();*/			//Data

				/*Data indata=DataMenager.serverReceive(this);
				if(indata.getType().equals("Require")) {
					Require re=(Require)indata;
					System.out.println(re.getReqtype()+" "+re.getUser().getUsername()+" "+re.getUser().getPassword());
				}else if(indata.getType().equals("Message")) {
					Message me=(Message)indata;
					System.out.println(me.getUserfrom()+" "+me.getUserto()+" "+me.getWords());
				}*/												//Require Message
				
				JSONObject indata=DataMenager.serverReceive(this);
				System.out.println(indata.toString());
				JSONObject packagedata=DataMenager.resolveData(this, indata);
				System.out.println(packagedata.toString());
				//System.out.println(packagedata.getIndex().toString()+" "+packagedata.getState()+" "+packagedata.getData().getType());
				if(!DataMenager.serverWrite(this, packagedata)){
					System.out.println(this.username+" client disconnects!");
					client.close();
					break;
				}
			}catch(Exception e) {
				//e.printStackTrace();
				System.out.println(this.username+" client is forced return!");
				DataMenager.serverthreads.remove(this);
				try {
					DataMenager.forcedReturn(this);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				break;
			}
		}
	}
	

	
	
}
