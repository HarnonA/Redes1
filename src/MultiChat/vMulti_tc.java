package MultiChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JOptionPane;

// For every client's connection we call this class
public class vMulti_tc extends Thread {
	private String clientName = null;
	private DataInputStream input = null;
	private PrintStream output = null;
	private Socket clientSocket = null;
	private final vMulti_tc[] m_threads;
	private int maxClientsCount;
	
	private Vector<String> v;
	private String userName;

	public vMulti_tc(Socket clientSocket, vMulti_tc[] threads) {
		this.clientSocket = clientSocket;
		this.m_threads = threads;
		maxClientsCount = threads.length;
		
		v = new Vector<>();
	}

	public void run() {
		int maxClientsCount = this.maxClientsCount;
		vMulti_tc[] threads = this.m_threads;
		

		try {
			/*
			 * Create input and output streams for this client.
			 */
			input = new DataInputStream(clientSocket.getInputStream());
			output = new PrintStream(clientSocket.getOutputStream());
			
			String name = input.readLine().trim();
			userName = name;
			//while (true) {
				
				/*
				String line = input.readLine();
		        if (line.startsWith("/quit")) {
		          break;
		        }
		        
				
				if (name.indexOf('@') == -1) {
					break;
				} else {
					output.println("The name should not contain '@' character.");
				}
				*/
			//}
			
			// compartilhar a mesma lista de usuarios
			synchronized (this) {
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null) {
						// System.out.println("antes"+ i+ " " + threads[i].v);
						threads[i].v.add(name+"#");
						//System.out.println(i + " " + threads[i].v);

					}

				}
				/*
				int maior = 0;
				for (int i = 0; i < maxClientsCount; i++) {
					if(threads[i] != null)
						if(threads[i].v.size() > maior)
							maior = threads[i].v.size();
						//System.out.println(i + " " + threads[i].v);
				}
				System.out.println("maior " + maior);
				*/
				
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null) {
						Vector<String> vec = threads[0].v;
						threads[i].v = (Vector<String>) vec.clone();
						//System.out.println(threads[i].v);
					}
				}
				
			}
				
				/*
				int x = 0;
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null) {
						if(x < threads[i].v.size())
							x = i;
					}
					
				}
				//System.out.println(x);
				
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null) {
						threads[i].v = threads[x].v; 
					}
					
				}
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null) {
					//	System.out.println(i+ " " + threads[i].v);
					}
					
				}
				
			}*/
			
			
			

			/* Welcome the new client. */
			//output.println("Welcome " + name
				//	+ " to our chat room.\nTo leave enter /quit in a new line.");
			synchronized (this) {
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null && threads[i] == this) {
						clientName = "@" + name;
						break;
					}
				}
				
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null) {
						String z = new String();
						for (int j = 0; j < v.size(); j++) 
						z = z+v.get(j);
					//if (threads[i] != null && threads[i] != this) {
						threads[i].output.println(z);
					}
				}
			}
			/* Start the conversation. */
			while (true) {
				
				String comando = input.readLine();
				System.out.println(comando);
				
				String line = input.readLine();
				if (line.startsWith("/quit")) {
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
			output.println("9"+name);
			input.close();
			output.close();
			clientSocket.close();
		} catch (IOException e) {
		}
	}
}
