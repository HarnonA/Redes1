package Velha;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class TicTacToeServ extends JFrame {
	
	private String[] board = new String[9];
	private JTextArea outputArea;
	//private Player[] players;
	private ServerSocket server;
	private int currentPlayer;
	private final static int p_X = 0;
	private final static int p_O = 1;
	private final static String[] MARKS = {"X", "O"};
	private ExecutorService rungame;
	private Lock gamelock;
	private Condition playerConnected;
	private Condition turn;
	
	public TicTacToeServ(){
		super("Velha");
		
		rungame = Executors.newFixedThreadPool(2);
		gamelock = new ReentrantLock();
		
		playerConnected = gamelock.newCondition();
		turn = gamelock.newCondition();
		
		for (int i = 0; i < 9; i++) 
			board[i] = "";
		//players = new Player[2];
		currentPlayer = p_X;
		
		try{
			server = new ServerSocket(4601, 2);
		}catch(IOException io){
			io.printStackTrace();
			System.exit(1);
		}		
		
		outputArea = new JTextArea();
		add(outputArea, BorderLayout.CENTER);
		outputArea.setText("Server on");
		
		setSize(300,300);
		setVisible(true);
	}
	
	public void execute(){
		for (int i = 0; i < players.length; i++) {
			
			
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TicTacToeServ();

	}

}
