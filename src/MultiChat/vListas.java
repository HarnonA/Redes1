package MultiChat;

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
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import MultiChat.Multi_cli.ChatFrame;

public class vListas extends JFrame {
	
	Vector<String> v;
	String botaoApertado; 
	
	JList list;
	public int size = 0;
	
	public vListas(){
		super("velha");
		setLayout(new BorderLayout());
		//setLayout(new FlowLayout());
		
		v = new Vector<String>();	
				
		list = new JList(v);		
		list.setVisibleRowCount(10);
		list.setFixedCellHeight(40);
		list.setFixedCellWidth(200);
		list.setSelectedIndex(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectionBackground(Color.blue);
		JButton button = new JButton("Exit");
		//add(button, BorderLayout.SOUTH);
		
		buttonPressed();		
		//listener para exit
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("exit");
			}
		});

		
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
		
		add(new JScrollPane(list));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		//setSize(300, 500);
		setLocationRelativeTo(null);
		setVisible(true);		
		setResizable(false);
	
		
	}
	
	public void buttonPressed(){
		list.addKeyListener(new KeyAdapter() 
	    {
	        public void keyPressed(KeyEvent evt)
	        {
	            if(evt.getKeyCode() == KeyEvent.VK_ENTER)
	            {
	            	botaoApertado = (String) list.getSelectedValue();
	                System.out.println(list.getSelectedValue());
	            }
	        }
	    });
	}
	
	
	public void updt(){
		list.updateUI();	
		
	}
	/*
	public void addCli(Vector<String> s){
		v.clear();
		for(int i = 0; i < s.size(); i++){
			v.add(s.get(i));
		}
		
		list.updateUI();	
	}
	*/
	
	public void addCli(String s){
		//System.out.println(s);
		
		String palavra = s;
		String letra = "#";
		int quantidade = palavra.length() - palavra.replaceAll(letra, "").length();
		System.out.println(quantidade);
		v.clear();
		for (int i = 0; i < quantidade; i++) {
			v.add(s.split("#")[i]);
		}
		
		list.updateUI();	
	}
	
	
	
	/*
	public void addCli(String s){
		v.add(s);
		++size;
		list.updateUI();	
	}
	*/
	
	
	public void removeCli(String s){

		for (int i = 0; i < v.size() ; i++) {
			System.out.println(v.get(i));
			if(s.equals(v.get(i))){
				System.out.println("ok");
				v.remove(i);
				list.updateUI();
			}
			
			
		}
		list.updateUI();	
	}
	
	public int geSize(){
		return size;
	}
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		vListas l = new vListas();
		Vector<String> a= new Vector<>();
		l.addCli("XXXX#");
		//a.add("x#aaa#bbb");
		
		//l.addCli(a);
		
		//l.removeCli("d");

		
		
		/*
		l.addCli("a");
		l.addCli("b");
		l.addCli("c");
		l.addCli("d");
		l.addCli("e");
		l.addCli("f");
		l.addCli("g");
		l.addCli("h");
		l.addCli("i");
		l.addCli("j");
		l.addCli("k");
		
		
		System.out.println(l.geSize());
		
		l.removeCli("a");
		*/
		
		
		
		
		
		

	}

}
