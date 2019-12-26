package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import Entity.Data;
import Entity.Message;
import Entity.Require;
import Entity.Response;
import Entity.User;

public class ChatRoom {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 6704231622520334518L;
	
	private JFrame frame;
	//private JTextPane text_show;		//��ʾ��������
	private JTextArea recordPane;
	private JTextField txt_msg;
	private JLabel info_name;
	private JLabel info_ip;
	private JButton btn_send;
	private JButton btn_pic;
	private JPanel northPanel;		
	private JPanel southPanel;
	private JScrollPane rightScroll;		//������
	private JScrollPane leftScroll;
	private JSplitPane centerSplit;			//��ֳ���������
	private JLabel lableTo;
	private JComboBox<String> comboBox;		//�����б�
	private SimpleAttributeSet attrset;
	
	private DefaultListModel<String> listModel;		//��̬��ʾ
	private JList<String> userList;

	private Socket socket;
	private String[] userlist;
	//private static FileInputStream doc_read; 		// �������ļ�
	//private static FileOutputStream fos; 		// д�����ļ�
	private MessageThread messageThread;		// ���������Ϣ���߳�
	//private Map<String, User_> onLineUsers = new HashMap<String, User_>();// ���������û�
	private Map<String, String> onLineUsers = new HashMap<String, String>();
	private boolean isConnected = false;
	private String name;
	//private String pic_path = null;
	private String UserValue = "";
	private int info_ip_ = 0;
	private int flag = 0;
	//private Gson mGson;
	//private boolean file_is_create = true;

/*	public static void main(String[] args) {
		new ChatRoom("bbb",new Socket());
		}*/
	public ChatRoom(String name, Socket socket, String[] userlist_) {
		this.socket = socket;
		this.name = name;
		this.userlist = userlist_;
		
		frame = new JFrame(name);
		frame.setVisible(true); 
		frame.setBackground(Color.GRAY);
		frame.setResizable(false); 		// ��С���ɱ�
		
	/*	text_show = new JTextPane();
		text_show.setEditable(false);
		attrset = new SimpleAttributeSet();
		StyleConstants.setFontSize(attrset, 15);*/
		recordPane = new JTextArea();
		recordPane.setEditable(false);	
		
		listModel = new DefaultListModel<>();
		userList = new JList<>(listModel);
		//������
		northPanel = new JPanel();
		northPanel.setBackground(Color.GRAY);
		northPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel info_a = new JLabel("UserName : ");
		info_a.setForeground(Color.WHITE);
		info_a.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 18));
		northPanel.add(info_a);
		info_name = new JLabel(name);
		info_name.setForeground(Color.WHITE);
		info_name.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 18));
		northPanel.add(info_name);
		TitledBorder info_b = new TitledBorder("My Info");
		info_b.setTitleColor(Color.WHITE);
		info_b.setTitleFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 15));
		northPanel.setBorder(info_b);
		
		//�м����Ϣ�����߳�Ա
		//rightScroll = new JScrollPane(text_show);
		rightScroll = new JScrollPane(recordPane);
		TitledBorder info_c = new TitledBorder("��Ϣ");
		info_c.setTitleColor(Color.DARK_GRAY);
		info_c.setTitleFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 15));
		rightScroll.setBorder(info_c);
		
		leftScroll = new JScrollPane(userList);
		TitledBorder info_d = new TitledBorder("�����û�");
		info_d.setTitleColor(Color.DARK_GRAY);
		info_d.setTitleFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 15));
		leftScroll.setBorder(info_d);
		
		//�²���
		southPanel = new JPanel(new BorderLayout());
		southPanel.setLayout(null);
		txt_msg = new JTextField();		//д����
		txt_msg.setBounds(0, 0, 590, 80);
		txt_msg.setBackground(Color.LIGHT_GRAY);
		southPanel.add(txt_msg);
		btn_send = new JButton("����");		//���ͼ�
		btn_send.setBounds(600, 0, 80, 80);
		btn_send.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 18));
		btn_send.setForeground(Color.DARK_GRAY);
		southPanel.add(btn_send);
		lableTo = new JLabel("��ѡ�����û�");		//	
		lableTo.setBounds(20, 90, 110, 35);
		lableTo.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 15));
		southPanel.setForeground(Color.DARK_GRAY);
		southPanel.add(lableTo);
		comboBox = new JComboBox<>();		//ѡ���͵���
		comboBox.addItem("*");
		comboBox.setBounds(160, 90, 100, 35);
		comboBox.setForeground(Color.DARK_GRAY);
		southPanel.add(comboBox);
		btn_pic = new JButton("ѡ��ͼƬ");		//���Ƿ�ͼƬ
		btn_pic.setBounds(290, 90, 100, 35);
		btn_pic.setForeground(Color.DARK_GRAY);
		btn_pic.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 15));
		southPanel.add(btn_pic);
		
		centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScroll, rightScroll);
		centerSplit.setDividerLocation(200);
		//���ø������ִ�С
		frame.setLayout(null);		
		northPanel.setBounds(0, 0, 700, 60);
		centerSplit.setBounds(0, 70, 700, 400);
		southPanel.setBounds(0, 480, 700, 200);
		frame.add(northPanel);
		frame.add(centerSplit);
		frame.add(southPanel);
		frame.setBounds(0, 0, 700, 660);
		int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;		//��λ
		int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
		frame.setLocation((screen_width - frame.getWidth()) / 2, (screen_height - frame.getHeight()) / 2);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	
		
		ConnectServer();// ���ӷ�����
		
