package testes;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class Listas extends JFrame {
	
	Vector<String> v;
	JList list;
	public int size = 0;
	
	public Listas(){
		super("velha");
		setLayout(new FlowLayout());
		
		v = new Vector<String>();	
				
		list = new JList(v);		
		list.setVisibleRowCount(10);
		list.setFixedCellHeight(40);
		list.setFixedCellWidth(200);
		list.setSelectedIndex(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectionBackground(Color.blue);
		
		
		list.addKeyListener(new KeyAdapter() 
	    {
	        public void keyPressed(KeyEvent evt)
	        {
	            if(evt.getKeyCode() == KeyEvent.VK_ENTER)
	            {
	                System.out.println(list.getSelectedValue());
	            }
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
		
	}
	
	public void addCli(String s){
		v.add(s);
		++size;
		list.updateUI();	
	}
	
	public void removeCli(String s){
		for (int i = 0; i < size ; i++) {
			if(s.equals(v.get(i))){
				System.out.println("ok");
				v.remove(i);
				size--;
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
		Listas l = new Listas();
		
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
		
		
		
		l.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		l.setSize(300, 500);
		l.setVisible(true);
		
		
		

	}

}
