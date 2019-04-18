


import java.awt.BorderLayout;
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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class XOmain extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	//ServerSocket, Socket, Input and Output Streams
	private ServerSocket serverSocket = null;
	private Socket connection = null;
	private ObjectInputStream input = null;
	private ObjectOutputStream output = null;

	private Dimension screenSize;									// screen size
	private int width;												// width of screen
	private int height;												// height of screen
	
	private JButton[] button = new JButton[9];
	//private JButton b2, b3, b4, b5, b6, b7, b8, b9;				// buttons XO fields
	/*
	 Button position:
	 [b1][b2][b3]
	 [b4][b5][b6]
	 [b7][b8][b9]
	 */
	
	private JTextArea textArea;										// text area on right side of frame for chat and notifications
	private JScrollPane sp;											// scroll pane for text area
	
	private JTextField ip, port, nick, message; 					// IP address, port number, nickname, chat message
	private JButton join, create, novaPartija; 						// buttons : JOIN, CREATE, NEW GAME
	
	private String gridField[] = { "","","", "","","", "","","" }; 		// FIELDS XO (see example in multiline comment)
	/*
	Explanation:
	for ex. if polje = { "X", "X", "X", ...}; then X win
	[X][X][X]
	[ ][ ][ ]
	[ ][ ][ ]
	2nd ex. if polje = { "O","","", "","O","", "","","O" };
	[O][ ][ ]
	[ ][O][ ]
	[ ][ ][O]
	 */
	
	private String xo = ""; 										// server is X , client is O
	private String nick1, nick2, messageField; 							// nick1 server, nick2 client, chat message
	
	private boolean signal; 										// signal for "WHOSE TURN"
	private boolean signalToSend = true;							// if this signal is false, then stop sending messages over Internet
	
	private int turn = 0;										//The number of parties // 0=X play first; 1=O play 2nd; 3=X turn....
	private int move = 0;										//The number of moves, if number is higher or equal to 9 , game is draw
	
	
	private String safeSing = "!pass123!#$%&/()!";					// i add this sing, its something like password, when data is transfer over ip
	
	// --- Constructor ---
	public XOmain()
	{
		initUI();
	}
	
	// --- User Interface ---
	private void initUI()
	{
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) { } // get&set system UI
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();	 // get screen size
		width = (int) screenSize.getWidth(); 						 // get width
		height = (int) screenSize.getHeight(); 						 // get height
		
		setSize(width/3, height/3); 								 // set the size of a frame to 1/3 of a window screen
		setResizable(true);
		setTitle("Jogo Velha");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		setLayout(new BorderLayout());
		
		
		// --- CENTAR PANEL ----
		// panel for xo buttons
		JPanel pCenter = new JPanel();
		pCenter.setLayout(new GridLayout(3, 3));
				
		button[0] = new JButton("[ ]");
		button[1] = new JButton("[ ]");
		button[2] = new JButton("[ ]");
		button[3] = new JButton("[ ]");
		button[4] = new JButton("[ ]");
		button[5] = new JButton("[ ]");
		button[6] = new JButton("[ ]");
		button[7] = new JButton("[ ]");
		button[8] = new JButton("[ ]");
		
		button[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				addActionPlayer(button[0], 0);
			}
		});
		pCenter.add(button[0]);

		button[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addActionPlayer(button[1], 1);
			}
		});
		pCenter.add(button[1]);

		button[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addActionPlayer(button[2], 2);
			}
		});
		pCenter.add(button[2]);

		button[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addActionPlayer(button[3], 3);

			}
		});
		pCenter.add(button[3]);

		button[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addActionPlayer(button[4], 4);
			}
		});
		pCenter.add(button[4]);

		button[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addActionPlayer(button[5], 5);
			}
		});
		pCenter.add(button[5]);

		button[6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addActionPlayer(button[6], 6);
			}
		});
		pCenter.add(button[6]);

		button[7].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addActionPlayer(button[7], 7);
			}
		});
		pCenter.add(button[7]);

		button[8].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addActionPlayer(button[8], 8);
			}
		});
		pCenter.add(button[8]);
		
		setButtonFalse(false); 	// set all buttons on false till we wait for client to join
		add(pCenter, BorderLayout.CENTER);
		
		// --- OSTALE KOMPONENTE - OTHER COMPONENTS ---
		// --- EAST PANEL ---
		JPanel pEast = new JPanel();
		pEast.setLayout(new BorderLayout());
		pEast.setPreferredSize(new Dimension(270, height));
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		sp = new JScrollPane(textArea); 
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		pEast.add(sp, BorderLayout.CENTER);
		add(pEast, BorderLayout.EAST);
		
		// --- SOUTH PANEL ---
		JPanel pSouth = new JPanel();
		pSouth.setLayout(new BorderLayout());
		pSouth.setPreferredSize(new Dimension(width/3, 50));
		message = new JTextField(" ");
		message.setEditable(false);
		message.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e) 
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					textArea.append(nick.getText() + ":" + message.getText() + "\n");
					scrollToBottom();
					sendMessage(message.getText());
					message.setText(" ");
				}
			}
		});
		pSouth.add(message, BorderLayout.CENTER);
		add(pSouth, BorderLayout.SOUTH);
		
		ip = new JTextField("127.0.0.1");
		ip.setToolTipText("Enter Host IP addres");
		ip.setPreferredSize(new Dimension(100, 25));
		port = new JTextField("9876");
		port.setToolTipText("Enter Host PORT nubmer, default:9876");
		port.setPreferredSize(new Dimension(100, 25));
		nick = new JTextField();
		nick.setToolTipText("Enter your Nickname");
		nick.setPreferredSize(new Dimension(100, 25));
		
		// --- CREATE BUTTON ---
		create = new JButton("Create");
		create.setToolTipText("Create game");
		create.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event) 
			{
				if(nick.getText().equals("") || nick.getText().equals(" "))
				{
					try { JOptionPane.showMessageDialog(null, "You did not input your nickname!"); } catch (ExceptionInInitializerError exc) { }
					return;
				}
				
				new CreateButtonThread("CreateButton"); // we need thread while we wait for client, because we don't want frozen frame
			}
		});
	
		// --- JOIN BUTTON ---
		join = new JButton("Join");
		join.setToolTipText("Join remote game");
		join.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event) 
			{
				try
				{ 	
					if(nick.getText().equals("") || nick.getText().equals(" "))
					{
						try { JOptionPane.showMessageDialog(null, "You did not input your nickname!"); } catch (ExceptionInInitializerError exc) { }
						return;
					}
					
					connection = new Socket(ip.getText(), Integer.parseInt(port.getText())); 
					
					output = new ObjectOutputStream(connection.getOutputStream());
					output.flush();
					input = new ObjectInputStream(connection.getInputStream());
					
					messageField = (String) input.readObject();
					textArea.append(messageField + "\n");
					scrollToBottom();
					
					xo = "O";
					signal = false;
					
					nick2 = nick.getText();
					
					messageField = (String) input.readObject(); // get nick from host
					nick1 = "" + messageField;
					
					sendMessage(nick2);
					
					setButtonFalse(true);
					message.setEditable(true);
					
					ip.setEnabled(false);
					port.setEnabled(false);
					nick.setEnabled(false);
					
					textArea.append("X plays first!\n");
					scrollToBottom();
					
					join.setEnabled(false);
					create.setEnabled(false);
					ip.setEnabled(false);
					port.setEnabled(false);
					nick.setEnabled(false);
					
					new reciveMessage("ServerMessage"); // thread for receive data from host		
				}
				catch(Exception e)
				{
					ugasiSve();
					pokreniSve();
					try { JOptionPane.showMessageDialog(null, "JoinButton: Error: Server is offline: \n" + e); } catch (ExceptionInInitializerError exc) { }
				}
			}
		});
		
		// --- DUGME ZA NOVU PARTIJU - BUTTON FOR NEW GAME ---
		novaPartija = new JButton("New Game");
		novaPartija.setToolTipText("Play a new game");
		novaPartija.setEnabled(false);
		novaPartija.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				sendMessage("ZahtevZaNovuPartiju!" + safeSing); // send request to client, for new game
				
				++turn;
				
				for (int i=0; i<gridField.length; i++)
				{
					gridField[i] = "";
				}
				
				if(turn %2 == 0)
				{
					signal = true;
					textArea.append("X  plays first!\n");
					scrollToBottom();
					sendMessage("false" + safeSing);
					sendMessage("X  plays first!");
				}
				else 
				{
					signal = false;
					sendMessage("true" + safeSing);
					textArea.append("O plays first!\n");
					scrollToBottom();
					sendMessage("O  plays first!");
				}
				
				postaviTekstPolja();
				setButtonFalse(true);
				novaPartija.setEnabled(false);
			}
		});
		
		JPanel pNorth = new JPanel();
		pNorth.add(ip);
		pNorth.add(port);
		pNorth.add(nick);
		pNorth.add(create);
		pNorth.add(join);
		pNorth.add(novaPartija);
		add(pNorth, BorderLayout.NORTH);
		
		// --- WINDOW ADAPTER ---
		addWindowListener(new WindowAdapter()
		{
			public void windowActivated(WindowEvent event) 
			{
				try 
				{
					InetAddress thisIp = InetAddress.getLocalHost();
					ip.setText(thisIp.getHostAddress());
				} 
				catch (Exception e) 
				{ 
					ip.setText("127.0.0.1"); 
				}
			}	
			
			public void windowClosing(WindowEvent event) 
			{
				if(connection != null) 
				{
					sendMessage("Going offline!");
				}
				ugasiSve();
			}
		});
	}
	
	// --- CREATE BUTTON THREAD ---
	private class CreateButtonThread implements Runnable
	{	
		public CreateButtonThread(String name)
		{
			new Thread(this, name).start();
		}
		
		public void run()
		{
			try 
			{
				join.setEnabled(false);
				create.setEnabled(false);
				port.setEnabled(false);
				nick.setEnabled(false);
								
				serverSocket = new ServerSocket(Integer.parseInt(port.getText())); 
				
				textArea.append("Waiting for client...\n");
				scrollToBottom();
				connection = serverSocket.accept();
				
				output = new ObjectOutputStream(connection.getOutputStream());
				output.flush();
				input = new ObjectInputStream(connection.getInputStream());
				sendMessage(nick.getText() +  ": Successfully connected!");
				textArea.append("Client Successfully connected!\n");
				scrollToBottom();
				
				xo = "X";
				signal = true;
				
				nick1 = nick.getText();
				
				sendMessage(nick1);
				
				messageField = (String) input.readObject(); // prima NICK OD SERVERA
				nick2 = "" + messageField;
				
				setButtonFalse(true);
				message.setEditable(true);
				ip.setEnabled(false);
				
				textArea.append("X plays first!\n");
				scrollToBottom();
				new reciveMessage("ClientMessage"); 
			}
			catch (Exception e) 
			{ 
				ugasiSve();
				pokreniSve();
				try { JOptionPane.showMessageDialog(null, "CreateButton: Error while creating game:\n" + e);  } catch (ExceptionInInitializerError exc) { }
			}
		}
	}
	
	// --- CHECK FIELDS --- 
	private void proveriTabelu()
	{
		// ------_X_X_X_------
		if
		(
			// CHECK X POSITIONS - VERTICAL
			(gridField[0].equals("X") && gridField[1].equals("X") && gridField[2].equals("X")) || 
			(gridField[3].equals("X") && gridField[4].equals("X") && gridField[5].equals("X")) ||
			(gridField[6].equals("X") && gridField[7].equals("X") && gridField[8].equals("X")) ||
			// CHECK X POSITIONS - HORIZONTAL
			(gridField[0].equals("X") && gridField[3].equals("X") && gridField[6].equals("X")) ||
			(gridField[1].equals("X") && gridField[4].equals("X") && gridField[7].equals("X")) ||
			(gridField[2].equals("X") && gridField[5].equals("X") && gridField[8].equals("X")) ||
			// CHECK X POSITIONS - DIAGONAL
			(gridField[0].equals("X") && gridField[4].equals("X") && gridField[8].equals("X")) ||
			(gridField[2].equals("X") && gridField[4].equals("X") && gridField[6].equals("X"))
		)
		{
			move = 0;
			setButtonFalse(false);
			JOptionPane.showMessageDialog(null, "Player " + nick1 + " WIN!");
			if(xo.equals("X")) { novaPartija.setEnabled(true); }
		}
		// ------_O_O_O_------
		else if
		(
				// CHECK O POSITIONS - HORIZONTAL
				(gridField[0].equals("O") && gridField[1].equals("O") && gridField[2].equals("O")) ||
				(gridField[3].equals("O") && gridField[4].equals("O") && gridField[5].equals("O")) ||
				(gridField[6].equals("O") && gridField[7].equals("O") && gridField[8].equals("O")) ||
				// CHECK O POSITIONS - VERTICAL
				(gridField[0].equals("O") && gridField[3].equals("O") && gridField[6].equals("O")) ||
				(gridField[1].equals("O") && gridField[4].equals("O") && gridField[7].equals("O")) ||
				(gridField[2].equals("O") && gridField[5].equals("O") && gridField[8].equals("O")) ||
				// CHECK O POSITIONS - DIAGONAL
				(gridField[0].equals("O") && gridField[4].equals("O") && gridField[8].equals("O")) ||
				(gridField[2].equals("O") && gridField[4].equals("O") && gridField[6].equals("O"))
		)
		{
			move = 0;
			setButtonFalse(false);
			JOptionPane.showMessageDialog(null, "Player " + nick2 + " WIN!");
			if(xo.equals("X")) { novaPartija.setEnabled(true); }
		}
		else
		{
			//CHECK IF IS DRAW
			if(move >= 9)
			{
				move = 0;
				sendMessage("NERESENO!" + safeSing);
				JOptionPane.showMessageDialog(null, "DRAW/NERESENO!");
				if(xo.equals("X")) { novaPartija.setEnabled(true); }
			}
		}
	}

	// --- enable/disable buttons ---
	private void setButtonFalse(boolean b)
	{
		button[0].setEnabled(b);
		button[1].setEnabled(b);
		button[2].setEnabled(b);
		button[3].setEnabled(b);
		button[4].setEnabled(b);
		button[5].setEnabled(b);
		button[6].setEnabled(b);
		button[7].setEnabled(b);
		button[8].setEnabled(b);		
	}
	
	// --- set default text state to buttons ---
	private void postaviTekstPolja()
	{
		button[0].setText("[ ]");
		button[1].setText("[ ]");
		button[2].setText("[ ]");
		button[3].setText("[ ]");
		button[4].setText("[ ]");
		button[5].setText("[ ]");
		button[6].setText("[ ]");
		button[7].setText("[ ]");
		button[8].setText("[ ]");
	}
	
	// --- Send Data over Internet ---
	private void sendMessage(String p)
	{			
		try
		{
			if(signalToSend)
			{
				output.writeObject(p);
				output.flush();
			}
		}
		catch(SocketException e)
		{
			if(signalToSend)
			{
				signalToSend = false;
				ugasiSve();
				pokreniSve();
			}
		}
		catch(Exception e) 
		{ 
			if(signalToSend)
			{
				signalToSend = false;
				ugasiSve();
				pokreniSve();
				try { JOptionPane.showMessageDialog(null, "Sending data/Disconnect:\n" + e); } catch (ExceptionInInitializerError exc) { }
			}
		}
	}
	
	// --- Receive data/messages thread ---
	private class reciveMessage implements Runnable
	{	
		private boolean nitSig;
		private String imeNiti;
		
		public reciveMessage(String i)
		{
			nitSig = true;
			imeNiti = i;
			new Thread(this, imeNiti).start();
		}
		
		public void run()
		{
			while(nitSig)
			{
				try
				{
					messageField = "";
					messageField = (String) input.readObject();			// receive messages
					
					String mark = "";
					int number = 0;
					
					if(messageField.startsWith("X")){
						mark = "X";
						number = defineNumber(messageField.substring(1, 2));
					}
					else if(messageField.startsWith("O")){
						mark = "O";
						number = defineNumber(messageField.substring(1, 2));
					}
					
						
					
					if(imeNiti.equals("ServerMessage")) 			// client receive data from host/server
					{
						if(messageField.equalsIgnoreCase("true" + safeSing))
						{
							signal = true;
						}
						else if(messageField.equalsIgnoreCase("false" + safeSing))
						{
							signal = false;
						}
						else if(messageField.equalsIgnoreCase("NERESENO!" + safeSing))
						{
							JOptionPane.showMessageDialog(null, "DRAW/NERESENO!");
						}
						else if(messageField.equalsIgnoreCase("ZahtevZaNovuPartiju!" + safeSing))
						{
							for (int i=0; i<gridField.length; i++)
							{
								gridField[i] = "";
							}
							signal = true;
							postaviTekstPolja();
							setButtonFalse(true);
						}
						else if(mark.equals("X")){
							button[number].setText("X");
							gridField[number] = "X";
							button[number].setEnabled(false);
							proveriTabelu();
							
						}					
						else
						{
							if(messageField.endsWith(safeSing))
							{
								messageField = messageField.substring(0, messageField.length() - safeSing.length());
							}
							textArea.append(nick1 + ":" + messageField + "\n");
							scrollToBottom();
						}
					}
					else if(imeNiti.equals("ClientMessage"))			// host/server receive data from client
					{
						if(messageField.equalsIgnoreCase("true" + safeSing))
						{
							signal = true;
						}
						else if(mark.equals("O")){
							button[number].setText("O");
							gridField[number] = "O";
							button[number].setEnabled(false);
							proveriTabelu();
							
						}
						else
						{
							if(messageField.endsWith(safeSing))
							{
								messageField = messageField.substring(0, messageField.length() - safeSing.length());
							}
							textArea.append(nick2 + ":" + messageField + "\n");
							scrollToBottom();
						}
					}
				}
				catch (Exception e)
				{
					nitSig = false;
					ugasiSve();
					pokreniSve();
					try { JOptionPane.showMessageDialog(null, "Receiving Data Failed/Disconnect:\n" + e); } catch (ExceptionInInitializerError exc) { }
				}
			}
		}
	}
	
	// --- restart the game to the initial state ---
	private void pokreniSve()
	{
		postaviTekstPolja();
		setButtonFalse(false);
		
		messageField = "";
		xo = "";
		signalToSend = true;
		move = 0;
		turn = 0;
		
		for (int i=0; i<gridField.length; i++)
		{
			gridField[i] = "";
		}
		
		ip.setEnabled(true);
		port.setEnabled(true);
		nick.setEnabled(true);
		create.setEnabled(true);
		join.setEnabled(true);
		
		novaPartija.setEnabled(false);
		message.setEditable(false);
	}
	
	// --- Turn OFF all streams ---
	private void ugasiSve()
	{
		try { output.flush(); 		} catch (Exception e) { }
		try { output.close(); 		} catch (Exception e) { }
		try { input.close(); 		} catch (Exception e) { }
		try { serverSocket.close();	} catch (Exception e) { }
		try { connection.close(); 	} catch (Exception e) { }
	}
	
	// --- scroll to bottom when receive chat message ---
	public void scrollToBottom()
	{
		textArea.setCaretPosition(textArea.getText().length());
	}
	
	public void addActionPlayer(JButton button, int nButton){
		if(signal)
		{
			button.setText(xo); // set X or O button text
			sendMessage(xo + (nButton+1) + safeSing);
			sendMessage("true" + safeSing);
			signal = false;
			button.setEnabled(false);
			if(xo.equals("X"))
			{
				gridField[nButton] = "X";
			}
			else
			{
				gridField[nButton] = "O";
			}
			++move;
			proveriTabelu();
		}
	}
	
	
	public int defineNumber(String str){
		if(str.equals("1"))
			return 0;
		else if(str.equals("2"))
			return 1;
		else if(str.equals("3"))
			return 2;
		else if(str.equals("4"))
			return 3;
		else if(str.equals("5"))
			return 4;
		else if(str.equals("6"))
			return 5;
		else if(str.equals("7"))
			return 6;
		else if(str.equals("8"))
			return 7;
		else if(str.equals("9"))
			return 8;
		return 0;
	}

	// --- Main ---
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new XOmain().setVisible(true);
				new XOmain().setVisible(true);
			}
		});
	}
}