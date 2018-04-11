import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Posta {
	@SuppressWarnings("null")
	public static void main(String[] args) throws UnknownHostException, IOException {
		String nomeServer=args[0];
		String mitt=args[1];
		String dest=args[2];
		FileReader file=new FileReader("posta.txt");
		BufferedReader buff=new BufferedReader(file);
		ArrayList<String> appendi=new ArrayList<>();
		
		String text;
		do{
			text=buff.readLine();
			if(text!=null){
				appendi.add(text);
			}
		}while(text!=null);
			
		Socket socket=new Socket(nomeServer, 25);
		PrintWriter outToServer=new PrintWriter(socket.getOutputStream(), true);
		BufferedReader inFromServer=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String command="";
		
		System.out.println("S: "+inFromServer.readLine());
		command="HELO c";
		System.out.println("C: "+command);
		outToServer.println(command);
		System.out.println("S: "+inFromServer.readLine());
		command="MAIL FROM: <"+mitt+">";
		System.out.println("C: "+command);
		outToServer.println(command);
		System.out.println("S: "+inFromServer.readLine());
		command="RCPT TO: <"+dest+">";
		System.out.println("C: "+command);
		outToServer.println(command);
		System.out.println("S: "+inFromServer.readLine());
		command="DATA";
		System.out.println("C: "+command);
		outToServer.println(command);
		System.out.println("S: "+inFromServer.readLine());
		for (int i = 0; i < appendi.size(); i++) {
			System.out.println(appendi.get(i));
			outToServer.print(appendi.get(i));
		}
		System.out.println();
		outToServer.println();
		System.out.println("C: "+".");
		outToServer.println(".");
		System.out.println("S: "+inFromServer.readLine());
		command="QUIT";
		System.out.println("C: "+command);
		outToServer.println(command);
		System.out.println("S: "+inFromServer.readLine());
		socket.close();
	}
}
