package testes;

import java.awt.BorderLayout;
import java.awt.DisplayMode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Servidor extends JFrame {
	
	

	private static final long serialVersionUID = 1L;
	private ServerSocket server;
	Socket conection;
	private int porta = 3011;
	
	private JTextField enterField;
	private JTextArea display;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private int cont;
	
	public Servidor(){
		super("server");
		
		/*
		enterField = new JTextField();
		enterField.setEditable(false);
		enterField.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				sendData(arg0.getActionCommand());
				enterField.setText("");				
			}
		});
		
		add(enterField, BorderLayout.SOUTH);
		*/
		
		display = new JTextArea();
		display.setEditable(false);
		
				
		
		
		add(new JScrollPane(display));
		setSize(300, 300);
		setVisible(true);
		
	}
	
	public void runServer(){
		try {
			server = new ServerSocket(porta);
			while(true){
				try{
				waitConection();
				getStream();
				processConection();
				}
				catch (Exception e) {
					System.out.println("erro");
					e.printStackTrace();
				}
				finally{
					closeConection();
					cont++;
				}
			}
		} catch (Exception e) {
			System.out.println("erro");
			e.printStackTrace();
		}
		
	}
	
	private void waitConection() throws Exception {
		displayMsg("\nAguardando conexao");
		conection = server.accept();
		displayMsg("\nCliente Conectado");
		//displayMsg("\nconexao " + conection + " recebida de " + conection.getInetAddress().getHostName());
	}
	
	private void getStream() throws Exception {
		output = new ObjectOutputStream(conection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(conection.getInputStream());
		displayMsg("\nTeste de conexao");
	}
	
	private void processConection() throws Exception {
		String msg = "\nConexao bem sucedida";
		sendData(msg);
		setTextFieldEditable(true);
		do{
			try{
				msg = (String) input.readObject();
				displayMsg("\n"+msg);
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
				
			}
		}while(!msg.equals("\nCLIENTE>TERMINATE"));
		
	}
	
	private void closeConection() throws InvocationTargetException, InterruptedException{
		displayMsg("\nEncerrando");
		setTextFieldEditable(false);
		try{
			output.close();
			input.close();
			conection.close();
		}catch(IOException io){
			io.printStackTrace();
		}
	}
	
	
	public void sendData(String msg){
		try {
			output.writeObject("server: " + msg);	//escreve msg p cliente
			output.flush();	//
			displayMsg("\nServer> " + msg); 	//exibe msg na tela
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void displayMsg(final String s) throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeLater(new Runnable() {
	
			public void run() {
				display.append(s);
			}
		});
	}
	
	private void setTextFieldEditable(final boolean b){
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				enterField.setEditable(b);
				
			}
		});
	}
	

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Servidor s = new Servidor();
		
		s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		s.runServer();
		

	}

}
