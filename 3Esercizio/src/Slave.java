import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Slave {
	public static void main(String argv[]) throws Exception {
		ArrayList<Integer>valori=new ArrayList<>();
		int somma=0;
		String read;
		
		Socket clientSocket = new Socket("127.0.0.1",9999);
		System.out.println("CLIENT PRONTO");

		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		read=inFromServer.readLine();
		StringTokenizer tk=new StringTokenizer(read);
		System.out.println("client: "+read);
		
		while(tk.hasMoreTokens()){
			String tmp=tk.nextToken().substring(4);
			valori.add(Integer.parseInt(tmp));
		}
		
		somma=contaPrimi(valori.get(0), valori.get(1));
		outToServer.writeBytes(("PRIMI="+somma+"\n"));
		clientSocket.close();
	}

	private static boolean eprimo(int n) {
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
	
	private static int contaPrimi(int min, int max) {
		int totale=0;
		for (int i = min; i <= max; i++) {
			if (eprimo(i)) {
				totale++;
			}
		}
	return totale;
	}
}
