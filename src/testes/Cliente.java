package testes;
import java.awt.BorderLayout;
import java.awt.DisplayMode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Cliente extends JFrame {

	private int porta = 3011;
	
	private JTextField enterField;
	private JTextArea display;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String name;
	String message;
	Socket client;
	
	
	public Cliente(String name){
		super("cliente");
		
		this.name = name;
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
		
		
		display = new JTextArea();
		display.setEditable(false);
		
		add(new JScrollPane(display));
		
		setSize(300, 150);
		setVisible(true);	
	}
	
	public void runClient(){

		try {
			conectToServer();
			getStream();
			processConection();
		} catch (Exception e) {
			System.out.println("erro cliente");
			e.printStackTrace();
		} finally {
			try {
				closeConection();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
		
	}
	
	private void conectToServer() throws Exception {
		displayMsg("\nBuscando conexao");
		client = new Socket(InetAddress.getByName(name), porta);
		displayMsg("\nConectado ao " + client.getInetAddress().getHostName());
	}
	
	private void getStream() throws Exception {
		output = new ObjectOutputStream(client.getOutputStream());
		output.flush();
		input = new ObjectInputStream(client.getInputStream());
		displayMsg("\nrecebeu Teste de conexao");
	}
	
	private void processConection() throws Exception {
		setTextFieldEditable(true);
		do{
			try{
				message = (String) input.readObject();
				displayMsg("\n"+message);
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
				
			}
		}while(!message.equals("\nSERVER>TERMINATE"));
		
	}
	
	private void closeConection() throws InvocationTargetException, InterruptedException{
		displayMsg("Encerrando");
		setTextFieldEditable(false);
		try{
			output.close();
			input.close();
			client.close();
		}catch(IOException io){
			System.out.println("erro ao encerrar");
			io.printStackTrace();
		}
	}
	
	
	public void sendData(String msg){
		try {
			output.writeObject("\nCLIENTE> " + msg);
			output.flush();
			displayMsg("\nCLIENTE> " + msg);
			
		} catch (Exception e) {
			display.append("\nerro ao escrever");
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
		
		Cliente c;
		
		if(args.length == 0)
			c = new Cliente("127.0.0.1");
		else
			c = new Cliente(args[0]);
		c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.runClient();
		
	}

}
