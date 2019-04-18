
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

public class XOmain1 extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	//ServerSocket, Socket, Input and Output Streams
	private ServerSocket serverSocket = null;
	private Socket conection = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;

	private Dimension screenSize;									// screen size
	private int width;												// width of screen
	private int height;												// height of screen
	
	private JButton b1, b2, b3, b4, b5, b6, b7, b8, b9;				// buttons XO fields
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
	
	private String table[] = { "","","", "","","", "","","" }; 		// FIELDS XO (see example in multiline comment)
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
	private String nick1, nick2, chatMessage; 							// nick1 server, nick2 client, chat message
	
	private boolean turn; 										// signal for "WHOSE TURN"
	private boolean stopSending = true;							// if this signal is false, then stop sending messages over Internet
	
	private int npartidas = 0;										//The number of parties // 0=X play first; 1=O play 2nd; 3=X turn....
	private int nvez = 0;										//The number of moves, if number is higher or equal to 9 , game is draw
	
	private Font fontText, fontButtons; 							// Fonts
	
	private String safeSing = "!pass123!#$%&/()!";					// i add this sing, its something like password, when data is transfer over ip
	
	// --- Constructor ---
	public XOmain1()
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
		
		setSize(500, 300);								 // tamanho da tela
		setResizable(false);			// tela de tamanho fixo
		setTitle("Jogo da Velha");	// nome da janela
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);	// abrir janela no meio
		
		setLayout(new BorderLayout());
		
		//fontText = new Font("Book Antiqua", Font.PLAIN, 30);
		//fontButtons = new Font("Book Antiqua", Font.PLAIN, 18);
		
		// --- CENTAR PANEL ----
		// panel for xo buttons
		JPanel pCenter = new JPanel();
		pCenter.setLayout(new GridLayout(3, 3));
				
		b1 = new JButton("[ ]");
		//b1.setFont(fontText);
		b1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(turn)
				{
					b1.setText(xo); // set X or O button text
					sendMensage(xo + "1" + safeSing);
					sendMensage("true" + safeSing);
					turn = false;
					b1.setEnabled(false);
					if(xo.equals("X"))
					{
						table[0] = "X";
					}
					else
					{
						table[0] = "O";
					}
					++nvez;
					showTable();
				}
			}
		});
		pCenter.add(b1);
		
		b2 = new JButton("[ ]");
		//b2.setFont(fontText);
		b2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(turn)
				{
					b2.setText(xo);
					sendMensage(xo + "2" + safeSing);
					sendMensage("true" + safeSing);
					turn = false;
					b2.setEnabled(false);
					if(xo.equals("X"))
					{
						table[1] = "X";
					}
					else
					{
						table[1] = "O";
					}
					++nvez;
					showTable();
				}
			}
		});
		pCenter.add(b2);
		
		b3 = new JButton("[ ]");
		//b3.setFont(fontText);
		b3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(turn)
				{
					b3.setText(xo);
					sendMensage(xo + "3" + safeSing);
					sendMensage("true" + safeSing);
					turn = false;
					b3.setEnabled(false);
					if(xo.equals("X"))
					{
						table[2] = "X";
					}
					else
					{
						table[2] = "O";
					}
					++nvez;
					showTable();
				}
			}
		});
		pCenter.add(b3);
		
		b4 = new JButton("[ ]");
		//b4.setFont(fontText);
		b4.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(turn)
				{
					b4.setText(xo);
					sendMensage(xo + "4" + safeSing);
					sendMensage("true" + safeSing);
					turn = false;
					b4.setEnabled(false);
					if(xo.equals("X"))
					{
						table[3] = "X";
					}
					else
					{
						table[3] = "O";
					}
					++nvez;
					showTable();
				}
			}
		});
		pCenter.add(b4);
		
		b5 = new JButton("[ ]");
		//b5.setFont(fontText);
		b5.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(turn)
				{
					b5.setText(xo);
					sendMensage(xo + "5" + safeSing);
					sendMensage("true" + safeSing);
					turn = false;
					b5.setEnabled(false);
					if(xo.equals("X"))
					{
						table[4] = "X";
					}
					else
					{
						table[4] = "O";
					}
					++nvez;
					showTable();
				}
			}
		});
		pCenter.add(b5);
		
		b6 = new JButton("[ ]");
		//b6.setFont(fontText);
		b6.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(turn)
				{
					b6.setText(xo);
					sendMensage(xo + "6" + safeSing);
					sendMensage("true" + safeSing);
					turn = false;
					b6.setEnabled(false);
					if(xo.equals("X"))
					{
						table[5] = "X";
					}
					else
					{
						table[5] = "O";
					}					
					++nvez;
					showTable();
				}
			}
		});
		pCenter.add(b6);
		
		b7 = new JButton("[ ]");
		//b7.setFont(fontText);
		b7.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(turn)
				{
					b7.setText(xo);
					sendMensage(xo + "7" + safeSing);
					sendMensage("true" + safeSing);
					turn = false;
					b7.setEnabled(false);
					if(xo.equals("X"))
					{
						table[6] = "X";
					}
						
					else
					{
						table[6] = "O";
					}
					++nvez;
					showTable();
				}
			}
		});
		pCenter.add(b7);
		
		b8 = new JButton("[ ]");
		//b8.setFont(fontText);
		b8.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(turn)
				{
					b8.setText(xo);
					sendMensage(xo + "8" + safeSing);
					sendMensage("true" + safeSing);
					turn = false;
					b8.setEnabled(false);
					if(xo.equals("X"))
					{
						table[7] = "X";
					}
					else
					{
						table[7] = "O";
					}
					++nvez;
					showTable();
				}
			}
		});
		pCenter.add(b8);
		
		b9 = new JButton("[ ]");
		//b9.setFont(fontText);
		b9.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(turn)
				{
					b9.setText(xo);
					sendMensage(xo + "9" + safeSing);
					sendMensage("true" + safeSing);
					turn = false;
					b9.setEnabled(false);
					if(xo.equals("X"))
					{
						table[8] = "X";
					}
					else
					{
						table[8] = "O";
					}
					++nvez;
					showTable();
				}	
			}
		});
		pCenter.add(b9);
		
		setAllEnable(false); 	// set all buttons on false till we wait for client to join
		add(pCenter, BorderLayout.CENTER);
		
		// --- OSTALE KOMPONENTE - OTHER COMPONENTS ---
		// --- EAST PANEL ---
		JPanel pEast = new JPanel();
		pEast.setLayout(new BorderLayout());
		pEast.setPreferredSize(new Dimension(270, height));
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		//textArea.setFont(fontButtons);
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
		//message.setFont(fontText);
		message.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e) 
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					textArea.append(nick.getText() + ":" + message.getText() + "\n");
					scrollToBottom();
					sendMensage(message.getText());
					message.setText(" ");
				}
			}
		});
		pSouth.add(message, BorderLayout.CENTER);
		add(pSouth, BorderLayout.SOUTH);
		
		ip = new JTextField("127.0.0.1");
		ip.setToolTipText("Enter Host IP addres");
		ip.setPreferredSize(new Dimension(100, 25));
		port = new JTextField("3001");
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
					
					conection = new Socket(ip.getText(), Integer.parseInt(port.getText())); 
					
					out = new ObjectOutputStream(conection.getOutputStream());
					out.flush();
					in = new ObjectInputStream(conection.getInputStream());
					
					chatMessage = (String) in.readObject();
					textArea.append(chatMessage + "\n");
					scrollToBottom();
					
					xo = "O";
					turn = false;
					
					nick2 = nick.getText();
					
					chatMessage = (String) in.readObject(); // get nick from host
					nick1 = "" + chatMessage;
					
					sendMensage(nick2);
					
					setAllEnable(true);
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
					
					new reciveMensage("Server message"); // thread for receive data from host		
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
				sendMensage("ZahtevZaNovuPartiju!" + safeSing); // send request to client, for new game
				
				++npartidas;
				
				for (int i=0; i<table.length; i++)
				{
					table[i] = "";
				}
				
				if(npartidas %2 == 0)
				{
					turn = true;
					textArea.append("X  plays first!\n");
					scrollToBottom();
					sendMensage("false" + safeSing);
					sendMensage("X  plays first!");
				}
				else 
				{
					turn = false;
					sendMensage("true" + safeSing);
					textArea.append("O plays first!\n");
					scrollToBottom();
					sendMensage("O  plays first!");
				}
				
				textFild();
				setAllEnable(true);
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
				if(conection != null) 
				{
					sendMensage("Going offline!");
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
				conection = serverSocket.accept();
				
				out = new ObjectOutputStream(conection.getOutputStream());
				out.flush();
				in = new ObjectInputStream(conection.getInputStream());
				sendMensage(nick.getText() +  ": Successfully connected!");
				textArea.append("Client Successfully connected!\n");
				scrollToBottom();
				
				xo = "X";
				turn = true;
				
				nick1 = nick.getText();
				
				sendMensage(nick1);
				
				chatMessage = (String) in.readObject(); // prima NICK OD SERVERA
				nick2 = "" + chatMessage;
				
				setAllEnable(true);
				message.setEditable(true);
				ip.setEnabled(false);
				
				textArea.append("X plays first!\n");
				scrollToBottom();
				new reciveMensage("Client message"); 
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
	private void showTable()
	{
		// ------_X_X_X_------
		if
		(
			// CHECK X POSITIONS - VERTICAL
			(table[0].equals("X") && table[1].equals("X") && table[2].equals("X")) || 
			(table[3].equals("X") && table[4].equals("X") && table[5].equals("X")) ||
			(table[6].equals("X") && table[7].equals("X") && table[8].equals("X")) ||
			// CHECK X POSITIONS - HORIZONTAL
			(table[0].equals("X") && table[3].equals("X") && table[6].equals("X")) ||
			(table[1].equals("X") && table[4].equals("X") && table[7].equals("X")) ||
			(table[2].equals("X") && table[5].equals("X") && table[8].equals("X")) ||
			// CHECK X POSITIONS - DIAGONAL
			(table[0].equals("X") && table[4].equals("X") && table[8].equals("X")) ||
			(table[2].equals("X") && table[4].equals("X") && table[6].equals("X"))
		)
		{
			nvez = 0;
			setAllEnable(false);
			JOptionPane.showMessageDialog(null, nick1 + " je pobedio! WIN!");
			if(xo.equals("X")) { novaPartija.setEnabled(true); }
		}
		// ------_O_O_O_------
		else if
		(
				// CHECK O POSITIONS - HORIZONTAL
				(table[0].equals("O") && table[1].equals("O") && table[2].equals("O")) ||
				(table[3].equals("O") && table[4].equals("O") && table[5].equals("O")) ||
				(table[6].equals("O") && table[7].equals("O") && table[8].equals("O")) ||
				// CHECK O POSITIONS - VERTICAL
				(table[0].equals("O") && table[3].equals("O") && table[6].equals("O")) ||
				(table[1].equals("O") && table[4].equals("O") && table[7].equals("O")) ||
				(table[2].equals("O") && table[5].equals("O") && table[8].equals("O")) ||
				// CHECK O POSITIONS - DIAGONAL
				(table[0].equals("O") && table[4].equals("O") && table[8].equals("O")) ||
				(table[2].equals("O") && table[4].equals("O") && table[6].equals("O"))
		)
		{
			nvez = 0;
			setAllEnable(false);
			JOptionPane.showMessageDialog(null, nick2 + " je pobedio! WIN!");
			if(xo.equals("X")) { novaPartija.setEnabled(true); }
		}
		else
		{
			//CHECK IF IS DRAW
			if(nvez >= 9)
			{
				nvez = 0;
				sendMensage("NERESENO!" + safeSing);
				JOptionPane.showMessageDialog(null, "DRAW/NERESENO!");
				if(xo.equals("X")) { novaPartija.setEnabled(true); }
			}
		}
	}

	// --- enable/disable buttons ---
	private void setAllEnable(boolean b)
	{
		b1.setEnabled(b);
		b2.setEnabled(b);
		b3.setEnabled(b);
		b4.setEnabled(b);
		b5.setEnabled(b);
		b6.setEnabled(b);
		b7.setEnabled(b);
		b8.setEnabled(b);
		b9.setEnabled(b);		
	}
	
	// --- set default text state to buttons ---
	private void textFild()
	{
		b1.setText("[ ]");
		b2.setText("[ ]");
		b3.setText("[ ]");
		b4.setText("[ ]");
		b5.setText("[ ]");
		b6.setText("[ ]");
		b7.setText("[ ]");
		b8.setText("[ ]");
		b9.setText("[ ]");
	}
	
	// --- Send Data over Internet ---
	private void sendMensage(String p)
	{			
		try
		{
			if(stopSending)
			{
				out.writeObject(p);
				out.flush();
			}
		}
		catch(SocketException e)
		{
			if(stopSending)
			{
				stopSending = false;
				ugasiSve();
				pokreniSve();
			}
		}
		catch(Exception e) 
		{ 
			if(stopSending)
			{
				stopSending = false;
				ugasiSve();
				pokreniSve();
				try { JOptionPane.showMessageDialog(null, "Sending data/Disconnect:\n" + e); } catch (ExceptionInInitializerError exc) { }
			}
		}
	}
	
	// --- Receive data/messages thread ---
	private class reciveMensage implements Runnable
	{	
		private boolean nitSig;
		private String imeNiti;
		
		public reciveMensage(String i)
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
					chatMessage = "";
					chatMessage = (String) in.readObject();			// receive messages
					
					if(imeNiti.equals("PorukaOdServera")) 			// client receive data from host/server
					{
						if(chatMessage.equalsIgnoreCase("true" + safeSing))
						{
							turn = true;
						}
						else if(chatMessage.equalsIgnoreCase("false" + safeSing))
						{
							turn = false;
						}
						else if(chatMessage.equalsIgnoreCase("NERESENO!" + safeSing))
						{
							JOptionPane.showMessageDialog(null, "DRAW/NERESENO!");
						}
						else if(chatMessage.equalsIgnoreCase("ZahtevZaNovuPartiju!" + safeSing))
						{
							for (int i=0; i<table.length; i++)
							{
								table[i] = "";
							}
							turn = true;
							textFild();
							setAllEnable(true);
						}
						else if(chatMessage.equalsIgnoreCase("X1" + safeSing))
						{
							b1.setText("X");
							table[0] = "X";
							b1.setEnabled(false);
							showTable();
						}
						else if(chatMessage.equalsIgnoreCase("X2" + safeSing))
						{
							b2.setText("X");
							table[1] = "X";
							b2.setEnabled(false);
							showTable();
						}
						else if(chatMessage.equalsIgnoreCase("X3" + safeSing))
						{
							b3.setText("X");
							table[2] = "X";
							b3.setEnabled(false);
							showTable();
						}
						else if(chatMessage.equalsIgnoreCase("X4" + safeSing))
						{
							b4.setText("X");
							table[3] = "X";
							b4.setEnabled(false);
							showTable();
						}
						else if(chatMessage.equalsIgnoreCase("X5" + safeSing))
						{
							b5.setText("X");
							table[4] = "X";
							b5.setEnabled(false);
							showTable();
						}
						else if(chatMessage.equalsIgnoreCase("X6" + safeSing))
						{
							b6.setText("X");
							table[5] = "X";
							b6.setEnabled(false);
							showTable();
						}
						else if(chatMessage.equalsIgnoreCase("X7" + safeSing))
						{
							b7.setText("X");
							table[6] = "X";
							b7.setEnabled(false);
							showTable();
						}
						else if(chatMessage.equalsIgnoreCase("X8" + safeSing))
						{
							b8.setText("X");
							table[7] = "X";
							b8.setEnabled(false);
							showTable();
						}
						else if(chatMessage.equalsIgnoreCase("X9" + safeSing))
						{
							b9.setText("X");
							table[8] = "X";
							b9.setEnabled(false);
							showTable();
						}
						else
						{
							if(chatMessage.endsWith(safeSing))
							{
								chatMessage = chatMessage.substring(0, chatMessage.length() - safeSing.length());
							}
							textArea.append(nick1 + ":" + chatMessage + "\n");
							scrollToBottom();
						}
					}
					else if(imeNiti.equals("PorukaOdKlijenta"))			// host/server receive data from client
					{
						if(chatMessage.equalsIgnoreCase("true" + safeSing))
						{
							turn = true;
						}
						else if(chatMessage.equalsIgnoreCase("O1" + safeSing))
						{
							++nvez;
							b1.setText("O");
							table[0] = "O";
							b1.setEnabled(false);
							showTable();
						}
						else if(chatMessage.equalsIgnoreCase("O2" + safeSing))
						{
							++nvez;
							b2.setText("O");
							table[1] = "O";
							b2.setEnabled(false);
							showTable();
						}
						else if(chatMessage.equalsIgnoreCase("O3" + safeSing))
						{
							++nvez;
							b3.setText("O");
							table[2] = "O";
							b3.setEnabled(false);
							showTable();
						}
						else if(chatMessage.equalsIgnoreCase("O4" + safeSing))
						{
							++nvez;
							b4.setText("O");
							table[3] = "O";
							b4.setEnabled(false);
							showTable();
						}
						else if(chatMessage.equalsIgnoreCase("O5" + safeSing))
						{
							++nvez;
							b5.setText("O");
							table[4] = "O";
							b5.setEnabled(false);
							showTable();
						}
						else if(chatMessage.equalsIgnoreCase("O6" + safeSing))
						{
							++nvez;
							b6.setText("O");
							table[5] = "O";
							b6.setEnabled(false);
							showTable();
						}
						else if(chatMessage.equalsIgnoreCase("O7" + safeSing))
						{
							++nvez;
							b7.setText("O");
							table[6] = "O";
							b7.setEnabled(false);
							showTable();
						}
						else if(chatMessage.equalsIgnoreCase("O8" + safeSing))
						{
							++nvez;
							b8.setText("O");
							table[7] = "O";
							b8.setEnabled(false);
							showTable();
						}
						else if(chatMessage.equalsIgnoreCase("O9" + safeSing))
						{
							++nvez;
							b9.setText("O");
							table[8] = "O";
							b9.setEnabled(false);
							showTable();
						}
						else
						{
							if(chatMessage.endsWith(safeSing))
							{
								chatMessage = chatMessage.substring(0, chatMessage.length() - safeSing.length());
							}
							textArea.append(nick2 + ":" + chatMessage + "\n");
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
		textFild();
		setAllEnable(false);
		
		chatMessage = "";
		xo = "";
		stopSending = true;
		nvez = 0;
		npartidas = 0;
		
		for (int i=0; i<table.length; i++)
		{
			table[i] = "";
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
		try { out.flush(); 		} catch (Exception e) { }
		try { out.close(); 		} catch (Exception e) { }
		try { in.close(); 		} catch (Exception e) { }
		try { serverSocket.close();	} catch (Exception e) { }
		try { conection.close(); 	} catch (Exception e) { }
	}
	
	// --- scroll to bottom when receive chat message ---
	public void scrollToBottom()
	{
		textArea.setCaretPosition(textArea.getText().length());
	}

	// --- Main ---
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new XOmain1().setVisible(true);				
			}
		});
	}
}