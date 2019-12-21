package Entity;
import java.util.ArrayList;
public class StorageManager {

	private Storage storage;
	public StorageManager(String path) {
		storage=new Storage(path);

	}

	public boolean addUser(User user){
		return storage.addUser(user, 
		(Checker<ArrayList<String[]>,User,Boolean>)(c,u)->{
			for(String[] i:c){
				if(u.getUsername().equals(i[0])){
					return false;
				}
			}
			return true;
		});
	}

	public boolean checkUser(User user){
		return storage.checkUser(user, 
		(Checker<ArrayList<String[]>,User,Boolean>)(c,u)->{
			for(String[] i:c){
				if(u.getUsername().equals(i[0])&&u.getPassword().equals(i[1])){
					return true;
				}
			}
			return false;
		});
	}

}
