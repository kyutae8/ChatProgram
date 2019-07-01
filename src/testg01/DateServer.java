package testg01;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DateServer {
public static void main(String[] args) throws Exception{// 이거 까먹지 말자 
	ServerSocket ss = new ServerSocket(5006);
	try {
		while(true) {
			Socket socket = ss.accept();
			try {
				PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
				out.println(new Date().toString());
			} finally {
				socket.close();
			}
		}
	} finally {
		ss.close();
	}
}
}
