package testg03.chat;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Vector;

public class CharServer extends Frame implements ActionListener{
	Button btnexit;
	TextArea ta;
	Vector vchatlist;//*
	ServerSocket ss;//*
	Socket sockclient;//*
	
	public CharServer() {
		setTitle("ä�ü���");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();//*exit(1)?
			}
		});
		vchatlist=new Vector<>();
		btnexit=new Button("��������");
		btnexit.addActionListener(this);
		ta=new TextArea();
		add(ta, BorderLayout.CENTER);
		add(btnexit, BorderLayout.SOUTH);
		setBounds(250,250,200,200);
		setVisible(true);
		//chatstart()�޼ҵ�ȣ��
		chatstart();
	}
	public void chatstart() {
		//���� ����
		try {
			ss=new ServerSocket(5005);
			while (true) {
				sockclient=ss.accept();//*
				ta.append(sockclient.getInetAddress().getHostAddress()+"������\n");
				//�������� ip���
				ChatHandle threadchat=new ChatHandle();
				vchatlist.add(threadchat);
				threadchat.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		dispose();// ��ư�� �̰��ϸ� ����
	}
	public static void main(String[] args) {
		new CharServer();
	}
	class ChatHandle extends Thread {
		BufferedReader br=null; //�Է´��
		PrintWriter pw=null; //��´��
		
		public ChatHandle() {
			try {
				InputStream is=sockclient.getInputStream(); //�Է´��
			
				br=new BufferedReader(new InputStreamReader(is));
				OutputStream os=sockclient.getOutputStream(); //��´��
				pw=new PrintWriter(new OutputStreamWriter(os));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void sendAllClient(String msg) {
			int size=vchatlist.size();
			try {
				for (int i = 0; i < size; i++) {
					ChatHandle chr=(ChatHandle)vchatlist.elementAt(i);
				
					chr.pw.println(msg);
					chr.pw.flush();
				}
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				String name=br.readLine();
				sendAllClient(name+"�Բ��� ����");
				while (true) { //ä�ó���ޱ�
					String msg=br.readLine(); //
					String str=sockclient.getInetAddress().getHostName();
					ta.append(msg+"\n"); //ä�ó�����  ta�� �߰�
					if (msg.equals("@@Exit")) {
						break;
					}else {
						sendAllClient(name+" : "+msg);
						//������ ��忡�� �޼��� ����
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				vchatlist.remove(this);
				try {
					br.close();
					pw.close();
					sockclient.close();
				} catch (IOException e) {
				}
			}//finally
		}//run
	}
}