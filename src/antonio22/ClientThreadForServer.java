package antonio22;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Vector;

/**Thread for each user whom connects to server*/
public class ClientThreadForServer extends Thread {
	private String clientName = null;
	private BufferedReader input = null;
	private PrintStream output = null;
	private Socket clientSocket = null;
	private final ClientThreadForServer[] m_threads;
	private int maxClientsCount;
	private Server parentServer;
	
	//private Vector<String> users;

	public ClientThreadForServer(Socket clientSocket, Server sv, ClientThreadForServer[] threads) {
		this.clientSocket = clientSocket;
		this.m_threads = threads;
		maxClientsCount = threads.length;
		parentServer = sv;
		
		//users = new Vector<String>();
	}

	public void run() {

		int maxClientsCount = this.maxClientsCount;
		ClientThreadForServer[] threads = this.m_threads;

		try {

			/** Create the input and output channels of communication */
			input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			output = new PrintStream(clientSocket.getOutputStream());

			/** First message from client is his username */
			String username = input.readLine().trim();

			/**  share the same users list
			 *  all threads receive the new client 
			 */
			synchronized (this) {
				/** Update the usernames list in the server */
				parentServer.addUser(username + "#");

				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null && threads[i] == this) {
						clientName = "@" + username;
						break;
					}
				}


			}
			

			/** Welcome the new client. */
			synchronized (this) {
				
				/** update clients */
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null) {
						String welcome = new String();
						for (int j = 0; j < parentServer.getUsers().size(); j++)
						welcome = welcome + parentServer.getUser(j);
						threads[i].output.println(welcome);
					}
				}
				
			}
			/** Start the conversation. */
			while (true) {
				String line = input.readLine();
				if (line.startsWith("/quit")) {

					break;
				}
        		/** If the message is private sent it to the given client. */
				if (line.startsWith("@")) {
					String[] words = line.split("\\s", 2);
					if (words.length > 1 && words[1] != null) {
						words[1] = words[1].trim();
						if (!words[1].isEmpty()) {
							synchronized (this) {
								for (int i = 0; i < maxClientsCount; i++) {
									if (threads[i] != null && threads[i] != this
											&& threads[i].clientName != null
											&& threads[i].clientName.equals(words[0])) {
										threads[i].output.println("<" + username + "> " + words[1]);
                    /**
                     * Echo this message to let the client know the private
                     * message was sent.
                     */
										this.output.println(">" + username + "> " + words[1]);
										break;
									}
								}
							}
						}
					}
				} else {
          /** The message is public, broadcast it to all other clients. */
					synchronized (this) {
						for (int i = 0; i < maxClientsCount; i++) {
							if (threads[i] != null && threads[i].clientName != null) {
								threads[i].output.println("<" + username + "> " + line);
							}
						}
					}
				}
			}
			synchronized (this) {
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null && threads[i] != this
							&& threads[i].clientName != null) {
						threads[i].output.println("*** The user " + username
								+ " is leaving the chat room !!! ***");
					}
				}
			}
			output.println("*** Bye " + username + " ***");

      /**
       * Clean up. Set the current thread variable to null so that a new client
       * could be accepted by the server.
       */
			synchronized (this) {
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] == this) {
						threads[i] = null;
					}
				}
			}
      /*
       * Close the output stream, close the input stream, close the socket.
       */
			input.close();
			output.close();
			clientSocket.close();
		} catch (IOException e) {
		}
	}
}
