package Client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Entity.Client;
import Entity.Require;
import Entity.Response;
import Entity.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SignUp extends JFrame{

	private static final long serialVersionUID = 1L;
	private JTextField userName; // �û���
	private JPasswordField password; // ����
	private JPasswordField password2; // ���룬������������
	private JLabel lableUser;
	private JLabel lablePwd;
	private JLabel lablePwd2;
	private JButton btnRegister;
	
	private Socket socket;
	
	public SignUp() {
		
		Container con = this.getContentPane();
		// �û������¼�����
		userName = new JTextField();
		userName.setBounds(200, 50, 250, 40);
		userName.setFont(new Font("����", Font.BOLD, 20));
		// ��¼������Աߵ�����
		lableUser = new JLabel("��д�û���");
		lableUser.setBounds(80, 50, 150, 40);
		lableUser.setFont(new Font("����", Font.BOLD, 20));
		// ���������
		password = new JPasswordField();
		password.setBounds(200, 100, 250, 40);
		password.setFont(new Font("����", Font.BOLD, 20));
		password2 = new JPasswordField();
		password2.setBounds(200, 150, 250, 40);
		password2.setFont(new Font("����", Font.BOLD, 20));
		// ����������Աߵ�����
		lablePwd = new JLabel("��д����");
		lablePwd.setBounds(100, 100, 100, 40);
		lablePwd.setFont(new Font("����", Font.BOLD, 20));
		lablePwd2 = new JLabel("��������");
		lablePwd2.setBounds(100, 150, 100, 40);
		lablePwd2.setFont(new Font("����", Font.BOLD, 20));
		// ע�ᰴť
		btnRegister = new JButton("ע��");
		btnRegister.setBackground(Color.LIGHT_GRAY);
		btnRegister.setBounds(200, 220, 150, 50);
		btnRegister.setFont(new Font("����", Font.BOLD, 20));
		
		btnRegister.addActionListener(new Register());

		con.add(lableUser);
		con.add(lablePwd);
		con.add(lablePwd2);
		con.add(userName);
		con.add(password);
		con.add(password2);
		con.add(btnRegister);
		this.setTitle("ע�ᴰ��");// ���ô��ڱ���
		this.setBackground(Color.GRAY);
		this.setLayout(null);// ���ò��ַ�ʽΪ���Զ�λ
		this.setBounds(0, 0, 550, 350);
		this.setResizable(false);// �����С���ܸı�
		this.setLocationRelativeTo(null);// ������ʾ
		this.setVisible(true);// ����ɼ�
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}
	
//-----------------------------------------
	public static JSONObject reqPackage(String type,String reqtype,User user) {
		JSONObject reqdata=new JSONObject();
		reqdata.put("Type", type);
		reqdata.put("Reqtype", reqtype);
		reqdata.put("User", user);
		return reqdata;
	}
	
	// ע�᷽��
	class Register implements ActionListener  {
	/*	private Socket socket;
		
		public Register(Socket socket) {
			this.socket = socket;
		}*/
		public void actionPerformed(ActionEvent e) {
			String name = userName.getText();
			String pwd1 = String.valueOf(password.getPassword());
			String pwd2 = String.valueOf(password2.getPassword());
			if (pwd1.equals(pwd2)) {
				try {
					Socket socket_ = new Socket(InetAddress.getLocalHost().getHostName(), 9876);
					ObjectOutputStream oos = new ObjectOutputStream(socket_.getOutputStream());
					//Require temp=new Require("Require","Signup",new User(name,pwd1));
					System.out.println("[reqtype : Signup, username : " + name +", password : "+pwd1+" ]");
					JSONObject temp=reqPackage("Require","Signup",new User(name,pwd1));
		
					oos.writeObject(temp.toString());
					ObjectInputStream ios = new ObjectInputStream(socket_.getInputStream());
					//Response receive=(Response)ios.readObject();
					String receive=(String)ios.readObject();
					JSONObject rdata=JSONObject.fromObject(receive);
					System.out.println(rdata.toString());
					
					if((Boolean)rdata.get("State")==true) {
						JOptionPane.showMessageDialog(new JFrame(),
								"ע��ɹ���\n���ס�����˺ź�����", "��ϲ", JOptionPane.CLOSED_OPTION);
						socket = socket_;
						
						File file=new File("./src/"+name,name);
						File fileparent=file.getParentFile();
						if(!file.exists()) {
							System.out.println("exist");
							fileparent.mkdir();
						}
						
						setVisible(false);
						
						JSONArray obj = (JSONArray)rdata.get("Usernamelist");
					    System.out.println(obj.toString());
						ArrayList<String> userlist = new ArrayList<String>();
						for(int i=0;i<obj.length();i++)
							userlist.add(obj.getString(i));
						
						new ChatRoom(name,socket,userlist);
					} else {
						JOptionPane.showMessageDialog(new JFrame(), "ע��ʧ��,�����û������԰ɣ�", "����",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (IOException | ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(new JFrame(), "�����������벻��ͬ��", "����",
						JOptionPane.ERROR_MESSAGE);
				password.setText("");
				password2.setText("");
				
			}
		}
	}
	/*
	private void registerUser(Socket socket, String name, String pwd1, String pwd2) {
		if (pwd1.equals(pwd2)) {
			try {
				ObjectOutputStream oos = new ObjectOutputStream(SignUp.socket.getOutputStream());
				Require temp=new Require("Require","Signup",new User(name,pwd1));
				System.out.println("[reqtype : Signup, username : " + name +", password : "+pwd1+" ]");
				oos.writeObject(temp);
				ObjectInputStream ios = new ObjectInputStream(Login.socket.getInputStream());
				Response receive=(Response)ios.readObject();
				if(receive.getState()==true) {
					System.out.println("you have enter the chatroom");
					JOptionPane.showMessageDialog(new JFrame(),
							"ע��ɹ���\n���ס�����˺ź�����", "��ϲ", JOptionPane.CLOSED_OPTION);
				} else {
					JOptionPane.showMessageDialog(new JFrame(), "ע��ʧ��,�����û������԰ɣ�", "����",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (IOException | ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(new JFrame(), "�����������벻��ͬ��", "����",
					JOptionPane.ERROR_MESSAGE);
			password.setText("");
			password2.setText("");
			
		}
	}*/
}
