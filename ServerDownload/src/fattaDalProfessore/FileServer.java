package fattaDalProfessore;



import java.net.*;
import java.io.*;
import java.util.Scanner;

public class FileServer {

	static final int LISTENING_PORT = 3210;

	static File directory;
	
	public static void main(String[] args) {

		ServerSocket listener;

		Socket connection;

		directory = new File("./directory_files");
		if (!checkDirectory(directory))
			return;

		/**
		 * Crea una socket in ascolto sulla porta di Listening specificata. Il server
		 * continua a rimanere in ascolto finchè il programma non è terminato
		 */
		try {
			listener = new ServerSocket(LISTENING_PORT);
			System.out.println("In ascolto sulla porta " + LISTENING_PORT);
			while (true) {
				connection = listener.accept();
				handleConnection(directory, connection);
				connection.close();
			}
		} catch (Exception e) {
			System.out.println("Server chiuso in maniera inaspettata.");
			System.out.println("Error:  " + e);
			return;
		}

	}

	/**
	 * Il metodo corrente processa le connessioni con il client. Legge i comandi in
	 * input e risponde con stampe in output.
	 */
	private static void handleConnection(File directory, Socket connection) {
		Scanner incoming; // For reading data from the client.
		PrintWriter outgoing; // For transmitting data to the client.
		String command = "Comando non ancora letto";
		try {

			incoming = new Scanner(connection.getInputStream());
			outgoing = new PrintWriter(connection.getOutputStream());
			command = incoming.nextLine();

			while (!command.equalsIgnoreCase("<EXIT>")) { 
				if (command.equalsIgnoreCase("content")) {
					sendIndex(outgoing);
				} else if (command.toLowerCase().startsWith("get")) {
					String fileName = command.substring(3).trim();
					sendFile(fileName, outgoing);
				} else if (command.toLowerCase().startsWith("mv")) {
					String newFolder = command.substring(2).trim();
					switchFolder(newFolder, outgoing);
				} else {
					outgoing.println("ERRORE comando non supportato");
					outgoing.println("<EOF>");
					outgoing.flush();
				}
				
				System.out.println("OK    " + connection.getInetAddress() + " " + command);
				command = incoming.nextLine();
			}
			outgoing.close();
			if (outgoing.checkError())
				throw new Exception("Errore durante la trasmissione dei dati.");
			
		} catch (Exception e) {
			System.out.println("ERRORE " + connection.getInetAddress() + " " + command + " " + e);
		}
	}

	/**
	 * Funzione richiamata da handleConnection() per rispondere al comando "INDEX".
	 * Ritorna il contenuto della directory corrente
	 */
	private static void sendIndex(PrintWriter outgoing) throws Exception {
		String[] fileList = directory.list();
		for (int i = 0; i < fileList.length; i++)
			outgoing.println(fileList[i]);
		outgoing.println("<EOF>");
		outgoing.flush();
	}

	/**
	 * Funzione richiamata da handleConnection() per rispondere al comando "mv
	 * <new/directory/path". Se il nuovo path esiste allora effettua lo switch della
	 * cartella corrente alla nuova cartella
	 */
	private static void switchFolder(String newFolder, PrintWriter outgoing) throws Exception {

		File new_dir = new File(newFolder);
		if (!checkDirectory(new_dir))
			outgoing.println("Impossibile cambiare directory !");
		else {
			directory = new File(newFolder);
			outgoing.println("Nuova directory: " + newFolder);
		}
		outgoing.println("<EOF>");
		outgoing.flush();
	}

	/**
	 * Funzione richiamata da handleConnection() per rispondere al comando "get
	 * <filename>". Invia al client (tramite stampa) il contenuto dei file
	 */
	private static void sendFile(String fileName, PrintWriter outgoing) throws Exception {
		File file = new File(directory, fileName);
		if ((!file.exists()) || file.isDirectory()) {
			outgoing.println("ERRORE");
		} else {
			outgoing.println("OK");
			BufferedReader fileIn = new BufferedReader(new FileReader(file));
			while (true) {
				String line = fileIn.readLine();
				if (line == null)
					break;
				outgoing.println(line);
			}
		}
		outgoing.println("<EOF>");
		outgoing.flush();
	}

	/**
	 * Il metodo server per controllare l'esistenza della directory di partenza e
	 * della directory di destinazione quando si inoltra il comando "MV
	 * <new/directory/path>"
	 */
	private static boolean checkDirectory(File dir) {
		if (!dir.exists()) {
			System.out.println("La directory specificata non esiste.");
			return false;
		}
		if (!dir.isDirectory()) {
			System.out.println("Il file richiesto non si trova nella directory corrente.");
			return false;
		}
		return true;
	}

}
