package MultiChat;

import javax.swing.*;

//Class to precise who is connected : Client or Server
public class vMulti_serv {

	public static void main(String[] args) {

		Object[] selectioValues = { "Server", "Client" };
		String initialSection = "Server";

		// caixa para server ou cliente
		Object selection = JOptionPane.showInputDialog(null, "Login as : ",
				"MyChatApp", JOptionPane.QUESTION_MESSAGE, null,
				selectioValues, initialSection);
		
		// opçao para rodar o server
		if (selection.equals("Server")) {
			String[] arguments = new String[] {};
			new Multi_thread().main(arguments);
			
		// opçao para rodar cliente
		} else if (selection.equals("Client")) {
			//String IPServer = JOptionPane
				//	.showInputDialog("Enter the Server ip adress");
			
			ClientDB db = new ClientDB();
			
			
			if(db.doLogin("")){
				String[] arguments = new String[] { "localhost", db.getName() };
				new Multi_cli().main(arguments);
			}
		}

	}

}