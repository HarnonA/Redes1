import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class ChatServer {

	String str = "";
	
	ServerSocket server;
	public ChatServer() {
	
		System.out.println("Servidor rodando");
		Scanner scan;
		try {
			server = new ServerSocket(3000);
			while (true) {
				Socket s = server.accept();
				new Thread(new escuta(s)).start();
				scan = new Scanner(s.getInputStream());
				System.out.println(scan.nextLine());

			}
		} catch (Exception e) {}

	}
	
	private class escuta implements Runnable{
		Scanner leitor;
		
		public escuta(Socket s){
			try {
			 leitor = new Scanner(s.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void run(){
			try{
			String txt;
			while( (txt = leitor.nextLine()) != null){
				str = str + txt;
				System.out.println("Recebeu: " + txt +"\ntxt= " + str );
				
			}
			}catch (Exception e){}
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChatServer();

	}

}
