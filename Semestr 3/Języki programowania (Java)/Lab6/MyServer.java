/* 
 *  Komunikator sieciowy
 *   - program serwera
 *
 *  Autor: Pawel Rogalinski
 *   Data: 1 grudnia 2017 r.
 */
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


class MyServer extends JFrame implements ActionListener, Runnable {
	
	private static final long serialVersionUID = 1L;

	static final int SERVER_PORT = 25000;
	
	public static void main(String [] args){
		new MyServer();
	}
	
	
	private JLabel clientLabel   = new JLabel("Odbiorca:");
	private JLabel messageLabel  = new JLabel("Napisz:");
	private JLabel textAreaLabel = new JLabel("Dialog:");
	private JComboBox<ClientThread> clientMenu = new JComboBox<ClientThread>();
	private JTextField messageField = new JTextField(20);
	private JTextArea  textArea  = new JTextArea(15,18);
	private JScrollPane scroll = new JScrollPane(textArea,
	  				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	MyServer(){ 
		super("SERWER");
	  	setSize(300,340);
	  	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  	JPanel panel = new JPanel();
	  	panel.add(clientLabel);
	  	clientMenu.setPrototypeDisplayValue(new ClientThread("#########################"));
	  	panel.add(clientMenu);
	 	panel.add(messageLabel);
	  	panel.add(messageField);
	  	messageField.addActionListener(this);
	  	textArea.setLineWrap(true);
	  	textArea.setWrapStyleWord(true);
	  	panel.add(textAreaLabel);
	  	textArea.setEditable(false);
	  	panel.add(scroll);
	  	setContentPane(panel);
	  	setVisible(true);
	  	new Thread(this).start(); // Uruchomienie dodatkowego watka
	                              // czekajacego na nowych klientow
	}
	
	synchronized public void printReceivedMessage(ClientThread client, String message){
		String text = textArea.getText();
		textArea.setText(client.getName() + " >>> " + message + "\n" + text);
	}
	
	synchronized public void printSentMessage(ClientThread client, String message){
	  	String text = textArea.getText();
	  	textArea.setText(client.getName() + " <<< " + message + "\n" + text);
	}
	
	synchronized void addClient(ClientThread client){
	  	clientMenu.addItem(client);
	}  
		
	synchronized void removeClient(ClientThread client){
      	clientMenu.removeItem(client);
	}
	
	public void actionPerformed(ActionEvent event){
		String message;
		Object source = event.getSource();
	  	if (source==messageField){
	  		ClientThread client = (ClientThread)clientMenu.getSelectedItem();
			if (client != null) {
				message = messageField.getText();
				printSentMessage(client, message);
				client.sendMessage(message);
			}
	  	}
	  	repaint();
	}
	
	
	public void run() {
		boolean socket_created = false;
	
		// inicjalizacja po��cze� sieciowych
		try (ServerSocket serwer = new ServerSocket(SERVER_PORT)) {
			String host = InetAddress.getLocalHost().getHostName();
			System.out.println("Serwer zosta� uruchomiony na hoscie " + host);
			socket_created = true;
			// koniec inicjalizacji po��cze� sieciowych

			while (true) {  // oczekiwanie na po��czenia przychdz�ce od klient�w
				Socket socket = serwer.accept();
				if (socket != null) {
					// Tworzy nowy w�tek do obs�ugi klienta, kt�re
					// w�a�nie po��czy� si� z serwerem.
					new ClientThread(this, socket);
				}
			}
		} catch (IOException e) {
			System.out.println(e);
			if (!socket_created) {
				JOptionPane.showMessageDialog(null, "Gniazdko dla serwera nie mo�e by� utworzone");
				System.exit(0);
			} else {
				JOptionPane.showMessageDialog(null, "BLAD SERWERA: Nie mozna polaczyc sie z klientem ");
			}
		}
	}
	
} // koniec klasy MyServer



class ClientThread implements Runnable {
	private Socket socket;
	private String name;
	private MyServer myServer;
	
	private ObjectOutputStream outputStream = null;
	
	// UWAGA: Ten konstruktor tworzy nieaktywny obiekt ClientThread,
	// kt�ry posiada tylko nazw� prototypow�, potrzebn� dla
	// metody setPrototypeDisplayValue z klasy JComboBox
	ClientThread(String prototypeDisplayValue){
		name = prototypeDisplayValue;
	}
	
	ClientThread(MyServer server, Socket socket) { 
		myServer = server;
	  	this.socket = socket;
	  	new Thread(this).start();  // Utworzenie dodatkowego watka
	  	                           // do obslugi komunikacji sieciowej
	}
	
	public String getName(){ return name; }
	
	public String toString(){ return name; }
	
	public void sendMessage(String message){
		try {
			outputStream.writeObject(message);
			if (message.equals("exit")){
				myServer.removeClient(this);
				socket.close();
				socket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void run(){  
		String message;
	   	try( ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
	   		 ObjectInputStream input = new ObjectInputStream(socket.getInputStream()); )
	   	{
	   		outputStream = output;
	   		name = (String)input.readObject();
	   		myServer.addClient(this);
			while(true){
				message = (String)input.readObject();
				myServer.printReceivedMessage(this,message);
				if (message.equals("exit")){
					myServer.removeClient(this);
					break;		
				}
			}
			socket.close();
			socket = null;
	   	} catch(Exception e) {
	   		myServer.removeClient(this);
	   	}
	}
	
} // koniec klasy ClientThread


