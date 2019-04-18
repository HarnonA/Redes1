package Velha;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class TicTacToeServer extends JFrame {

	private String[] board = new String[9]; 
	private JTextArea outputArea;
	private Player[] players;
	private ServerSocket server;
	private int current;
	private final static int PLAY_X = 0;
	private final static int PLAY_O = 1;
	private final static String[] MARK = { "X", "O" };
	private ExecutorService runGame;
	private Lock gameLock;
	private Condition otherPlayerConnected;
	private Condition otherPlayerTurn;
	
	
	public TicTacToeServer(){
		super("Server");
		
		runGame = Executors.newFixedThreadPool(2);
		gameLock = new ReentrantLock();
		
		otherPlayerConnected = gameLock.newCondition();
		otherPlayerTurn = gameLock.newCondition();
		
		for (int i = 0; i < 9; i++) {
			board[i] = new String("");
		}
		players = new Player[2];
		current = PLAY_X;
		
		try{
			server = new ServerSocket(5000, 2);
		} catch(IOException io){
			io.printStackTrace();
			System.exit(1);
		}
		 
		outputArea = new JTextArea();
		add(outputArea, BorderLayout.CENTER);
		outputArea.setText("Server awaiting...");
		
		setSize(300, 300);
		setVisible(true);
	}
	
	public void execute(){
		for (int i = 0; i < players.length; i++) {
			try{
				players[i] = new Player(server.accept(), i);
				runGame.execute(players[i]);
			} catch(IOException io){
				io.printStackTrace();
				System.exit(1);
			}	
		}
		
		gameLock.lock();
		try{
			players[PLAY_X].setSuspended(false);
			otherPlayerConnected.signal();
		}finally{
			gameLock.unlock();
		}
		
	}
	
	private void displayMessage(final String message){
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				outputArea.append(message);
				
			}
		});
		
	}
	
	
	public boolean validateMove(int location, int player){
		while(player != current ){
			gameLock.lock();
			
			try{
				otherPlayerTurn.await();
			}catch(InterruptedException ie){
				ie.printStackTrace();
			}finally{
				gameLock.unlock();
			}
			
		}
		
		if( !isOcupped(location) ){
			board[location] = MARK[current];
			if(won()){
				System.out.println("WON");
				//break;
			}
			current = (current + 1) % 2;
			
			players[current].otherPlayerMoved(location);
			gameLock.lock();
			
			
			
			
			try{
				otherPlayerTurn.signal();
			}finally{
				gameLock.unlock();
			}
			return true;
		} else {
			return false;
		}
		
	}
	
	public boolean won(){
			
		
		if ((board[0].equals(MARK[current]))
				&& (board[1].equals(MARK[current]))
				&& (board[2].equals(MARK[current]))) {
			return true;
		}
		else if ((board[3].equals(MARK[current]))
				&& (board[4].equals(MARK[current]))
				&& (board[5].equals(MARK[current]))) {
			return true;
		}
		else if ((board[6].equals(MARK[current]))
				&& (board[7].equals(MARK[current]))
				&& (board[8].equals(MARK[current]))) {
			return true;
		}
		
		// ====
		if ((board[0].equals(MARK[current]))
				&& (board[3].equals(MARK[current]))
				&& (board[6].equals(MARK[current]))) {
			return true;
		}
		else if ((board[1].equals(MARK[current]))
				&& (board[4].equals(MARK[current]))
				&& (board[7].equals(MARK[current]))) {
			return true;
		}
		else if ((board[2].equals(MARK[current]))
				&& (board[5].equals(MARK[current]))
				&& (board[8].equals(MARK[current]))) {
			return true;
		}
		
		
		// ====
		if ((board[0].equals(MARK[current]))
				&& (board[4].equals(MARK[current]))
				&& (board[8].equals(MARK[current]))) {
			return true;
		}
		else if ((board[2].equals(MARK[current]))
				&& (board[4].equals(MARK[current]))
				&& (board[6].equals(MARK[current]))) {
			return true;
		}
		
		
		
		return false;
	}

	
	public boolean isOcupped(int location){
		if( board[location].equals(MARK[PLAY_X]) || board[location].equals(MARK[PLAY_O]))
			return true;
		else 
			return false;
	}
	
	public boolean isGameOver(){
		return false;
	}
	//==================================
	//          *  PLAYER  *
	//==================================
	
	private class Player implements Runnable {
		private Socket connection;
		private Scanner input;
		private Formatter output;
		private int playerNumber;
		private String mark;
		private boolean susppended = true;
		
		public Player(Socket socket, int number){
			playerNumber = number;
			mark = MARK[playerNumber];
			connection = socket;
			
			try{
				input = new Scanner(connection.getInputStream());
				output = new Formatter(connection.getOutputStream());
			} catch (IOException io){
				io.printStackTrace();
				System.exit(1);
			}
			
					
		}
		public void otherPlayerMoved(int location) {
			output.format("Opponent moved\n");
			output.format("%d\n", location);
			output.flush();
			 
		}
		
		public void run(){
			try{
				displayMessage("Player" + mark + "connected\n");
				output.format("%s\n", mark);
				output.flush();
				
				if( playerNumber == PLAY_X){
					output.format("%s\n%s", "Player X connected", 
							"Waiting other player\n");
					output.flush();
					gameLock.lock();
				
				try{
					while(susppended){
						otherPlayerConnected.await();
					}
					
				}catch( InterruptedException e){
					e.printStackTrace();
				}finally{
					gameLock.unlock();
				}
				
				output.format("Other player connected. Your move\n");
				output.flush();
				
				}else{
					output.format("Player O connected, wait\n");
					output.flush();
					
				}
				
				while( !isGameOver() ){
					int location = 0;
					
					if(input.hasNext() )
						location = input.nextInt();
					
					if( validateMove(location, playerNumber) ){
						displayMessage("\nLocation " + location);
						output.format("Valid move\n");
						output.flush();
					}
					else {
						output.format("Invalid move, try again\n");
						output.flush();
					}
					
				}
				
			}finally{
				try{
					connection.close();
				}catch(IOException io){
					io.printStackTrace();
					System.exit(1);
				}
			}
		}
		
		public void setSuspended(boolean status){
			susppended = status;
			
		}
		
		
	}
	
	
	public static void main(String[] args) {
		TicTacToeServer ttt = new TicTacToeServer();
		ttt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ttt.execute();

	}

}