//--------��ʼ���û��б�
		String username = null;
		//String userIp = null;
		for (int i = 0; i < userlist.length; i++) {
			if (userlist[i] == null)
				break;
			username = userlist[i] ;
			//userIp = str_msg[i + 1];
			//User user = new User(username, userIp);
			onLineUsers.put(username, username);
			if (listModel.contains(username))
				;
			else
				listModel.addElement(username);
			int len = comboBox.getItemCount();
			int _i = 0;
			for (; _i < len; _i++) {
				if (comboBox.getItemAt(_i).toString().equals(username))
					break;
			}
			if (_i == len)
				comboBox.addItem(username);
			else
				;
		}
		
		
		// txt_msg�س���ʱ�¼�
		txt_msg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ComboBoxValue();
			}
		});
		// btn_send�������Ͱ�ťʱ�¼�
		btn_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ComboBoxValue();
			}
		});
		// btn_pic����ͼƬ�¼�
		btn_pic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
			}
		});
		// �رմ���ʱ�¼�
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (isConnected) {
					try {
						// �Ͽ�����
						boolean flag = ConnectClose();
						if (flag == false) {
							throw new Exception("�Ͽ����ӷ����쳣��");
						} else {
							JOptionPane.showMessageDialog(frame, "�ɹ��Ͽ�!");
							txt_msg.setEnabled(false);
							btn_send.setEnabled(false);
						}
					} catch (Exception e4) {
						JOptionPane.showMessageDialog(frame, "�Ͽ����ӷ������쳣��" + e4.getMessage(), "����",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (!isConnected) {
					ConnectServer();
					txt_msg.setEnabled(true);
					btn_send.setEnabled(true);
				}
			}		
		});
		
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				try {
					if (ItemEvent.SELECTED == evt.getStateChange()) {
						String value = comboBox.getSelectedItem().toString();
						//System.out.println(value);
						UserValue = value;
					}
				} catch (Exception e) {
					System.out.println("GGGFFF");
				}
			}
		});
		
	}
	public void ConnectServer() {
		info_ip = new JLabel(socket.getLocalAddress().toString());
		info_ip.setForeground(Color.WHITE);
		info_ip.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 18));
		if (info_ip_ == 0) {
			northPanel.add(info_ip);
			onLineUsers.put(name, name);		//��¼��ʾ�б���ֻ���Լ�
			listModel.addElement(name);
			comboBox.addItem(name);
			JOptionPane.showMessageDialog(frame, name + " ���ӷ������ɹ�!");
		}
		info_ip_++;
		
		messageThread = new MessageThread();
		messageThread.start();
		isConnected = true;// �Ѿ���������
		
		frame.setVisible(true);
	}
	@SuppressWarnings("deprecation")
	public synchronized boolean ConnectClose() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());	
			System.out.println("[type : Require, reqtype : Quit , username = "+name+"]"); 
			Require reqdata=new Require("Require","Quit",new User(name,"uuu"));
			oos.writeObject(reqdata);
			
			messageThread.stop();// ֹͣ������Ϣ�߳�
		
			if (socket != null) {
				socket.close();
			}
			isConnected = false;
			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, name + " �Ͽ����ӷ������ɹ�!");
			isConnected = true;
			return false;
		}
	}
	
	// Ⱥ�ġ�˽��ѡ�񣬴����Ϣ�������б�
	public void ComboBoxValue() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()); 
			String a = "[type : Message, userto : "; 
			String message = txt_msg.getText();
			if (message == null || message.equals("")) {
				JOptionPane.showMessageDialog(frame, "��Ϣ����Ϊ�գ�", "����", JOptionPane.ERROR_MESSAGE);
				return;
			}
			/*if (UserValue == "ALL") {
				String userto = "*";
				a += userto;
				a += ", words : ";
				Message mesdata=new Message("Message",name,userto,message); 
				oos.writeObject(mesdata);
			} else {*/
				String userto = comboBox.getSelectedItem().toString();
				a += userto;
				a += ", words : ";
				Message mesdata=new Message("Message",name,userto,message); 
				oos.writeObject(mesdata);
			//}
			System.out.println(a+message+"]");
			txt_msg.setText(null);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	
