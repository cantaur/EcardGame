package ecardGame;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;


public class ClientThread extends Thread implements ActionListener{
    Sender sender; //����Ŭ���� Sender    
    Listener listener; //����Ŭ���� Listener
    Login login;
    int port = 4003;
    DataInputStream dis;
    DataOutputStream dos;
    String chatID,msg,ip;
    boolean gameStart; 
    String playerName, playerScore, playerIdx, wPlayer, wCard; 
    String nonPlayer;
    String chatID0;
    public static LinkedList<String> cardHost = new LinkedList<String>();

    
    public void startChat(){//ä�� ���� ����
        bgm("//Play");
        EcardGUI.defLabel.setVisible(false);
        EcardGUI.vicLabel.setVisible(false);
        EcardGUI.drwLabel.setVisible(false);
        disableBtn(); //ī�� ��ư ��Ȱ��ȭ
        invisibleCard(); //�߰� ī�� �� ��Ȱ��ȭ
        chatID = Login.nickName; // Login ���� nickName �� �޾ƿ�.
		ip = Login.IP; // Login ���� ip�� �޾ƿ�

        try {
            Socket sc = new Socket(ip,port);
            sender = new Sender(sc, chatID);
            listener = new Listener(sc);
            new Thread(sender).start();
            new Thread(listener).start();

            //<-------�̺�Ʈ������-------->
            EcardGUI.text_msg.addKeyListener(new Sender(sc, chatID));
			EcardGUI.btn_exit.addActionListener(new Sender(sc, chatID));
			EcardGUI.btn_Ready.addActionListener(new Sender(sc, chatID));
			EcardGUI.btn_myKing.addActionListener(new Sender(sc, chatID));
			EcardGUI.btn_mySlav.addActionListener(new Sender(sc, chatID));
			EcardGUI.btn_myCtzn1.addActionListener(new Sender(sc, chatID));
			EcardGUI.btn_myCtzn2.addActionListener(new Sender(sc, chatID));
			EcardGUI.btn_myCtzn3.addActionListener(new Sender(sc, chatID));
			EcardGUI.btn_myCtzn4.addActionListener(new Sender(sc, chatID));
        } catch (UnknownHostException ue){
            pln("ȣ��Ʈ�� ã�� �� ����");
        } catch (IOException ie) {
            pln("������ �����ִ°� ����");
			System.exit(0);
        }

    }

    //Ŭ���̾�Ʈ ���� ������Ʈ
    public void updateClientList(){
        if(Integer.parseInt(playerIdx)==0){
            EcardGUI.myScore1.setText(playerScore);
            EcardGUI.yourScore2.setText(playerScore);
        }else if(Integer.parseInt(playerIdx) == 1){
            EcardGUI.myScore2.setText(playerScore);
            EcardGUI.yourScore1.setText(playerScore);
        }
    }


    public void pln(String str){
        System.out.println(str);
    }

