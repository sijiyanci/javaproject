package Entity;

public class Guidata extends Data{
	
	private String jftype;		//��Ӧ�û���¼��ע��ʧ��

	public Guidata(String type,String jftype) {
		super(type);

		this.jftype=jftype;
	}
	public String getJftype() {
		return jftype;
	}

}
