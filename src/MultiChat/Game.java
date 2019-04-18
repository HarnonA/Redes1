package MultiChat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Game extends Thread {
	JFrame frame;
	JButton button;
	
	public Game(Socket player1, Socket player2){
		frame = new JFrame();
		button = new JButton("Button");
		frame.add(button, BorderLayout.SOUTH);
		frame.setSize(300, 300);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		
	}
	
	
	public void run(){
		button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    	System.out.println("Sair");
                }
            });
		
	}
	
	public static void main(String[] args) {
		//new Game().start();
		// TODO Auto-generated method stub

	}

}
