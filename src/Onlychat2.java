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

public class Onlychat2 extends JFrame {

	private static final long serialVersionUID = 1L;
	private ServerSocket serverSocket = null;
	private Socket conection = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;

	private int width = 1500; // width of screen
	private int height = 800; // height of screen

	private JTextArea textArea; // text area on right side of frame for chat and
								// notifications
	private JScrollPane sp; // scroll pane for text area

	private JTextField ip, port, nick, messageFild; // IP address, port number,
													// nickname, chat message
	private JButton join, create, novaPartija; // buttons : JOIN, CREATE, NEW
												// GAME

	private String nick1, nick2, chatMessage; // nick1 server, nick2 client,
												// chat message

	private boolean stopSending = true; // if this signal is false, then stop
										// sending messages over Internet

	private String safeSing = "!pass123!#$%&/()!"; // i add this sing, its
													// something like password,
													// when data is transfer
													// over ip

	// --- Constructor ---
	public Onlychat2(String s) {
		nick1 = s;
		initUI();
	}

	// --- User Interface ---
	private void initUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		} // get&set system UI

		// definiçao da janela
		setSize(width / 3, height / 3);
		setResizable(false);
		setTitle("Jogo da Velha");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// # PANEL DA VELHA #

		// --- PANEL DO CHAT ---
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());
		chatPanel.setPreferredSize(new Dimension(270, height));
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		sp = new JScrollPane(textArea);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		chatPanel.add(sp, BorderLayout.CENTER);
		add(chatPanel, BorderLayout.EAST);

		// --- SOUTH PANEL ---
		JPanel pSouth = new JPanel();
		pSouth.setLayout(new BorderLayout());
		pSouth.setPreferredSize(new Dimension(width / 3, 50));
		messageFild = new JTextField(" ");
		messageFild.setEditable(false);
		messageFild.addKeyListener(new KeyAdapter() {
			// quando 'enter' é pressionado a mensagem é enviada
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					//envia mensagem pra jenala do outro jogador
					textArea.append(nick.getText() + ":" + messageFild.getText() + "\n");
					scrollToBottom();
					// envia mensagem p propria janela
					sendMensage(messageFild.getText());
					messageFild.setText(" ");
				}
			}
		});
		pSouth.add(messageFild, BorderLayout.CENTER);
		add(pSouth, BorderLayout.SOUTH);

		ip = new JTextField("127.0.0.1");
		ip.setToolTipText("Enter Host IP addres");
		ip.setPreferredSize(new Dimension(100, 25));

		port = new JTextField("3000");
		port.setToolTipText("Enter Host PORT nubmer, default:9876");
		port.setPreferredSize(new Dimension(100, 25));

		nick = new JTextField(nick1);
		nick.setToolTipText("Enter your Nickname");
		nick.setPreferredSize(new Dimension(100, 25));

		// --- CREATE BUTTON ---
		create = new JButton("Create");
		create.setToolTipText("Create game");
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				if (nick.getText().equals("") || nick.getText().equals(" ")) {
					try {
						JOptionPane.showMessageDialog(null,
								"No nickname!");
					} catch (ExceptionInInitializerError exc) {
					}
					return;
				}

				new CreateButtonThread("CreateButton"); // we need thread while
														// we wait for client,
														// because we don't want
														// frozen frame
			}
		});

		// --- JOIN BUTTON ---
		join = new JButton("Join");
		join.setToolTipText("Join remote game");
		join.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					if (nick.getText().equals("") || nick.getText().equals(" ")) {
						try {
							JOptionPane.showMessageDialog(null,
									"No nickname!");
						} catch (ExceptionInInitializerError exc) {
						}
						return;
					}

					conection = new Socket("127.0.0.1", 3000);

					out = new ObjectOutputStream(conection.getOutputStream());
					out.flush();
					in = new ObjectInputStream(conection.getInputStream());

					chatMessage = (String) in.readObject();
					textArea.append(chatMessage + "\n");
					scrollToBottom();

					nick2 = nick.getText();

					chatMessage = (String) in.readObject(); // get nick from
															// host
					nick1 = "" + chatMessage;

					sendMensage(nick2);

					// setAllEnable(true);
					messageFild.setEditable(true);

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

					new PrimajPoruke("PorukaOdServera"); // thread for receive
															// data from host
				} catch (Exception e) {
					turnOffStream();
					restartGame();
					try {
						JOptionPane.showMessageDialog(null,
								"JoinButton: Error: Server is offline: \n" + e);
					} catch (ExceptionInInitializerError exc) {
					}
				}
			}
		});

		// --- DUGME ZA NOVU PARTIJU - BUTTON FOR NEW GAME ---
		novaPartija = new JButton("New Game");
		novaPartija.setToolTipText("Play a new game");
		novaPartija.setEnabled(false);
		novaPartija.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMensage("ZahtevZaNovuPartiju!" + safeSing); // send request
																// to client,
																// for new game
				// postaviTekstPolja();
				// setAllEnable(true);
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
		addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent event) {
				try {
					InetAddress thisIp = InetAddress.getLocalHost();
					ip.setText(thisIp.getHostAddress());
				} catch (Exception e) {
					ip.setText("127.0.0.1");
				}
			}

			public void windowClosing(WindowEvent event) {
				if (conection != null) {
					sendMensage("Going offline!");
				}
				turnOffStream();
			}
		});
	}

	// --- CREATE BUTTON THREAD ---
	private class CreateButtonThread implements Runnable {
		public CreateButtonThread(String name) {
			new Thread(this, name).start();
		}

		public void run() {
			try {
				join.setEnabled(false);
				create.setEnabled(false);
				port.setEnabled(false);
				nick.setEnabled(false);

				// recebe a porta definida pelo host
				serverSocket = new ServerSocket(
						Integer.parseInt(port.getText()));

				textArea.append("Waiting for client...\n");
				scrollToBottom();
				conection = serverSocket.accept();

				out = new ObjectOutputStream(conection.getOutputStream());
				out.flush();
				in = new ObjectInputStream(conection.getInputStream());
				sendMensage(nick.getText() + ": Successfully connected!");
				textArea.append("Client Successfully connected!\n");

				scrollToBottom();

				nick1 = nick.getText();

				sendMensage(nick1);

				chatMessage = (String) in.readObject(); // prima NICK OD SERVERA
				nick2 = "" + chatMessage;

				// setAllEnable(true);
				messageFild.setEditable(true);
				ip.setEnabled(false);

				textArea.append("X plays first!\n");
				scrollToBottom();
				new PrimajPoruke("PorukaOdKlijenta");
			} catch (Exception e) {
				turnOffStream();
				restartGame();
				try {
					JOptionPane.showMessageDialog(null,
							"CreateButton: Error while creating game:\n" + e);
				} catch (ExceptionInInitializerError exc) {
				}
			}
		}
	}

	// --- Send Data over Internet ---
	private void sendMensage(String p) {
		try {
			if (stopSending) {
				out.writeObject(p);
				out.flush();
			}
		} catch (SocketException e) {
			if (stopSending) {
				stopSending = false;
				turnOffStream();
				restartGame();
			}
		} catch (Exception e) {
			if (stopSending) {
				stopSending = false;
				turnOffStream();
				restartGame();
				try {
					JOptionPane.showMessageDialog(null,
							"Sending data/Disconnect:\n" + e);
				} catch (ExceptionInInitializerError exc) {
				}
			}
		}
	}

	// --- Receive data/messages thread ---
	private class PrimajPoruke implements Runnable {
		private boolean nitSig;
		private String imeNiti;

		public PrimajPoruke(String i) {
			nitSig = true;
			imeNiti = i;
			new Thread(this, imeNiti).start();
		}

		public void run() {
			while (nitSig) {
				try {
					chatMessage = "";
					chatMessage = (String) in.readObject(); // receive messages

					if (imeNiti.equals("PorukaOdServera")) // client receive
															// data from
															// host/server
					{
						if (chatMessage.endsWith(safeSing)) {
							chatMessage = chatMessage.substring(0,
									chatMessage.length() - safeSing.length());
						}
						textArea.append(nick1 + ":" + chatMessage + "\n");
						scrollToBottom();
					}
					// }
					else if (imeNiti.equals("PorukaOdKlijenta")) // host/server
																	// receive
																	// data from
																	// client
					{
						if (chatMessage.endsWith(safeSing)) {
							chatMessage = chatMessage.substring(0,
									chatMessage.length() - safeSing.length());
						}
						textArea.append(nick2 + ":" + chatMessage + "\n");
						scrollToBottom();
					}
				}
				// }
				catch (Exception e) {
					nitSig = false;
					turnOffStream();
					restartGame();
					try {
						JOptionPane.showMessageDialog(null,
								"Receiving Data Failed/Disconnect:\n" + e);
					} catch (ExceptionInInitializerError exc) {
					}
				}
			}
		}
	}

	// --- restart the game to the initial state ---
	private void restartGame() {
		// postaviTekstPolja();
		// setAllEnable(false);

		chatMessage = "";
		stopSending = true;

		ip.setEnabled(true);
		port.setEnabled(true);
		nick.setEnabled(true);
		create.setEnabled(true);
		join.setEnabled(true);

		novaPartija.setEnabled(false);
		messageFild.setEditable(false);
	}

	// --- Turn OFF all streams ---
	private void turnOffStream() {
		try {
			out.flush();
		} catch (Exception e) {
		}
		try {
			out.close();
		} catch (Exception e) {
		}
		try {
			in.close();
		} catch (Exception e) {
		}
		try {
			serverSocket.close();
		} catch (Exception e) {
		}
		try {
			conection.close();
		} catch (Exception e) {
		}
	}

	// --- scroll to bottom when receive chat message ---
	public void scrollToBottom() {
		textArea.setCaretPosition(textArea.getText().length());
	}

	// --- Main ---
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Onlychat2("a").setVisible(true);
				new Onlychat2("b").setVisible(true);
			}
		});
	}
}