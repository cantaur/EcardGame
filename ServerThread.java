package ecardGame;

import java.io.*;
import java.net.*;
import java.util.*;
import ecardGame.ServerThread;

public class ServerThread extends Thread{
    private Socket gtsc;
    private DataInputStream dis;
    private DataOutputStream dos;

    public static final int maxclient = 2;
    public String pName; //�÷��̾� ID
    public int score;
    boolean gameStart;
    

    //�迭
    private static LinkedHashMap<String, DataOutputStream> clientList = new LinkedHashMap<String, DataOutputStream>(ServerGUI.maxclient);
    private static LinkedHashMap<String, Integer> clientInfo = new LinkedHashMap<String,Integer>(ServerGUI.maxclient);
    private static Vector<Integer> readyPlayer = new Vector<Integer>(); 
    private static Vector<Integer> roundCount = new Vector<Integer>();
     
    private static String client1 = "";
    private static String client2 = "";
    private static String client1Card = "";
    private static String client2Card = "";

    public ServerThread(Socket sc){ //�����ڿ��� ��Ʈ�� ����
        gtsc = sc;        
        try {
            dis = new DataInputStream(sc.getInputStream());
            dos = new DataOutputStream(sc.getOutputStream());
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }
    

    public void run(){//�÷��̾� �ʿ� ����, filter �޼ҵ� ����, �÷��̾� ����� ���� 
        try {
            pName = dis.readUTF(); 
            if(!clientList.containsKey(pName)){ //�г��� �ߺ�����, �ߺ��Ǹ� ���� ����
                clientList.put(pName,dos); //�ʿ� �÷��̾��� �̸�, �Է��� ���°� ����
                clientInfo.put(pName,score); //�ʿ� �÷��̾��� �̸�, ������ ����
            }else if(clientList.containsKey(pName)){
                gtsc.close();
            }else if(clientList.size()> ServerGUI.maxclient){ //�ο��� ����
                gtsc.close();
            }


            if(client1 == "") { //client1�� ��������� ���ʷ� �α����� ��� ����
                client1 = pName;
                sendMessage("//King "+client1);
            } else {
                client2 = pName;
                sendMessage("//Slav "+client2);
            }
            sendMessage("[ "+pName+"���� �����ϼ̽��ϴ�. ]");
            //setClientInfo();
            while(true){
                String msg = dis.readUTF(); //Ŭ���̾�Ʈ�κ��� ���ŵǴ� �޼��� ����
                filter(msg); //�޼���or�������� ��ε�ĳ��Ʈ
            }
        } catch (IOException ie) {
            clientList.remove(pName); //���������� ����Ʈ���� ���� 
            clientInfo.remove(pName, this); 
            if(clientList.size()==0){
                try {
                    ServerGUI.ss.close();
                    System.exit(0);
                } catch (Exception e) {
                    //TODO: handle exception
                }
            }
            sendMessage("[ "+pName+"���� �����ϼ̽��ϴ�. ]");
            // pln(pName+"�� ����!");
            readyPlayer.removeAllElements();
            System.out.println(readyPlayer.size());


        } 
        finally{
            try {
                if(dis != null) dis.close(); 
                if(dos != null) dos.close(); 
                if(gtsc != null) gtsc.close();
            } catch (IOException ie) {
                //TODO: handle exception
            }
        }
    }

    

    void filter(String msg){//Ŭ���̾�Ʈ�ʿ��� ������ ������ �����÷���, ä�� �� ���� �ٸ� ������ ��
        StopWatch tm = new StopWatch(); 
        String temp = msg.substring(0,7);
        
        if(temp.equals("//Chat ")){ //ä���� �Է¹޾��� ���
            // pln(msg.substring(7));
            sendMessage(msg.substring(7)); 
        }else if(temp.equals("//Ready")){ //�غ��ư�� �ԷµǾ��� ���
            readyPlayer.addElement(1);
            if(readyPlayer.size()>=2&&readyPlayer.size() == clientList.size()) { //�غ��ư �迭�� ������� Ŭ���̾�Ʈ����Ʈ ������(�ִ�:2) ������ ����
                for(int i=3; i>0; i--){
                    try{
                        sendMessage("[ " + i + "�� �� ������ �����մϴ� .. ]");						 	
                        Thread.sleep(1000);
                    }catch(InterruptedException ie){}
                }
                gameStart = true;
                tm.start();
                sendMessage("//Start"); //Start ����� ����	
                //readyPlayer.removeAllElements();
            }
        }else if(temp.equals("//Press")){//�÷��̾ ī�带 ������ ���
            String cardType = msg.substring(7,13); //������ ī�� ����
            String member = msg.substring(13);     //�÷��̾� ����

            if(member.equals(client1)) {
                client1Card = cardType;
            }else if(member.equals(client2)) {
                client2Card = cardType;
            }
            if(!client2Card.equals("") && !client1Card.equals("")) {
                readyPlayer.removeAllElements();
                switch (client1Card) {//�÷��̾�1
                    case "//Ctzn": //�÷��̾�1�� �ù�ī�� ������
                        if(client2Card.equals("//Slav")){//�÷��̾�2 �뿹
                            sendMessage("[ WIN : "+ client1 +" ]" + "\n" + "[ LOSE : "+client2+" ]");
                            sendMessage("//Wcard" + client1 + "#Ctzn");
                            sendMessage("//Wcard" + client2 + "#Slav");
                            clientInfo.put(client1, clientInfo.get(client1)+1); //���� ���� �߰�
                            setClientInfo();
                            // showCard();
                            setRound();
                            client1Card = client2Card = "";
                            break;
                        }else if(client2Card.equals("//Ctzn")){//�÷��̾�2 �ù�
                            sendMessage("[ ���º�! �ٽ� �ϼ���. ]");
                            sendMessage("//Wcard" + client1 + "#Ctzn");
                            sendMessage("//Wcard" + client2 + "#Ctzn");
                            setClientInfo();
                            // showCard();
                            client1Card = client2Card = "";
                            break;
                        }else if(client2Card.equals("//King")){
                            sendMessage("[ WIN : "+ client2 +" ]" + "\n" + "[ LOSE : "+client1+" ]");
                            sendMessage("//Wcard" + client1 + "#Ctzn");
                            sendMessage("//Wcard" + client2 + "#King");
                            clientInfo.put(client2, clientInfo.get(client2)+1); //���� ���� �߰�
                            setClientInfo();
                            // showCard();
                            setRound();
                            client1Card = client2Card = "";
                            break;
                        }
                    case "//King": //�÷��̾�1
                        if(client2Card.equals("//Slav")){//�÷��̾�2 �뿹
                            sendMessage("[ WIN : "+ client1 +" ]" + "\n" + "[ LOSE : "+client2+" ]");
                            sendMessage("//Wcard" + client1 + "#King");
                            sendMessage("//Wcard" + client2 + "#Slav");
                            clientInfo.put(client2, clientInfo.get(client2)+1); //���� ���� �߰�
                            setClientInfo();
                            // showCard();
                            setRound();
                            client1Card = client2Card = "";
                            break;
                        }else if(client2Card.equals("//Ctzn")){//�÷��̾�2 �ù�
                            sendMessage("[ WIN : "+ client2 +" ]" + "\n" + "[ LOSE : "+client1+"> ]");
                            sendMessage("//Wcard" + client1 + "#King");
                            sendMessage("//Wcard" + client2 + "#Ctzn");
                            clientInfo.put(client1, clientInfo.get(client1)+1); //���� ���� �߰�
                            setClientInfo();
                            // showCard();
                            setRound();
                            client1Card = client2Card = "";
                            break;
                        }
                    case "//Slav": //�÷��̾�1
                        if(client2Card.equals("//King")){//�÷��̾�2 ��
                            sendMessage("[ WIN : "+ client1 +" ]" + "\n" + "[ LOSE : "+client2+" ]");
                            sendMessage("//Wcard" + client1 + "#Slav");
                            sendMessage("//Wcard" + client2 + "#King");
                            clientInfo.put(client1, clientInfo.get(client1)+1); //���� ���� �߰�
                            setClientInfo();
                            // showCard();
                            setRound();
                            client1Card = client2Card = "";
                            break;
                        }else if(client2Card.equals("//Ctzn")){//�÷��̾�2 �ù�
                            sendMessage("[ WIN : "+ client2 +" ]" + "\n" + "[ LOSE : "+client1+" ]");
                            sendMessage("//Wcard" + client1 + "#Slav");
                            sendMessage("//Wcard" + client2 + "#Ctzn");
                            clientInfo.put(client2, clientInfo.get(client2)+1); //���� ���� �߰�
                            setClientInfo();
                            // showCard();
                            setRound();
                            client1Card = client2Card = "";
                            break;
                        }
                    break;
                }
            }
        }
    }



    public void setRound(){
        if(roundCount.size() == 0){
            roundCount.add(2);
            //sendMessage("//Round2"); //�� ���� ������ ���� ǥ��? ���ص� ��� ������
        }else {
            int nextRound = roundCount.get(0) + 1;
            if(nextRound == 4) {
                roundCount.set(0, nextRound);
                //sendMessage("//Round" + nextRound);
                sendMessage("//Chnge"+"//Slav" +" "+client1); 
                sendMessage("//Chnge"+"//King" +" "+client2);
            }else if(nextRound == 7){
                sendMessage("//GmEnd");
            }else {
                roundCount.set(0, nextRound);
                //sendMessage("//Round" + nextRound);
            }
                
        }
    }


    public void setClientInfo(){ //clientInfo  key���� �÷��̾��, value���� ���ھ�
        String[] keys = new String[clientInfo.size()];
        int[] values = new int[clientInfo.size()];
        //String[] client = new String[2];
        int index = 0;
        for(Map.Entry<String, Integer> mapEntry : clientInfo.entrySet()){
            keys[index] = mapEntry.getKey();
            values[index] = mapEntry.getValue();
            index++;
        }
        for(int i=0; i<clientList.size(); i++){
            sendMessage("//SList" + keys[i] + " " + values[i] + "#" + i); 
            System.out.println("//SList" + keys[i] + " " + values[i] + "#" + i);
            //client[i] = keys[i];
            //System.out.println(client[i]);
        }

    }

    public void sendScore(){ //������ �Բ� �÷��̾� �̸�, ���� �۽�
        sendMessage("//CList"+client1+" "+clientInfo.get(client1));
        sendMessage("//CList"+client2+" "+clientInfo.get(client2));
        // sendMessage("//Times"+client1);
        // sendMessage("//Times"+client2);
    }

    public void showCard(){
        sendMessage("//Wcard"+client1+" "+client1Card);
        sendMessage("//Wcard"+client2+" "+client2Card);
    }

    public void sendMessage(String msg){ //�޼��� �۽� �޼ҵ�
        Iterator<String> iter = clientList.keySet().iterator();
        //���� key���� ����Ǿ� �ִ� �÷��̾�鿡�� �޼��� �۽�
        while(iter.hasNext()){
            try {
                DataOutputStream dos = clientList.get(iter.next());
                dos.writeUTF(msg);
                dos.flush();
            } catch (IOException ie) {
            }
        }
    }

    void pln(String str){
        System.out.println(str);
    }

    class StopWatch extends Thread {
		long preTime = System.currentTimeMillis();
		
		public void run() {
			
            try{
                
				while(gameStart == true){
					// sleep(10);
					long time = System.currentTimeMillis() - preTime;
					sendMessage("//Timer" + (toTime(time)));
                    //pln("//Timer" + (toTime(time)));
                    sleep(1000);
					if(toTime(time).equals("00")){
                        readyPlayer.removeAllElements();
						gameStart = false;
                        if((client1Card.equals(""))&& !client2Card.equals("")){
                            sendMessage("//Randm"+client1);
                            break;
                        }else if((client2Card.equals(""))&& !client1Card.equals("")){
                            sendMessage("//Randm"+client2);
                            break;
                        }

                        
                        
                         // �ð� �ʰ���, ���� ����

						break;
					}else if(readyPlayer.size() == 0){
						break;
					}
				}
			}catch (Exception e){}

		}
		
		String toTime(long time){
			int s = (int)(33-(time % (1000.0 * 60) / 1000.0));
			return String.format("%02d", s);
		}
	}


}
