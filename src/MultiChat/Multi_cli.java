package MultiChat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

// Class to manage Client chat Box.
public class Multi_cli {
	static String name;
	static Listas lista;
	
	/** Chat client access */
	static class ChatAccess extends Observable {
		private Socket socket;
		private OutputStream outputStream;

		@Override
		public void notifyObservers(Object arg) {
			super.setChanged();
			super.notifyObservers(arg);
		}

		/** Create socket, and receiving thread */
		public ChatAccess(String server, int port) throws IOException {
			socket = new Socket(server, port);
			outputStream = socket.getOutputStream();

			Thread receivingThread = new Thread() {
				@Override
				public void run() {
					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(socket.getInputStream()));
						String line;
							while ((line = reader.readLine()) != null)
							notifyObservers(line);
						
					} catch (IOException io) {
						notifyObservers(io);
					}
				}
			};
			receivingThread.start();
		}

		private static final String CRLF = "\r\n"; // newline

		/** Send a line of text */
		public void send(String text) {
			try {
				outputStream.write((text + CRLF).getBytes());
				outputStream.flush();
			} catch (IOException ex) {
				notifyObservers(ex);
			}
		}

		/** Close the socket */
		public void close() {
			try {
				socket.close();
			} catch (IOException ex) {
				notifyObservers(ex);
			}
		}
	}
	
	//====================================
	//        end of ChatAcess
	//====================================

	/** Chat client UI */
	static class ChatFrame extends JFrame implements Observer {

		VelhaGame velha;
		private ChatAccess chatAccess;
		private JButton botao; 
		
		public ChatFrame(ChatAccess chatAccess) {
			this.chatAccess = chatAccess;
			chatAccess.addObserver(this);
			buildGUI();
		}

		/** Builds the user interface */
		private void buildGUI() {
			//
			lista = new Listas(name);
			botao = new JButton("SAIR");
			lista.addNewButton(botao);
			chatAccess.send(name);	
				
			/** Only exit pressing the button 'exit' */
			ActionListener sendListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    	System.out.println("Sair");
                        chatAccess.send("9"+name);
                        ((JButton) e.getSource()).getTopLevelAncestor().hide(); 
                        dispose();
                }
            };
            botao.addActionListener(sendListener);
            
            
	
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					chatAccess.close();					
				}
			});
		}

		/** Updates the UI depending on the Object argument */
		public void update(Observable o, Object arg) {
			System.out.print("update ");
			final Object finalArg = arg;
			System.out.print("*"+finalArg.toString()+"*");
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					
					if (finalArg.toString().contains("9"))	{		
						System.out.println("if 9");
						lista.removeCli(finalArg.toString());
						//chatAccess.send("TooBusy");
					}
					
					else if(finalArg.toString().startsWith("game")){
						System.out.println("game: " + finalArg);
						new TicTacToeClient("localhost");
						
						//velha = new VelhaGame(chatAccess.socket);
						//velha.start();
						
						//new Game(null, null).start();
						/*String s = finalArg.toString();
						s = s.split("game")[1];
						synchronized (this) {
							
						
						if(name.equals(s)){
							System.out.println(name);
						new Onlychat2(name).main(null);
						}
						}*/
						//new Onlychat2(name).main(null);
						//System.out.println("game "+finalArg.toString());
					}
					else if(finalArg.toString().contains("pressed")){
						String nAcao = finalArg.toString().split("pressed")[1];
						System.out.println("Eh " + nAcao);
						if(nAcao.equals("0"))
							velha.acao(0);
						else if(nAcao.equals("1"))
							velha.acao(1);
						else if(nAcao.equals("2"))
							velha.acao(2);
						else if(nAcao.equals("3"))
							velha.acao(3);
						else if(nAcao.equals("4"))
							velha.acao(4);
						else if(nAcao.equals("5"))
							velha.acao(5);
						else if(nAcao.equals("6"))
							velha.acao(6);
						else if(nAcao.equals("7"))
							velha.acao(7);
						else if(nAcao.equals("8"))
							velha.acao(8);
						
						
						
						
					}
										
					else {
						System.out.println("add");
						try{
						lista.addCli(finalArg.toString());
						}catch(NullPointerException e){
							chatAccess.send("TooBusy");
							botao.setEnabled(false);
							
							
							//System.out.println("To busy");
						}
					}
				}
			});
		}
		
	}

	public static void main(String[] args) {
		String server = args[0];
		name = args[1];
		int port = 2222;
		ChatAccess access = null;		
		
		try {
			access = new ChatAccess(server, port);
		} catch (IOException ex) {
			System.out.println("Cannot connect to " + server + ":" + port);
			System.out.println("Maybe the server is out :(");
			//ex.printStackTrace();
			System.exit(0);
		}
		
		JFrame frame = new ChatFrame(access);
		frame.setTitle(name);
		
	}
}