package MultiChat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
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

public class NewGame extends JFrame implements Runnable{
	
	private JTextField idField;
	private JTextArea displayArea;
	private JPanel boardJPanel;
	private JPanel panel2;
	private Square[][] board;
	private Square currentSquare;
	private Socket connection;
	private Scanner input;
	private Formatter output;
	private String hostName;
	private String myMark;
	private boolean myTurn;
	private final String X_MARK = "X";
	private final String O_MARK = "O";
	
	public NewGame(String host){
		hostName = host;
		displayArea = new JTextArea(4, 30);
		displayArea.setEditable(false);
		add(new JScrollPane(displayArea), BorderLayout.SOUTH);
		
		boardJPanel = new JPanel();
		boardJPanel.setLayout( new GridLayout(3,3,0,0) );
		board = new Square[3][3];
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = new Square( "", i * 3 + j);
				boardJPanel.add( board[i][j] );
				
			}	
		}
		
		idField = new JTextField();
		idField.setEditable(false);
		add(idField, BorderLayout.NORTH);
		
		panel2 = new JPanel();
		panel2.add(boardJPanel, BorderLayout.CENTER);
		add(panel2, BorderLayout.CENTER);
		
		setSize(300, 225);
		setVisible(true);
		
		startClient();
		
	}
	
	public void startClient(){
		try{
			connection = new Socket(InetAddress.getByName(hostName), 2222);
			input = new Scanner(connection.getInputStream());
			output = new Formatter(connection.getOutputStream());
			
		} catch(IOException io){
			io.printStackTrace();
		}
		
		ExecutorService worker = Executors.newFixedThreadPool(1);
		worker.execute(this);
	}
	
	public void run(){
		myMark = input.nextLine();
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				idField.setText("you are Player " + myMark + "\"");
				
			}
		});
		
		myTurn = (myMark.equals(X_MARK));
		
		while(true){
			if(input.hasNextLine() )
				processMessage(input.nextLine());
		}
	}
	
	private void processMessage(String message){
		
		if(message.equals("Valid move")){
			displayMessage("Valid move, please wait\n");
			setMark(currentSquare, myMark);
		}
		else if(message.equals("Invalid move, try again")){
			displayMessage(message + "\n");
			myTurn = true;			
		}
		else if(message.equals("Opponent moved")){
			int location = input.nextInt();
			input.nextLine();
			int i = location / 3;
			int j = location % 3;
			setMark( board[i][j], myMark.equals(X_MARK) ? O_MARK : X_MARK);
			displayMessage("Now your turn");
			myTurn = true;
			
		} 
		else if(message.equals("WON")){
			displayMessage("ganhou");
			myTurn = false;
			
		}
		else {
			displayMessage(message + "\n");
		}
	}
	
	
	private void displayMessage(final String msg){
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				displayArea.append(msg);
			}
		});
		
	}
	
	private void setMark( final Square squareToMark, final String mark){
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
			squareToMark.setMark(mark);
				
			}
		});
	}
	
	public void sendClick(int location){
		if(myTurn){
			output.format("%d\n", location);
			output.flush();
			myTurn = false;
		}
	}
	
	public void SetCurrentSquare(Square square){
		currentSquare = square;
		
	}
	
	private class Square extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String mark;
		private int location;
		
		public Square(String squareMark, int squareLocation){
			mark = squareMark;
			location = squareLocation;
			
			addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e){
					SetCurrentSquare(Square.this);
					sendClick(getSquareLocation());
				}
			});
		}
		
		
		public Dimension getPreferredSize(){
			return new Dimension(30, 30);
		}
		
		public Dimension getMinimumSize(){
			return getPreferredSize();
		}
		
		public void setMark(String m){
			mark = m;
			repaint();
		}
		
		public int getSquareLocation(){
			return location;
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.drawRect(0, 0, 29, 29);
			//g.setColor(Color.RED);
			g.drawString(mark, 11, 20);
			
		}
		
		
	}
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NewGame tttc;
		if( args.length == 0){
			tttc = new NewGame("localhost");
		}
		else
			tttc = new NewGame(args[0]);
		tttc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
