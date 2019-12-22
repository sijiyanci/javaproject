package Entity;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
public class Server {
	public static int port=9876;
	public static ServerSocket server;
	public static StorageManager storagemanager=new StorageManager(Path.path);
	public static void main(String[] arg) {
		System.out.println("Chatroom Server:");
		try {
			System.out.println("Initialize port...");
			server=new ServerSocket(port);
			System.out.println("Listening at port "+port+"...");
			while(true) {
				Socket client=server.accept();
				System.out.println("a client is trying to connect...");
				ServerThread newone=new ServerThread(client);
				newone.start();
			}
			
			
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
}
