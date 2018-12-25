/*
 * Ksi¹¿ka telefoniczna - serwer
 * 
 * Kamil Kluba
 * 02.01.2017r.
 * 
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;


class PhoneBook extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;

	static final int SERVER_PORT = 25000;
	
	private JLabel clientLabel   = new JLabel("Aktualni u¿ytkownicy:");
	private JComboBox<PhoneBookClientThread> clientMenu = new JComboBox<PhoneBookClientThread>();
	private JTextArea  textAreaConversation  = new JTextArea(15,18);
	private JScrollPane scrollPaneConversation = new JScrollPane(textAreaConversation,
	  				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	private ConcurrentHashMap<String, Integer> book = new ConcurrentHashMap<String, Integer>();
	private boolean isFinished = false;
	
	PhoneBook(){ 
		super("SERWER");
	  	setSize(300,340);
	  	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  	setResizable(false);
	  	
	  	JPanel panel = new JPanel();
	  	panel.add(clientLabel);
	  	clientMenu.setPrototypeDisplayValue(new PhoneBookClientThread("#########################"));
	  	panel.add(clientMenu);
	  	textAreaConversation.setLineWrap(true);
	  	textAreaConversation.setWrapStyleWord(true);
	  	textAreaConversation.setEditable(false);
	  	panel.add(scrollPaneConversation);
	  	setContentPane(panel);
	  	
	  	setVisible(true);
	  	new Thread(this).start(); 
	}
	
	synchronized public void printReceivedMessage(PhoneBookClientThread client, String message){
		String text = textAreaConversation.getText();
		textAreaConversation.setText(client.getName() + " >>> " + message + "\n" + text);
	}
	
	synchronized public void printSentMessage(PhoneBookClientThread client, String message){
	  	String text = textAreaConversation.getText();
	  	textAreaConversation.setText(client.getName() + " <<< " + message + "\n" + text);
	}
	
	@SuppressWarnings("unchecked")
	synchronized public void loadFromFile(String file_name)throws Exception{
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file_name))){
			book = (ConcurrentHashMap<String, Integer>)ois.readObject();
		} catch (FileNotFoundException e){
			JOptionPane.showMessageDialog(this, "Nie odnaleziono pliku " + file_name);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Wyst¹pi³ b³¹d podczas odczytu danych z pliku");
		}
	}
	
	synchronized public void saveToFile(String file_name) throws Exception{
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file_name))){
			oos.writeObject(book);
		} catch (FileNotFoundException e){
			JOptionPane.showMessageDialog(this, "Nie odnaleziono pliku " + file_name);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Wyst¹pi³ b³¹d podczas odczytu danych z pliku");
		}
	}
	
	synchronized public String getNumber(String name){
		if (book.get(name) == null)
			return "ERORR W ksiazce nie ma danej osoby.";
		return "OK " + book.get(name);
	}
	
	synchronized public String putNumber(String name, int number){
		if (book.get(name) != null)
			return "ERROR W ksiazce znajduje sie juz podany numer!";
		book.put(name, number);
		return "OK Pomyslnie zapisano numer.";
	}
	
	synchronized public String replaceNumber(String name, int number){
		if (book.get(name) != null){
			book.replace(name, number);
			return "OK Pomyslnie zamieniono numer.";
		}
		return "ERROR W ksiazce nie ma osoby o danym imieniu";
	}
	
	synchronized public String deleteNumber(String name){
		if (book.get(name) == null)
			return "ERROR W ksiazce nie ma osoby o danym imieniu";
		book.remove(name);
		return "OK Pomyslnie usunieto numer.";
	}
	
	synchronized public String getList(){
		if (book.size() > 0)
			return "OK " + book.keySet().toString();
		return "ERROR Ksiazka jest pusta.";
	}
	
	synchronized public void addClient(PhoneBookClientThread client){
		clientMenu.addItem(client);
	}
	
	synchronized public void removeClient(PhoneBookClientThread client){
		clientMenu.removeItem(client);
	}
	
	public void decide(PhoneBookClientThread client, String message){
		String parts[] = message.split(" ");
		
		switch (parts[0]){
		case "LOAD":
			try {
				loadFromFile(parts[1]);
				client.sendMessage("OK - Wczytano dane z pliku");
			} catch (Exception e) {
				client.sendMessage("ERROR - Nie mozna wczytac danych z pliku");
			}
			break;
		case "SAVE":
			try{
				saveToFile(parts[1]);
				client.sendMessage("OK - Zapisano dane do pliku");
			}
			catch (Exception e) {
				client.sendMessage("ERROR - Nie mozna zapisac danych do pliku");
			}
			break;
		case "GET":
			client.sendMessage(getNumber(parts[1]));
			break;
		case "PUT":
			client.sendMessage(putNumber(parts[1], Integer.parseInt(parts[2])));
			break;
		case "REPLACE":
			client.sendMessage(replaceNumber(parts[1], Integer.parseInt(parts[2])));
			break;
		case "DELETE":
			client.sendMessage(deleteNumber(parts[1]));
			break;
		case "LIST":
			client.sendMessage(getList());
			break;
		case "CLOSE":
			isFinished = !isFinished;
			break;
		case "BYE":
			client.isFinished();
			String text = textAreaConversation.getText();
			textAreaConversation.setText("Uzytkownik rozlaczyl sie. \n" + text);
			break;
		default:
			client.sendMessage("Nie da siê wykonaæ danego polecenia.");
		}
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
					new PhoneBookClientThread(this, socket);
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
		new PhoneBook();
	}
}



class PhoneBookClientThread implements Runnable {
	private Socket socket;
	private String name;
	private PhoneBook book;
	private boolean isFinished = false;
	
	private ObjectOutputStream outputStream = null;
	
	PhoneBookClientThread(String prototypeDisplayValue){
		name = prototypeDisplayValue;
	}
	
	PhoneBookClientThread(PhoneBook server, Socket socket) { 
		book = server;
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
	   		book.addClient(this);
			while(!isFinished){
				message = (String)input.readObject();
				book.printReceivedMessage(this,message);
				book.decide(this, message);
			}
			book.removeClient(this);
			socket.close();
			socket = null;
	   	} catch(Exception e) {
	   		book.removeClient(this);
	   	}
	}
	
	public void isFinished(){
		isFinished = !isFinished;
	}
}


