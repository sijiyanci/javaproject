package Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;



import Entity.Client;
import Entity.Require;
import Entity.Response;
import Entity.User;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

@SuppressWarnings("unused")
public class Login extends JFrame{
	private static final long serialVersionUID = -6256528270698337060L;
	private JTextField userName; // �û���
	private JPasswordField password; // ����
	private JLabel lableUser;
	private JLabel lablePwd;
	private JButton btnLogin; // ��ť
	private JButton btnRegister;
	private int wx, wy;
	private boolean isDraging = false;
	private JPanel contentPane;
	
	public static Socket socket;
	
	public static void main(String[] args) {
		Login frame = new Login();
		frame.setVisible(true);
	}

	public Login() {
		// �����ޱ�����
		setUndecorated(true);
		// ������� ȷ�������ܹ���ק
		addMouseListener((MouseListener) new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				isDraging = true;
				wx = e.getX();
				wy = e.getY();
			}
			public void mouseReleased(MouseEvent e) {
				isDraging = false;
			}
		});
		addMouseMotionListener((MouseMotionListener) new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (isDraging) {
					int left = getLocation().x;
					int top = getLocation().y;
					setLocation(left + e.getX() - wx, top + e.getY() - wy);
				}
			}
		});
		
		setBounds(100, 100, 439, 369);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		//contentPane.setBackground(Color.PINK);
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setResizable(false);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// �������		
		JLabel lblv = new JLabel("CHAT ROOM");
		lblv.setForeground(Color.WHITE);
		lblv.setBackground(Color.WHITE);
		lblv.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 58));
		lblv.setBounds(37, 60, 357, 80);
		contentPane.add(lblv);
		// �û��������
		userName = new JTextField("");
		userName.setBounds(170, 167, 219, 35);
		userName.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		userName.setBorder(null);
		contentPane.add(userName);
		// ��¼������Աߵ�����
		lableUser = new JLabel("�û���");
		lableUser.setBounds(37, 170, 126, 27);
		lableUser.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		lableUser.setForeground(Color.WHITE);
		contentPane.add(lableUser);
		// ���������
		password = new JPasswordField("");
		password.setBounds(170, 212, 219, 35);
		password.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 30));
		password.setBorder(null);
		contentPane.add(password);
		//  ����������Աߵ�����
		lablePwd = new JLabel("����");
		lablePwd.setBounds(37, 215, 126, 27);
		lablePwd.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		lablePwd.setForeground(Color.WHITE);
		contentPane.add(lablePwd);
		
		//��¼��ť
		btnLogin = new JButton("��¼");
		btnLogin.setBounds(225, 285, 170, 40);
		//btnLogin.setBackground(new Color(0xFFC0CB));
		btnLogin.setBackground(Color.LIGHT_GRAY);
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBorder(null);
		btnLogin.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		btnLogin.setPreferredSize(new Dimension(170, 40));
		btnLogin.setFocusPainted(false);
		contentPane.add(btnLogin);
		
		btnLogin.addActionListener( new Signin());
		
		//ע��
		btnRegister = new JButton("ע��");
		btnRegister.setBounds(37, 285, 170, 40);
		btnRegister.setFocusPainted(false);
		//btnRegister.setBackground(new Color(0xFFC0CB));
		btnRegister.setBackground(Color.LIGHT_GRAY);
		btnRegister.setForeground(Color.WHITE);
		btnRegister.setBorder(null);
		btnRegister.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		btnRegister.setPreferredSize(new Dimension(170, 40));
		contentPane.add(btnRegister);
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new SignUp();		//��ʾע�����
			}
		});
	}
	
	
	//----------------------------------------------------
	public static JSONObject reqPackage(String type,String reqtype,User user) {
		JSONObject reqdata=new JSONObject();
		reqdata.put("Type", type);
		reqdata.put("Reqtype", reqtype);
		reqdata.put("User", user);
		return reqdata;
	}
	class Signin implements ActionListener {
	/*	private Socket socket;
		public Signin(Socket socket) {
			this.socket = socket;
		}*/
		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent e) {
			
			String name = userName.getText();
			String pwd = String.valueOf(password.getPassword());
			// �����¼
			try {
				Socket socket_ = new Socket(InetAddress.getLocalHost().getHostName(), 9876);
				ObjectOutputStream oos = new ObjectOutputStream(socket_.getOutputStream());
				//Require temp=new Require("Require","Signin",new User(name,pwd));
				System.out.println("[reqtype : Signin, username : " + name +", password : "+pwd+" ]");
				JSONObject temp=reqPackage("Require","Signin",new User(name,pwd));
				
				oos.writeObject(temp.toString());
				ObjectInputStream ios = new ObjectInputStream(socket_.getInputStream());
				//Response receive=(Response)ios.readObject();
				String receive=(String)ios.readObject();
				JSONObject rdata=JSONObject.fromObject(receive);
				System.out.println(rdata.toString());
				
				if((Boolean)rdata.get("State")==true) {
					System.out.println("you have enter the chatroom");
					socket=socket_;
					//System.out.println("this");
					
					File file=new File("./src/"+name,name);
					File fileparent=file.getParentFile();
					if(!file.exists()) {
						System.out.println("exist");
						fileparent.mkdir();
					}
					//System.out.println("this");
					setVisible(false);
					
					JSONArray obj = (JSONArray)rdata.get("Usernamelist");
				    //System.out.println(obj.toString());
					ArrayList<String> userlist = new ArrayList<String>();
					for(int i=0;i<obj.length();i++)
						userlist.add(obj.getString(i));
					
					new ChatRoom(name,socket,userlist);
					
				} else {
					JOptionPane.showMessageDialog(new JFrame(), "�û������������", "����", JOptionPane.ERROR_MESSAGE);
				}
			} catch (IOException | ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
		}
		
	}
	
}
