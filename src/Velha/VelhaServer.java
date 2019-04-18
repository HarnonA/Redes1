package Velha;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.midi.Receiver;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class VelhaServer extends JFrame {
	private JTextArea displayArea;
	private DatagramSocket socket;
		
	

	public VelhaServer(){
		super("Server");
		
		
		displayArea = new JTextArea();
		add( new JScrollPane(displayArea), BorderLayout.CENTER);
		setSize(300, 400);
		setVisible(true);
		
		try{
			socket = new DatagramSocket(5000);
		} catch(SocketException e){
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	public void waitForPackets(){
		while(true){
			try{
				byte[] data = new byte[100];
				DatagramPacket receivePacket = new DatagramPacket(data, data.length);
				
				socket.receive(receivePacket);
				
				displayMessage("\nPacket received:" + "\nfrom host:");
				sendPacketToClient(receivePacket);
				
			} catch(IOException io){
				io.printStackTrace();
			}
		}
	}
	
	private void sendPacketToClient(DatagramPacket receivePacket){
		displayMessage("Data to client: ");
		DatagramPacket sendPacket = new DatagramPacket(receivePacket.getData(),
				receivePacket.getLength(), receivePacket.getAddress(),
				receivePacket.getPort());
		socket.send(sendPacket);
		displayMessage("Packet sent ");
	}
	
	private void display
	
	
}
