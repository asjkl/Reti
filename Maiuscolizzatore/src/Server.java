import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	
	private ServerSocket server;
	private String string;
	private BufferedReader dalClient;
	private DataOutputStream alClient;
	
	
	public Server() throws IOException {
		this.server=new ServerSocket(1234);
	}
	
	@Override
	public void run() {
		try{
		Socket clientSocket = null;
		clientSocket = server.accept();
		dalClient=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		alClient=new DataOutputStream(clientSocket.getOutputStream());
		while(true){
			try {
				string=dalClient.readLine();
				System.out.println("C: "+string);
				
				if(string.equalsIgnoreCase("end")){
					clientSocket.close();
					break;
				}else{
					string=string.toUpperCase();
					alClient.writeBytes(string+"\n");
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		server.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		Server server=new Server();
		server.start();
		
	}	
}
