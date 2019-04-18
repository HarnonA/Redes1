    import java.io.IOException;  
import java.io.PrintStream;  
import java.io.PrintWriter;
import java.net.Socket;  
import java.net.UnknownHostException;
import java.util.Scanner;

import cli_serv.Interface3;
      
    public class Cliente {
    	
    	
    	/*
    	private void configurar(){
    		try{
        		socket = new Socket("192.168.0.13", 3008);
        		writer = new PrintWriter(socket.getOutputStream());
        		
        		} catch (Exception e) {}

    		
    	}
    	*/
    	
    	public static void main(String[] args) throws Exception{
    		Interface3 i = new Interface3();
    		Socket socket;
        	PrintWriter writer;
        	
    		try{
        		socket = new Socket("192.168.0.13", 3000);
        		writer = new PrintWriter(socket.getOutputStream());
        		System.out.println(writer);
        		
        		
    		
    		Scanner scaner = new Scanner(socket.getInputStream());
    		
    		socket = new Socket("192.168.0.13", 3000);
    		writer = new PrintWriter(socket.getOutputStream());
    		} catch (Exception e) {}
    		
    		
    		System.out.println("msg ");// + scaner.nextLine());
    		
    		
    		
    		//scaner.close();
    	}
      
      
        
    }  