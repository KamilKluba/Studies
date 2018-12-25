/*
 * Komunikator - serwer
 * 
 * Kamil Kluba
 * 02.01.2017r.
 * 
 */

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
import javax.swing.ScrollPaneConstants;


class CommunicatorServer extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;

	static final int SERVER_PORT = 25000;
	
	private JLabel clientLabel = new JLabel("Aktualni u¿ytkownicy:");
	private JComboBox<CommunicatorUserThread> clientMenu = new JComboBox<CommunicatorUserThread>();
	private JTextArea  textAreaConversation  = new JTextArea(15,18);
	private JScrollPane scrollPaneConversation = new JScrollPane(textAreaConversation,
	  				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	private boolean isFinished = false;
	
	CommunicatorServer(){ 
		super("SERWER");
	  	setSize(300,340);
	  	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  	setResizable(false);
	  	
	  	JPanel panel = new JPanel();
	  	panel.add(clientLabel);
	  	clientMenu.setPrototypeDisplayValue(new CommunicatorUserThread("#########################"));
	  	panel.add(clientMenu);
	  	textAreaConversation.setLineWrap(true);
	  	textAreaConversation.setWrapStyleWord(true);
	  	textAreaConversation.setEditable(false);
	  	panel.add(scrollPaneConversation);
	  	setContentPane(panel);
	  	
	  	setVisible(true);
	  	new Thread(this).start(); 
	}
	
	synchronized public void printReceivedMessage(CommunicatorUserThread client, String part0, String part1){
	  	String text = textAreaConversation.getText();
	  	textAreaConversation.setText(client.getName() + " do " + part0 + ": " + part1 + "\n" + text);
	  	for (int i = 0; i < clientMenu.getItemCount(); i++){
	  		if (clientMenu.getItemAt(i).toString().equals(part0)){
	  			clientMenu.getItemAt(i).sendMessage("Od " + client.getName() + ": " + part1);
	  		}
	  	}
	}
	
	
	synchronized public void addClient(CommunicatorUserThread client){
		clientMenu.addItem(client);
		String users = "-:-BOXSTART-:-";
		for (int i = 0; i < clientMenu.getItemCount(); i++){
			users = users + "-:-BOX-:-" + clientMenu.getItemAt(i).toString() ;
		}
		for (int i = 0; i < clientMenu.getItemCount(); i++){
			CommunicatorUserThread user = clientMenu.getItemAt(i);
			user.sendMessage(users);
		}
		
		String text = textAreaConversation.getText();
		textAreaConversation.setText(client.getName() + " przylaczyl sie. \n" + text);
	}
	
	synchronized public void removeClient(CommunicatorUserThread client){
		clientMenu.removeItem(client);
		String users = "-:-BOXSTART-:-";
		for (int i = 0; i < clientMenu.getItemCount(); i++){
			users = users + "-:-BOX-:-" + clientMenu.getItemAt(i).toString() ;
		}
		for (int i = 0; i < clientMenu.getItemCount(); i++){
			CommunicatorUserThread user = clientMenu.getItemAt(i);
			user.sendMessage(users);
		}
		
		String text = textAreaConversation.getText();
		textAreaConversation.setText(client.getName() + " rozlaczyl sie. \n" + text);
	}
	
	public void run() {
		boolean socket_created = false;

		try (ServerSocket serwer = new ServerSocket(SERVER_PORT)) {
			String host = InetAddress.getLocalHost().getHostName();
			textAreaConversation.setText("Serwer zostal uruchomiony na hoscie " + host);
			socket_created = true;

			while (!isFinished) {
				Socket socket = serwer.accept();
				if (socket != null) {
					new CommunicatorUserThread(this, socket);
				}
			}
		} catch (IOException e) {
			System.out.println(e);
			if (!socket_created) {
				JOptionPane.showMessageDialog(null, "Gniazdko dla serwera nie moze byc utworzone");
				System.exit(0);
			} else {
				JOptionPane.showMessageDialog(null, "BLAD SERWERA: Nie mozna polaczyc sie z klientem ");
			}
		}
	}
	
	public static void main(String [] args){
		new CommunicatorServer();
	}
}



class CommunicatorUserThread implements Runnable {
	private Socket socket;
	private String name;
	private CommunicatorServer communicator;
	private boolean isFinished = false;
	
	private ObjectOutputStream outputStream = null;
	
	CommunicatorUserThread(String prototypeDisplayValue){
		name = prototypeDisplayValue;
	}
	
	CommunicatorUserThread(CommunicatorServer server, Socket socket) { 
		communicator = server;
	  	this.socket = socket;
	  	new Thread(this).start();  
	}
	
	public String getName(){ return name; }
	
	public String toString(){ return name; }
	
	public void sendMessage(String message){
		try {
			outputStream.writeObject(message);
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
	   		communicator.addClient(this);
			while(!isFinished){
				message = (String)input.readObject();
				String parts[] = message.split("-:-USER-:-");
				communicator.printReceivedMessage(this, parts[0], parts[1]);
			}
			communicator.removeClient(this);
			socket.close();
			socket = null;
	   	} catch(Exception e) {
	   		communicator.removeClient(this);
	   	}
	}
	
	public void isFinished(){
		isFinished = !isFinished;
	}
}


