package testes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;



public class x_cli {

	public static void main(String[] args) {
		
		ObjectOutputStream output;
		ObjectInputStream input;
		
		String enviar;
		String modificado;
		
		Socket cliente = null;
				
		try {
			cliente = new Socket("168.192.0.30", 3026);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			cliente.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try{
		output = new ObjectOutputStream(cliente.getOutputStream());
		output.flush();
		input = new ObjectInputStream(cliente.getInputStream());
		}catch(Exception e){}
		

	}

}
