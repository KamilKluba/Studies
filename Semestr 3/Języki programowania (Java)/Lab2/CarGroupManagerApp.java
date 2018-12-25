/* Klasa CarGroupManagerApp pozwalaj�ca zarz�dza� obiektami
 * klasy GroupOfCars oraz przeprowadza� na nich operacje logiczne
 * 
 * Autor: Kamil Kluba
 * Data: Listopad 2017r.
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class CarGroupManagerApp extends JFrame implements ActionListener, WindowListener{
	private static final long serialVersionUID = 1L;

	private List<GroupOfCars> currentList = new ArrayList<GroupOfCars>();
	
	private static final String ALL_GROUPS_FILE = "LISTA_GRUP.BIN"; 
	
	JButton addGroupButton = new JButton("Utw�rz");
	JButton editGroupButton = new JButton("Edytuj");
	JButton deleteGroupButton = new JButton("Usu�");
	JButton saveGroupButton = new JButton("Zapisz");
	JButton loadGroupButton = new JButton("Wczytaj");
	JButton unionButton = new JButton("Suma");
	JButton intersectionButton = new JButton("Iloczyn");
	JButton differenceButton = new JButton("R�nica");
	JButton symetricDifferenceButton = new JButton("R�nica symetryczna");
	
	
	JMenu menu[] = {new JMenu("Grupy"),
					new JMenu("Grupy specjalne")};
	
	JMenuItem menuItem[] = {new JMenuItem("Utw�rz grup�"),
							new JMenuItem("Edytuj grup�"),
							new JMenuItem("Usu� grup�"),
							new JMenuItem("Zapisz grup� do pliku"),
							new JMenuItem("Wczytaj grup� z pliku"),
							new JMenuItem("Po��czenie grup"),
							new JMenuItem("Cz�� wsp�lna grup"),
							new JMenuItem("R�nica grup"),
							new JMenuItem("R�nica symetryczna grup"),
							new JMenuItem("O programie")};
	
	ViewCarGroup listTable;
	
	public CarGroupManagerApp(){
		super("Zarz�dzanie grupami samochod�w");
		setSize(500,400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		
		addWindowListener(this);
		
		try{
			loadGroupListFromFile(ALL_GROUPS_FILE);
			JOptionPane.showMessageDialog(null, "Dane zosta�y wczytane z pliku " + ALL_GROUPS_FILE);
		} catch (CarException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "B��d", JOptionPane.ERROR_MESSAGE);
		}
		
		JMenuBar menuBar = new JMenuBar();
		for(JMenu data : menu)
			menuBar.add(data);
		menuBar.add(menuItem[9]);
		
		menu[0].add(menuItem[0]);
		menu[0].add(menuItem[1]);
		menu[0].add(menuItem[2]);
		menu[0].addSeparator();
		menu[0].add(menuItem[3]);
		menu[0].add(menuItem[4]);
		menu[1].add(menuItem[5]);
		menu[1].add(menuItem[6]);
		menu[1].add(menuItem[7]);
		menu[1].add(menuItem[8]);
		
		setJMenuBar(menuBar);
		
		for (JMenuItem item : menuItem)
			item.addActionListener(this);
		
		addGroupButton.addActionListener(this);
		editGroupButton.addActionListener(this);
		deleteGroupButton.addActionListener(this);
		saveGroupButton.addActionListener(this);
		loadGroupButton.addActionListener(this);
		unionButton.addActionListener(this);
		intersectionButton.addActionListener(this);
		differenceButton.addActionListener(this);
		symetricDifferenceButton.addActionListener(this);
		
		JPanel mainPanel = new JPanel(new BorderLayout(5,5));
		JPanel listPanel = new JPanel();
		JPanel buttonPanel = new JPanel(new FlowLayout());
	
		
		listTable = new ViewCarGroup(currentList, 425,260);
		listPanel.add(listTable);
			
		buttonPanel.setPreferredSize(new Dimension(450, 75));
		buttonPanel.add(addGroupButton);
		buttonPanel.add(editGroupButton);
		buttonPanel.add(deleteGroupButton);
		buttonPanel.add(saveGroupButton);
		buttonPanel.add(loadGroupButton);
		buttonPanel.add(unionButton);
		buttonPanel.add(intersectionButton);
		buttonPanel.add(differenceButton);
		buttonPanel.add(symetricDifferenceButton);

		mainPanel.add(listPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		setContentPane(mainPanel);
		
		setVisible(true);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		try {
			if (source == menuItem[0] || source == addGroupButton){
				GroupOfCars group = GroupOfCarsWindowDialog.createNewGroup(this);
				if (group != null){
					currentList.add(group);
				}
			}
			else if (source == menuItem[1] || source == editGroupButton){
				int index = listTable.getSelectedIndex();
				if (index >= 0){
					Iterator<GroupOfCars> iterator = currentList.iterator();
					while (index-- > 0)
						iterator.next();
					new GroupOfCarsWindowDialog(this, iterator.next(), null, null);
				}
			}
			else if (source == menuItem[2] || source == deleteGroupButton){
				int index = listTable.getSelectedIndex();
				if (index >= 0){
					Iterator<GroupOfCars> iterator = currentList.iterator();
					while (index-- >= 0)
						iterator.next();
					iterator.remove();
				}
			}
			else if (source == menuItem[3] || source == saveGroupButton){
				int index = listTable.getSelectedIndex();
				if (index >= 0){
					Iterator<GroupOfCars> iterator = currentList.iterator();
					while (index-- > 0)
						iterator.next();
					GroupOfCars group = iterator.next();
					JFileChooser chooser = new JFileChooser();
					int returnVal = chooser.showSaveDialog(this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						GroupOfCars.printToFile(chooser.getSelectedFile().getPath(), group);
					}
				}
			}
			else if (source == menuItem[4] || source == loadGroupButton){
				JFileChooser chooser = new JFileChooser(".");
				int returnVal = chooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					GroupOfCars group = GroupOfCars.readFromFile(chooser.getSelectedFile().getName());
					currentList.add(group);
				}
			}
			else if (source == menuItem[5] || source == unionButton) {
				String message1 = 
						"SUMA GRUP\n\n" + 
			            "Tworzenie grupy zawieraj�cej wszystkie osoby z grupy pierwszej\n" + 
						"oraz wszystkie osoby z grupy drugiej.\n" + 
			            "Wybierz pierwsz� grup�:";
				String message2 = 
						"SUMA GRUP\n\n" + 
					    "Tworzenie grupy zawieraj�cej wszystkie osoby z grupy pierwszej\n" + 
						"oraz wszystkie osoby z grupy drugiej.\n" + 
					    "Wybierz drug� grup�:";
				GroupOfCars group1 = chooseGroup(this, message1);
				if (group1 == null)
					return;
				GroupOfCars group2 = chooseGroup(this, message2);
				if (group2 == null)
					return;
				currentList.add(GroupOfCars.createGroupUnion(group1, group2));
			}
			else if (source == menuItem[6] || source == intersectionButton) {
				String message1 = 
						"ILOCZYN GRUP\n\n" + 
				        "Tworzenie grupy os�b, kt�re nale�� zar�wno do grupy pierwszej,\n" +
						"jak i do grupy drugiej.\n" + 
				        "Wybierz pierwsz� grup�:";
				String message2 = 
						"ILOCZYN GRUP\n\n" + 
						"Tworzenie grupy os�b, kt�re nale�� zar�wno do grupy pierwszej,\n" +
						"jak i do grupy drugiej.\n" + 
						"Wybierz drug� grup�:";
				GroupOfCars group1 = chooseGroup(this, message1);
				if (group1 == null)
					return;
				GroupOfCars group2 = chooseGroup(this, message2);
				if (group2 == null)
					return;
				currentList.add(GroupOfCars.createGroupIntersection(group1, group2));
			}
			else if (source == menuItem[7] || source == differenceButton) {
				String message1 = 
						"RӯNICA GRUP\n\n" + 
				        "Tworzenie grupy os�b, kt�re nale�� do grupy pierwszej\n" +
						"i nie ma ich w grupie drugiej.\n" + 
				        "Wybierz pierwsz� grup�:";
				String message2 = 
						"RӯNICA GRUP\n\n" + 
						"Tworzenie grupy os�b, kt�re nale�� do grupy pierwszej\n" +
						"i nie ma ich w grupie drugiej.\n" + 
						"Wybierz drug� grup�:";
				GroupOfCars group1 = chooseGroup(this, message1);
				if (group1 == null)
					return;
				GroupOfCars group2 = chooseGroup(this, message2);
				if (group2 == null)
					return;
				currentList.add(GroupOfCars.createGroupDifference(group1, group2) );
			}
			else if (source == menuItem[8] || source == symetricDifferenceButton) {
				String message1 = "RӯNICA SYMETRYCZNA GRUP\n\n"
						+ "Tworzenie grupy zawieraj�cej osoby nale��ce tylko do jednej z dw�ch grup,\n"
						+ "Wybierz pierwsz� grup�:";
				String message2 = "RӯNICA SYMETRYCZNA GRUP\n\n"
						+ "Tworzenie grupy zawieraj�cej osoby nale��ce tylko do jednej z dw�ch grup,\n"
						+ "Wybierz drug� grup�:";
				GroupOfCars group1 = chooseGroup(this, message1);
				if (group1 == null)
					return;
				GroupOfCars group2 = chooseGroup(this, message2);
				if (group2 == null)
					return;
				currentList.add(GroupOfCars.createGroupSymmetricDiff(group1, group2));
			}
			else if (source == menuItem[9]){
				JOptionPane.showMessageDialog(this, "Program do zarz�dzania grupami samochod�w " + 
													"- wersja okienkowa\n\n" + 
													"Autor: Kamil Kluba\n" + 
													"Data:  listopad 2017 r.\n");
			}
		} catch (CarException e){
			JOptionPane.showMessageDialog(this, e.getMessage(), "B��d", JOptionPane.ERROR_MESSAGE);
		}
		listTable.refreshView();
	}
	
	private GroupOfCars chooseGroup(Window parent, String message){
		Object[] groups = currentList.toArray();
		GroupOfCars type = (GroupOfCars)JOptionPane.showInputDialog(
		                    parent,
		                    message,
		                    "Wybierz grup�",
		                    JOptionPane.QUESTION_MESSAGE,
		                    null,
		                    groups,
		                    null);
		return type;
	}
	
	@SuppressWarnings("unchecked")
	void loadGroupListFromFile(String file_name) throws CarException{
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file_name))){
			currentList = (List<GroupOfCars>)ois.readObject();
		} catch (FileNotFoundException e){
			throw new CarException ("Nie odnaleziono pliku " + file_name);
		} catch (Exception e) {
			throw new CarException("Wyst�pi� b��d podczas odczytu danych z pliku");
		}
	}
	
	void saveGroupListToFile(String file_name) throws CarException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file_name))) {
			oos.writeObject(currentList);
		} catch (FileNotFoundException e) {
			throw new CarException("Nie odnaleziono pliku " + file_name);
		} catch (IOException e) {
			throw new CarException("Wyst�pi� b��d podczas zapisu danych do pliku.");
		}
	}
	
	public static void main(String args[]){
		new CarGroupManagerApp();
	}


	@Override
	public void windowActivated(WindowEvent arg0) {
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
	/*	try {
			saveGroupListToFile(ALL_GROUPS_FILE);
			JOptionPane.showMessageDialog(null, "Dane zosta�y zapisne do pliku " + ALL_GROUPS_FILE);
		} catch (CarException e){
			JOptionPane.showMessageDialog(null, e.getMessage(), "B��d", JOptionPane.ERROR_MESSAGE);
		}*/
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		windowClosed(arg0);
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}
	@Override
	public void windowIconified(WindowEvent arg0) {
	}
	@Override
	public void windowOpened(WindowEvent arg0) {
	}
}

