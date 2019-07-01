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
		setTitle("채팅서버");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();//*exit(1)?
			}
		});
		vchatlist=new Vector<>();
		btnexit=new Button("서버종료");
		btnexit.addActionListener(this);
		ta=new TextArea();
		add(ta, BorderLayout.CENTER);
		add(btnexit, BorderLayout.SOUTH);
		setBounds(250,250,200,200);
		setVisible(true);
		//chatstart()메소드호출
		chatstart();
	}
	public void chatstart() {
		//소켓 생성
		try {
			ss=new ServerSocket(5005);
			while (true) {
				sockclient=ss.accept();//*
				ta.append(sockclient.getInetAddress().getHostAddress()+"접속함\n");
				//접속자의 ip얻기
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
		dispose();// 버튼에 이거하면 꺼짐
	}
	public static void main(String[] args) {
		new CharServer();
	}
	class ChatHandle extends Thread {
		BufferedReader br=null; //입력담당
		PrintWriter pw=null; //출력담당
		
		public ChatHandle() {
			try {
				InputStream is=sockclient.getInputStream(); //입력담당
			
				br=new BufferedReader(new InputStreamReader(is));
				OutputStream os=sockclient.getOutputStream(); //출력담당
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
				sendAllClient(name+"님께서 입장");
				while (true) { //채팅내용받기
					String msg=br.readLine(); //
					String str=sockclient.getInetAddress().getHostName();
					ta.append(msg+"\n"); //채팅내용을  ta에 추가
					if (msg.equals("@@Exit")) {
						break;
					}else {
						sendAllClient(name+" : "+msg);
						//접속자 모드에게 메세지 전달
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