/* Klasa GroupOfCarsWindowDialog pozwalaj¹ca na okienkow¹
 * komunikacjê u¿ytkownika z klas¹ GroupOfCars
 * 
 * Autor: Kamil Kluba
 * Data: Listopad 2017r.
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class GroupOfCarsWindowDialog extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;

	private GroupOfCars currentGroup;
	
	JButton addNewCarButton = new JButton("Dodaj nowy samochód");
	JButton editCarButton = new JButton("Edytuj samochod");
	JButton deleteCarButton = new JButton("Usuñ samochód");
	JButton loadCarButton = new JButton("Wczytaj samochód z pliku");
	JButton saveCarButton = new JButton("Zapisz samochód do pliku");
	
	JLabel groupNameLabel = new JLabel("Nazwa grupy:  ", JLabel.RIGHT);
	JLabel collectionTypeLabel = new JLabel("Rodzaj kolekcji:  ", JLabel.RIGHT);
	
	JTextField groupNameTextField = new JTextField(12);
	JTextField collectionTypeTextField = new JTextField(12);
	
	JMenu menu[] = {new JMenu("Lista samochodów"),
					new JMenu("Sortowanie"),
					new JMenu("W³aœciwoœci")};
	
	JMenuItem menuItem[] = {new JMenuItem("Dodaj nowy samochód"),
							new JMenuItem("Edytuj samochód"),
							new JMenuItem("Usuñ samochód"),
							new JMenuItem("Wczytaj samochód z pliku"),
							new JMenuItem("Zapisz samochód do pliku"),
							new JMenuItem("Sortuj alfabetycznie"),
							new JMenuItem("Sortuj wg roku produkcji"),
							new JMenuItem("Sortuj wg pojemnoœci silnika"),
							new JMenuItem("Zmieñ nazwê"),
							new JMenuItem("Zmieñ typ kolekcji"),
							new JMenuItem("O programie")};
	
	ViewCarList listTable;
	
	public GroupOfCarsWindowDialog(Window parent, GroupOfCars group, String name, GroupType type) throws HeadlessException, CarException{
		super("Modyfikacja grupy samochodów");
		setSize(500,450);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		
		if (group == null)
			currentGroup = new GroupOfCars(type, name);
		else{
			currentGroup = group;
			groupNameTextField.setText(group.getName());
			collectionTypeTextField.setText(group.getType().toString());
		}
		
		
		JMenuBar menuBar = new JMenuBar();
		for(JMenu data : menu)
			menuBar.add(data);
		menuBar.add(menuItem[10]);
		menu[0].add(menuItem[0]);
		menu[0].add(menuItem[1]);
		menu[0].add(menuItem[2]);
		menu[0].addSeparator();
		menu[0].add(menuItem[3]);
		menu[0].add(menuItem[4]);
		menu[1].add(menuItem[5]);
		menu[1].add(menuItem[6]);
		menu[1].add(menuItem[7]);
		menu[2].add(menuItem[8]);
		menu[2].add(menuItem[9]);
		
		setJMenuBar(menuBar);
		
		for (JMenuItem item : menuItem)
			item.addActionListener(this);
		
		addNewCarButton.addActionListener(this);
		editCarButton.addActionListener(this);
		deleteCarButton.addActionListener(this);
		loadCarButton.addActionListener(this);
		saveCarButton.addActionListener(this);
		
		groupNameTextField.setEditable(false);
		collectionTypeTextField.setEditable(false);
		
		JPanel mainPanel = new JPanel(new BorderLayout(5,5));
		JPanel infoPanel = new JPanel(new FlowLayout());
		JPanel listPanel = new JPanel();
		JPanel buttonPanel = new JPanel(new FlowLayout());
	
		infoPanel.add(groupNameLabel);
		infoPanel.add(groupNameTextField);
		infoPanel.add(collectionTypeLabel);
		infoPanel.add(collectionTypeTextField);
		
		listTable = new ViewCarList(currentGroup.getCollection(), 425,260);
		listPanel.add(listTable);
			
		buttonPanel.setPreferredSize(new Dimension(450, 75));	
		buttonPanel.add(addNewCarButton);
		buttonPanel.add(editCarButton);
		buttonPanel.add(deleteCarButton);
		buttonPanel.add(loadCarButton);
		buttonPanel.add(saveCarButton);
		
		mainPanel.add(infoPanel, BorderLayout.NORTH);
		mainPanel.add(listPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		setContentPane(mainPanel);
		
		setVisible(true);
	}
	
	public static GroupOfCars createNewGroup(Window parent) throws HeadlessException, CarException{
		Object[] groups = GroupType.values();
		String name = JOptionPane.showInputDialog("Podaj nazwe grupy: ");
		if (name == null || name.equals(""))
			return null;
		GroupType type = (GroupType)JOptionPane.showInputDialog(
                parent,
                "Wybierz grupê",
                null,
                JOptionPane.QUESTION_MESSAGE,
                null,
                groups,
                null);
		if (type == null)
			return null;
		
		 GroupOfCarsWindowDialog group = new GroupOfCarsWindowDialog(null, null, name, type);
		 group.groupNameTextField.setText(name);
		 group.collectionTypeTextField.setText(type.toString());
		 return group.currentGroup;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		try {
			if (source == menuItem[0] || source == addNewCarButton){
				Car car = CarWindowDialog.createNewCar(this);
				if (car != null)
					currentGroup.add(car);
			}
			else if (source == menuItem[1] || source == editCarButton){
				int index = listTable.getSelectedIndex();
				if (index >= 0){
					Iterator<Car> iterator = currentGroup.iterator();
					while (index-- > 0)
						iterator.next();
					CarWindowDialog.changeCar(this, iterator.next());
				}
			}
			else if (source == menuItem[2] || source == deleteCarButton){
				int index = listTable.getSelectedIndex();
				if (index >= 0){
					Iterator<Car> iterator = currentGroup.iterator();
					while (index-- > 0)
						iterator.next();
					iterator.next();
					iterator.remove();
				}
			}
			else if (source == menuItem[3] || source == saveCarButton){
				int index = listTable.getSelectedIndex();
				if (index >= 0){
					Iterator<Car> iterator = currentGroup.iterator();
					while (index-- > 0)
						iterator.next();
					Car group = iterator.next();
					JFileChooser chooser = new JFileChooser(".");
					int returnVal = chooser.showSaveDialog(this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						Car.printToFile(chooser.getSelectedFile().getName(), group);
					}
				}
			}
			else if (source == menuItem[4] || source == loadCarButton){
				JFileChooser chooser = new JFileChooser(".");
				int returnVal = chooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					Car car = Car.readFromFile(chooser.getSelectedFile().getName());
					currentGroup.add(car);
				}
			}
			else if (source == menuItem[5]){
				currentGroup.sortCarName();
			}
			else if (source == menuItem[6]){
				currentGroup.sortProductionYear();
			}
			else if (source == menuItem[7]){
				currentGroup.sortEngineCapacity();
			}
			else if (source == menuItem[8]){
				String name = JOptionPane.showInputDialog("Podaj nazwê grupy:");
				if (name == null)
					return;
				currentGroup.setName(name);
				groupNameTextField.setText(name);
			}
			else if (source == menuItem[9]){
				GroupType type = chooseGroup(this);
				if (type == null)
					return;
				collectionTypeTextField.setText(type.toString());
				currentGroup.setType(type);
			}
			else if (source == menuItem[10]){
				JOptionPane.showMessageDialog(this, "Program do zarz¹dzania grup¹ osób " + 
													"- wersja okienkowa\n\n" + 
													"Autor: Kamil Kluba\n" + 
													"Data:  listopad 2017 r.\n");
			}
		} catch (CarException e){
			JOptionPane.showMessageDialog(this, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
		listTable.refreshView();
	}
	
	private  GroupType chooseGroup(Window parent){
		Object[] groups = GroupType.values();
		GroupType type = (GroupType)JOptionPane.showInputDialog(
		                    parent,
		                    "Wybierz grupê",
		                    null,
		                    JOptionPane.QUESTION_MESSAGE,
		                    null,
		                    groups,
		                    null);
		return type;
	}
}

class ViewCarList extends JScrollPane{
	private static final long serialVersionUID = 1L;
	
	private Collection<Car> collection;
	private JTable table;
	private DefaultTableModel tableModel;
	
	public ViewCarList(Collection<Car> list, int width, int height){
		setPreferredSize(new Dimension(width, height));
		collection = list;
		setBorder(BorderFactory.createTitledBorder("Lista samochodów:"));
		String[] tableHeader = {"Marka", "Rok produkcji", "Pojemnosc silnika", "Opis"};
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
		for (Car car : collection){
			if (collection != null) {
				String[] row = {car.getCarName().toString(), "" + car.getProductionYear(), "" + car.getEngineCapacity(), car.getDescription()};
				tableModel.addRow(row);
			}
		}
	}
	
	public int getSelectedIndex(){
		int index = table.getSelectedRow();
		if (index < 0)
			JOptionPane.showMessageDialog(this, "¯aden samochód nie zosta³ utworzony/zaznaczony.", "B³¹d", JOptionPane.ERROR_MESSAGE);
		return index;
	}
}
