package testes;

import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class atualizar {
	LinkedList<String> usernames;
	LinkedList<String> passwords;
	
	private atualizar(){
		usernames = new LinkedList<String>();
		passwords = new LinkedList<String>();
		
		usernames.add("admin");
		passwords.add("123");
		
		usernames.add("user1");
		passwords.add("user");
		
		usernames.add("user2");
		passwords.add("user");
		
		usernames.add("user3");
		passwords.add("user");
	}
	
	
	public boolean checkLogin(String u, String p){
		for (int i = 0; i < usernames.size(); i++) {
			if( u.equals( usernames.get(i)) && p.equals(passwords.get(i)) ) 
					return true;
		}
		return false;
	}

	public static void main(String[] args) {
		
		JTextField username = new JTextField();	// campo usuario
		JTextField password = new JPasswordField();	//campo senha
		Object[] message = { "Username:", username, "Password:", password };

		int option = JOptionPane.showConfirmDialog(null, message, "Login",
				JOptionPane.OK_CANCEL_OPTION);
		
		if (option == JOptionPane.OK_OPTION) {
			if(new atualizar().checkLogin(username.getText(), password.getText()))
				System.out.println("Login successful");
			 else {
				System.out.println("login failed");
			}
		} else {
			System.out.println("Login canceled");
		}
		
		
		

	}

}
