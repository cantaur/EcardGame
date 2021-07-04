package ecardGame;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;


public class EcardGUI extends JFrame{

    //GUI ??
    Font font;
	JFrame frame;
    static JTextArea text_chatLog;
	static JTextField text_msg;
	static JScrollPane scroll;
    static JPanel background;
    static JLabel jTimer;
	static JLabel jSlave, jKing;
	static JLabel yLabel, mLabel;
	static JLabel btn_myBack, btn_yourBack;
	static JLabel laftKing, laftSlav, laftCtzn;
	static JLabel rightKing, rightSlav, rightCtzn;
	static JLabel yourScore1, yourScore2, myScore1, myScore2;
	static JLabel vicLabel, defLabel, drwLabel, chat;
	ImageIcon img_king, img_slav, img_ctzn1, img_ctzn2, img_ctzn3, img_ctzn4, img_background, img_ready, img_exit, img_backCard, slaveSet, kingSet, victory, defeat, draw, chatLog;
	
	static JButton btn_myKing, btn_mySlav, btn_myCtzn1, btn_myCtzn2, btn_myCtzn3, btn_myCtzn4;
	static JButton btn_Ready, btn_exit;

    //??? ??
	private static final long serialVersionUID = 1L;
    public static ClientThread ccThread; //ClientThread ??
    public static EcardGUI ecardGUI; //?? ??? ??

	
	
	EcardGUI() {
		loadImg();
		setPanel();

		ClientThread ccThread= new ClientThread();
		ccThread.startChat();
	}
	static void dialog(){
		JOptionPane.showMessageDialog(null, "??? ?????.", "EcardGame", JOptionPane.INFORMATION_MESSAGE);
	}

	static void gameEnd(){
		JOptionPane.showMessageDialog(null, "??? ???????. \n ??? ??? ???!", "EcardGame", JOptionPane.INFORMATION_MESSAGE);

		String my ="";
			String your ="";
			text_chatLog.append("??? ???????. \n ??? ??? ???!\n");
			scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
			if(myScore1.isVisible()){
				my= myScore1.getText();
				your= yourScore1.getText();
			}else if(myScore2.isVisible()){
				my= myScore2.getText();
				your= yourScore2.getText();
			}
	
			if(Integer.parseInt(my)>Integer.parseInt(your)){
				invisibleCenter();
				vicLabel.setVisible(true);
			}else if(Integer.parseInt(my)<Integer.parseInt(your)){
				invisibleCenter();
				defLabel.setVisible(true);
			}else if(Integer.parseInt(my)==Integer.parseInt(your)){
				invisibleCenter();
				drwLabel.setVisible(true);
			}
	}

	static void invisibleCenter(){
		laftKing.setVisible(false);
		laftSlav.setVisible(false);
		laftCtzn.setVisible(false);
		rightKing.setVisible(false);
		rightSlav.setVisible(false);
		rightCtzn.setVisible(false);
	}

