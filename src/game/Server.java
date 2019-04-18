package game;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Server {
	
	   /** socket server and client */
	  private static ServerSocket serverSocket = null;
	  private static Socket clientSocket = null;
      private int portNumber = 2222;

	  // limite de 10 clientes para server
	  private static final int maxClientsCount = 10;
	  private static final Multi_tc[] m_threads = new Multi_tc[maxClientsCount];

	  public void createServer() {

	        /** port chosen */
            System.out.println("Serve online on port " + portNumber);

            /*
             * Open a server socket on the portNumber (default 2222)
             */
            try {
                serverSocket = new ServerSocket(portNumber);
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
             * Create a client socket for each connection and pass it to a new client
             * thread.
             */
            while (true) {
                  try {
                        clientSocket = serverSocket.accept();
                            int i = 0;
                            /*
                             * if there is space, create a new thread
                             * if don't, show it's full
                             */
                        for (i = 0; i < maxClientsCount; i++) {
                              if (m_threads[i] == null) {
                                    (m_threads[i] = new Multi_tc(clientSocket, m_threads)).start();
                                break;
                              }
                        }
                        if (i == maxClientsCount) {
                              PrintStream output = new PrintStream(clientSocket.getOutputStream());
                              System.out.println("busyy");
                              JOptionPane.showMessageDialog(null, "Busy server", "Erro", JOptionPane.ERROR_MESSAGE);
                              output.println("Server too busy. Try later.");
                              output.close();
                              clientSocket.close();
                        }
                  } catch (IOException e) {
                        System.out.println("erro on clientSocker");
                        System.out.println(e);
                  }
            }
      }
}