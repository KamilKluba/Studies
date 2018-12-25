import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CarWindowApp extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;

	private JButton newCarButton = new JButton("Nowy samochód");
	private JButton deleteCarButton = new JButton("Usuñ samochód");
	private JButton changeCarButton = new JButton("Edytuj samochód");
	private JButton saveButton = new JButton("Zapisz");
	private JButton loadButton = new JButton("Wczytaj");
	private JButton exitButton = new JButton("Wyjœcie");
	private JButton infoButton = new JButton("O autorze");
	private JButton saveBinaryButton = new JButton("Zapisz bin");
	private JButton loadBinaryButton = new JButton("Wczytaj bin");

	private JLabel currentNameLabel = new JLabel("Marka: ", JLabel.CENTER);
	private JLabel currentYearLabel = new JLabel("Rok produkcji: ", JLabel.CENTER);
	private JLabel currentCapacityLabel = new JLabel("Pojemnoœæ silnika: ", JLabel.CENTER);
	private JLabel currentDescriptionLabel = new JLabel("Opis: ", JLabel.CENTER);
	
	private JTextField currentNameText = new JTextField(10);
	private JTextField currentYearText = new JTextField(10);
	private JTextField currentCapacityText = new JTextField(10);
	private JTextField currentDescText = new JTextField(10);
	
	private JMenu menu[] = {new JMenu("Plik"),
							new JMenu("Edycja"),
							new JMenu("Informacje")};
	
	private JMenuItem menuItem[] = {new JMenuItem("Zapisz do pliku"),
									new JMenuItem("Wczytaj z pliku"),
									new JMenuItem("Zamknij program"),
									new JMenuItem("Nowy samochód"),
									new JMenuItem("Usuñ samochód"),
									new JMenuItem("Edycja samochodu"),
									new JMenuItem("O autorze"),
									new JMenuItem("Zapisz bin"),
									new JMenuItem("Wczutaj bin")};
	
	private Car currentCar;
	
	public CarWindowApp(){
		super("CarWindowApp by Kamil Kluba");
		setSize(300,300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		for (JMenu data : menu)
			menuBar.add(data);
		
		menu[0].add(menuItem[0]);
		menu[0].add(menuItem[1]);
		menu[0].add(menuItem[2]);
		menu[1].add(menuItem[3]);
		menu[1].add(menuItem[4]);
		menu[1].add(menuItem[7]);
		menu[1].add(menuItem[8]);
		menu[1].add(menuItem[5]);
		menu[2].add(menuItem[6]);
		
		
		JPanel mainPanel = new JPanel(new GridLayout(0,2));	
		mainPanel.add(currentNameLabel);
		mainPanel.add(currentNameText);
		mainPanel.add(currentYearLabel);
		mainPanel.add(currentYearText);
		mainPanel.add(currentCapacityLabel);
		mainPanel.add(currentCapacityText);
		mainPanel.add(currentDescriptionLabel);
		mainPanel.add(currentDescText);
		mainPanel.add(newCarButton);
		mainPanel.add(deleteCarButton);
		mainPanel.add(changeCarButton);
		mainPanel.add(saveButton);
		mainPanel.add(loadButton);
		mainPanel.add(saveBinaryButton);
		mainPanel.add(loadBinaryButton);
		mainPanel.add(infoButton);
		mainPanel.add(exitButton);
		
		currentNameText.setEditable(false);
		currentYearText.setEditable(false);
		currentCapacityText.setEditable(false);
		currentDescText.setEditable(false);
		
		newCarButton.addActionListener(this);
		deleteCarButton.addActionListener(this);
		changeCarButton.addActionListener(this);
		saveButton.addActionListener(this);
		loadButton.addActionListener(this);
		exitButton.addActionListener(this);
		infoButton.addActionListener(this);
		saveBinaryButton.addActionListener(this);
		loadBinaryButton.addActionListener(this);
		
		for (JMenuItem item : menuItem)
			item.addActionListener(this);
		
		setContentPane(mainPanel);
		
		setVisible(true);
	}
	
	public void showCurrentCar(){
		if (currentCar == null){
			currentNameText.setText("");
			currentYearText.setText("");
			currentCapacityText.setText("");
			currentDescText.setText("");
		}
		else { 
			currentNameText.setText("" + currentCar.getCarName());
			currentYearText.setText("" + currentCar.getProductionYear());
			currentCapacityText.setText("" + currentCar.getEngineCapacity());
			currentDescText.setText(currentCar.getDescription());
		}
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource();
		
		try{
			if (src == saveButton || src == menuItem[0]){
				String fileName = JOptionPane.showInputDialog("Podaj nazwê pliku:");
				if (fileName == null || fileName.equals(""))
					return;
				Car.printToFile(fileName, currentCar);
			}
			else if(src == loadButton || src == menuItem[1]){
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("C:/Users"));
				chooser.setDialogTitle("Wybierz plik");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
					currentCar = Car.readFromFile(chooser.getSelectedFile().getName());
			}
			else if (src == exitButton || src == menuItem[2]){
				System.exit(0);
			}
			if (src == newCarButton || src == menuItem[3]){
				currentCar = CarWindowDialog.createNewCar(this);
			}
			else if (src == deleteCarButton || src == menuItem[4]){
				currentCar = null;
			}
			else if (src == changeCarButton || src == menuItem[5]){
				if (currentCar == null) 
					throw new CarException("¯aden samochód nie zosta³ utworzony");
				CarWindowDialog.changeCar(this, currentCar);
			}
			else if (src == infoButton || src == menuItem[6]){
				JOptionPane.showMessageDialog(this, "Program Car - wersja okienkowa\n" + 
													"Autor: Kamil Kluba\n" + 
													"Data:  paŸdziernik 2017 r.\n");
			}
			else if (src == saveBinaryButton || src == menuItem[7]){
				String fileName = JOptionPane.showInputDialog("Podaj nazwe pliku binarnego.");
				if (fileName == null || fileName.equals(""))
					return;
				Car.writeBinary(fileName, currentCar);
			}
			else if (src == loadBinaryButton || src == menuItem[8]){
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("C:/Users"));
				chooser.setDialogTitle("Wybierz plik");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
					currentCar = Car.readBinary(chooser.getSelectedFile().getName());
				}
			}
		}catch (CarException | HeadlessException e){
			JOptionPane.showMessageDialog(this, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
		showCurrentCar();
	}
	
	public static void main(String[] args){
		new CarWindowApp();
	}
}
