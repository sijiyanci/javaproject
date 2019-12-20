package Entity;

public class Guidata extends Data{
	
	private String jftype;		//表示登录或注册失败的回应

	public Guidata(String type,String jftype) {
		super(type);

		this.jftype=jftype;
	}
	public String getJftype() {
		return jftype;
	}

}
