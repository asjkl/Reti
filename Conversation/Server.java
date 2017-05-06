import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	public static void main(String[] args) throws IOException {

		ArrayList<Socket> lista = new ArrayList<>();
		ArrayList<BufferedReader> dalCliente = new ArrayList<>();
		ArrayList<DataOutputStream> fuoriAlCliente = new ArrayList<>();

		ServerSocket welcomeSocket = new ServerSocket(1234);
		System.out.println("SERVER PRONTO");

		new Thread() {
			public void run() {
				while (true) {
					System.out.println(lista.size()+" "+dalCliente.size());
					for (int a = 0; a < lista.size(); a++) {
						try {
							if (dalCliente.size() == lista.size()) {
								while (dalCliente.get(a).ready()) {
									String read = dalCliente.get(a).readLine();
									for (int b = 0; b < lista.size(); b++) {
										if (a != b) {
											fuoriAlCliente.get(b).writeBytes(read + '\n');
										}
									}
								}
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}
			};
		}.start();

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			lista.add(connectionSocket);
			dalCliente.add(new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())));
			fuoriAlCliente.add(new DataOutputStream(connectionSocket.getOutputStream()));
		}
	}
}
