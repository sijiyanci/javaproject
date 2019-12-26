package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class onLinWindow extends Thread {
	public static JFrame frame;
	//private JTextArea showMsg;// 显示消息
	private String name;
	public  onLinWindow(String username) {
		this.name=username;
	}
	@Override
	public void run() {
		frame = new JFrame("上线提醒");
		//frame.setIconImage(new ImageIcon("image/icon.png").getImage());
		/*showMsg = new JTextArea();
		showMsg.setText(" "+name+" 上线啦！");
		showMsg.setFont(new Font("宋体", Font.BOLD, 25));
		showMsg.setEditable(false);
		showMsg.setForeground(Color.WHITE);*/
		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.GRAY);
		northPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		String mes = " "+name+" 上线啦！";
		JLabel info_name = new JLabel(mes);
		info_name.setForeground(Color.WHITE);
		info_name.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 18));
		northPanel.add(info_name);
		
		
		frame.setVisible(true);
		frame.setSize(250, 80);
		frame.setLayout(new BorderLayout());
		//frame.add(showMsg);
		frame.add(northPanel);

		int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
		frame.setLocation((screen_width - frame.getWidth()) ,
				(screen_height - frame.getHeight())-60);
	}

}
