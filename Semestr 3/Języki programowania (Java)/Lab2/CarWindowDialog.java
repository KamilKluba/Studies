import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CarWindowDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private JLabel currentNameLabel = new JLabel("Marka: ", JLabel.CENTER);
	private JLabel currentYearLabel = new JLabel("Rok produkcji: ", JLabel.CENTER);
	private JLabel currentCapacityLabel = new JLabel("Pojemnoœæ silnika: ", JLabel.CENTER);
	private JLabel currentDescriptionLabel = new JLabel("Opis: ", JLabel.CENTER);
	
	private JComboBox<Name> currentNameText = new JComboBox<Name>(Name.values());
	private JTextField currentYearText = new JTextField(10);
	private JTextField currentCapacityText = new JTextField(10);
	private JTextField currentDescText = new JTextField(10);
	
	private JButton createButton = new JButton("Stwórz samochód");
	private JButton abortButton = new JButton("Anuluj");
	
	private Car car;
	
	public CarWindowDialog(Window parent, Car car){
		super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
		setSize(300,160);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		
		JPanel mainPanel = new JPanel(new GridLayout(0,2));	
		mainPanel.add(currentNameLabel);
		mainPanel.add(currentNameText);
		mainPanel.add(currentYearLabel);
		mainPanel.add(currentYearText);
		mainPanel.add(currentCapacityLabel);
		mainPanel.add(currentCapacityText);
		mainPanel.add(currentDescriptionLabel);
		mainPanel.add(currentDescText);
		mainPanel.add(createButton);
		mainPanel.add(abortButton);
		
		createButton.addActionListener(this);
		abortButton.addActionListener(this);
		
		this.car = car;
		
		if (car == null)
			setTitle("Nowy samochód");
		else{
			setTitle(car.toString());
			currentNameText.setSelectedItem(car.getCarName());
			currentYearText.setText("" + car.getProductionYear());
			currentCapacityText.setText("" + car.getEngineCapacity());
			currentDescText.setText(car.getDescription());
		}
		
		setContentPane(mainPanel);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource();
		
		if (src == createButton){
			try{
				if (car == null)
					car = new Car();
				car.setCarName((Name)currentNameText.getSelectedItem());
				car.setProductionYear(currentYearText.getText());
				car.setEngineCapacity(currentCapacityText.getText());
				car.setDescription(currentDescText.getText());
				dispose();
			} catch (CarException e){
				JOptionPane.showMessageDialog(this, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (src == abortButton)
			dispose();
	}
	
	public static Car createNewCar(Window parent){
		CarWindowDialog dialog = new CarWindowDialog(parent, null);
		return dialog.car;
	}
	
	public static void changeCar(Window parent, Car car){
		new CarWindowDialog(parent, car);
	}

}
