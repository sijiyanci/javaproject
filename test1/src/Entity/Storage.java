package Entity;

import java.io.*;
import java.util.ArrayList;

public class Storage {
	private String path;
	private ArrayList<String[]> contents;
	public Storage(String path) {
		this.path=path;
		contents=readFile();
	}
	public ArrayList<String[]> readFile() {
		ArrayList<String[]> contents=new ArrayList<String[]>();
		try {
			BufferedReader fin=new BufferedReader(new FileReader(path));
			
			String str;
			while((str=fin.readLine())!=null) {
				String[] user;
				user=str.split("&");
				contents.add(user);
			}
			fin.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return contents;
		
	}
	
	public void writeFile(ArrayList<String[]> dirtycontents) {
		try {
			BufferedWriter fout=new BufferedWriter(new FileWriter(path));
			

			for(String[] i:dirtycontents) {
				String str="";
				for(String j:i) {
					if(j.equals(i[0])) {
						str+=j;
					}else {
						str+="&"+j;
					}
				}
				fout.write(str+'\n');
			}
			
			fout.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public boolean addUser(User user,
	Checker<ArrayList<String[]>,User,Boolean> checker){
		String[] temp=new String[2];
	 	temp[0]=user.getUsername();
		temp[1]=user.getPassword();
		if(checker.check(contents,user)){
			contents.add(temp);
			sync();
			return true;
		}else{
			return false;
		}
	}

	public boolean checkUser(User user,
	Checker<ArrayList<String[]>,User,Boolean> checker){
		if(checker.check(contents,user)){
			return true;
		}else{
			return false;
		}
	}
	public void sync() {
		writeFile(contents);
	}
	public void removeUser(String username){

	}
	
	
}
