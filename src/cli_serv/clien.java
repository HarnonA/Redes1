package cli_serv;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class clien {

	private String nome;
	Socket socket;
	PrintWriter escritor;
	
	
	
	public clien(String n){
		nome = n;
		
		System.out.println("Cliente on");
		new Interface3();
		
		String txt;
		Scanner s = new Scanner(System.in);
		txt = s.next();
		
		System.out.println(nome + " " + txt);
		
		
		try{
			socket = new Socket("192.168.0.27", 3065);
			escritor = new PrintWriter(socket.getOutputStream());
			escritor.println(nome + " " );
			} catch(Exception e){ }
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		new clien("joao");

	}

}
