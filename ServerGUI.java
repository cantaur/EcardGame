package ecardGame;

import java.awt.*; 
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.net.*;

import ecardGame.ServerThread;

public class ServerGUI extends JFrame implements ActionListener{

    ServerThread client;
    static ServerSocket ss;
    Socket sc;
    public static final int port = 4003;
    public static final int maxclient = 2;
    
    
    Container cp;
    JButton startBtn;
	JButton closeBtn; 
	JLabel serverStatus;
	JPanel contentPane, mainPanel, textPanel, btnPanel;
	JScrollPane scrollPane;
    JTextArea chatLog;
    static private final String newline = "\n"; //????
    ImageIcon imageIcon1, imageIcon2, imageIcon3;
    String path = ServerGUI.class.getResource("").getPath();


    ServerGUI(){
        loadImg();
        init();
    }
    
    public void init(){
    	
        contentPane = new JPanel(){
            public void paintComponent(Graphics g) {
                g.drawImage(imageIcon3.getImage(), 0, 0, null);
                setOpaque(false); 
                super.paintComponent(g);
            }
        };
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 10, 0));
    	
		mainPanel = new JPanel(){
            public void paintComponent(Graphics g) {
                g.drawImage(imageIcon3.getImage(), 0, 0, null);
                setOpaque(false); 
                super.paintComponent(g);
            }
        };
		contentPane.add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		serverStatus = new JLabel("[ E-card Game Server ]");
		serverStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
		serverStatus.setPreferredSize(new Dimension(96, 50));
		mainPanel.add(serverStatus);
		serverStatus.setHorizontalTextPosition(SwingConstants.CENTER);
		serverStatus.setHorizontalAlignment(SwingConstants.CENTER);
		serverStatus.setFont(new Font("나눔바른고딕", Font.PLAIN, 20));
		
		textPanel = new JPanel();
		mainPanel.add(textPanel);
		textPanel.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Color.DARK_GRAY));
		textPanel.add(scrollPane);
		
		chatLog = new JTextArea();
		chatLog.setLineWrap(true);
		chatLog.setEditable(false);
		scrollPane.setViewportView(chatLog);
		
		btnPanel = new JPanel(){
            public void paintComponent(Graphics g) {
                g.drawImage(imageIcon3.getImage(), 0, 0, null);
                setOpaque(false); 
                super.paintComponent(g);
            }
        };
		btnPanel.setPreferredSize(new Dimension(10, 47));
		btnPanel.setAutoscrolls(true);
		mainPanel.add(btnPanel);
		btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		startBtn = new JButton(imageIcon1);
		startBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		startBtn.setPreferredSize(new Dimension(120, 40));
		startBtn.setFocusPainted(false);
		startBtn.setFont(new Font("나눔바른고딕", Font.BOLD, 16));
		startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		startBtn.setForeground(Color.WHITE);
		startBtn.setBackground(Color.DARK_GRAY);
		startBtn.setBorder(null);
		btnPanel.add(startBtn);
		startBtn.addActionListener(this);
		
		closeBtn = new JButton(imageIcon2);
		closeBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		closeBtn.setPreferredSize(new Dimension(120, 40));
		closeBtn.setFocusPainted(false);
		closeBtn.setFont(new Font("나눔바른고딕", Font.BOLD, 16));
		closeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		closeBtn.setForeground(Color.WHITE);
		closeBtn.setBackground(Color.DARK_GRAY);
		closeBtn.setBorder(null);
		btnPanel.add(closeBtn);
		closeBtn.addActionListener(this);
		closeBtn.setEnabled(false);
		
		setUI();
	}

    void loadImg(){
        try {
            imageIcon1 = new ImageIcon(ImageIO.read(new File("img/serverStart.png")));
            imageIcon2 = new ImageIcon(ImageIO.read(new File("img/serverExit.png")));
            imageIcon3 = new ImageIcon(ImageIO.read(new File("img/serverBG.png")));
        } catch (IOException ie) {
            //TODO: handle exception
        }
    }
    void setUI(){
		setTitle("Ecard Game Server");
		setSize(400,500);
		setVisible(true);
        setLocationRelativeTo(null); 
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
    public static void main(String[] args) {
        new ServerGUI();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startBtn){
            new Thread(){
                public void run(){
                    try {
                        
                        ss = new ServerSocket(port);
                        serverStatus.setText("[ Server Started ]");
                        chatLog.append("[ 서버가 시작되었습니다. ]" + "\n");
                        startBtn.setEnabled(false);
                        closeBtn.setEnabled(true);
                        while(true){
                            sc = ss.accept();
                            client = new ServerThread(sc);
                            client.start();
                        }
                    } catch (IOException ie) {
                    } finally{
                        try {
                            if(ss !=null) ss.close();
                        } catch (IOException ie) {}
                    } 
                }
            }.start();
        }else if(e.getSource() == closeBtn){
            int select = JOptionPane.showConfirmDialog(null, "서버를 정말 종료하시겠습니까?", "Ecard Game Server", JOptionPane.OK_CANCEL_OPTION);
			try{
				if(select == JOptionPane.YES_OPTION){
					ss.close();
					serverStatus.setText("[ Server Closed ]");
					chatLog.append("[ 서버가 종료되었습니다 ]" + "\n");
					startBtn.setEnabled(true);
					closeBtn.setEnabled(false);
				}
			}catch(IOException io){}
        }
		
	}

}


