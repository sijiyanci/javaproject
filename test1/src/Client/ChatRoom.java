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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Entity.Client;
import Entity.Data;
import Entity.Message;
import Entity.Require;
import Entity.Response;
import Entity.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ChatRoom {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 6704231622520334518L;
	
	private JFrame frame;
	private JTextPane text_show;		//��ʾ��������
	//private JTextArea recordPane;
	private JTextField txt_msg;
	private JLabel info_name;
	private JLabel info_ip;
	private JButton btn_send;
	private JButton btn_pic;
	private JButton btn_anony;
	private JPanel northPanel;		
	private JPanel southPanel;
	private JScrollPane rightScroll;		//������
	private JScrollPane leftScroll;
	private JSplitPane centerSplit;			//��ֳ���������
	private JLabel lableTo;
	private JComboBox<String> comboBox;		//�����б�
	private SimpleAttributeSet attrset;
	private JButton btn_sure;	
	private JButton btn_chat;
	
	private DefaultListModel<String> listModel;		//��̬��ʾ
	private JList<String> userList;

	private Socket socket;
	//private String[] userlist;
	private ArrayList<String> userlist;
	private ArrayList<String> fctype=new ArrayList<String>();
	private ArrayList<String> chat_userlist;
	private static FileInputStream doc_read; 		// �������ļ�
	private static FileOutputStream fos; 		// д�����ļ�
	private MessageThread messageThread;		// ���������Ϣ���߳�
	//private Map<String, User_> onLineUsers = new HashMap<String, User_>();// ���������û�
	private Map<String, String> onLineUsers = new HashMap<String, String>();
	private boolean isConnected = false;
	private String name;
	private String pic_path = null;
	private String UserValue = "";
	private int info_ip_ = 0;
	//private int flag = 0;
	//private Gson mGson;
	//private boolean file_is_create = true;

/*	public static void main(String[] args) {
		String[] a= new String[1];
		a[0]="abc";
		new ChatRoom("abc",new Socket(),a);
	}*/
	
	public ChatRoom(String name, Socket socket, ArrayList<String> object) {
		this.socket = socket;
		this.name = name;
		this.userlist = object;
		
		frame = new JFrame(name);
		frame.setVisible(true); 
		frame.setBackground(Color.GRAY);
		frame.setResizable(false); 		// ��С���ɱ�
		
		text_show = new JTextPane();
		text_show.setEditable(false);
		attrset = new SimpleAttributeSet();
		StyleConstants.setFontSize(attrset, 15);
		//recordPane = new JTextArea();
		//recordPane.setEditable(false);	
		
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
		rightScroll = new JScrollPane(text_show);
		//rightScroll = new JScrollPane(recordPane);
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
		
		lableTo = new JLabel("ѡ��˽���û�");		//	
		lableTo.setBounds(20, 90, 100, 35);
		lableTo.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 15));
		southPanel.setForeground(Color.DARK_GRAY);
		southPanel.add(lableTo);
		comboBox = new JComboBox<>();		//ѡ���͵���
		comboBox.addItem("*");
		comboBox.setBounds(140, 90, 90, 35);
		comboBox.setForeground(Color.DARK_GRAY);
		southPanel.add(comboBox);
		btn_sure = new JButton("ȷ���û�");		//ȷ����
		btn_sure.setBounds(250, 90, 90, 35);
		btn_sure.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 15));
		btn_sure.setForeground(Color.DARK_GRAY);
		southPanel.add(btn_sure);
		btn_chat = new JButton("����˽��");		//����˽��
		btn_chat.setBounds(360, 90, 90, 35);
		btn_chat.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 15));
		btn_chat.setForeground(Color.DARK_GRAY);
		southPanel.add(btn_chat);
		btn_pic = new JButton("ѡ��ͼƬ");		//���Ƿ�ͼƬ
		btn_pic.setBounds(470, 90, 90, 35);
		btn_pic.setForeground(Color.DARK_GRAY);
		btn_pic.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 15));
		southPanel.add(btn_pic);
		btn_anony = new JButton("����");		//����
		btn_anony.setBounds(580, 90, 90, 35);
		btn_anony.setForeground(Color.DARK_GRAY);
		btn_anony.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 15));
		southPanel.add(btn_anony);
		
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
		
