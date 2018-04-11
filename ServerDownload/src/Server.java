import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
	
	//TODO PROBLEMI: I SOCKET NON VENGONO CANCENLLATI DALL'ARRAYLIST
	
	
	private static File folder=new File("C:/Users/Giovanni/Desktop/Esercizi ECLIPSE/RETI/ServerDownload/CartellaDownload");
	static int clientConnessi=0;
	static ArrayList<Socket> client=new ArrayList<>();
	static ArrayList<Boolean> clientExit=new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		
		ServerSocket ServerSocket=new ServerSocket(1234);
		System.out.println("Server pronto!");
		
		while(true){
			Socket socket=ServerSocket.accept();
			client.add(socket);
			clientExit.add(false);
			clientConnessi++;
			
			int tmp=restituisciID(clientExit);
			setup(tmp, socket);
			
			
			new Thread(){
				int id=tmp;
//				int id=client.size()-1;
				@Override
				public void run() {
					try {
						do{
							BufferedReader inFromClient=new BufferedReader(new InputStreamReader(client.get(id).getInputStream()));
							DataOutputStream outOfClient=new DataOutputStream(client.get(id).getOutputStream());
									
							String contenutoClient=inFromClient.readLine();
							//contenutoClient=contenutoClient.toUpperCase()
							System.out.println("Richiesta client: "+id+" "+contenutoClient);
							if(contenutoClient.toUpperCase().equals("EXIT")){
//								System.out.println(id);
								//client.remove(id);
								clientExit.set(id, true);
								clientConnessi--;
								System.out.println("Utenti connessi: "+clientConnessi);
								break;
							}else{
								String []string=contenutoClient.split(" ");
								
								if(string.length==1 && string[0].toUpperCase().equals("CONTENT")){
									String stringTmp=leggiIlContenuto(folder);
									if(stringTmp.length()==0){
										outOfClient.writeBytes("Nessun file presente!"+"\n");
									}else{
										outOfClient.writeBytes(stringTmp+"\n");
									}
								}else if(string.length==2 && string[0].toUpperCase().equals("GET")){
									String stringaFile=string[1];
									File file=trovaFileNellaDirectory(folder,stringaFile);
									if(file!=null){	
										Scanner input = new Scanner(file);
										String contenutoFile="";
										while (input.hasNextLine()) {
							                String line = input.nextLine();
							                contenutoFile+=line+" ";
							            }
							            input.close();
										outOfClient.writeBytes(contenutoFile+"\n");
									}else{
										outOfClient.writeBytes("ERROR: Server non può rispondere"+"\n");
									}
								}else if (string[0].toUpperCase().equals("MV")){
									for(int a=2; a<string.length; a++){
										string[1]+=" "+string[a];
									}
									if(string[1].contains("CartellaDownload")){
										folder=new File(string[1].replace("\\", File.separator));
										outOfClient.writeBytes("Nuova directoty "+folder.getAbsolutePath()+"\n");
									}else{
										outOfClient.writeBytes("Percorso sbagliato!\n");
									}
								}else{
									outOfClient.writeBytes("Errore!"+"\n");
								}
							}
						}while(true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();			
		}
	}

	protected static int restituisciID(ArrayList<Boolean> clientExit) {
		for(int a=0; a<clientExit.size(); a++){
			if(clientExit.get(a)==true){
				client.remove(a);
				clientExit.set(a, false);
				return a;
			}
		}
	return clientExit.size()-1;
	}

	private static void setup(int i, Socket socket) throws IOException {
		DataOutputStream outOfClient=new DataOutputStream(socket.getOutputStream());
		outOfClient.writeBytes(i+"\n");
	}

	private static File trovaFileNellaDirectory(File folder, String stringaFile) {
		File fileDaRestituire = null;
		for(File file:folder.listFiles()){
			if(file.getName().equals(stringaFile)){
				int i = file.getName().lastIndexOf('.');
				if (i > 0) {
				   String extension = file.getName().substring(i+1);
				   if(extension.equals("txt")){
					   fileDaRestituire=file;
					   break;
				   }
				}
			}
		}
	return fileDaRestituire;
	}

	private static String leggiIlContenuto(File folder) {
		String allFiles ="";
		for (File file : folder.listFiles()) {
			allFiles+=file.getName()+" ";
		}
	return allFiles;
	}
}
