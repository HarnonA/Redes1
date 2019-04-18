import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;


public class MainVelha extends JFrame  {

	/**
	 * @param args
	 */
	
	JButton botao1;
	JButton botao2;
	JButton botao3;
	JButton botao4;
	JButton botao5;
	JButton botao6;
	JButton botao7;
	JButton botao8;
	JButton botao9;
	static int velha[][];
	
	int vez;
	
	// =========================================
	// =========================================
	
	
	public MainVelha() {
		super("Jogo da Velha");
		
		int linha, coluna;
		
		linha = 0;
		coluna = 0;

		// inicializa a velha
		velha = new int[3][3];
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				velha[i][j] = 0;
		
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout(10,10) );
		
		Container c2 = new Container();
		c2.setLayout(new GridLayout(3,3,10,10));
		
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
		
		
		// coloca botao na janela
		//c.add(BorderLayout.NORTH, new JButton("X"));
		c.add(BorderLayout.CENTER, c2);
		//c.add(BorderLayout.SOUTH, new JButton("X"));
		// ====

		// defalut
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 400);
		setVisible(true);
		
		click();
		
	}
	
	// =========================================
	// =========================================
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MainVelha interfac = new MainVelha();
		interfac.repaint();

	}

	
	// define qual simbolo marcar de acordo com a vez do jogador
	public void acaoParaJogador(JButton b){
		
		if(velha[0][0] == 0){
		
		if( (vez%2) == 0){
			b.setText("O");
			b.setForeground(Color.RED);}
			else{
				b.setText("X");
				b.setForeground(Color.GREEN);
				
			}
			b.setBackground(Color.gray);
			vez++;
			velha[0][0]=1;
			System.out.println("ok");
		}else System.out.println("ocupado");
		System.out.println(velha[0][0]);
	}
	
	
	// marca simbolo no botao
	public void click(){
		
		botao1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botao1);
			}
		});
		
		botao2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botao2);
			}
		});
		
		botao3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botao3);
			}
		});
		
		botao4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botao4);
			}
		});
		
		botao5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botao5);
			}
		});
		
		botao6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botao6);
			}
		});
		
		botao7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botao7);
			}
		});
		
		botao8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botao8);
			}
		});
		
		botao9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botao9);
			}
		});
	}

	
	
	
	

}