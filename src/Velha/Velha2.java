package Velha;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class Velha2 extends JFrame {
	
	private class doStuff  implements Runnable{
		public doStuff(){
			new Thread(this).start();
		}

		@Override
		public void run() {

			try {

				server = new ServerSocket(3000);
				conection = server.accept();
		
		
				//conection = new Socket("192.168.0.27", 3000);

				//escritor = new PrintWriter(conection.getOutputStream());

				out = new ObjectOutputStream(conection.getOutputStream());
				out.flush();

				in = new ObjectInputStream(conection.getInputStream());

				System.out.println("ok");
				

			} catch (Exception e) {
				System.out.println("erro");
			}

			
			
		}
		
	}
	
	// ===================
	
	// ===================
	
	// ===================
	
	
	private Dimension screenSize;									// screen size
	private int width;												// width of screen
	private int height;												// height of screen
	
	ServerSocket server;
	Socket conection;
	PrintWriter escritor;
	
	
	private boolean signalTosend = true;		
	ObjectOutputStream out = null;
	ObjectInputStream in = null;

		
	
	JButton botao1;
	JButton botao2;
	JButton botao3;
	JButton botao4;
	JButton botao5;
	JButton botao6;
	JButton botao7;
	JButton botao8;
	JButton botao9;
	
	
	
	static // ===========================================================================================
	// ##########################################################################################
	// ===========================================================================================
	
	int velha[][];
	int vez;
	
	
	
	public Velha2() 
	{
		
		velha = new int[3][3];
		vez = 0;

				 
		
		
		setSize(500, 300); 								 // set the size of a frame to 1/3 of a window screen
		setTitle("Jogo da Velha");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		
		setLayout(new BorderLayout());
		
		// #-#-# Painel Central #-#-#
		JPanel pCenter = new JPanel();
		pCenter.setLayout(new GridLayout(3, 3));
		botao1 = new JButton("-");
		botao2 = new JButton("-");
		botao3 = new JButton("-");
		botao4 = new JButton("-");
		botao5 = new JButton("-");
		botao6 = new JButton("-");
		botao7 = new JButton("-");
		botao8 = new JButton("-");
		botao9 = new JButton("-");
		
		
		
		
		
		botao1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checaBotao(botao1, 1);
				/*
				try {
					//out.writeObject("S");
					//out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Diconectado");
					//e.printStackTrace();
				}
				*/
			}
		});

		// ============================================================

		botao2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//checaBotao(botao2, 2);
				new doStuff();
			}
		});

		// ============================================================
		botao3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					conection = new Socket("127.0.0.1", 3000);
				

				out = new ObjectOutputStream(conection.getOutputStream());
				out.flush();
				in = new ObjectInputStream(conection.getInputStream());

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//checaBotao(botao3, 3);
				
			}
		});

		// ============================================================
		// ============================================================
		botao4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				checaBotao(botao4, 4);
			}
		});

		// ============================================================
		// ============================================================
		botao5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				checaBotao(botao5, 5);
			}
		});

		// ============================================================
		// ============================================================
		botao6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				checaBotao(botao6, 6);
				
			}
		});

		// ============================================================
		// ============================================================
		botao7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				checaBotao(botao7, 7);
			}
		});

		// ============================================================
		// ============================================================
		botao8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				checaBotao(botao8, 8);
				
				
			}
		});

		// ============================================================
		botao9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				checaBotao(botao9, 9);
				
				
			}
		});

		
		// ============================================================
		
		
		pCenter.add(botao1);
		pCenter.add(botao2);
		pCenter.add(botao3);
		pCenter.add(botao4);
		pCenter.add(botao5);
		pCenter.add(botao6);
		pCenter.add(botao7);
		pCenter.add(botao8);
		pCenter.add(botao9);
		
		add(pCenter, BorderLayout.CENTER);
		
		// ============================================================
		// ============================================================
		JPanel pEast = new JPanel();
		pEast.setLayout(new BorderLayout());
		pEast.setPreferredSize(new Dimension(150, height));
		
		final JTextArea textArea;
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		
		
		add(pEast, BorderLayout.EAST);
		
		// ==============
		JPanel pSouth = new JPanel();
		pSouth.setLayout(new BorderLayout());
		pSouth.setPreferredSize(new Dimension(width/3, 50));
		
		final JTextField message;
		message = new JTextField(" ");
		message.setEditable(false);
		
		message.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e) 
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					textArea.append( message.getText() + "\n");
					sendMensage(message.getText());
					message.setText(" ");
				}
			}
		});
		
		
		pSouth.add(message, BorderLayout.CENTER);
		add(pSouth, BorderLayout.SOUTH);
		
		
		
		
		
		// ============================================================
		// ============================================================
		
		
		
		
		
	}
	
	
	
	
	// ===========================================================================================
	// ##########################################################################################
	// ===========================================================================================
	
	public void acaoParaJogador(JButton b){
		
			b.setText("O");
			b.setForeground(Color.RED);
			b.setBackground(Color.gray);
			
	}
	
	public void setPosicao(int posicao, int vezJogador){
		if(posicao == 1)
			velha[0][0] = vezJogador;
		else if(posicao == 2)
			velha[0][1] = vezJogador;
		else if(posicao == 3)
			velha[0][2] = vezJogador;
		else if(posicao == 4)
			velha[1][0] = vezJogador;
		else if(posicao == 5)
			velha[1][1] = vezJogador;
		else if(posicao == 6)
			velha[1][2] = vezJogador;
		else if(posicao == 7)
			velha[2][0] = vezJogador;
		else if(posicao == 8)
			velha[2][1] = vezJogador;
		else if(posicao == 9)
			velha[2][2] = vezJogador;
		
		
	}
	
	
	
	public void checaBotao(JButton b, int posicao){
		sendMensage("x");
		//b.setEnabled(false);
		if ((vez % 2) == 0) {
			b.setText("O");
			b.setForeground(Color.RED);
			setPosicao(posicao, 1);
		} else {
			b.setText("X");
			b.setForeground(Color.GREEN);
			setPosicao(posicao, 1);
		}
		b.setBackground(Color.gray);
		
		if(!checarVencedor()){
			temVencedor();
			
		}
		
		
		vez++;
		
		
		
	}
	
	public void temVencedor(){
		botao1.setEnabled(false);
		botao2.setEnabled(false);
		botao3.setEnabled(false);
		botao4.setEnabled(false);
		botao5.setEnabled(false);
		botao6.setEnabled(false);
		botao7.setEnabled(false);
		botao8.setEnabled(false);
		botao9.setEnabled(false);
		
	}
	
	
	public static boolean checarVencedor() {
		int soma = 0;

		// verifica vencedor por coluna
		for (int i = 0; i < 3; i++) {
			
			soma = velha[i][0] + velha[i][1] + velha[i][2];
			if (soma == 3) {
				System.out.println("Jogador1 venceu");
				return false;
			} else if (soma == -3) {
				System.out.println("Jogador2 venceu");
				return false;
			}
			soma = 0;
		}

		// verifica vencedor por coluna
		for (int j = 0; j < 3; j++) {
			soma = velha[0][j] + velha[1][j] + velha[2][j];
			if (soma == 3) {
				System.out.println("Jogador1 venceu");
				return false;
			} else if (soma == -3) {
				System.out.println("Jogador2 venceu");
				return false;
			}
			soma = 0;
		}

		// vencedor na diagonal principal
		soma = velha[0][0] + velha[1][1] + velha[2][2];
		if (soma == 3) {
			System.out.println("Jogador1 venceu");
			return false;
		} else if (soma == -3) {
			System.out.println("Jogador2 venceu");
			return false;
		}
		soma = 0;

		// vencedor na diagonal secundaria
		soma = velha[0][2] + velha[1][1] + velha[2][0];
		if (soma == 3) {
			System.out.println("Jogador1 venceu");
			return false;
		} else if (soma == -3) {
			System.out.println("Jogador2 venceu");
			return false;
		}
		soma = 0;

		return true;
	}
	
	
	
	
	private void sendMensage(String p)
	{			
		
		try
		{
			if(signalTosend)
			{
				out.writeObject(p);
				out.flush();
			}
		}
		catch(SocketException e)
		{
			if(signalTosend)
			{
				signalTosend = false;
				closeAll();
				//restart();
			}
		}
		catch(Exception e) 
		{ 
			if(signalTosend)
			{
				signalTosend = false;
				closeAll();
				//restart();
				try { JOptionPane.showMessageDialog(null, "Sending data/Disconnect:\n" + e); } catch (ExceptionInInitializerError exc) { }
			}
		}
		
	}
	
	
	private void closeAll()
	{
		try { out.flush(); 		} catch (Exception e) { }
		try { out.close(); 		} catch (Exception e) { }
		//try { in.close(); 		} catch (Exception e) { }
		try { server.close();	} catch (Exception e) { }
		try { conection.close(); 	} catch (Exception e) { }
	}
	
	
	
	// ===========================================================================================
	// ##########################################################################################
	// ===========================================================================================
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new Velha2().setVisible(true);				
			}
		});

	}

}
