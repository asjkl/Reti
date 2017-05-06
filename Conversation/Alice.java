import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Alice {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket alice=new Socket("127.0.0.1", 1234);
		System.out.println("ALICE PRONTA");
		int contatoreStampe=0;
		
		BufferedReader leggiDalServer=new BufferedReader(new InputStreamReader(alice.getInputStream()));
		DataOutputStream mandaAlServer=new DataOutputStream(alice.getOutputStream());
		
		while(true){
			String tmp=new Random().nextInt(27)+"a";
			mandaAlServer.writeBytes(tmp+ '\n');
			contatoreStampe++;
			 System.out.println(contatoreStampe);
		}
	}
}
