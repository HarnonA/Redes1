package MultiChat;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class VelhaWindow extends JFrame {
	
	public VelhaWindow() {
		System.out.println("iniciar velha");
		JTextArea textArea = new JTextArea();
		JTextField messageFild = new JTextField(" ");
		add(textArea, BorderLayout.CENTER);
		add(messageFild, BorderLayout.SOUTH);
		
		
		setSize(300, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		//chatPanel.setVisible(true);
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new VelhaWindow();

	}

}
