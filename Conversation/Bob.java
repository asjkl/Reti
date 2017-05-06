import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.plaf.SliderUI;

public class Bob {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket bob=new Socket("127.0.0.1", 1234);
		
		System.out.println("BOB PRONTO");
		int contatoreLettura=0;
		
		BufferedReader leggiDalServer=new BufferedReader(new InputStreamReader(bob.getInputStream()));
		DataOutputStream mandaAlServer=new DataOutputStream(bob.getOutputStream());
		
		while(true){
			String tmp=leggiDalServer.readLine();
			contatoreLettura++;
			System.out.println(tmp);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