    public void bgm(String play){ // BGM ��� & ����
        File file = new File("bgm\\bgm.wav");
        //System.out.println(file.exists()); //true
        FloatControl volume; 
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            if(play.equals("//Play")){
                clip.open(stream);
                clip.start();
                volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-10.0f);
            }else if(play.equals("//Stop")){
                clip.stop();
            }
        } catch(Exception e) {
            
            e.printStackTrace();
        }
    }

    //��ư Ȱ��ȭ
    void enableBtn() {
        EcardGUI.btn_myKing.setEnabled(true);
        EcardGUI.btn_mySlav.setEnabled(true);
        EcardGUI.btn_myCtzn1.setEnabled(true);
        EcardGUI.btn_myCtzn2.setEnabled(true);
        EcardGUI.btn_myCtzn3.setEnabled(true);
        EcardGUI.btn_myCtzn4.setEnabled(true);
    }

    //��ư ��Ȱ��ȭ
    void disableBtn() {
        EcardGUI.btn_myKing.setEnabled(false);
        EcardGUI.btn_mySlav.setEnabled(false);
        EcardGUI.btn_myCtzn1.setEnabled(false);
        EcardGUI.btn_myCtzn2.setEnabled(false);
        EcardGUI.btn_myCtzn3.setEnabled(false);
        EcardGUI.btn_myCtzn4.setEnabled(false);

    }


    void invisibleCard(){
        EcardGUI.laftKing.setVisible(false);
        EcardGUI.laftSlav.setVisible(false);
        EcardGUI.laftCtzn.setVisible(false);
        EcardGUI.rightKing.setVisible(false);
        EcardGUI.rightSlav.setVisible(false);
        EcardGUI.rightCtzn.setVisible(false);
    }




    //���� ������
    class Listener extends Thread{
        Socket sc;
        DataInputStream dis;

        Listener(Socket sc){
            this.sc = sc;
            try {
                dis = new DataInputStream(sc.getInputStream());
            } catch (IOException ie) {
            }
        }

        public void run(){
            while(dis !=null){
                try {
                    String msg = dis.readUTF();

                    //���� ���� �÷��̾�� �� ī�� ����
                    if(msg.startsWith("//King ") && msg.indexOf(cardHost.get(0)) != -1){
                        EcardGUI.btn_mySlav.setVisible(false);
                        EcardGUI.btn_myKing.setVisible(true);
                        EcardGUI.jKing.setVisible(false);
                        EcardGUI.jSlave.setVisible(true);
                        EcardGUI.myScore2.setVisible(false);
                        EcardGUI.myScore1.setVisible(true);
                        EcardGUI.yourScore2.setVisible(false);
                        EcardGUI.yourScore1.setVisible(true);
                    }else if(msg.startsWith("//Slav ") && msg.indexOf(cardHost.get(0)) != -1){
                        EcardGUI.btn_myKing.setVisible(false);
                        EcardGUI.btn_mySlav.setVisible(true);
                        EcardGUI.jSlave.setVisible(false);
                        EcardGUI.jKing.setVisible(true);
                        EcardGUI.myScore1.setVisible(false);
                        EcardGUI.myScore2.setVisible(true);
                        EcardGUI.yourScore1.setVisible(false);
                        EcardGUI.yourScore2.setVisible(true);
                    }else if(msg.startsWith("//Chnge")){
                        
                        String temp = msg.substring(7,13);
                        String cName = msg.substring(msg.indexOf(" ")+1);
                        if(temp.equals("//King") && cName.equals(cardHost.get(0))){
                            EcardGUI.btn_mySlav.setVisible(false);
                            EcardGUI.btn_myKing.setVisible(true);
                            EcardGUI.jKing.setVisible(false);
                            EcardGUI.jSlave.setVisible(true);
                            EcardGUI.dialog();
                            
                        }else if(temp.equals("//Slav") && cName.equals(cardHost.get(0))){
                            EcardGUI.btn_myKing.setVisible(false);
                            EcardGUI.btn_mySlav.setVisible(true);
                            EcardGUI.jSlave.setVisible(false);
                            EcardGUI.jKing.setVisible(true);
                            EcardGUI.dialog();

                        }

                    }else if(msg.equals("//GmEnd")){
                        gameStart = false;
                        disableBtn();
                        EcardGUI.btn_Ready.setEnabled(false); 
                        EcardGUI.gameEnd();

                    }else if(msg.startsWith("//Randm")){
                        nonPlayer = msg.substring(7);
                        disableBtn();
                        dos.writeUTF("//Press"+"//Ctzn"+nonPlayer);
                        EcardGUI.text_chatLog.append("[ �ð��ʰ��� <citizen> ī�带 �����մϴ� .. ] \n");
                        EcardGUI.scroll.getVerticalScrollBar().setValue(EcardGUI.scroll.getVerticalScrollBar().getMaximum());
                        dos.flush();

                    }else if(msg.startsWith("//SList")){ //�÷��̾� ���� �޾ƿ�
                        playerName = msg.substring(7, msg.indexOf(" "));
                        playerScore = msg.substring(msg.indexOf(" ") + 1, msg.indexOf("#"));
                        playerIdx = msg.substring(msg.indexOf("#") + 1);
                        updateClientList();
                        EcardGUI.btn_Ready.setEnabled(true); 
    
                    }else if(msg.startsWith("//Start")){
                        enableBtn();
                        invisibleCard();
                        EcardGUI.btn_myBack.setVisible(true);
                        EcardGUI.btn_yourBack.setVisible(true);
                        gameStart = true;
                        EcardGUI.text_chatLog.append("[ Game Start : ī�带 �������ּ���..]"+ "\n");
                        EcardGUI.scroll.getVerticalScrollBar().setValue(EcardGUI.scroll.getVerticalScrollBar().getMaximum());
                    }else if(msg.startsWith("//Timer")){
                        EcardGUI.jTimer.setText(msg.substring(7));

                    }else if(msg.startsWith("//Wcard")){
                        wPlayer = msg.substring(7, msg.indexOf("#"));
                        wCard = msg.substring(msg.indexOf("#") + 1);
                        if(wPlayer.equals(cardHost.get(0))){
                            // EcardGUI.text_chatLog.append("���ʰ� : "+ wCard + "\n");
                            if(wCard.equals("King")){
                                EcardGUI.btn_myBack.setVisible(false);
                                EcardGUI.rightKing.setVisible(true);
                            }else if(wCard.equals("Slav")){
                                EcardGUI.btn_myBack.setVisible(false);
                                EcardGUI.rightSlav.setVisible(true);
                            }else if(wCard.equals("Ctzn")){
                                EcardGUI.btn_myBack.setVisible(false);
                                EcardGUI.rightCtzn.setVisible(true);
                            }
                        } else {
                            // EcardGUI.text_chatLog.append("�����ʰ� : "+ wCard + "\n");
                            if(wCard.equals("King")){
                                EcardGUI.btn_yourBack.setVisible(false);
                                EcardGUI.laftKing.setVisible(true);
                            }else if(wCard.equals("Slav")){
                                EcardGUI.btn_yourBack.setVisible(false);
                                EcardGUI.laftSlav.setVisible(true);
                            }else if(wCard.equals("Ctzn")){
                                EcardGUI.btn_yourBack.setVisible(false);
                                EcardGUI.laftCtzn.setVisible(true);
                            }
                        }

                    } else if(!msg.startsWith("//Slav")){
                        EcardGUI.text_chatLog.append(msg + "\n");
                        EcardGUI.scroll.getVerticalScrollBar().setValue(EcardGUI.scroll.getVerticalScrollBar().getMaximum());
                    }
                } catch (IOException io) {
                    EcardGUI.text_chatLog.append("[ �������� ������ ���������ϴ�. ]\n[ 3�� �� ���α׷��� �����մϴ� .. ]");
                    EcardGUI.scroll.getVerticalScrollBar().setValue(EcardGUI.scroll.getVerticalScrollBar().getMaximum());     
                    try{
                        Thread.sleep(3000);         
                        System.exit(0);
                    }catch (InterruptedException ite) {
                        System.out.println(ite);
                    }
                }
            }
       
        }
        
    }


    //����Ŭ���� -- �г��Ӱ� ä�� �۽�
    public class Sender extends Thread implements KeyListener, ActionListener{
        Socket sc;
        String chatID0;
        String msg;

        Sender(Socket sc, String chatID){
            this.sc = sc;
            this.chatID0 = chatID;
            try {
                dos = new DataOutputStream(this.sc.getOutputStream());
            } catch (Exception e) {
            }           
        }

        public void run(){
            try {
                dos.writeUTF(chatID0);
                cardHost.add(chatID0);
            } catch (IOException ie) {
            }
        }

        public void keyPressed(KeyEvent ke){ //ä���Է�
            if(ke.getKeyCode() == KeyEvent.VK_ENTER){
                msg = EcardGUI.text_msg.getText();
                System.out.println(msg);
                EcardGUI.text_msg.setText("");
                try {
                    dos.writeUTF("//Chat "+chatID0+">> "+msg);
                    dos.flush();
                } catch (IOException ie) {
                }
            }
        }
        public void keyTyped(KeyEvent e){}
		public void keyReleased(KeyEvent e){}

        public void actionPerformed(ActionEvent e){
            if(e.getSource() ==  EcardGUI.btn_Ready) {
                try {

                    dos.writeUTF("//Ready");
                    dos.flush();
                    invisibleCard();
                    EcardGUI.btn_myBack.setVisible(true);
                    EcardGUI.btn_yourBack.setVisible(true);
                    EcardGUI.btn_Ready.setEnabled(false);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if(e.getSource() == EcardGUI.btn_exit){
                System.exit(0);
            }
            if(e.getSource() == EcardGUI.btn_myKing){
                try {
                    dos.writeUTF("//Press"+"//King"+chatID0);
                    EcardGUI.btn_myBack.setVisible(false);
                    EcardGUI.rightKing.setVisible(true);
                    EcardGUI.text_chatLog.append("[ ����� ������ ī��� <King> ī���Դϴ� .. ] \n");
                    EcardGUI.scroll.getVerticalScrollBar().setValue(EcardGUI.scroll.getVerticalScrollBar().getMaximum());
                    disableBtn();
                    dos.flush();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if(e.getSource() == EcardGUI.btn_mySlav){
                try {
                    dos.writeUTF("//Press"+"//Slav"+chatID0);
                    EcardGUI.btn_myBack.setVisible(false);
                    EcardGUI.rightSlav.setVisible(true);
                    EcardGUI.text_chatLog.append("[ ����� ������ ī��� <Slave> ī���Դϴ� .. ] \n");
                    EcardGUI.scroll.getVerticalScrollBar().setValue(EcardGUI.scroll.getVerticalScrollBar().getMaximum());
                    disableBtn();
                    dos.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if(e.getSource()== EcardGUI.btn_myCtzn1){
                try {
                    dos.writeUTF("//Press"+"//Ctzn"+chatID0);
                    EcardGUI.btn_myBack.setVisible(false);
                    EcardGUI.rightCtzn.setVisible(true);
                    EcardGUI.text_chatLog.append("[ ����� ������ ī��� <citizen> ī���Դϴ� .. ] \n");
                    EcardGUI.scroll.getVerticalScrollBar().setValue(EcardGUI.scroll.getVerticalScrollBar().getMaximum());
                    disableBtn();
                    dos.flush();
                } catch (IOException e1) {
                    //TODO: handle exception
                }
            }
            if(e.getSource()== EcardGUI.btn_myCtzn2){
                try {
                    dos.writeUTF("//Press"+"//Ctzn"+chatID0);
                    EcardGUI.btn_myBack.setVisible(false);
                    EcardGUI.rightCtzn.setVisible(true);
                    EcardGUI.text_chatLog.append("[ ����� ������ ī��� <citizen> ī���Դϴ� .. ] \n");
                    EcardGUI.scroll.getVerticalScrollBar().setValue(EcardGUI.scroll.getVerticalScrollBar().getMaximum());
                    disableBtn();
                    dos.flush();
                } catch (IOException e1) {
                    //TODO: handle exception
                }
            }
            if(e.getSource()== EcardGUI.btn_myCtzn3){
                try {
                    dos.writeUTF("//Press"+"//Ctzn"+chatID0);
                    EcardGUI.btn_myBack.setVisible(false);
                    EcardGUI.rightCtzn.setVisible(true);
                    EcardGUI.text_chatLog.append("[ ����� ������ ī��� <citizen> ī���Դϴ� .. ] \n");
                    EcardGUI.scroll.getVerticalScrollBar().setValue(EcardGUI.scroll.getVerticalScrollBar().getMaximum());
                    disableBtn();
                    dos.flush();
                } catch (IOException e1) {
                    //TODO: handle exception
                }
            }
            if(e.getSource()== EcardGUI.btn_myCtzn4){
                try {
                    dos.writeUTF("//Press"+"//Ctzn"+chatID0);
                    EcardGUI.btn_myBack.setVisible(false);
                    EcardGUI.rightCtzn.setVisible(true);
                    EcardGUI.text_chatLog.append("[ ����� ������ ī��� <citizen> ī���Դϴ� .. ] \n");
                    EcardGUI.scroll.getVerticalScrollBar().setValue(EcardGUI.scroll.getVerticalScrollBar().getMaximum());
                    disableBtn();
                    dos.flush();
                } catch (IOException e1) {
                    //TODO: handle exception
                }
            }
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        
    }
}


