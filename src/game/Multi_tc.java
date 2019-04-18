package game;

import java.awt.BorderLayout;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// For every client's connection we call this class
public class Multi_tc extends Thread {
	private String clientName = null;
	private BufferedReader input = null;
	private PrintStream output = null;
	private Socket clientSocket = null;
	private final Multi_tc[] m_threads;
	private int maxClientsCount;
	
	private Vector<String> vector;
	private String userName;

	public Multi_tc(Socket clientSocket, Multi_tc[] threads) {
		this.clientSocket = clientSocket;
		this.m_threads = threads;
		maxClientsCount = threads.length;
		
		vector = new Vector<String>();
	}

	public void run() {
		int maxClientsCount = this.maxClientsCount;
		Multi_tc[] threads = this.m_threads;
		System.out.println("Max "+maxClientsCount);
		

		try {
			/*
			 * Create input and output streams for this client.
			 */	
			
			input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			output = new PrintStream(clientSocket.getOutputStream());
			
			String name = input.readLine().trim();
			userName = name;
		
			/*
			 *  share the same users list 
			 *  all threads receive the new client 
			 */
			synchronized (this) {
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null) {
						threads[i].vector.add(name+"#");
					}
				}
				
				/** take the list sizes */
				LinkedList<Integer> index = new LinkedList<>();
				int biggest = 1;
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null)
						if(threads[i].vector.size() > biggest)
							biggest = threads[i].vector.size();
				}
				
				/** take the biggest one */
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null)
						if(threads[i].vector.size() == biggest)
							biggest = i;
				}
				
				/** update all threads with the biggest one */
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null) {
						Vector<String> vec = threads[biggest].vector;
						threads[i].vector = (Vector<String>) vec.clone();
					}
				}
			}
			

			/* Welcome the new client. */
			synchronized (this) {
				
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null && threads[i] == this) {
						clientName = "@" + name;
						break;
					}
				}
				
				// update clients
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null) {
						String z = new String();
						for (int j = 0; j < vector.size(); j++) 
						z = z+vector.get(j);
						threads[i].output.println(z);
					}
				}
				
			}
			/* Start the conversation. */
			int cont = 0;			
			while (true) {
				/*
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null) {
						cont++;
						
						if (cont == 2) {
							//System.out.println(userName);
							output.println("game"+ threads[0].userName);
							//output.println("game"+ threads[1].userName);
							//new Game(threads[0].clientSocket, threads[1].clientSocket).start();
						}
						
						if (cont == 4) {
							//System.out.println(userName);
							output.println("game"+ threads[3].userName);
							//output.println("game"+ threads[1].userName);
							//new Game(threads[0].clientSocket, threads[1].clientSocket).start();
						}
						
					}
				}
				*/
				
				// receive data from client
				String line;
				line = input.readLine().trim();
				if(line.startsWith("9")){
					String remove = line.split("9")[1]+"#";
					for (int i = 0; i < maxClientsCount; i++) {
						if (threads[i] != null) {
							
							int index = threads[i].vector.indexOf(remove);
							threads[i].vector.remove(index);
							//System.out.println(index);//threads[i].v);
							System.out.println(threads[i].vector);
						}
							
					}
					
					System.out.println("line " + line);
				}
				else if(line.equals("TooBusy")){
					System.out.println("break");
					break;
				}
			
				
				
				// código pra conversar
				// desnecessario
				if (line.startsWith("ç")) {
					break;
				}
				/* If the message is private sent it to the given client. */
				if (line.startsWith("@")) {
					String[] words = line.split("\\s", 2);
					if (words.length > 1 && words[1] != null) {
						words[1] = words[1].trim();
						if (!words[1].isEmpty()) {
							synchronized (this) {
								for (int i = 0; i < maxClientsCount; i++) {
									if (threads[i] != null
											&& threads[i] != this
											&& threads[i].clientName != null
											&& threads[i].clientName
													.equals(words[0])) {
										threads[i].output.println("Private from " + name + "> "
												+ words[1]);
										/*
										 * Echo this message to let the client
										 * know the private message was sent.
										 */
										this.output.println("Private to " + name + "> "
												+ words[1]);
										break;
									}
								}
							}
						}
					}
				} else {
					/* The message is public, broadcast it to all other clients. */
					synchronized (this) {
						for (int i = 0; i < maxClientsCount; i++) {
							if (threads[i] != null
									&& threads[i].clientName != null) {
								threads[i].output.println("<" + name + "> " + line);
							}
						}
					}
				}
			}
			synchronized (this) {
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null && threads[i] != this
							&& threads[i].clientName != null) {
						threads[i].output.println("*** The user " + name
								+ " is leaving the chat room !!! ***");
					}
				}
			}
			output.println("*** Bye " + name + " ***");

			/*
			 * Clean up. Set the current thread variable to null so that a new
			 * client could be accepted by the server.
			 */
			synchronized (this) {
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] == this) {
						threads[i] = null;
					}
				}
			}
			/*
			 * Close the output stream, close the input stream, close the
			 * socket.
			 */
			input.close();
			output.close();
			clientSocket.close();
		} catch (IOException e) { 
			System.out.println("Erro");
		}
	}
}
