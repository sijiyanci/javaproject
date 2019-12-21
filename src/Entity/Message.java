package Entity;

public class Message extends Data{
	private String words;
	private String userfrom;
	private String userto;

	public Message(){}
	public Message(String type,String userfrom,String userto,String words) {
		super(type);
		this.words=words;
		this.userfrom=userfrom;
		this.userto=userto;
	}
	
	public String getWords() {
		return words;
	}

	public String getUserfrom() {
		return this.userfrom;
	}
	public String getUserto() {
		return this.userto;
	}

}
