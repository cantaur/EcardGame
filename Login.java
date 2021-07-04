package ecardGame;

import java.awt.*;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Login extends JFrame implements ActionListener {

	public static String IP, nickName;
	public String msg_logon="";
	
	//GUI����
	Container cp;
	Font f = new Font("���� ���", Font.BOLD, 20);
	JPanel jpcen, background;
	JLabel laImg;
	JLabel label_id, label_ip;
	JTextField tf_id, tf_ip;
	JButton start, button_ip;
	JFrame frame;
	ImageIcon i1;

	
	public Login(){
		try {
			background = new JPanel() {
				Image img = new ImageIcon(ImageIO.read(new File("img/login_BG.png"))).getImage();
				public void paint(Graphics g) {
					g.drawImage(img, 0, 0,null);
				}
			};
		} catch (IOException e) {
			e.printStackTrace();
		}
		init();

		
	}
	
	public void init() {
		background.setBounds(0, 0, 430, 522);
		frame = new JFrame();
		laImg = new JLabel(i1);
		laImg.setBounds(0, 0, 430, 522);
		
		jpcen = new JPanel();
		
		//id
		tf_id = new JTextField();
		tf_id.setBounds(75, 183, 306, 63);
		tf_id.setFont(f);
		tf_id.setForeground(Color.WHITE);
		tf_id.setText("");
		tf_id.setOpaque(false);
		tf_id.setBorder(null);
		frame.add(tf_id);
		
		//ip
		tf_ip = new JTextField("127.0.0.1");
		tf_ip.setBounds(75, 290, 306, 63);
		tf_ip.setFont(f);
		tf_ip.setForeground(Color.WHITE);
		tf_ip.setEnabled(true);
		tf_ip.setOpaque(false);
		tf_ip.setBorder(null);
		frame.add(tf_ip);

		
		start = new JButton(new ImageIcon("img/loginBtn.png"));
		start.setBounds(62, 400, 300, 60);
		start.addActionListener(this);

		frame.add(start);
		frame.add(background);
		frame.add(laImg);
		setUi();

	}
	
	void setUi() {
		revalidate();
		repaint();
		frame.getContentPane().setLayout(null);
		frame.setSize(445,561);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		tf_id.requestFocus();
		if(obj == start) {//start ��ư ��������
			if(tf_id.getText().equals("") || tf_ip.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "ID�� �Է����ּ���", "�����޽���", JOptionPane.ERROR_MESSAGE);
			}else if(tf_ip.getText().equals("")){
				JOptionPane.showMessageDialog(null, "IP�� �Է����ּ���", "�����޽���", JOptionPane.ERROR_MESSAGE);
			}else if(tf_id.getText().trim().length() > 7) {
				JOptionPane.showMessageDialog(null, "ID�� 7�� ���ܷ� �Է����ּ���", "�����޽���", JOptionPane.ERROR_MESSAGE);
				tf_id.setText("");
			}else {
				nickName = tf_id.getText().trim();
				String temp = tf_ip.getText();
				if(temp.matches("(^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$)")){
					IP = temp;
					JOptionPane.showMessageDialog(null, "�α��� ����!", "ECARD GAME LOGIN", JOptionPane.INFORMATION_MESSAGE);
					start.setEnabled(false);
					start.setBorderPainted(false); 
					start.setFocusPainted(false); 
					start.setContentAreaFilled(false);
					frame.setVisible(false);
					tf_id.setEnabled(false);
					setVisible(false);
					EcardGUI eg = new EcardGUI();
				}else{
					JOptionPane.showMessageDialog(null, "IP �ּҸ� ��Ȯ�ϰ� �Է��� �ּ���! ", "ERROR!", JOptionPane.WARNING_MESSAGE);
				}
			}
		} 

		
	}

	public static void main(String[] args) {
		new Login();


	}
}
