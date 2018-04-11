package rifaccioDiNuovo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
	private Socket socket;
	private int id;
	private int min;
	private int max;
	private BufferedReader dalServer;
	private DataOutputStream alServer;
	
	public Client() throws UnknownHostException, IOException {
		this.socket=new Socket("127.0.0.1",1234);
		System.out.println("Schiavo creato");
	}
	
	private boolean eprimo(int n) {
		if (n <= 3)
			return true;
		if (n % 2 == 0)
			return false;
		for (int i = 3; i <= Math.sqrt(n); i++) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

	private int contaPrimi(int min, int max) {
		int totale = 0;
		for (int i = min; i <= max; i++) {
			if (eprimo(i)) {
				totale++;
			}
		}
		try {
			sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return totale;
	}
	
	@Override
	public void run() {
		try {
			
			dalServer=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			alServer=new DataOutputStream(socket.getOutputStream());
			
			String stringa=dalServer.readLine();				//ID:0|MIN:0|MAX:3
			
			System.out.println("SERVER: "+stringa);
			
			String []split=stringa.split("-");
			for (String string : split) {
				String []tmp=string.split(":");
				if(tmp[0].equalsIgnoreCase("id")){
					id=Integer.parseInt(tmp[1]);
				}else if(tmp[0].equalsIgnoreCase("min")){
					min=Integer.parseInt(tmp[1]);
				}else if(tmp[0].equalsIgnoreCase("max")){
					max=Integer.parseInt(tmp[1]);
				}
			}													
													
			int totaleNumeriPrimi=contaPrimi(min, max);												
			String stringaDaInviareAlServer="ID:"+id+"-TOTALE:"+totaleNumeriPrimi;
			alServer.writeBytes(stringaDaInviareAlServer+"\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Client client=new Client();
		client.start();
	}
	
	
	

	
}