	void loadImg() {
		try {
			img_king = new ImageIcon(ImageIO.read(new File("img/king.png")));
			img_slav = new ImageIcon(ImageIO.read(new File("img/slav.png")));
			img_ctzn1 = new ImageIcon(ImageIO.read(new File("img/ctzn1.png")));
			img_ctzn2 = new ImageIcon(ImageIO.read(new File("img/ctzn2.png")));
			img_ctzn3 = new ImageIcon(ImageIO.read(new File("img/ctzn3.png")));
			img_ctzn4 = new ImageIcon(ImageIO.read(new File("img/ctzn4.png")));
			img_ready = new ImageIcon(ImageIO.read(new File("img/ready.png")));
			img_exit = new ImageIcon(ImageIO.read(new File("img/exit.png")));
			img_backCard = new ImageIcon(ImageIO.read(new File("img/backCard.png")));
			slaveSet = new ImageIcon(ImageIO.read(new File("img/slaveSet.png")));
			kingSet = new ImageIcon(ImageIO.read(new File("img/kingSet.png")));
			victory = new ImageIcon(ImageIO.read(new File("img/victory.png")));
			defeat = new ImageIcon(ImageIO.read(new File("img/defeat.png")));
			draw = new ImageIcon(ImageIO.read(new File("img/draw.png")));
			chatLog = new ImageIcon(ImageIO.read(new File("img/chat.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void setPanel(){

        frame = new JFrame();
        font = new Font("???", Font.PLAIN, 70);
		try {
			background = new JPanel() {
				private static final long serialVersionUID = 1L;
				Image img_background = new ImageIcon(ImageIO.read(new File("img/BG.png"))).getImage();
				public void paint(Graphics g) {//??? ??
					g.drawImage(img_background, 0, 0, null);
				}
			};
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		jTimer = new JLabel("30");
		jTimer.setFont(font);
		jTimer.setLayout(null);
		jTimer.setBounds(1300, 120, 150, 150);

		chat = new JLabel(chatLog);
		chat.setBounds(1200, 320, 315, 568);

		jKing = new JLabel(kingSet);
		jKing.setBounds(390,80,790,238);
		jSlave = new JLabel(slaveSet);
		jSlave.setBounds(390,80,790,238);

		Color color = new Color(0xf4ead0);
		
		yLabel = new JLabel("[ ??? ?? ]");
		yLabel.setForeground(color);
		yLabel.setFont(new Font("?? ??", Font.BOLD, 20));
		yLabel.setBounds(155, 270, 300, 150);
		mLabel = new JLabel("[ ? ?? ]");
		mLabel.setForeground(color);
		mLabel.setFont(new Font("?? ??", Font.BOLD, 20));
		mLabel.setBounds(170, 430, 300, 150);


		yourScore1 = new JLabel("0");
		yourScore1.setForeground(color);
		yourScore1.setFont(new Font("?? ??", Font.BOLD, 70));
		yourScore1.setBounds(200, 325, 150, 150);
		yourScore2 = new JLabel("0");
		yourScore2.setForeground(color);
		yourScore2.setFont(new Font("?? ??", Font.BOLD, 70));
		yourScore2.setBounds(200, 325, 150, 150);
		myScore1 = new JLabel("0");
		myScore1.setForeground(color);
		myScore1.setFont(new Font("?? ??", Font.BOLD, 70));
		myScore1.setBounds(200, 485, 150, 150);
		myScore2 = new JLabel("0");
		myScore2.setForeground(color);
		myScore2.setFont(new Font("?? ??", Font.BOLD, 70));
		myScore2.setBounds(200, 485, 150, 150);
		btn_myKing = new JButton(img_king); 
		btn_mySlav = new JButton(img_slav);
		btn_myCtzn1 = new JButton(img_ctzn1); 
		btn_myCtzn2 = new JButton(img_ctzn2);
		btn_myCtzn3 = new JButton(img_ctzn3); 
		btn_myCtzn4 = new JButton(img_ctzn4);
		
		btn_Ready = new JButton(img_ready); 
		btn_exit = new JButton(img_exit);
		btn_myBack = new JLabel(img_backCard); 
		btn_yourBack = new JLabel(img_backCard);
		laftKing = new JLabel(img_king);
		laftSlav = new JLabel(img_slav);
		laftCtzn = new JLabel(img_ctzn1);
		rightKing = new JLabel(img_king);
		rightSlav = new JLabel(img_slav);
		rightCtzn = new JLabel(img_ctzn1);
        
		scroll = new JScrollPane(text_chatLog);
		scroll.setFont(new Font("?? ??", Font.PLAIN, 14));
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(1210, 330, 300, 510);
		scroll.setBorder(null);
		scroll.setOpaque(false);
		frame.add(scroll);


        text_chatLog = new JTextArea(); 
		text_chatLog.setForeground(color);
		text_chatLog.setBorder(null);
		text_chatLog.setEditable(false);
		text_chatLog.setLineWrap(true);
		text_chatLog.setOpaque(false);
		scroll.setViewportView(text_chatLog);
		scroll.getViewport().setOpaque(false);

		text_msg = new JTextField();
		text_msg.setFont(new Font("?? ??", Font.PLAIN, 14));
		text_msg.setForeground(color);
		text_msg.setBorder(null);
		text_msg.setOpaque(false);
		text_msg.setBounds(1210, 838, 300, 35);

		vicLabel = new JLabel(victory);
		vicLabel.setBounds(460, 360, 681, 294);
		
		defLabel = new JLabel(defeat);
		defLabel.setBounds(510, 360, 598, 290);

		drwLabel = new JLabel(draw);
		drwLabel.setBounds(540, 360, 527, 290);


		background.setBounds(0, 0, 1580, 960);
		btn_myKing.setBounds(390, 640, 150, 238); 
		btn_myCtzn1.setBounds(550, 640, 150, 238);
		btn_myCtzn2.setBounds(710, 640, 150, 238); 
		btn_myCtzn3.setBounds(870, 640, 150, 238);
		btn_myCtzn4.setBounds(1030, 640, 150, 238); 
		btn_mySlav.setBounds(390, 640, 150, 238);
		btn_yourBack.setBounds(610, 328, 150, 238); 
		laftKing.setBounds(610, 328, 150, 238); 
		laftSlav.setBounds(610, 328, 150, 238); 
		laftCtzn.setBounds(610, 328, 150, 238); 
		btn_myBack.setBounds(810, 392, 150, 238);
		rightKing.setBounds(810, 392, 150, 238);
		rightSlav.setBounds(810, 392, 150, 238);
		rightCtzn.setBounds(810, 392, 150, 238);
		btn_Ready.setBounds(60, 660, 320, 90); 
		btn_exit.setBounds(60, 780, 320, 90);
		


	
		
		frame.add(btn_myKing); frame.add(btn_myCtzn1); frame.add(btn_myCtzn2);
		frame.add(btn_myCtzn3); frame.add(btn_myCtzn4); frame.add(btn_mySlav);

		frame.add(jKing); frame.add(jSlave);
		frame.add(yLabel); frame.add(mLabel);
		frame.add(yourScore1); frame.add(yourScore2);
		frame.add(myScore1); frame.add(myScore2);
		
		frame.add(btn_myBack); frame.add(btn_yourBack);
		frame.add(laftKing); frame.add(laftSlav); frame.add(laftCtzn);
		frame.add(rightKing); frame.add(rightSlav); frame.add(rightCtzn);
		frame.add(btn_Ready); frame.add(btn_exit);
		

		frame.add(text_msg);
		frame.add(chat); 

		frame.add(jTimer);
		frame.add(vicLabel);
		frame.add(defLabel);
		frame.add(drwLabel);
		frame.add(background);

		revalidate();
		repaint();
		frame.setTitle("Ecard Game");
		// frame.setLocationRelativeTo(null);	
		frame.getContentPane().setLayout(null);
		frame.setBounds(50, 50, 1580,960);
		// frame.setSize(1580,960);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
    }

}
