package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class Listas extends JFrame {
	
	Vector<String> vector;
	String botaoApertado; 
	String userName;
	JList list;

	
	public Listas(String userName){
		super("Velha  -  " + userName);
		this.userName = userName;

		setLayout(new BorderLayout());
		vector = new Vector<String>();	
				
		list = new JList(vector);		
		
		list.setVisibleRowCount(10);
		list.setFixedCellHeight(40);
		list.setFixedCellWidth(200);
		list.setSelectedIndex(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectionBackground(Color.blue);
		
		buttonPressed();
		/*
		JButton button = new JButton("Exit");
		add(button, BorderLayout.SOUTH);
		
		
		ActionListener sendListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                	System.out.println("Sair");
                	dispose();
                    
                    
            }
        };
        button.addActionListener(sendListener);
		*/
		//buttonPressed();		
		//listener para exit
		
		

		
		/*
		list.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent e) {
				Object selected = list.getSelectedValue();
				//list.getSelectedValue();
				selected = list.getSelectedValue();
					System.out.println(selected);
			}
		});
		*/
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("bye");
				
				//dispose();
				//setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				
				
				//setVisible(false);
			}
		});
		
		add(new JScrollPane(list));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);		
		setResizable(false);
	
		
	}
	
	
	/** add a new actionlistener to a button */
	public void addNewButton(JButton b){
		
		add(b, BorderLayout.SOUTH);
		/*
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("exit");
			}
		});
		*/
		
		setVisible(true);	
		list.updateUI();
	}
	
	
	public String buttonPressed(){
		botaoApertado = null;
		list.addKeyListener(new KeyAdapter() 
	    {
	        public void keyPressed(KeyEvent evt)
	        {
	            if(evt.getKeyCode() == KeyEvent.VK_ENTER)
	            {
					if (list.getSelectedValue() != userName) {
						
						botaoApertado = (String) list.getSelectedValue();
						System.out.println(botaoApertado );
						//System.out.println(userName.equals(botaoApertado));
						//System.out.println("x"+list.getSelectedValue());
					}
	            }
	        }
	    });
		return botaoApertado;
	}
	
	
	
	/** updates the vector and show in the window */
	public void updt(Vector vec){
		vector.clear();
		for(int i=0; i < vec.size();i++){
			vector.add(vec.get(i).toString());
			
		}
		list.updateUI();
	}
	
	/*
	public void updateVector(){
		for(int i = 0; i < vector.size(); i++){
			System.out.println("update vector"+vector.get(i));
		}
	}
	*/
	
	/** add a new client and update window */
	// '#' is a flag
	public void addCli(String s){
		
		String palavra = s;
		String letra = "#";
		int quantidade;
		if(palavra != null)
		if (palavra.contains("#")) {
			quantidade = palavra.length() - palavra.replaceAll(letra, "").length();
			vector.clear();
			for (int i = 0; i < quantidade; i++) {
				vector.add(s.split("#")[i]);
			}
		}
		
			//JOptionPane.showMessageDialog(null, "Busy server", "Erro", JOptionPane.ERROR_MESSAGE);
		
		
		list.updateUI();	
	}

	/** remove a client and update */
	// '9' is a flag
	public void removeCli(String s){
		System.out.println("Sring "+s);
		s = s.split("9")[1];
		for (int i = 0; i < vector.size() ; i++) {
			if(vector.get(i).equals(s)){
				System.out.println("remvendo " + vector.get(i));
				vector.remove(i);
			}	
		}
		list.updateUI();	
	}
	
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Listas l = new Listas("lista");
		Vector vec = new Vector<>();
	
		l.addCli("oi#");
		vec.add("aaa#");
		vec.add("bbb#");
		vec.add("lista");
		l.updt(vec);
		
		//l.addCli("aaa#bbb#ccc#");
		//l.removeCli("9aaa");
		JButton j = new JButton("OOO");
		
	
		

	}

}

