package cli_serv;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Interface3 extends JFrame {

	String str;
	JTextArea textArea;
	JTextField messageFild;
	private boolean bool = true;
	
	private ObjectOutputStream out = null;
	
	public Interface3(){
		super("Jogo da Velha");
		
		
		//Container c = new Container();
		
		
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());
		chatPanel.setPreferredSize(new Dimension(200, 30));
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		JScrollPane sp = new JScrollPane(textArea); 
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		chatPanel.add(sp, BorderLayout.CENTER);
		add(chatPanel, BorderLayout.EAST);
		
		JPanel pSouth = new JPanel();
		pSouth.setLayout(new BorderLayout());
		pSouth.setPreferredSize(new Dimension(300, 50));
		messageFild = new JTextField(" ");
		messageFild.setEditable(true);
		pSouth.add(messageFild, BorderLayout.CENTER);
		add(pSouth, BorderLayout.SOUTH);
		
		str = "";
		
		messageFild.addKeyListener(new KeyAdapter()
		{
			// quando 'enter' é pressionado a mensagem é enviada
			public void keyPressed(KeyEvent e) 
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					String s = messageFild.getText()+"\n";
					textArea.append(s);
					altera(s);
					sendMensage(s);
					try {
						//out.writeObject(messageFild.getText()+"\n");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						//System.out.println("Erro");
						
					}
			
				}
			}
			});
		
		
		System.out.println(str);
		
		// defalut	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 400);
		setVisible(true);
		setResizable(true);
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Interface3 i = new Interface3();
		System.out.println(i.str);
		
		//System.out.println(i.fazer());
	}
	
	private void sendMensage(String p) {
		try {
			if (bool) {
				out.writeObject(p);
				out.flush();
			}
		} catch (Exception e) {
			if (bool) {
				bool = false;
				try {
					JOptionPane.showMessageDialog(null,
							"Sending data/Disconnect:\n" + e);
				} catch (ExceptionInInitializerError exc) {
				}
			}
		}
	}
	
	
	
	
	public void altera(String s){
		str = s;
		
	}
	

}
