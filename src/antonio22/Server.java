package antonio22;

import java.awt.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.*;

public class Server extends JFrame {
	
	   /** socket server and client */
	  private volatile static ServerSocket serverSocket = null;
	  private static Socket clientSocket = null;
      private int portNumber = 2222, clientCounter;
      private JList userlist;
      private volatile Vector<String> usernames;

	  // limite de 10 clientes para server
	  private static final int maxClientsCount = 10;
	  private static final ClientThreadForServer[] clientThread = new ClientThreadForServer[maxClientsCount];

      public Server(){

            super("Usernames");
            usernames = new Vector<>();
            userlist = new JList(usernames);

            userlist.setVisibleRowCount(10);
            userlist.setFixedCellHeight(25);
            userlist.setFixedCellWidth(200);
            userlist.setSelectedIndex(ListSelectionModel.SINGLE_SELECTION);
            userlist.setSelectionBackground(Color.blue);
            add(new JScrollPane(userlist));
            setSize(250, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
      }

	  public void createServer() {

            /** Open a server socket on the portNumber (default 2222)*/
            try {
                  serverSocket = new ServerSocket(portNumber);
            } catch (IOException e) {
                e.printStackTrace();
            }
            final Server sv = this;
            /** Create a client socket for each connection and pass it to a new client thread.*/
            new Thread(){
                  public void run(){
                        while(true) {
                              try {
                                    clientSocket = serverSocket.accept();
                                    clientCounter = 0;

                                    /** if there is space, create a new thread
                                    * if don't, show it's full
                                    */
                                    for (clientCounter = 0; clientCounter < maxClientsCount; clientCounter++) {
                                          if (clientThread[clientCounter] == null) {
                                                (clientThread[clientCounter] = new ClientThreadForServer(clientSocket, sv, clientThread)).start();
                                                break;
                                          }
                                    }
                                    if (clientCounter == maxClientsCount) {
                                          PrintStream output = new PrintStream(clientSocket.getOutputStream());
                                          output.println("Servidor est� cheio, tente novamente mais tarde.");
                                          output.close();
                                          clientSocket.close();
                                    }
                              } catch (IOException e) {
                                    JOptionPane.showMessageDialog(null, "Erro ao estabelecer conex�o com o ClientSocket");
                                    e.printStackTrace();
                              }
                        }
                  }
            }.start();

      }

      public void update(Vector users){
            users.clear();
            for(int i=0; i < users.size();i++){
                  users.add(users.get(i).toString());

            }
            userlist.updateUI();
      }

      public Vector<String> getUsers() {
            return usernames;
      }

      public String getUser(int index) {
            return usernames.get(index);
      }


      public synchronized void addUser(String username) {
            usernames.add(username);
      }

      public synchronized void removeUser(String username){
            usernames.remove(username);
      }
}