package Entity;

public class Data implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	public Data(){}
	public Data(String type) {
		this.type=type;
	}
	public String getType() {
		return type;
	}
	public String toString() {
		return "[Data: type = "+type+"]";
	}
}
