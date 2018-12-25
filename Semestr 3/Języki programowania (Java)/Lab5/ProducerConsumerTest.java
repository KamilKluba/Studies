import java.awt.Graphics;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JTextArea;

/* 
 *  Problem producenta i konsumenta
 *
 *  Autor: Pawe� Rogali�ski,
 *  Uprawnienie do wersji wizualnej: Kamil Kluba
 *  Data: 29 listopada 2017 r.
 */


abstract class  Worker extends Thread {
	
	// Metoda usypia w�tek na podany czas w milisekundach
	public static void sleep(int millis){
		try {
			Thread.sleep(millis);
			} catch (InterruptedException e) { }
	}
	
	// Metoda usypia w�tek na losowo dobrany czas z przedzia�u [min, max) milsekund
	public static void sleep(int min_millis, int max_milis){
		sleep(ThreadLocalRandom.current().nextInt(min_millis, max_milis));
	}
	
	// Unikalny identyfikator przedmiotu wyprodukowanego
	// przez producenta i zu�ytego przez konsumenta
	// Ten identyfikator jest wsp�lny dla wszystkich producent�w
	// i b�dzie zwi�kszany przy produkcji ka�dego nowego przedmiotu
	static int itemID = 0;
	
	String name;
	Buffer buffer;
	
	@Override
	public abstract void run();
}


class Producer extends Worker{
	boolean kill = false;
	JTextArea log;
	Point p;
	int minTime, maxTime;
	
	public Producer(String name , Buffer buffer, JTextArea area, Point p, int minTime, int maxTime){ 
		this.name = name;
		this.buffer = buffer;
		log = area;
		this.p = p;
		this.minTime = minTime;
		this.maxTime = maxTime;
	}
	
	@Override
	public void run(){ 
		int item;
		while(kill == false){
			// Producent "produkuje" nowy przedmiot.
			item = itemID++;
			log.append("Producent <" + name + "> produkuje: " + item + " \n");
			sleep(minTime, maxTime);
			
			// Producent umieszcza przedmiot w buforze.
			buffer.put(this, item, p);
		}
	}	
	
	void draw(Graphics g){
		g.fillOval(150, 150, 50, 50);
	}
} // koniec klasy Producer


class Consumer extends Worker {
	boolean kill = false;
	JTextArea log;
	Point p;
	int minTime, maxTime;
	
	public Consumer(String name , Buffer buffer, JTextArea area, Point p, int minTime, int maxTime){ 
		this.name = name;
		this.buffer = buffer;
		log = area;
		this.p = p;
		this.minTime = minTime;
		this.maxTime = maxTime;
	}

	@Override
	public void run(){ 
		int item;
		while(kill == false){
			// Konsument pobiera przedmiot z bufora
			item = buffer.get(this, p);
			
			// Konsument zu�ywa popraany przedmiot.
			sleep(minTime, maxTime);
			log.append("Konsument <" + name + "> zużył: " + item + " \n");
		}
	}
	
} // koniec klasy Consumer


class Buffer {
	
	private int contents[];
	private int currentContent = 0;
	private int maxSize;
	private JTextArea log;
	GraphicPanel picturesPanel;
	private int data;
	private Point[] tabPoint;
	
	public Buffer(int size, JTextArea area, GraphicPanel picturesPanel, Point[] tabPoint, int data){
		contents = new int[size];
		for (int i = 0; i < size; i++) contents[i] = 0;
		maxSize = size;
		log = area;
		this.picturesPanel = picturesPanel;
		this.tabPoint = tabPoint;
		this.data = data;
	}

	public synchronized int get(Consumer consumer, Point p){
		log.append("Konsument <" + consumer.name + "> chce zabrac \n");
		p.y -= 30;
		p.g = 255;
		picturesPanel.repaint();
		while (currentContent == 0){
			try { log.append("Konsument <" + consumer.name + "> bufor pusty - czekam \n");
				  wait();
				} catch (InterruptedException e) { }
		}
		currentContent--;
		int item = contents[currentContent];
		log.append("Konsument <" + consumer.name + "> zabral: " + contents[currentContent]+" \n");
		contents[currentContent] = 0;
		tabPoint[data + currentContent].g = 192;
		p.y += 30;
		p.g = 0;
		picturesPanel.repaint();
		notifyAll();
		return item;
	}

	public synchronized void put(Producer producer, int item, Point p){
		log.append("Producent <" + producer.name + ">  chce oddac: " + item + " \n");
		p.y += 30;
		p.b = 255;
		picturesPanel.repaint();
		while (currentContent == maxSize){
			try { log.append("Producent <" + producer.name + "> bufor zajety - czekam \n");
				  wait();
				} catch (InterruptedException e) { }
		}
		contents[currentContent] = item;
		tabPoint[data + currentContent].g = 255;
		currentContent++;
		log.append("Producent <" + producer.name + "> oddal: " + item + " \n");
		p.y -= 30;
		p.b = 0;
		picturesPanel.repaint();
		notifyAll();
	}
} // koniec klasy Buffer


public class ProducerConsumerTest{

	public static void main(String[] args){
		new ProdConsGraphic();
	}
} // koniec klasy ProducerConsumerTest

