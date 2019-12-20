package Entity;

public class Message extends Data{
	private String mtype;
	private String words;
	private String userfrom;
	private String userto;
	private String[] userlist;
	public Message(String type,String mtype,String userfrom) {
		super(type);
		this.mtype=mtype;
		this.userfrom=userfrom;
	}
	public Message(String type,String mtype,String userfrom,String[] userlist) {		//signin
		super(type);
		this.mtype=mtype;
		this.userlist=userlist;
		this.userfrom=userfrom;
	}
	public Message(String type,String mtype,String userfrom,String words) {			//正常和匿名
		super(type);
		this.mtype=mtype;
		this.words=words;
		this.userfrom=userfrom;
	}
	public Message(String type,String mtype,String userfrom,String userto,String words) {
		super(type);
		this.mtype=mtype;
		this.words=words;
		this.userfrom=userfrom;
		this.userto=userto;
	}
	
	public String getWords() {
		return words;
	}
	public String getMtype() {
		return this.mtype;
	}
	public String getUserfrom() {
		return this.userfrom;
	}
	public String getUserto() {
		return this.userto;
	}
	public String[] getUserlist() {
		return this.userlist;
	}
}
