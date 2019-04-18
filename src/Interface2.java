import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import Velha.Velha;


public class Interface2 extends JFrame  {

	/**
	 * @param args
	 */
	JButton[] botoes;
	String grid[];
	
	int vez;
	
	// =========================================
	// =========================================
	
	
	public Interface2() {
		super("Jogo da Velha");
		
		botoes = new JButton[9];
		vez = 0;
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout(10,10) );
		
		Container c2 = new Container();
		c2.setLayout(new GridLayout(3,3,10,10));
		
		// criar botoes
		c2.add(botoes[0] = new JButton(" "));
		c2.add(botoes[1]= new JButton(" "));
		c2.add(botoes[2] = new JButton(" "));
		c2.add(botoes[3] = new JButton(" "));
		c2.add(botoes[4] = new JButton(" "));
		c2.add(botoes[5] = new JButton(" "));
		c2.add(botoes[6] = new JButton(" "));
		c2.add(botoes[7] = new JButton(" "));
		c2.add(botoes[8] = new JButton(" "));
		
		grid = new String[9];
		for (int i = 0; i < 9; i++) {
			grid[i] = " ";
			
		}
		
		
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
		
		
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				Interface2 interfac = new Interface2();
				//new Velha().setVisible(true);				
			}
		});
		
		

	}
	
	public void checaVencedor(String xo){
		System.out.println("x");
		
		// checa linhas
		if (grid[0].equals(xo) && grid[1].equals(xo) && grid[2].equals(xo)){
			ganhador(0, 1, 2);
		}
			
		else if(grid[3].equals(xo) && grid[4].equals(xo) && grid[5].equals(xo)){
			ganhador(3, 4, 5);
		}
		else if(grid[6].equals(xo) && grid[7].equals(xo) && grid[8].equals(xo)){
			ganhador(6, 7, 8);
		}
		
		
		// checa colunas
		else if (grid[0].equals(xo) && grid[3].equals(xo) && grid[6].equals(xo)){
			ganhador(0, 3, 6);
		}
		else if(grid[1].equals(xo) && grid[4].equals(xo) && grid[7].equals(xo)){
			ganhador(1, 4, 7);
		}
		else if(grid[2].equals(xo) && grid[5].equals(xo) && grid[8].equals(xo)){
			ganhador(2, 5, 8);
		}
		
		// checa diagonal
		else if (grid[0].equals(xo) && grid[4].equals(xo) && grid[8].equals(xo)){
			ganhador(0, 4, 8);
		}
		else if (grid[2].equals(xo) && grid[4].equals(xo) && grid[6].equals(xo)){
			ganhador(2, 4, 6);
		}
		
		
	}

	public void ganhador(int a, int b, int c) {

		for (int i = 0; i < 9; i++) {
			if (i != a && i != b && i != c)
				botoes[i].setEnabled(false);
		}
	}
	
	
	// define qual simbolo marcar de acordo com a vez do jogador
	public void acaoParaJogador(JButton b, int posicao) {
		if (grid[posicao] == " ") {
			if ((vez % 2) == 0) {
				b.setText("O");
				b.setForeground(Color.RED);
				grid[posicao] = "o";
				checaVencedor("o");
			} else {
				b.setText("X");
				b.setForeground(Color.GREEN);
				grid[posicao] = "x";
				checaVencedor("x");

			}
			b.setBackground(Color.gray);
			vez++;
			
			
		} else
			System.out.println("Ocupado");
	}
	
	// marca simbolo no botao
	public void click(){
		
		botoes[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botoes[0],0);
			}
		});
		
		botoes[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botoes[1], 1);
			}
		});
		
		botoes[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botoes[2], 2);
			}
		});
		
		botoes[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botoes[3], 3);
			}
		});
		
		botoes[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botoes[4], 4);
			}
		});
		
		botoes[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botoes[5], 5);
			}
		});
		
		botoes[6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botoes[6], 6);
			}
		});
		
		botoes[7].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botoes[7], 7);
			}
		});
		
		botoes[8].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acaoParaJogador(botoes[8], 8);
			}
		});
	}

	
	
	
	

}
