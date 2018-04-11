import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread{
	private BufferedReader dalServer;
	private DataOutputStream alServer;
	private Socket socket;
	
	public Client() throws UnknownHostException, IOException {
		this.socket=new Socket("127.0.0.1", 1234);
	}
	
	@Override
	public void run() {
		BufferedReader read=new BufferedReader(new InputStreamReader(System.in));
		try {
			dalServer=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			alServer=new DataOutputStream(socket.getOutputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while(true){
			try {
				String string=read.readLine();
				if(string.equalsIgnoreCase("end")){
					alServer.writeBytes(string+"\n");
					break;
				}else{
					alServer.writeBytes(string+"\n");
					String tmp=dalServer.readLine();
					System.out.println("S: "+tmp);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Client client=new Client();
		client.start();
	}
}
