package Client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Entity.Require;
import Entity.Response;
import Entity.User;

public class SignUp extends JFrame{

	private static final long serialVersionUID = 1L;
	private JTextField userName; // 用户名
	private JPasswordField password; // 密码
	private JPasswordField password2; // 密码，输入两遍密码
	private JLabel lableUser;
	private JLabel lablePwd;
	private JLabel lablePwd2;
	private JButton btnRegister;
	
	//private Socket socket;
	
	public SignUp(Socket socket) {
		//this.socket = socket;
		
		Container con = this.getContentPane();
		// 用户号码登录输入框
		userName = new JTextField();
		userName.setBounds(200, 50, 250, 40);
		userName.setFont(new Font("宋体", Font.BOLD, 20));
		// 登录输入框旁边的文字
		lableUser = new JLabel("填写用户名");
		lableUser.setBounds(80, 50, 150, 40);
		lableUser.setFont(new Font("宋体", Font.BOLD, 20));
		// 密码输入框
		password = new JPasswordField();
		password.setBounds(200, 100, 250, 40);
		password.setFont(new Font("宋体", Font.BOLD, 20));
		password2 = new JPasswordField();
		password2.setBounds(200, 150, 250, 40);
		password2.setFont(new Font("宋体", Font.BOLD, 20));
		// 密码输入框旁边的文字
		lablePwd = new JLabel("填写密码");
		lablePwd.setBounds(100, 100, 100, 40);
		lablePwd.setFont(new Font("宋体", Font.BOLD, 20));
		lablePwd2 = new JLabel("重填密码");
		lablePwd2.setBounds(100, 150, 100, 40);
		lablePwd2.setFont(new Font("宋体", Font.BOLD, 20));
		// 注册按钮
		btnRegister = new JButton("注册");
		btnRegister.setBackground(Color.LIGHT_GRAY);
		btnRegister.setBounds(200, 220, 150, 50);
		btnRegister.setFont(new Font("宋体", Font.BOLD, 20));
		btnRegister.addActionListener(new Register(socket)
				/*new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = userName.getText();
				String pwd1 = String.valueOf(password.getPassword());
				String pwd2 = String.valueOf(password2.getPassword());
				//registerUser(name, pwd1, pwd2);
				if (pwd1.equals(pwd2)) {
					try {
						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						Require temp=new Require("Require","Signup",new User(name,pwd1));
						System.out.println("[reqtype : Signup, username : " + name +", password : "+pwd1+" ]");
						oos.writeObject(temp);
						ObjectInputStream ios = new ObjectInputStream(socket.getInputStream());
						Response receive=(Response)ios.readObject();
						if(receive.getState()==true) {
							System.out.println("you have enter the chatroom");
							JOptionPane.showMessageDialog(new JFrame(),
									"注册成功！\n请记住您的账号和密码", "恭喜", JOptionPane.CLOSED_OPTION);
						} else {
							JOptionPane.showMessageDialog(new JFrame(), "注册失败,换个用户名试试吧！", "错误",
									JOptionPane.ERROR_MESSAGE);
						}
					} catch (IOException | ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(new JFrame(), "两次输入密码不相同！", "错误",
							JOptionPane.ERROR_MESSAGE);
					password.setText("");
					password2.setText("");
					
				}
			}
		}*/);

		con.add(lableUser);
		con.add(lablePwd);
		con.add(lablePwd2);
		con.add(userName);
		con.add(password);
		con.add(password2);
		con.add(btnRegister);
		this.setTitle("注册窗口");// 设置窗口标题
		this.setBackground(Color.GRAY);
		this.setLayout(null);// 设置布局方式为绝对定位
		this.setBounds(0, 0, 550, 350);
		this.setResizable(false);// 窗体大小不能改变
		this.setLocationRelativeTo(null);// 居中显示
		this.setVisible(true);// 窗体可见
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	// 注册方法
	class Register implements ActionListener  {
		private Socket socket;
		
		public Register(Socket socket) {
			this.socket = socket;
		}
		public void actionPerformed(ActionEvent e) {
			String name = userName.getText();
			String pwd1 = String.valueOf(password.getPassword());
			String pwd2 = String.valueOf(password2.getPassword());
			if (pwd1.equals(pwd2)) {
				try {
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					Require temp=new Require("Require","Signup",new User(name,pwd1));
					System.out.println("[reqtype : Signup, username : " + name +", password : "+pwd1+" ]");
					oos.writeObject(temp);
					ObjectInputStream ios = new ObjectInputStream(socket.getInputStream());
					Response receive=(Response)ios.readObject();
					if(receive.getState()==true) {
						JOptionPane.showMessageDialog(new JFrame(),
								"注册成功！\n请记住您的账号和密码", "恭喜", JOptionPane.CLOSED_OPTION);
					} else {
						JOptionPane.showMessageDialog(new JFrame(), "注册失败,换个用户名试试吧！", "错误",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (IOException | ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(new JFrame(), "两次输入密码不相同！", "错误",
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
							"注册成功！\n请记住您的账号和密码", "恭喜", JOptionPane.CLOSED_OPTION);
				} else {
					JOptionPane.showMessageDialog(new JFrame(), "注册失败,换个用户名试试吧！", "错误",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (IOException | ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(new JFrame(), "两次输入密码不相同！", "错误",
					JOptionPane.ERROR_MESSAGE);
			password.setText("");
			password2.setText("");
			
		}
	}*/
}
