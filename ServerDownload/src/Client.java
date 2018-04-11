import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

public class Client {
	public static void main(String[] args) throws IOException{
		Socket socket=new Socket("127.0.0.1", 1234);
		System.out.println("Client connesso!");
		
		BufferedReader inFromUser=new BufferedReader(new InputStreamReader(System.in));
		DataOutputStream outToServer=new DataOutputStream(socket.getOutputStream());
		BufferedReader inFromServer=new BufferedReader(new InputStreamReader(socket.getInputStream()));
	
		String numero=inFromServer.readLine();
		System.out.println("IdServer: "+numero);
		
//		uscita(socket);
		
		while(true){
			String string = inFromUser.readLine();
			System.out.println(string);
			if(!string.toUpperCase().equals("EXIT")){
				outToServer.writeBytes(string+"\n");
				String read=inFromServer.readLine();
				System.out.println(read);
			}else{
				uscita(socket, outToServer);
				break;
			}
		}		
	}

	private static void uscita(Socket socket, DataOutputStream outToServer) throws IOException {
		
		outToServer.writeBytes("EXIT");
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Ciao cliente, alla prossima!");
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}));
	}

//	private static void simulatoreSystemIn() {
//		String data="CONTENT";
//		InputStream stdin=System.in;
//		System.setIn(new ByteArrayInputStream(data.getBytes()));
//		Scanner scanner=new Scanner(System.in);
//		System.out.println(scanner.nextLine());
//		System.setIn(stdin);
//	}
}
