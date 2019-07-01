package testg03.chat2;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient extends Frame implements Runnable{ // 일단 액션 ㄴ
 Button bex; 
 Button bse; 
 Button bco; 
 
 TextArea tl ;//채팅내용
 TextField tf ;// 아이피 입력
 TextField tf1;//아이디?
 TextField tf2 ;//채팅 입력창
 
 Socket c;//클라이언트 소켓
 BufferedReader br;//입력버퍼
 PrintWriter pw;//출력
 String sip;// 서버아이피 주소
 final int port = 5025;
 CardLayout cl;// 카드 레이아웃
 
	public ChatClient() {//일단 기본 생성자 
		setTitle("채팅");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
//				super.windowClosing(e);
				dispose();
			}
		});
		cl = new CardLayout();
		setLayout(cl);
		Panel p = new Panel();
		p.setBackground(new Color(0,200,150));
		p.setLayout(new BorderLayout());
		bco = new Button("서버접속");
	
		tf = new TextField("192.168.0.48",15);//자신의 아이피
		tf1 = new TextField("규태",15);//아이디
		Panel p1 = new Panel();
		p1.add(new Label("서버아이피"));
		p1.add(tf);
		p1.add(new Label("대화명"));
		p1.add(tf1);
		
		//ㅎㅁ
		Panel chat = new Panel();
		chat.setLayout(new BorderLayout());
		Label cha = new Label("채팅접속화면",Label.CENTER);
		p.add(cha,"North");
		p.add(p1,"Center");
		p.add(bco,"South");
		
		//화면구성
		tl = new TextArea();//내용 보여
		tf2 = new TextField("",25);//입력
		bex = new Button("종료");
		bco.addActionListener(e->{// 연결 액션
			sip = tf.getText();
			Thread th = new Thread(this);
			th.start();
			cl.show(this,"채팅창");
		});
		bex.addActionListener(e->{//나가기 액션
			dispose();
		});
		bse = new Button("전송");
		bse.addActionListener(e->{//전송 액션
			String msg = tf2.getText();
			pw.println(msg);
			pw.flush();
			tf2.setText("");
			tf2.requestFocus();//커서 두기
		});
		tf2.addActionListener(e->{//여기도 액션
					String msg = tf2.getText();
					pw.println(msg);
					pw.flush();
					tf2.setText("");
					tf2.requestFocus();//커서 두기
					
				
			
		});
		Panel p2 = new Panel();
		p2.add(tf2);
		p2.add(bse);
		p2.add(bex);
		
		Label ct = new Label("채팅프로그램",Label.CENTER);
		chat.add(ct,"North");
		chat.add(tl,"Center");
		chat.add(p2,"South");
		
		add(p,"접속창");
		add(chat,"채팅창");
		cl.show(this,"접속창");//ㅇㅇ
		setBounds(250,250,300,300);
		setSize(500,300);
		
		
		setVisible(true);
	}
	@Override
	public void run() {
		try {
			c = new Socket(sip, port);//여긴 형식임 외우셈
			InputStream is = c.getInputStream();
			OutputStream os = c.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is));
			pw = new PrintWriter(new OutputStreamWriter(os));
			String msg = tf1.getText();//대화명 얻기
			pw.println(msg);
			pw.flush();// 내용 비우기 이거 해주는게 좋음
			tf2.requestFocus();
			while (true) {
				msg=br.readLine();
				tl.append(msg+"\n");//대화명에 줄바꿈 추가
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		} 
	
	}
//여기 액션자리
	public static void main(String[] args) {
		new ChatClient();
	}

}
