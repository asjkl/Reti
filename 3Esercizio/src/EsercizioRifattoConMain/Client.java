package EsercizioRifattoConMain;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

public class Client {
	Socket socket;
	BufferedReader inFromServer;
	DataOutputStream outToServer;
	ArrayList<Integer> valori;
	int totale;
	int indice;

	public Client() throws UnknownHostException, IOException {
		this.socket = new Socket("127.0.0.1", 1234);
		this.inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.outToServer = new DataOutputStream(socket.getOutputStream());
		this.valori = new ArrayList<>();
		this.totale = 0;
		System.out.println("CLIENT PRONTO");
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
		return totale;
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Client client=new Client();
		String read = null;
		try {
			
				read = client.inFromServer.readLine();

				System.out.println("client->: " + read);
				StringTokenizer tk = new StringTokenizer(read);
				while (tk.hasMoreTokens()) {
					String tmp = tk.nextToken().substring(4);
					client.valori.add(Integer.parseInt(tmp));
				}
				client.totale = client.contaPrimi(client.valori.get(0), client.valori.get(1));
				client.outToServer.writeBytes("PRIMI=" +client.totale+ '\n');
				client.socket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
