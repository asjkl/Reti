import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	public static void main(String[] args) throws IOException {
		int intervalloMin=0;
		int intervalloMax=100;
		
		String risultato;
		String invia;
		int somma=0;
		
		int estremoMIN=0;
		int estremoMAX=intervalloMin;
		boolean entrato=false;
		
		ServerSocket welcomeSocket = new ServerSocket(9999);
		System.out.println("SERVER PRONTO");
		int pezzoTotale=intervalloMax-intervalloMin;
		int cont=3;
		while((pezzoTotale%cont)!=0){
			cont++;
		}
		int divisione=pezzoTotale/cont;
		
		while (estremoMAX<intervalloMax) {
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			
			estremoMIN=estremoMAX;
			estremoMAX+=divisione;
			if(entrato){
				estremoMIN+=1;
			}
			entrato=true;
			invia="MIN="+estremoMIN+" "+"MAX="+estremoMAX;
			
			System.out.println("server: "+invia);
			outToClient.writeBytes(invia+ '\n');
			
			risultato=inFromClient.readLine();
			System.out.println(risultato);
			somma+=Integer.parseInt(risultato.substring(6));
			System.out.println("somma-> "+somma);
		}
	}
}
