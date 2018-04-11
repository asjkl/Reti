package EsercizioRifattoConMain;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Server extends Thread {
	DataOutputStream[] outToClient = null;
	BufferedReader[] inFromClient = null;
	ServerSocket server = null;
	//Client[] arrayDiClient;
	ArrayList<Socket>arrayDiClient;
	ArrayList<Finiti> finish;
	int indiceClient;
	int min;
	int max;
	int divisione;
	int sommaTotale;
	boolean entrato = false;
	int estremoMIN = 0;
	int estremoMAX = min;
	int totaleClient = 3;

	public Server(int min, int max) throws IOException {
		this.server = new ServerSocket(1234);
		System.out.println("SERVER PRONTO");
		//this.arrayDiClient = new Client[totaleClient];
		arrayDiClient=new ArrayList<>();
		this.inFromClient = new BufferedReader[totaleClient];
		this.outToClient = new DataOutputStream[totaleClient];
		this.indiceClient = 0;
		this.min = min;
		this.max = max;
		this.divisione = (max - min) / totaleClient;
		if ((max - min) < totaleClient) {
			while ((max - min) % totaleClient != 0) {
				totaleClient--;
			}
			this.divisione = (max - min) / totaleClient;
		}
		this.sommaTotale = 0;
		this.finish = new ArrayList<>();
		for (int i = 0; i < totaleClient; i++) {
			finish.add(new Finiti(i, false));
		}
	}

	@Override
	public void run() {
		try {
			while (!bufferReadVuoti()) {
				while (indiceClient < totaleClient) {
					Socket socket;
					try {
						//arrayDiClient[indiceClient] = new Client(this, indiceClient);
						
						socket = server.accept();
						arrayDiClient.add(socket);
						inFromClient[indiceClient] = new BufferedReader(
								new InputStreamReader(socket.getInputStream()));
						outToClient[indiceClient] = new DataOutputStream(socket.getOutputStream());

						estremoMIN = estremoMAX;
						estremoMAX += divisione;
						if (entrato) {
							estremoMIN += 1;
						}
						entrato = true;

						String valori = "MIN=" + estremoMIN + " " + "MAX=" + estremoMAX;
						outToClient[indiceClient].writeBytes(valori + '\n');
						finish.add(new Finiti(indiceClient, false));
						indiceClient++;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				try {
					for (int a = 0; a < totaleClient; a++) {
						while (inFromClient[a].ready()) {
							sommaTotale += Integer.parseInt(inFromClient[a].readLine().substring(6));
							for (int b = 0; b < finish.size(); b++) {
								if (finish.get(b).client == a) {
									finish.get(b).finish = true;
								}
							}
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println(sommaTotale);
			server.close();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean bufferReadVuoti() throws IOException {
		for (int a = 0; a < finish.size(); a++) {
			if (finish.get(a).finish == false) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) throws IOException {
		Server server = new Server(0,10);
		server.start();
	}
}

class Finiti {
	int client;
	boolean finish;

	public Finiti(int client, boolean finish) {
		this.client = client;
		this.finish = finish;
	}
}