//--------------------------------
	/*class User_ {
		private String name;
		private String ip;
		public User_(String name,String ip) {
			this.name = name;
			this.ip = ip;
		}
	}*/
	class MessageThread extends Thread {
		
		public void run() {
			while(true) {
				try {
					
					ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
					Data data=(Data)ois.readObject();
					
					if(flag==0) {
						if(data.getType().equals("Response")) {
							Response resdata=(Response)data;
							//signin & signup �Ǵ����������յ�ĳ�˵ĵ�¼ע��ɹ���Ϣ��ʧ�ܵĻ������յ���
							if(resdata.getRestype().equals("Signin")||resdata.getRestype().equals("Signup")) {
							//if(resdata.getRestype().equals("Signin")) {
								System.out.println(resdata.getUsername()+" enters this chatroom");
								System.out.println("userlist = "+ Response.toString(resdata.getUserlist()));
								if(resdata.getUsername() != name) {
									new onLinWindow(resdata.getUsername()).start();
								}
								
									String username = null;
									//String userIp = null;
									for (int i = 0; i < resdata.getUserlist().length; i++) {
										if (resdata.getUserlist()[i] == null)
											break;
										username = resdata.getUserlist()[i] ;
										//userIp = str_msg[i + 1];
										//User user = new User(username, userIp);
										onLineUsers.put(username, username);
										if (listModel.contains(username))
											;
										else
											listModel.addElement(username);
										int len = comboBox.getItemCount();
										int _i = 0;
										for (; _i < len; _i++) {
											if (comboBox.getItemAt(_i).toString().equals(username))
												break;
										}
										if (_i == len)
											comboBox.addItem(username);
										else
											;
									}
								
							}else if(resdata.getRestype().equals("Quit")){
								String username = resdata.getUsername();
								//User_ user = (User_) onLineUsers.get(username);
								onLineUsers.remove(username);
								listModel.removeElement(username);
								comboBox.removeItem(username);
								
								if(resdata.getUsername().equals(name)) {
									System.out.println("you quit");
									break;
								}else {
									System.out.println(resdata.getUsername()+" exit the chatroom");
								}
								
							}
						}else if(data.getType().equals("Message")) {
							SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");		// �������ڸ�ʽ
							String time = df.format(new java.util.Date());
							Message mesdata=(Message)data;
							System.out.println("[ " + time + "] " + mesdata.getUserfrom()+" say : "+mesdata.getWords());
						//	Document docs = text_show.getDocument();
							
							if(mesdata.getUserto().equals("*")) {		//Ⱥ����Ϣ
							//	try {
									String words = "[" + time + "]\r\n" + mesdata.getUserfrom() + " ˵ : " + mesdata.getWords() + "\r\n\n";
									//docs.insertString(docs.getLength(),words, attrset);// ���ı�����׷��
									recordPane.append(words);
							/*	} catch (BadLocationException e) {
									e.printStackTrace();
								}*/
							}
							else {
								if(mesdata.getUserto().equals(name) || mesdata.getUserfrom().equals(name)) {
								//	try {
										String words = "[" + time + "]\r\n" + mesdata.getUserfrom()+"��"+ mesdata.getUserto()+"˵ : "+ mesdata.getWords()+"\r\n\n";
										//docs.insertString(docs.getLength(),words,attrset);	// ���ı�����׷��
										recordPane.append(words);
								/*	} catch (BadLocationException e) {
										e.printStackTrace();
									}*/
								}
							}
						}
						
						//�������ѹر��ź�
						/*if () {
							Document docs = text_show.getDocument();
							try {
								docs.insertString(docs.getLength(), "�������ѹر�!\\r\\n", attrset);// ���ı�����׷��
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
							// text_show.add("�������ѹر�!\r\n", null);
							closeCon();// �ر�����
							return;// �����߳�
						}
						// �����Ѵ������ź�
						else if (command.equals("MAX")) {
							closeCon();// �ر�����
							JOptionPane.showMessageDialog(frame, "��������������,���Ժ����ԣ�", "��ʾ", JOptionPane.CANCEL_OPTION);
							return;// �����߳�
						}*/
							
					}
					else if(flag == 1) {
							// �ͻ��˽�����Ϣ������ͼƬ
							
							
						flag = 0;
					}
				}catch (IOException e1) {
					// ConnectServer();
					e1.printStackTrace();
					System.out.println("�ͻ��˽��� ��Ϣ �߳� run() e1:" + e1.getMessage());
					break;
				} catch (Exception e2) {
					// ConnectServer();
					e2.printStackTrace();
					System.out.println("�ͻ��˽��� ��Ϣ �߳� run() e2:" + e2.getMessage());
					break;
				}
			}
		}
		
		public synchronized void closeCon() throws Exception {
			listModel.removeAllElements();// ����û��б�
			
			if (socket != null) {
				socket.close();
			}
			isConnected = false; // �޸�״̬Ϊ�Ͽ�
		}
	}
}



