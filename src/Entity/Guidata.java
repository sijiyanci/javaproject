package Entity;

public class Guidata extends Data{
	
	private String jftype;		//响应用户登录和注册失败

	public Guidata(String type,String jftype) {
		super(type);

		this.jftype=jftype;
	}
	public String getJftype() {
		return jftype;
	}

}
