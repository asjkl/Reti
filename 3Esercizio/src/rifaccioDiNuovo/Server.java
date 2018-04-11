package rifaccioDiNuovo;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread{
	
	
	private int min;
	private int max;
	private int minNumeroDiClienti;
	private int percentuale;
	private int clientiOnline;
	private ArrayList<Point> rangeDaCalcolare;
	private ArrayList<Socket> clienti;
	private ServerSocket server;
	private ArrayList<BufferedReader> dalCliente;			//MESSAGGI CHE ARRIVANO DAL CLIENTE AL SERVER
	private ArrayList<DataOutputStream> alCliente;			//MESSAGGI DA INVIARE AL CLIENTE
	
	
	public Server(int min, int max) throws IOException {
		this.percentuale=20;
		this.min=min;
		this.max=max;
		this.minNumeroDiClienti=3;
		this.clientiOnline=0;
		this.rangeDaCalcolare=new ArrayList<>();
		calcolaRange();
		this.clienti=new ArrayList<>();
		this.dalCliente=new ArrayList<>();
		this.alCliente=new ArrayList<>();
		this.server=new ServerSocket(1234);
		
		new Thread(){
			@Override
			public void run() {
				while(true){
					for(int a=0; a<dalCliente.size(); a++){
						try {
							//if(dalCliente.get(a).ready()){
								String tmp=dalCliente.get(a).readLine();
								//DA FINIRE
							//}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}.start();
	}
	
	private void calcolaRange() {
		double calcoloPercentuale=((double)(max*percentuale)/100);
		int tmpMin1,tmpMin2; 
		tmpMin1=min;						
		tmpMin2=(int) (min+calcoloPercentuale);	
		while(tmpMin2<max){
			Point tmp=new Point(tmpMin1, tmpMin2);
			rangeDaCalcolare.add(tmp);
			tmpMin1=tmpMin2+1;							
			tmpMin2=(int) (tmpMin2+calcoloPercentuale)+1;
		}
		rangeDaCalcolare.add(new Point(tmpMin1, max));
		for(int a=0; a<rangeDaCalcolare.size(); a++){
			System.out.println(rangeDaCalcolare.get(a).x+" "+rangeDaCalcolare.get(a).y);
		}
	}

	@Override
	public void run() {
		while(true){
			Socket clientAccept;
			try {
				clientAccept = server.accept();
				System.out.println("CLIENTE ACCETTATO");
				clienti.add(clientAccept);
				dalCliente.add(new BufferedReader(new InputStreamReader(clientAccept.getInputStream())));
				alCliente.add(new DataOutputStream(clientAccept.getOutputStream()));
				String daInviare="ID:"+clientiOnline+"-MIN:"+rangeDaCalcolare.get(clientiOnline).x+"-MAX:"+rangeDaCalcolare.get(clientiOnline).y;
				alCliente.get(alCliente.size()-1).writeBytes(daInviare+"\n");
				clientiOnline++;			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		Server server=new Server(0, 17);
		server.start();
	}

}
