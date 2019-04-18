package testes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

public class x_serv {
	ServerSocket server;
	Socket cliente;
	Listas l;

	public x_serv() {
		// TODO Auto-generated constructor stub
		l = new Listas();
		l.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		l.setSize(300, 300);
		l.setVisible(true);
		
	}
	
	public void runServ(){
		try {
			while(true){
			server = new ServerSocket(3026);
			cliente = server.accept();
			l.addCli("oi");
			System.out.println("ok");
			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			System.out.println("fechando");
			closeSocket();
		}
		
	}
	
	private void closeSocket(){
		try {
			cliente.close();
			server.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("iniciado");
		x_serv serv = new x_serv();
		serv.runServ();

	}

}
