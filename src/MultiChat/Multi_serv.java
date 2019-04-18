package MultiChat;

import javax.swing.*;

//Class to precise who is connected : Client or Server
public class Multi_serv {

	public static void main(String[] args) {

		Object[] selectioValues = { "Server", "Client" };
		String initialSection = "Server";

		/** box for client or server */
		Object selection = JOptionPane.showInputDialog(null, "Login as : ",
				"MyChatApp", JOptionPane.QUESTION_MESSAGE, null,
				selectioValues, initialSection);
		
		// run server
		if (selection.equals("Server")) {
			String[] arguments = new String[] {};
			new Multi_thread().main(arguments);
			
		// run client
		} else if (selection.equals("Client")) {
			ClientDB db = new ClientDB();
			
			if(db.doLogin("")){
				String[] arguments = new String[] { "localhost", db.getName() };
				new Multi_cli().main(arguments);
			}
		}

	}

}
