package testg01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class DateClient {
public static void main(String[] args) throws Exception{
	Socket s = new Socket("로컬소켓",5006);
	BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	String res = in.readLine();
	System.out.println(res);
	System.exit(0);
}
}
