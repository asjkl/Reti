package fattaDalProfessore;



import java.io.*;
import java.net.*;

public class TCPClient {

	//
	// throws Exception. Kids, don't do this at home.
	//

	public static void main(String argv[]) throws Exception {
		String clientCMD;
		String serverInput;

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket = new Socket("127.0.0.1", 3210);

		
		clientCMD = inFromUser.readLine();

		while (true) {
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			System.out.println("USER INPUT:" + clientCMD);
			outToServer.writeBytes(clientCMD + '\n');

			if (clientCMD.equalsIgnoreCase("<EXIT>"))
				break;
			
			serverInput = inFromServer.readLine();

			while (!serverInput.equalsIgnoreCase("<EOF>")) {
				System.out.println("FROM SERVER: " + serverInput);
				serverInput = inFromServer.readLine();
			}

			clientCMD = inFromUser.readLine();
		}

		clientSocket.close();

	}
};