//--------------------
		
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
		
		// btn_sure����ѡ���˽���û�
		btn_sure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chat_userlist.add(UserValue);
			}
		});
		// btn_chat����˽��
		btn_chat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PrivateChat(name,socket,chat_userlist);
			}
		});
		// btn_anony�Ƿ�����
		btn_anony.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fctype.add("Anonymous");
			}
		});
		
		// btn_pic����ͼƬ�¼�
		btn_pic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Filechose();
				try {
					ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());
			    	//File file=new File("./src/user1/user1."+texttype.toLowerCase());
					File file = new File(pic_path);
					String filename=file.getName();
			    	long filelength=file.length();
			    	BufferedInputStream fis=new BufferedInputStream(new FileInputStream(file));
			    	byte[] bytes = new byte[1024];    
			        long times = (filelength%1024==0)?filelength/1024:filelength/1024+1;;
					
			        dos.writeUTF(filename);
			        dos.flush();
			        dos.writeLong(filelength);
			        dos.flush();
			        
			        while(times!=0) {
			        	times--;
			        	int length=fis.read(bytes, 0, bytes.length);
			        	
			        	dos.write(bytes, 0, length);
			        	dos.flush();
			        }
					System.out.println("�ļ��ϴ����");
				} catch (FileNotFoundException e1) {
					System.out.println("�ļ������ڣ�");
				} catch (IOException e2) {
					System.out.println("�ļ�д���쳣");
				} finally {
					try {
						doc_read.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
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
	
//-----------------------------
	public static JSONObject reqPackage(String type,String reqtype,User user) {
		JSONObject reqdata=new JSONObject();
		reqdata.put("Type", type);
		reqdata.put("Reqtype", reqtype);
		reqdata.put("User", user);
		return reqdata;
	}
	public static JSONObject mesPackage(String type,String way,ArrayList<String> fctype,String texttype
			,String userfrom,ArrayList<String> userto,String words) {
		JSONObject mesdata=new JSONObject();
		mesdata.put("Type", type);
		mesdata.put("Way", way);
		mesdata.put("Fctype", fctype);
		mesdata.put("Texttype", texttype);
		mesdata.put("Userfrom", userfrom);
		mesdata.put("Userto", userto);
		mesdata.put("Words", words);
		return mesdata;
	}
	public static JSONObject mesPackage(String type,String way,ArrayList<String> fctype,String texttype
			,String userfrom,ArrayList<String> userto) {
		JSONObject mesdata=new JSONObject();
		mesdata.put("Type", type);
		mesdata.put("Way", way);
		mesdata.put("Fctype", fctype);
		mesdata.put("Texttype", texttype);
		mesdata.put("Userfrom", userfrom);
		mesdata.put("Userto", userto);
		return mesdata;
	}
	
	public void ConnectServer() {
		info_ip = new JLabel(socket.getLocalAddress().toString());
		info_ip.setForeground(Color.WHITE);
		info_ip.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 18));
		if (info_ip_ == 0) {
			northPanel.add(info_ip);
				
			String username = null;
			//String userIp = null;
			for (int i = 0; i < userlist.size(); i++) {
				username = userlist.get(i);
				onLineUsers.put(username, username);
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
			//onLineUsers.put(name, name);	
			//listModel.addElement(name);
			//comboBox.addItem(name);
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
			//Require reqdata=new Require("Require","Quit",new User(name,"uuu"));
			//oos.writeObject(reqdata);
			JSONObject reqdata=reqPackage("Require","Quit",new User(name,"***"));
			oos.writeObject(reqdata.toString());
			
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
	
	// �����Ϣ
	public void ComboBoxValue() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()); 
			String message = txt_msg.getText();
			if (message == null || message.equals("")) {
				JOptionPane.showMessageDialog(frame, "��Ϣ����Ϊ�գ�", "����", JOptionPane.ERROR_MESSAGE);
				return;
			}
			//�Լ���message���ᷢ���Լ�
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");		// �������ڸ�ʽ
			String time = df.format(new java.util.Date());
			System.out.println("[ " + time + "] " + name+" say : "+ message);
			Document docs = text_show.getDocument();
				try {
					String words = "[" + time + "]\r\n" + name + " ˵ : " + message + "\r\n\n";
					docs.insertString(docs.getLength(),words, attrset);// ���ı�����׷��
					//recordPane.append(words);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			
			String way = "All",texttype="Words";
			ArrayList<String> userto = userlist; 
			JSONObject mesdata=mesPackage("ServerMessage",way,fctype,texttype,name,userto,message);
			oos.writeObject(mesdata.toString());
			/*if (UserValue == "ALL") {
				String userto = "*";
				a += userto;
				a += ", words : ";
				Message mesdata=new Message("Message",name,userto,message); 
				oos.writeObject(mesdata);
			} else {
				String userto = comboBox.getSelectedItem().toString();
				a += userto;
				a += ", words : ";
				Message mesdata=new Message("Message",name,userto,message); 
				oos.writeObject(mesdata);
			//}*/
			String a = "[type : ServerMessage, way = Some, fctype(end with !) : "+ fctype.get(0)
				+"texttype : Words, userto : *, data : "; 
			System.out.println(a+message+"]");
			txt_msg.setText(null);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// �ļ�ѡ���������·��
		public void Filechose() {
			JFileChooser jfc = new JFileChooser();
			jfc.setCurrentDirectory(new File(""));
			jfc.addChoosableFileFilter(new MyFileFilter());
			// jfc.
			JFrame pic_chose = new JFrame();
			pic_chose.setVisible(false);
			pic_chose.setBounds(100, 100, 800, 600);
			if (jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				pic_path = jfc.getSelectedFile().getAbsolutePath().toString();
				System.out.println(pic_path);
			}
		}

		// �ļ����͹���
		class MyFileFilter extends FileFilter {
			public boolean accept(File pathname) {
				if (pathname.getAbsolutePath().endsWith(".gif") || pathname.isDirectory()
						|| pathname.getAbsolutePath().endsWith(".png"))
					return true;
				return false;
			}

			public String getDescription() {
				return "ͼ���ļ�";
			}
		}

	
//--------------------------------
	
	class MessageThread extends Thread {
		
		public void run() {
			while(true) {
				try {
					
					ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
					//Data data=(Data)ois.readObject();
					String datastr=(String)ois.readObject();
					JSONObject data=JSONObject.fromObject(datastr);
					
					
						if(data.get("Type").equals("Response")) {
							// resdata=(Response)data;
							//signin & signup �Ǵ����������յ�ĳ�˵ĵ�¼ע��ɹ���Ϣ��ʧ�ܵĻ������յ���
							if(data.get("Reqtype").equals("Signin")||data.get("Reqtype").equals("Signup")) {
							//if(resdata.getRestype().equals("Signin")) {
								System.out.println(data.get("Username")+" enters this chatroom");
								JSONArray usernamelist=(JSONArray)data.get("Usernamelist");
								System.out.println("userlist = "+ usernamelist.toString());
								if(data.get("Username") != name) {
									new onLinWindow((String) data.get("Username")).start();
								}
								
									String username = null;
									//String userIp = null;
									for (int i = 0; i < usernamelist.size(); i++) {
										if (usernamelist.getString(i) == null)
											break;
										username = usernamelist.getString(i) ;
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
								
							}else if(data.get("Reqtype").equals("Quit")){
								String username = (String) data.get("Username");
								//User_ user = (User_) onLineUsers.get(username);
								onLineUsers.remove(username);
								listModel.removeElement(username);
								comboBox.removeItem(username);
								
								if(username.equals(name)) {
									System.out.println("you quit");
									break;
								}else {
									System.out.println(username+" exit the chatroom");
								}
							}
						}else if(data.get("Type").equals("ClientMessage") && data.get("Way").equals("All")) {
							if(data.get("Texttype").equals("Words")) {
								SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");		// �������ڸ�ʽ
								String time = df.format(new java.util.Date());
								System.out.println("[ " + time + "] " + data.get("Userfrom")+" say : "+data.get("Words"));
								Document docs = text_show.getDocument();
								
								try {
									String words = "[" + time + "]\r\n" +  data.get("Userfrom") + " ˵ : " +data.get("Words") + "\r\n\n";
									docs.insertString(docs.getLength(),words, attrset);// ���ı�����׷��
									//recordPane.append(words);
								} catch (BadLocationException e) {
									e.printStackTrace();
								}
								/*if(mesdata.getUserto().equals("*")) {		//Ⱥ����Ϣ
									try {
										String words = "[" + time + "]\r\n" + mesdata.getUserfrom() + " ˵ : " + mesdata.getWords() + "\r\n\n";
										docs.insertString(docs.getLength(),words, attrset);// ���ı�����׷��
										//recordPane.append(words);
									} catch (BadLocationException e) {
										e.printStackTrace();
									}
								}
								else {
									if(mesdata.getUserto().equals(name) || mesdata.getUserfrom().equals(name)) {
										try {
											String words = "[" + time + "]\r\n" + mesdata.getUserfrom()+"��"+ mesdata.getUserto()+"˵ : "+ mesdata.getWords()+"\r\n\n";
											docs.insertString(docs.getLength(),words,attrset);	// ���ı�����׷��
											//recordPane.append(words);
										} catch (BadLocationException e) {
											e.printStackTrace();
										}
									}
								}*/
						
							}
							//����ͼƬ
							else {
								ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());
						    	String filename = dis.readUTF();
						        long filelength = dis.readLong();
						        //File file=new File("./src/"+name+"/"+filename+"."+texttype.toLowerCase());
						        File file=new File("./src/"+name+"/"+filename);
						        BufferedOutputStream fos=new BufferedOutputStream(new FileOutputStream(file,true));
						        byte[] bytes = new byte[1024];    
						        long times = (filelength%1024==0)?filelength/1024:filelength/1024+1;
						        while(times!=0) {
						        	System.out.println(times);
						        	times--;
						        	
						        	int length=dis.read(bytes, 0, bytes.length);
						        	fos.write(bytes, 0, length);

						        	fos.flush();
						        }  
						        fos.close();
						       /* JSONObject temp=new JSONObject();
						        temp.put("Filename", filename);
						        temp.put("Filelength", filelength);*/
						        ImageIcon icon = new ImageIcon("" + filename);
								// icon.
								SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");// �������ڸ�ʽ
								String time = df.format(new java.util.Date());
								StyledDocument doc = text_show.getStyledDocument();
								Document docs = text_show.getDocument();
								try {
									docs.insertString(docs.getLength(),
											"[" + time + "]\r\n" + name + " ˵ : " + "\r\n", attrset);// ���ı�����׷��
									text_show.setCaretPosition(doc.getLength());
									text_show.insertIcon(icon);
									docs = text_show.getDocument();
									docs.insertString(docs.getLength(), "\r\n", attrset);
								} catch (BadLocationException e) {
									e.printStackTrace();
								}

						        System.out.print(filename+" ");
						        System.out.println(filelength);
							}
							
						}
						else{
							if( data.get("Way").equals("Some")|| data.get("Way").equals("One")) {
								new PrivateChat(name,socket,chat_userlist,data);
							}
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



