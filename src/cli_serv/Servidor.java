package cli_serv;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Servidor {
	static int i;
	public Servidor(){
		
	}
	
	private class Listenner implements Runnable {
		Scanner scn;
		
		public Listenner(Socket socket) throws IOException{
			scn = new Scanner(socket.getInputStream());
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String txt;
			while( (txt = scn.nextLine() ) != null ){
				System.out.println("ok " + txt);
			}
			
		}
	
	}

	public static void main(String[] args) throws Exception {
		Socket socket;
		ServerSocket server;
		System.out.println("Servidor on");
		try { 
			server = new ServerSocket(3065); 
			System.out.println(i++);
 
			while (true) {
				socket = server.accept();
				try{
				new Thread(  ).start(); //new Listenner(socket) 
				} catch(Exception e){ }
				//Scanner scan = new Scanner(socket.getInputStream());
				//System.out.println(scan.nextLine());

			}
		} catch (Exception e) {
		}

	}
}