class ViewCarGroup extends JScrollPane{
	private static final long serialVersionUID = 1L;
	
	private List<GroupOfCars> list;
	private JTable table;
	private DefaultTableModel tableModel;
	
	public ViewCarGroup(List<GroupOfCars> list, int width, int height){
		setPreferredSize(new Dimension(width, height));
		this.list = list;
		setBorder(BorderFactory.createTitledBorder("Lista samochod�w:"));
		String[] tableHeader = {"Nazwa grupy", "Typ kolekcji", "Liczba samochod�w"};
		tableModel = new DefaultTableModel(tableHeader, 0);
		table = new JTable(tableModel) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int rowIndex, int colIndex){
				return false;
			}
		};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		setViewportView(table);
	}
	
	public void refreshView(){
		tableModel.setRowCount(0);
		for (GroupOfCars group : list){
			if (list != null) {
				String[] row = {group.getName(), group.getType().toString(), "" + group.getSize()};
				tableModel.addRow(row);
			}
		}
	}
	
	public int getSelectedIndex(){
		int index = table.getSelectedRow();
		if (index < 0)
			JOptionPane.showMessageDialog(this, "�aden grupa nie zosta�a utworzona/zaznaczona.", "B��d", JOptionPane.ERROR_MESSAGE);
		return index;
	}
}
