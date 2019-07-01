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

public class ChatClient extends Frame implements Runnable{ // �ϴ� �׼� ��
 Button bex; 
 Button bse; 
 Button bco; 
 
 TextArea tl ;//ä�ó���
 TextField tf ;// ������ �Է�
 TextField tf1;//���̵�?
 TextField tf2 ;//ä�� �Է�â
 
 Socket c;//Ŭ���̾�Ʈ ����
 BufferedReader br;//�Է¹���
 PrintWriter pw;//���
 String sip;// ���������� �ּ�
 final int port = 5025;
 CardLayout cl;// ī�� ���̾ƿ�
 
	public ChatClient() {//�ϴ� �⺻ ������ 
		setTitle("ä��");
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
		bco = new Button("��������");
	
		tf = new TextField("192.168.0.48",15);//�ڽ��� ������
		tf1 = new TextField("����",15);//���̵�
		Panel p1 = new Panel();
		p1.add(new Label("����������"));
		p1.add(tf);
		p1.add(new Label("��ȭ��"));
		p1.add(tf1);
		
		//����
		Panel chat = new Panel();
		chat.setLayout(new BorderLayout());
		Label cha = new Label("ä������ȭ��",Label.CENTER);
		p.add(cha,"North");
		p.add(p1,"Center");
		p.add(bco,"South");
		
		//ȭ�鱸��
		tl = new TextArea();//���� ����
		tf2 = new TextField("",25);//�Է�
		bex = new Button("����");
		bco.addActionListener(e->{// ���� �׼�
			sip = tf.getText();
			Thread th = new Thread(this);
			th.start();
			cl.show(this,"ä��â");
		});
		bex.addActionListener(e->{//������ �׼�
			dispose();
		});
		bse = new Button("����");
		bse.addActionListener(e->{//���� �׼�
			String msg = tf2.getText();
			pw.println(msg);
			pw.flush();
			tf2.setText("");
			tf2.requestFocus();//Ŀ�� �α�
		});
		tf2.addActionListener(e->{//���⵵ �׼�
					String msg = tf2.getText();
					pw.println(msg);
					pw.flush();
					tf2.setText("");
					tf2.requestFocus();//Ŀ�� �α�
					
				
			
		});
		Panel p2 = new Panel();
		p2.add(tf2);
		p2.add(bse);
		p2.add(bex);
		
		Label ct = new Label("ä�����α׷�",Label.CENTER);
		chat.add(ct,"North");
		chat.add(tl,"Center");
		chat.add(p2,"South");
		
		add(p,"����â");
		add(chat,"ä��â");
		cl.show(this,"����â");//����
		setBounds(250,250,300,300);
		setSize(500,300);
		
		
		setVisible(true);
	}
	@Override
	public void run() {
		try {
			c = new Socket(sip, port);//���� ������ �ܿ��
			InputStream is = c.getInputStream();
			OutputStream os = c.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is));
			pw = new PrintWriter(new OutputStreamWriter(os));
			String msg = tf1.getText();//��ȭ�� ���
			pw.println(msg);
			pw.flush();// ���� ���� �̰� ���ִ°� ����
			tf2.requestFocus();
			while (true) {
				msg=br.readLine();
				tl.append(msg+"\n");//��ȭ�� �ٹٲ� �߰�
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		} 
	
	}
//���� �׼��ڸ�
	public static void main(String[] args) {
		new ChatClient();
	}

}
