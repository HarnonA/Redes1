import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;


public class Evento extends JFrame implements ActionListener {

	
	JButton botao;
	/**
	 * @param args
	 */
	
	public Evento(){
		super("Evento");
		
		botao = new JButton("X");
		botao.addActionListener(this);
		getContentPane().add(botao);
		
		
		setSize(200,200);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Evento();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		botao.setText("O");
		botao.setForeground(Color.RED);
		botao.setBackground(Color.gray);
		
	}

}
