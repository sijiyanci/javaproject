package Entity;

import java.io.*;
import java.util.ArrayList;
public class Storage {
	private String path;
	
	public Storage(String path) {
		this.path=path;
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
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return contents;
		
	}
	
	public void writeFile(ArrayList<String[]> dirtycontents) {
		try {
			BufferedWriter fout=new BufferedWriter(new FileWriter(path));
			String str="";

			for(String[] i:dirtycontents) {
				for(String j:i) {
					if(j.equals(i[0])) {
						str+=j;
					}else {
						str+="&"+j;
					}
				}
			}
			fout.write(str+'\n');
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
