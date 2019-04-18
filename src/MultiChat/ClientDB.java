package MultiChat;

import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class ClientDB {
	LinkedList<String> usernames;
	LinkedList<String> passwords;
	private String nameC;
	private String passwordC;

	public ClientDB() {
		nameC = new String();
		passwordC = new String();

		usernames = new LinkedList<String>();
		passwords = new LinkedList<String>();
		startDb();
	}

	private void startDb() {
		usernames.add("");
		passwords.add("");

		usernames.add("admin");
		passwords.add("");

		usernames.add("user");
		passwords.add("");

		usernames.add("user2");
		passwords.add("user");

		usernames.add("user3");
		passwords.add("user");
	}

	private boolean checkLogin(String u, String p) {
		for (int i = 0; i < usernames.size(); i++) {
			if (u.equals(usernames.get(i)) && p.equals(passwords.get(i)))
				return true;
		}
		return false;
	}

	public boolean doLogin(String s) {
		JTextField username = new JTextField(); // user field
		JTextField password = new JPasswordField(); // password field
		Object[] message = { s + "\nUsername:", username, "Password:", password };

		int option = JOptionPane.showConfirmDialog(null, message, "Login",
				JOptionPane.OK_CANCEL_OPTION);

		if (option == JOptionPane.OK_OPTION) {
			nameC = username.getText();
			passwordC = password.getText();

			if (checkLogin(nameC, passwordC)) {
				System.out.println("Login successful");
				return true;
			}

			else {
				System.out.println("login failed");
				return doLogin("Try again");

			}
		} else {
			System.out.println("Login canceled");
		}
		return false;
	}

	public String getName() {
		return nameC;
	}

	public static void main(String[] args) {
		ClientDB c = new ClientDB();
		if (c.doLogin(""))
			System.out.println(c.getName());
	}

}
