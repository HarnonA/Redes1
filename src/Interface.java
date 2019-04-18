import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;


public class Interface extends JFrame implements ActionListener {

	JButton botao1;
	JButton botao2;
	JButton botao3;
	JButton botao4;
	JButton botao5;
	JButton botao6;
	JButton botao7;
	JButton botao8;
	JButton botao9;
	int vez;
	
	public Interface(){
		super("Jogo da Velha");
		
		Container c = getContentPane();
		//===
		c.setLayout(new BorderLayout(10,10) );
		
		Container c2 = new Container();
		c2.setLayout(new GridLayout(3,3,10,10));
		
	//	
		// criar botoes
		c2.add(botao1 = new JButton(" "));
		c2.add(botao2 = new JButton(" "));
		c2.add(botao3 = new JButton(" "));
		c2.add(botao4 = new JButton(" "));
		c2.add(botao5 = new JButton(" "));
		c2.add(botao6 = new JButton(" "));
		c2.add(botao7 = new JButton(" "));
		c2.add(botao8 = new JButton(" "));
		c2.add(botao9 = new JButton(" "));
		
		// evento de cliclar no botao
		
		botao8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if( (vez%2) == 0){
					botao1.setText("O");
					botao1.setForeground(Color.RED);}
					else{
						botao1.setText("X");
						botao1.setForeground(Color.GREEN);
						
					}
					botao1.setBackground(Color.gray);
					vez++;
			}
		});
		

		botao7.addActionListener(this);
		botao2.addActionListener(this);
		/*
		botao2.addActionListener(this);
		botao3.addActionListener(this);
		botao4.addActionListener(this);
		botao5.addActionListener(this);
		botao6.addActionListener(this);
		botao7.addActionListener(this);
		botao8.addActionListener(this);
		botao9.addActionListener(this);
		*/
		
		
		// coloca botao na janela
		c.add(BorderLayout.NORTH, new JButton("X"));
		c.add(BorderLayout.CENTER, c2); 
		c.add(BorderLayout.SOUTH, new JButton("X"));
		//====
		
		// defalut	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 400);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Interface i = new Interface();
	}

	public void coisa(JButton b){
		b.addActionListener(this);
		b.setText("O");
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg) {
		// TODO Auto-generated method stub
		
		System.out.println(!botao7.isSelected());
		
		if(!botao7.isSelected())
			botao7.setText("O");
		/*
		if( (vez%2) == 0){
		botao1.setText("O");
		botao1.setForeground(Color.RED);}
		else{
			botao1.setText("X");
			botao1.setForeground(Color.GREEN);
			
		}
		botao1.setBackground(Color.gray);
		vez++;
		*/
	}

}
