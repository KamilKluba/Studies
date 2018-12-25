


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class ProdConsGraphic extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;

	
	public Point[] tabPoint;
	Producer[] prodTab;
	Consumer[] consTab;
	Buffer buff;
	
	JButton buttonStart = new JButton("Start");
	JButton buttonStop = new JButton("Stop");
	
	String tab[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	JComboBox<String> comboBoxConsumers = new JComboBox<String>(tab);;
	JComboBox<String> comboBoxProducers = new JComboBox<String>(tab);
	JComboBox<String> comboBoxBuffer = new JComboBox<String>(tab);
	
	JLabel labelProducers = new JLabel("Podaj liczbê Producentów");
	JLabel labelConsumers = new JLabel("Podaj liczbe Konsumentów");
	JLabel labelBuffer = new JLabel("Podaj pojemnoœæ Bufora");
	JLabel labelMinTimeProd = new JLabel("Min. czas akcji producenta");
	JLabel labelMaxTimeProd = new JLabel("Max. czas akcji producenta");
	JLabel labelMinTimeCons = new JLabel("Min. czas akcji konsumenta");
	JLabel labelMaxTimeCons = new JLabel("Max. czas akcji konsumenta");
	
	JPanel panelSettings = new JPanel();
	JPanel panelInfo = new JPanel();
	GraphicPanel panelPictures = new GraphicPanel();
	
	JSlider slideMinTimeProd = new JSlider(JSlider.HORIZONTAL, 100, 1000, 550);
	JSlider slideMaxTimeProd = new JSlider(JSlider.HORIZONTAL, 1000, 5000, 3000);
	JSlider slideMinTimeCons = new JSlider(JSlider.HORIZONTAL, 100, 1000, 550);
	JSlider slideMaxTimeCons = new JSlider(JSlider.HORIZONTAL, 1000, 5000, 3000);
	
	JTextArea textAreaInfo = new JTextArea(29,35);		
	JScrollPane scrollPaneLog = new JScrollPane(textAreaInfo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	DefaultCaret caret;
	
	JMenu[] menu = {new JMenu("Program"),
					new JMenu("O programie")};
	
	JMenuItem[] menuItem = {new JMenuItem("Ilosc producentów"),
							new JMenuItem("Ilosc konsumentow"),
							new JMenuItem("Pojemnosc bufora"),
							new JMenuItem("Minimalny czas producenta"),
							new JMenuItem("Maksymalny czas producenta"),
							new JMenuItem("Minimalny czas konsumenta"),
							new JMenuItem("Maksymalny czas konsumenta"),
							new JMenuItem("Autor"),
							new JMenuItem("Informacje")};
	
	/*
	 * Tworzê metody ¿eby konstruktor klasy by³ bardziej czytelny
	 */
	public ProdConsGraphic(){
		super("Producent - Konsument wersja okienkowa");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600,845);
		setResizable(false);
		
		panelInit();
		menuInit();
		actionListenerInit();
		
		setVisible(true);
	}

	private void panelInit(){
		this.setLayout(new BorderLayout(5,5));

		this.add(panelSettings, BorderLayout.EAST);
		panelSettings.setPreferredSize(new Dimension(170,0));
		panelSettings.setBorder(BorderFactory.createTitledBorder("Ustawienia"));
		panelSettings.add(labelProducers);
			comboBoxProducers.setPreferredSize(new Dimension(100,25));
		panelSettings.add(comboBoxProducers);
		panelSettings.add(labelConsumers);
		panelSettings.add(comboBoxConsumers);
			comboBoxConsumers.setPreferredSize(new Dimension(100,25));
		panelSettings.add(labelBuffer);
		panelSettings.add(comboBoxBuffer);
			comboBoxBuffer.setPreferredSize(new Dimension(100,25));
		panelSettings.add(labelMinTimeProd);
		panelSettings.add(slideMinTimeProd);
			slideMinTimeProd.setMinorTickSpacing(100);
			slideMinTimeProd.setMajorTickSpacing(450);
			slideMinTimeProd.setPaintTicks(true);
			slideMinTimeProd.setPaintLabels(true);
			slideMinTimeProd.setPreferredSize(new Dimension(150,45));
		panelSettings.add(labelMaxTimeProd);
		panelSettings.add(slideMaxTimeProd);
			slideMaxTimeProd.setMinorTickSpacing(500);
			slideMaxTimeProd.setMajorTickSpacing(2000);
			slideMaxTimeProd.setPaintTicks(true);
			slideMaxTimeProd.setPaintLabels(true);
			slideMaxTimeProd.setPreferredSize(new Dimension(150,45));
		panelSettings.add(labelMinTimeCons);
		panelSettings.add(slideMinTimeCons);
			slideMinTimeCons.setMinorTickSpacing(100);
			slideMinTimeCons.setMajorTickSpacing(450);
			slideMinTimeCons.setPaintTicks(true);
			slideMinTimeCons.setPaintLabels(true);
			slideMinTimeCons.setPreferredSize(new Dimension(150,45));
		panelSettings.add(labelMaxTimeCons);
		panelSettings.add(slideMaxTimeCons);
			slideMaxTimeCons.setMinorTickSpacing(500);
			slideMaxTimeCons.setMajorTickSpacing(2000);
			slideMaxTimeCons.setPaintTicks(true);
			slideMaxTimeCons.setPaintLabels(true);
			slideMaxTimeCons.setPreferredSize(new Dimension(150,45));
		panelSettings.add(buttonStart);
		panelSettings.add(buttonStop);
		
		this.add(panelInfo, BorderLayout.CENTER);
		panelInfo.setBorder(BorderFactory.createTitledBorder("Informacje"));
		panelInfo.add(scrollPaneLog);
		textAreaInfo.setEditable(false);
		textAreaInfo.setAutoscrolls(true);
		caret = (DefaultCaret)textAreaInfo.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		this.add(panelPictures, BorderLayout.NORTH);
		panelPictures.setPreferredSize(new Dimension(0,290));
		panelPictures.setBorder(BorderFactory.createTitledBorder("Wizualizacja"));
	}
	
	private void menuInit(){
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menuBar.add(menu[0]);
		menuBar.add(menu[1]);
		
		for (int i = 0; i < 7; i++) menu[0].add(menuItem[i]);
		menu[1].add(menuItem[7]);
		menu[1].add(menuItem[8]);
	}
	private void actionListenerInit(){
		buttonStart.addActionListener(this);
		buttonStop.addActionListener(this);
		buttonStop.setEnabled(false);
		
		for (JMenuItem item : menuItem) item.addActionListener(this);
	}
	
	/*
	 * Ogólnym planem na stworzenie tego programu by³o, aby przekazywaæ do producentów i konsumentów
	 * dane, by ci mogli na nich operowaæ, a klasa ProdConsGraphic mia³a tylko zbieraæ dane i wyœwietlaæ
	 * nie spodziewa³em siê, ¿e klasy BUffer, Consumer i Producer bêd¹ musia³y tak porzeszyæ swoje
	 * konstruktory by dostaæ tyle danych do operacji
	 * 
	 * Kolejnym problemem z jakim siê spotka³em by³a klasa Buffer. Jako ¿e ka¿dy producent i konsument
	 * jest osobnym obiektem a buff jest jednym obiektem który ma wiele miejsc ³adunkocywch (buforów) 
	 * nie mog³em tak jak w pozosta³ych przypadkach przekazaæ jednego obiektu z tablicy tabPoint tylko
	 * musia³em przekazac ca³¹, i dopiero na miejscu dobieraæ siê do odpowiednich w tej tablicy miejsc.
	 * Oczywiœcie mowa tutaj o wizualizacji problemu.
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		
		int data1 = comboBoxProducers.getSelectedIndex() + 1;
		int data2 = comboBoxConsumers.getSelectedIndex() + 1;
		int data3 = comboBoxBuffer.getSelectedIndex() + 1;
		
		if (source == buttonStart){
			buttonStart.setEnabled(false);
			buttonStop.setEnabled(true);
			comboBoxProducers.setEnabled(false);
			comboBoxConsumers.setEnabled(false);
			comboBoxBuffer.setEnabled(false);
			slideMinTimeProd.setEnabled(false);
			slideMaxTimeProd.setEnabled(false);
			slideMinTimeCons.setEnabled(false);
			slideMaxTimeCons.setEnabled(false);
			textAreaInfo.setText("");
			
			tabPoint = new Point[data1 + data2 + data3];
			
			for (int i = 0; i < tabPoint.length; i++){
				if (i < data1) tabPoint[i] = new Point(300 - data1 * 25 + i * 50, 50, 255, 0, 0);
				else if (i < data1 + data2) tabPoint[i] = new Point(300 - data2 * 25 + (i - data1) * 50, 200, 0, 0, 255);
				else tabPoint[i] = new Point(300 - data3 * 25 + (i - data1 - data2) * 50, 125, 255, 191, 0);
			}
			
			prodTab = new Producer[data1];
			consTab = new Consumer[data2];
			buff = new Buffer(data3, textAreaInfo, panelPictures, tabPoint, data1 + data2);
			
			panelPictures.setTab(tabPoint);
			panelPictures.repaint();
			
			for (int i = 0; i < data1; i++) prodTab[i] = new Producer("" + (i + 1), buff, textAreaInfo, tabPoint[i], slideMinTimeProd.getValue(), slideMaxTimeProd.getValue());
			for (int i = 0; i < data2; i++) consTab[i] = new Consumer("" + (i + 1), buff, textAreaInfo, tabPoint[data1 + i], slideMinTimeCons.getValue(), slideMaxTimeCons.getValue());	
			for (int i = 0; i < data1; i++) prodTab[i].start();
			for (int i = 0; i < data2; i++) consTab[i].start();
		}
		else if (source == buttonStop){
			buttonStart.setEnabled(true);
			buttonStop.setEnabled(false);
			comboBoxProducers.setEnabled(true);
			comboBoxConsumers.setEnabled(true);
			comboBoxBuffer.setEnabled(true);
			slideMinTimeProd.setEnabled(true);
			slideMaxTimeProd.setEnabled(true);
			slideMinTimeCons.setEnabled(true);
			slideMaxTimeCons.setEnabled(true);
			
			for (int i = 0; i < data1; i++) prodTab[i].kill = true;
			for (int i = 0; i < data2; i++) consTab[i].kill = true;
		}
		else if (source == menuItem[0]) {
			int data = Integer.parseInt(JOptionPane.showInputDialog("Podaj liczbê z przedzia³u 1 - 10"));
			if (data < 1) data = 1;
			else if (data > 10) data = 10;
			comboBoxProducers.setSelectedIndex(data - 1);
		}
		else if (source == menuItem[1]) {
			int data = Integer.parseInt(JOptionPane.showInputDialog("Podaj liczbê z przedzia³u 1 - 10"));
			if (data < 1) data = 1;
			else if (data > 10) data = 10;
			comboBoxConsumers.setSelectedIndex(data - 1);
		}
		else if (source == menuItem[2]) {
			int data = Integer.parseInt(JOptionPane.showInputDialog("Podaj liczbê z przedzia³u 1 - 10"));
			if (data < 1) data = 1;
			else if (data > 10) data = 10;
			comboBoxBuffer.setSelectedIndex(data - 1);
		}
		else if (source == menuItem[3]) {
			int data = Integer.parseInt(JOptionPane.showInputDialog("Podaj liczbê z przedzia³u 100 - 1000"));
			if (data < 100) data = 100;
			else if (data > 1000) data = 1000;
			slideMinTimeProd.setValue(data);
		}
		else if (source == menuItem[4]) {
			int data = Integer.parseInt(JOptionPane.showInputDialog("Podaj liczbê z przedzia³u 1000 - 5000"));
			if (data < 1000) data = 1000;
			else if (data > 5000) data = 5000;
			slideMaxTimeProd.setValue(data);
		}
		else if (source == menuItem[5]) {
			int data = Integer.parseInt(JOptionPane.showInputDialog("Podaj liczbê z przedzia³u 100 - 1000"));
			if (data < 100) data = 100;
			else if (data > 1000) data = 1000;
			slideMinTimeCons.setValue(data);
		}
		else if (source == menuItem[6]) {
			int data = Integer.parseInt(JOptionPane.showInputDialog("Podaj liczbê z przedzia³u 1000 - 5000"));
			if (data < 1000) data = 1000;
			else if (data > 5000) data = 5000;
			slideMaxTimeCons.setValue(data);
		}
		else if (source == menuItem[7]) {
			JOptionPane.showMessageDialog(this, "Problem Producenta - Konsumenta \n"
											  + "wersja okienkowa \n"
											  + "Autor: Kamil Kluba \n"
											  + "29 listopada 2017r.");
		}
		else if (source == menuItem[8]) {
			JOptionPane.showMessageDialog(this, " Kolor producenta to czerwony \n" + 
												" Kolor producenta, gdy bufor jest pe³ny a ten chce oddaæ element to magenta \n" + 
												" Kolor bufora to ciemno¿ó³ty \n" + 
												" Kolor bufora gdy jest zaêty to jasno¿ó³ty \n" + 
												" Kolor konsumenta to niebieski \n" + 
												" Kolor konsumenta gdy chce wzi¹æ element, a bufor jest pusty to cyan \n");
		}
	}
}

/* Niestety nie uda³o mi siê zmusiæ programu, by ten pokazywa³ moment w którym 
 * producent chce oddaæ przedmiot, a konsument zabraæ np przez samo wysuniêcie siê 
 * z szeregu. Dzia³a to tylko w przypadku, gdy bufor jest pusty, albo pe³ny.
 */

class GraphicPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	Point[] tabPoint = new Point[0];
	
	public void setTab(Point[] tabPoint){
		this.tabPoint = tabPoint;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < tabPoint.length; i++){
			g.setColor(new Color(tabPoint[i].r, tabPoint[i].g, tabPoint[i].b));
			g.fillOval(tabPoint[i].x, tabPoint[i].y, 30, 30);
		}
	}
}

/*
 * Kolor producenta to czerwony
 * Kolor producenta, gdy bufor jest pe³ny a ten chce oddaæ element to magenta
 * Kolor bufora to ciemno¿ó³ty
 * Kolor bufora gdy jest zaêty to jasno¿ó³ty
 * Kolor konsumenta to niebieski
 * Kolor konsumenta gdy chce wzi¹æ element, a bufor jest pusty to cyan
 * 
*/

class Point{
	public int x,y,r,g,b;
	
	public Point(int x, int y, int r, int g, int b){
		this.x = x;
		this.y = y;
		this.r = r;
		this.g = g;
		this.b = b;
	}
}