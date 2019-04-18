package Velha;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class TicTacToe extends JFrame implements Runnable {

	private JTextField idField;
	private JTextArea displayArea;
	private JPanel boardPanel;
	private JPanel panel2;
	private Square[][] board;
	private Square currentSquare;
	private Socket conection;
	private Scanner input;
	private Formatter output;
	private String ticTacToeHost;
	private String myMark;
	private boolean myTurn;
	private final String X_MARK = "X";
	private final String O_MARK = "O";
	
	
	public TicTacToe(String host){
		
		ticTacToeHost = host;
		displayArea = new JTextArea(4, 30);
		displayArea.setEditable(false);
		add(new JScrollPane(displayArea), BorderLayout.SOUTH);
		
		boardPanel =  new JPanel();
		boardPanel.setLayout(new GridLayout(3,3,0,0));
		
		board = new Square[3][3];
		
		
		for (int row = 0; row < board.length; row++) {
			
			
			for (int column = 0; column < board[row].length; column++) {
				
				
				board[row][column] = new Square( " ", row * 3 + column);
				boardPanel.add(board[row][column]);
			}
		}
		
		idField = new JTextField();
		idField.setEditable(false);
		add(idField, BorderLayout.NORTH);
		
		panel2 = new JPanel();
		panel2.add(boardPanel, BorderLayout.CENTER);
		add(panel2, BorderLayout.CENTER);
		
		setSize(300, 225);
		setVisible(true);
		
		startClient();
	}
	
	
	public void startClient(){
			
		try{
			
			
			conection = new Socket(
					InetAddress.getByName(ticTacToeHost), 12345);
			
			
			input = new Scanner(conection.getInputStream());
			output = new Formatter(conection.getOutputStream());			
		}
		catch(IOException io){
			
			io.printStackTrace();
		}
		
		
		ExecutorService worker =  Executors.newFixedThreadPool(1);
		worker.execute(this);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		myMark = input.nextLine();
		
		SwingUtilities.invokeLater(
			new Runnable() {

				public void run() {
					
					
					idField.setText("You are player  " + myMark + "\n");
				}	
			}
		);
		
		myTurn = ( myMark.equals( X_MARK ) );
		
		
		while(true){
			
			if(input.hasNextLine())
				processMassage(input.nextLine());
		}
	}
	
	
	private void processMassage(String msg){
		
		
		if(msg.equals("Valid move.")){
			
			displayMessage("Valid move, please wait.\n");
			setMark(currentSquare, myMark);
		}
		else if(msg.equals("Invalid move, try again.\n")){
			
			displayMessage(msg + "\n");
			myTurn = true;
		}
		else if(msg.equals("Opponent moved")){
			
			int location = input.nextInt();
			input.nextLine();
			int row = location;
			int column = location % 3;
			
			setMark(board[row][column], 
					myMark.equals(X_MARK) ? O_MARK : X_MARK);
			displayMessage("Opponent moved. Your turn.\n");
			myTurn = true;
		}
		else
			displayMessage(msg + "\n");
	}
	
	
	private void displayMessage(final String msgDisplay){
		
		SwingUtilities.invokeLater(
			new Runnable() {
				@Override
				public void run() {
					
					displayArea.append(msgDisplay);
				}
			}
		);
	}
	
	
	private void setMark(final Square squareToMark, final String mark){
	
		SwingUtilities.invokeLater(
			new Runnable() {	
				@Override
				public void run() {
				// TODO Auto-generated method stub
					squareToMark.setMark(mark);
				}
			}
		);
	}
	
	
	public void sendClickedSquare(int location){
		
		
		if(myTurn){
			
			output.format("%d\n", location);
			output.flush();
			myTurn = false;
		}
	}
	
	
	public void setCurrentSquare(Square square){
		
		currentSquare = square;
	}
	
	
	private class Square extends JPanel {
		
		private String mark;
		private int location;
		
		public Square(String squareMark, int squareLocation){
			
			mark = squareMark;
			location = squareLocation;
			
			addMouseListener(
				new MouseAdapter() {
					
					public void mouseReleased(MouseEvent e){
						
						setCurrentSquare(Square.this);
						
						
						sendClickedSquare(getSquareLocation());
					}
				}
			);	
		}
		
		
		public Dimension getPreferedSize(){
			
			return new Dimension(30,30);
		}
		
		
		public Dimension getMinimunSize(){
			
			return getPreferedSize();	
		}
		
		
		public void setMark(String newMark){
			
			mark = newMark;
			repaint();
		}
		
		
		public int getSquareLocation(){
			
			return location;
		}
		
		
		public void paintComponent( Graphics g){
			
			super.paintComponent(g);
			
			g.drawRect(0, 0, 29, 29);
			g.drawString(mark, 11, 20);
		}
	}
}