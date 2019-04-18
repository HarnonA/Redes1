import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ChatCliente extends JFrame{
	
	JTextField texto;
	Socket socket;
	PrintWriter escritor;
	String nome;
	
	public ChatCliente(String nome){
		
		super("Cliente");
		this.nome = nome;
		System.out.println("cliente rodando");
		texto = new JTextField();
		JButton botao = new JButton("Enviar");
		Container c = new JPanel();
		c.setLayout(new BorderLayout());
		c.add(BorderLayout.CENTER, texto);
		c.add(BorderLayout.EAST, botao);
		getContentPane().add(BorderLayout.SOUTH, c);
		
		botao.addActionListener(new enviarListener()); 
		rede();
		
		setSize(300,300);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private class enviarListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			escritor.println(nome + " " + texto.getText() );
			escritor.flush();
			texto.setText("");
			texto.requestFocus();
			
		} 
	}
	
	private void rede(){
		try{
		socket = new Socket("192.168.0.30", 3000);
		escritor = new PrintWriter(socket.getOutputStream());
		} catch(Exception e){ }
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChatCliente("Mario");
		new ChatCliente("Joao");

	}

	

}
