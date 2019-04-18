package Velha;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Botao extends JFrame{

	JButton b;
	
	public Botao(){
		b = new JButton("XXXX");
	}
	
	public void x(){
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				b.setEnabled(false);
				
					b.setText("O");
					b.setForeground(Color.RED);
				
					b.setText("X");
					b.setForeground(Color.GREEN);

				
				b.setBackground(Color.gray);

			}

		});
		
	}

}
