/*
 *  Program EdytorGraficzny - aplikacja z graficznym interfejsem
 *   - obs�uga zdarze� od klawiatury, myszki i innych element�w GUI.
 *
 *  Autor: Pawe� Rogalinski, ...
 *   Data: 1. 10, 2015 r.
 */

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


abstract class Figure{

	static Random random = new Random();

	private boolean selected = false;

	public boolean isSelected() { return selected; }
	public void select() {	selected = true; }
	public void select(boolean z) { selected = z; }
	public void unselect() { selected = false; }

	protected void setColor(Graphics g) {
		if (selected) g.setColor(Color.RED);
		           else g.setColor(Color.BLACK);
	}

	public abstract boolean isInside(float px, float py);
	public boolean isInside(int px, int py) {
		return isInside((float) px, (float) py);
	}

	protected String properties() {
		String s = String.format("  Pole: %.0f  Obwod: %.0f", computeArea(), computePerimeter());
		if (isSelected()) s = s + "   [SELECTED]";
		return s;
	}

	abstract String getName();
	abstract float  getX();
	abstract float  getY();

    abstract float computeArea();
    abstract float computePerimeter();

    abstract void move(float dx, float dy);
    abstract void scale(float s);

    abstract void draw(Graphics g);

    @Override
    public String toString(){
        return getName();
    }

}


class Point extends Figure{

	protected float x, y;

	Point()
	{ this.x=random.nextFloat()*400;
	  this.y=random.nextFloat()*400;
	}

	Point(float x, float y)
	{ this.x=x;
	  this.y=y;
	}

	@Override
	public boolean isInside(float px, float py) {
		// by umo�liwi� zaznaczanie punktu myszk�
		// miejsca odleg�e nie wi�cej ni� 6 le�� wewn�trz
		return (Math.sqrt((x - px) * (x - px) + (y - py) * (y - py)) <= 6);
	}


    @Override
	String getName() {
		return "Point(" + x + ", " + y + ")";
	}

	@Override
	float getX() {
		return x;
	}

	@Override
	float getY() {
		return y;
	}

	@Override
    float computeArea(){ return 0; }

	@Override
	float computePerimeter(){ return 0; }

	@Override
    void move(float dx, float dy){ x+=dx; y+=dy; }

	@Override
    void scale(float s){ }

	@Override
    void draw(Graphics g){
		setColor(g);
		g.fillOval((int)(x-3),(int)(y-3), 6,6);
	}

    String toStringXY(){ return "(" + x + " , " + y + ")"; }

}


class Circle extends Point{
    float r;

    Circle(){
        super();
        r=random.nextFloat()*100;
    }

    Circle(float px, float py, float pr){
        super(px,py);
        r=pr;
    }

    @Override
	public boolean isInside(float px, float py) {
		return (Math.sqrt((x - px) * (x - px) + (y - py) * (y - py)) <= r);
	}

    @Override
   	String getName() {
   		return "Circle(" + x + ", " + y + ")";
   	}

    @Override
    float computeArea(){ return (float)Math.PI*r*r; }

    @Override
    float computePerimeter(){ return (float)Math.PI*r*2; }

    @Override
    void scale(float s){ r*=s; }

    @Override
    void draw(Graphics g){
    	setColor(g);
        g.drawOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
    }

}


class Triangle extends Figure{
    Point point1, point2, point3;

    Triangle(){
    	point1 = new Point();
    	point2 = new Point();
    	point3 = new Point();
    }

    Triangle(Point p1, Point p2, Point p3){
        point1=p1; point2=p2; point3=p3;
    }

    @Override
    public boolean isInside(float px, float py)
    { float d1, d2, d3;
      d1 = px*(point1.y-point2.y) + py*(point2.x-point1.x) +
           (point1.x*point2.y-point1.y*point2.x);
      d2 = px*(point2.y-point3.y) + py*(point3.x-point2.x) +
           (point2.x*point3.y-point2.y*point3.x);
      d3 = px*(point3.y-point1.y) + py*(point1.x-point3.x) +
           (point3.x*point1.y-point3.y*point1.x);
      return ((d1<=0)&&(d2<=0)&&(d3<=0)) || ((d1>=0)&&(d2>=0)&&(d3>=0));
    }

    @Override
	String getName() {
    	return "Triangle{"+point1.toStringXY()+
                point2.toStringXY()+
                point3.toStringXY()+"}";
	}

	@Override
	float getX() {
		return (point1.x+point2.x+point3.x)/3;
	}

	@Override
	float getY() {
		return (point1.y+point2.y+point3.y)/3;
	}

	@Override
	float computeArea(){
        float a = (float)Math.sqrt( (point1.x-point2.x)*(point1.x-point2.x)+
                                    (point1.y-point2.y)*(point1.y-point2.y));
        float b = (float)Math.sqrt( (point2.x-point3.x)*(point2.x-point3.x)+
                                    (point2.y-point3.y)*(point2.y-point3.y));
        float c = (float)Math.sqrt( (point1.x-point3.x)*(point1.x-point3.x)+
                                    (point1.y-point3.y)*(point1.y-point3.y));
        float p=(a+b+c)/2;
        return (float)Math.sqrt(p*(p-a)*(p-b)*(p-c));
    }

	@Override
    float computePerimeter(){
        float a = (float)Math.sqrt( (point1.x-point2.x)*(point1.x-point2.x)+
                                    (point1.y-point2.y)*(point1.y-point2.y));
        float b = (float)Math.sqrt( (point2.x-point3.x)*(point2.x-point3.x)+
                                    (point2.y-point3.y)*(point2.y-point3.y));
        float c = (float)Math.sqrt( (point1.x-point3.x)*(point1.x-point3.x)+
                                    (point1.y-point3.y)*(point1.y-point3.y));
        return a+b+c;
    }

	@Override
    void move(float dx, float dy){
        point1.move(dx,dy);
        point2.move(dx,dy);
        point3.move(dx,dy);
    }

	@Override
    void scale(float s){
        Point sr1 = new Point((point1.x+point2.x+point3.x)/3,
                              (point1.y+point2.y+point3.y)/3);
        point1.x*=s; point1.y*=s;
        point2.x*=s; point2.y*=s;
        point3.x*=s; point3.y*=s;
        Point sr2 = new Point((point1.x+point2.x+point3.x)/3,
                              (point1.y+point2.y+point3.y)/3);
        float dx=sr1.x-sr2.x;
        float dy=sr1.y-sr2.y;
        point1.move(dx,dy);
        point2.move(dx,dy);
        point3.move(dx,dy);
    }

	@Override
    void draw(Graphics g){
		setColor(g);
        g.drawLine((int)point1.x, (int)point1.y,
                   (int)point2.x, (int)point2.y);
        g.drawLine((int)point2.x, (int)point2.y,
                   (int)point3.x, (int)point3.y);
        g.drawLine((int)point3.x, (int)point3.y,
                   (int)point1.x, (int)point1.y);
    }

}

class Square extends Figure
{
	Point point1, point2, point3, point4;

	private float buffer;
	

	Square()
	{
		point1 = new Point();
		point2 = new Point();
		point3 = new Point();
		point4 = new Point();
		
		point2.y = point1.y;
		buffer = Math.abs(point2.x - point1.x);
		point3.x = point1.x;
		point3.y = point1.y + buffer;
		point4.x = point2.x;
		point4.y = point3.y;	
	}
	
	Square(Point p1, Point p2, Point p3, Point p4){
        point1=p1; point2=p2; point3=p3; point4 = p4;
    }
	
	 public boolean isInside(float px, float py)
	    { float d1, d2, d3, d4;
	      d1 = px*(point1.y-point2.y) + py*(point2.x-point1.x) +
	    		  (point1.x*point2.y-point1.y*point2.x);
	      d2 = px*(point2.y-point4.y) + py*(point4.x-point2.x) +
	           (point2.x*point4.y-point2.y*point4.x);
	      d3 = px*(point4.y-point3.y) + py*(point3.x-point4.x) +
	           (point4.x*point3.y-point4.y*point3.x);
	      d4 = px*(point3.y-point1.y) + py*(point1.x-point3.x) +
	    	   (point3.x*point1.y-point3.y*point1.x);
	      return ((d1<=0)&&(d2<=0)&&(d3<=0)&&(d4<=0)) || ((d1>=0)&&(d2>=0)&&(d3>=0)&&(d4>=0));
	    }

	 @Override
	String getName() {
	    return "Square{"+point1.toStringXY()+
	    		point2.toStringXY()+
	    		point3.toStringXY()+
	    		point4.toStringXY()+"}";
	 }
		@Override
		float getX() {
			return (point1.x+point2.x)/4;
		}
	
		@Override
		float getY() {
			return (point1.y+point3.y)/4;
		}
			
		float computeArea(){	      
			return (point2.x - point1.x)*(point1.y - point3.y);
	    }

		@Override
	    float computePerimeter(){
	        return 4*(point2.x - point1.x);
		}
		
		void move(float dx, float dy){
	        point1.move(dx,dy);
	        point2.move(dx,dy);
	        point3.move(dx,dy);
	        point4.move(dx,dy);
		}  
		
		@Override
	    void scale(float s){
	        Point sr1 = new Point((point1.x+point2.x+point3.x+point4.x)/4,
	                              (point1.y+point2.y+point3.y+point4.y)/4);
	        point1.x*=s; point1.y*=s;
	        point2.x*=s; point2.y*=s;
	        point3.x*=s; point3.y*=s;
	        point4.x*=s; point4.y*=s;
	        Point sr2 = new Point((point1.x+point2.x+point3.x+point4.x)/4,
	                              (point1.y+point2.y+point3.y+point4.y)/4);
	        float dx=sr1.x-sr2.x;
	        float dy=sr1.y-sr2.y;
	        point1.move(dx,dy);
	        point2.move(dx,dy);
	        point3.move(dx,dy);
	        point4.move(dx,dy);
		}
		
	    void draw(Graphics g){
	    	setColor(g);
	    	g.drawLine((int)point1.x, (int)point1.y,
	    			   (int)point2.x, (int)point2.y);
	    	g.drawLine((int)point2.x, (int)point2.y,
	                   (int)point4.x, (int)point4.y);
	        g.drawLine((int)point4.x, (int)point4.y,
	                   (int)point3.x, (int)point3.y);
	        g.drawLine((int)point3.x, (int)point3.y,
	        		   (int)point1.x, (int)point1.y);
	}	
}

class Section extends Figure
{
	Point point1, point2;
	
	/**
	 * Konstruktor klasy
	 */
	Section()
	{
		point1 = new Point();
		point2 = new Point();
	}
	
	/**
	 * Konstruktor klasy
	 */
	Section(Point p1, Point p2)
	{
		point1 = p1; point2 = p2;
	}
	
	public boolean isInside(float px, float py)
	{
		return (Math.sqrt(((point1.x + point2.x)/2 - px) * ((point1.x + point2.x)/2 - px) +
				((point1.y + point2.y)/2 - py) * ((point1.y + point2.y)/2 - py)) <= 12);
	}
	 @Override
	String getName() {
		 return "Square{"+point1.toStringXY() +	point2.toStringXY()+"}";
	}
		    		
	@Override
	float getX() {
		return (point1.x+point2.x)/2;
	}
		
	@Override
	float getY() {
		return (point1.y+point2.y)/2;
	}	
	
	float computeArea(){	      
		return 0;
    }

	@Override
    float computePerimeter(){
        return 0;
	}
	
	void move(float dx, float dy){
        point1.move(dx,dy);
        point2.move(dx,dy);
	}  
	@Override
    void scale(float s){
        Point sr1 = new Point((point1.x+point2.x)/2,
                              (point1.y+point2.y)/2);
        point1.x*=s; point1.y*=s;
        point2.x*=s; point2.y*=s;
  
        Point sr2 = new Point((point1.x+point2.x)/2,
                              (point1.y+point2.y)/2);
        float dx=sr1.x-sr2.x;
        float dy=sr1.y-sr2.y;
        point1.move(dx,dy);
        point2.move(dx,dy);
   
	}
	
    void draw(Graphics g){
    	setColor(g);
    	g.drawLine((int)point1.x, (int)point1.y,
    			   (int)point2.x, (int)point2.y);
    }
}

class Pentagon extends Figure
{
Point point1, point2, point3, point4, point5;
	
	Pentagon()
	{
		point1 = new Point();
		point2 = new Point();
		point3 = new Point();
		point4 = new Point();
		point5 = new Point();
	}
	
	Pentagon(Point p1, Point p2, Point p3, Point p4, Point p5)
	{
		point1 = p1; point2 = p2; point3 = p3; point4 = p4; point5 =p5;
	}
	
	public boolean isInside(float px, float py)
	{
	 float d1, d2, d3, d4, d5;
	      d1 = px*(point1.y-point2.y) + py*(point2.x-point1.x) +
	           (point1.x*point2.y-point1.y*point2.x);
	      d2 = px*(point2.y-point3.y) + py*(point3.x-point2.x) +
	           (point2.x*point3.y-point2.y*point3.x);
	      d3 = px*(point3.y-point4.y) + py*(point4.x-point3.x) +
	           (point3.x*point4.y-point3.y*point4.x);
	      d4 = px*(point4.y-point5.y) + py*(point5.x-point4.x) +
	    	   (point4.x*point5.y-point4.y*point5.x);
	      d5 = px*(point5.y-point1.y) + py*(point1.x-point5.x) +
	    	   (point5.x*point1.y-point5.y*point1.x);
	      return ((d1<=0)&&(d2<=0)&&(d3<=0)&&(d4<=0)&&(d5<=0)) || ((d1>=0)&&(d2>=0)&&(d3>=0)&&(d4>=0)&&(d5>=0));	
	}
	
	@Override
	String getName() {
		 return "Pentagon{"+point1.toStringXY()+point2.toStringXY()+point3.toStringXY()+point4.toStringXY()+point5.toStringXY() +"}";
	}
		    		
	@Override
	float getX() {
		return (point1.x+point2.x+point3.x+point4.x+point5.x)/5;
	}
		
	@Override
	float getY() {
		return (point1.y+point2.y+point3.y+point4.y+point5.y)/5;
	}	
	
	float computeArea(){	      
		return 0;
    }

	@Override
    float computePerimeter()
	{
		 float a = (float)Math.sqrt( (point1.x-point2.x)*(point1.x-point2.x)+
                 (point1.y-point2.y)*(point1.y-point2.y));
		 float b = (float)Math.sqrt( (point2.x-point3.x)*(point2.x-point3.x)+
                 (point2.y-point3.y)*(point2.y-point3.y));
		 float c = (float)Math.sqrt( (point3.x-point4.x)*(point3.x-point4.x)+
                 (point3.y-point4.y)*(point3.y-point4.y));
		 float d = (float)Math.sqrt( (point4.x-point5.x)*(point4.x-point5.x)+
                 (point4.y-point5.y)*(point4.y-point5.y));
		 float e = (float)Math.sqrt( (point4.x-point5.x)*(point4.x-point5.x)+
                 (point4.y-point5.y)*(point4.y-point5.y));
		 return a+b+c+d+e;
	}
	
	void move(float dx, float dy){
        point1.move(dx,dy);
        point2.move(dx,dy);
        point3.move(dx,dy);
        point4.move(dx,dy);
        point5.move(dx,dy);
	}  
	@Override
    void scale(float s){
        Point sr1 = new Point((point1.x+point2.x+point3.x+point4.x+point5.x)/5,
                              (point1.y+point2.y+point3.y+point4.y+point5.y)/5);
        point1.x*=s; point1.y*=s;
        point2.x*=s; point2.y*=s;
        point3.x*=s; point3.y*=s;
        point4.x*=s; point4.y*=s;
        point5.x*=s; point5.y*=s;
        Point sr2 = new Point((point1.x+point2.x+point3.x+point4.x+point5.x)/5,
                			  (point1.y+point2.y+point3.y+point4.y+point5.y)/5);
        float dx=sr1.x-sr2.x;
        float dy=sr1.y-sr2.y;
        point1.move(dx,dy);
        point2.move(dx,dy);
        point3.move(dx,dy);
        point4.move(dx,dy);
        point5.move(dx,dy);
	}
	
    void draw(Graphics g){
    	setColor(g);
    	g.drawLine((int)point1.x, (int)point1.y,
    			   (int)point2.x, (int)point2.y);
    	g.drawLine((int)point2.x, (int)point2.y,
                   (int)point3.x, (int)point3.y);
        g.drawLine((int)point3.x, (int)point3.y,
                   (int)point4.x, (int)point4.y);
        g.drawLine((int)point4.x, (int)point4.y,
        		   (int)point5.x, (int)point5.y);
        g.drawLine((int)point5.x, (int)point5.y,
     		   	   (int)point1.x, (int)point1.y);
    }
}


class Picture extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private static final long serialVersionUID = 1L;
	
	float mouseX,mouseY;

	Vector<Figure> figures = new Vector<Figure>();



	/*
	 * UWAGA: ta metoda b�dzie wywo�ywana automatycznie przy ka�dej potrzebie
	 * odrysowania na ekranie zawarto�ci panelu
	 *
	 * W tej metodzie NIE WOLNO !!! wywo�ywa� metody repaint()
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Figure f : figures)
			f.draw(g); 
	}


    void addFigure(Figure fig)
    { for (Figure f : figures){ f.unselect(); }
      fig.select();
      figures.add(fig);
      repaint();
    }


    void moveAllFigures(float dx, float dy){
    	for (Figure f : figures){
    		if (f.isSelected()) f.move(dx,dy);
    	}
        repaint();
    }

    void scaleAllFigures(float s){
    	for (Figure f : figures)
        	{ if (f.isSelected()) f.scale(s);
        	}
          repaint();
    }

    public String toString(){
        String str = "Rysunek{ ";
        for(Figure f : figures)
            str+=f.toString() +"\n         ";
        str+="}";
        return str;
    }


    /*
     *  Impelentacja interfejsu KeyListener - obs�uga zdarze� generowanych
     *  przez klawiatur� gdy focus jest ustawiony na ten obiekt.
     */
    public void keyPressed (KeyEvent evt)
    //Virtual keys (arrow keys, function keys, etc) - handled with keyPressed() listener.
    {  int dist;
       if (evt.isShiftDown()){ dist = 10; System.out.println("DZUAA");}
                         else dist = 1;
		switch (evt.getKeyCode()) {
		case KeyEvent.VK_UP:
			moveAllFigures(0, -dist);
			break;
		case KeyEvent.VK_DOWN:
			moveAllFigures(0, dist);
			break;
		case KeyEvent.VK_LEFT:
			moveAllFigures(-dist, 0);
			break;
		case KeyEvent.VK_RIGHT:
			moveAllFigures(dist, 0);
			break;
		case KeyEvent.VK_DELETE:
			Iterator<Figure> i = figures.iterator();
			while (i.hasNext()) {
				Figure f = i.next();
				if (f.isSelected()) {
					i.remove();
				}
			}
			repaint();
			break;
		}
    }

   public void keyReleased (KeyEvent evt)
   {  }

   public void keyTyped (KeyEvent evt)
   //Characters (a, A, #, ...) - handled in the keyTyped() listener.
   {
     char znak=evt.getKeyChar(); //reakcja na przycisku na naci�ni�cie klawisza
		switch (znak) {
		case 'p':
			addFigure(new Point());
			break;
		case 'c':
			addFigure(new Circle());
			break;
		case 't':
			addFigure(new Triangle());
			break;
		case 's':
			addFigure(new Square());
			break;
		case 'e':
			addFigure(new Section());
			break;
		case 'n':
			addFigure(new Pentagon());
			break;

		case '+':
			scaleAllFigures(1.1f);
			break;
		case '-':
			scaleAllFigures(0.9f);
			break;
		}
   }


   /*
    * Implementacja interfejsu MouseListener - obs�uga zdarze� generowanych przez myszk�
    * gdy kursor myszki jest na tym panelu
    */
   public void mouseClicked(MouseEvent e)
   // Invoked when the mouse button has been clicked (pressed and released) on a component.
   { int px = e.getX();
     int py = e.getY();

     for (Figure f : figures)
       { if (e.isAltDown()==false) f.unselect();
         if (f.isInside(px,py)) f.select( !f.isSelected() );
       }
     repaint();
   }

   public void mouseEntered(MouseEvent e)
   //Invoked when the mouse enters a component.
   { }

   public void mouseExited(MouseEvent e)
   //Invoked when the mouse exits a component.
   { }

   public void mousePressed(MouseEvent e)
	// Invoked when a mouse button has been pressed on a component.
	{
	     mouseX = e.getX();
	     mouseY = e.getY();
	}

   public void mouseReleased(MouseEvent e)
   //Invoked when a mouse button has been released on a component.
   { }


	@Override
	public void mouseDragged(MouseEvent arg0) {
		float currentX, currentY;
		for (Figure f : figures){
			if (f.isSelected()){
				currentX = arg0.getX(); currentY = arg0.getY();
				f.move(currentX - mouseX, currentY - mouseY);
				mouseX = currentX; mouseY = currentY;
				repaint();
			}
		}
	}


	@Override
	public void mouseMoved(MouseEvent arg0) {
		
	}


	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		float roll = e.getWheelRotation();
		for (Figure f : figures){
			if (f.isSelected()){
				if (roll < 0)
					f.scale((1.05f));
				else
					f.scale(0.95f);
			}
		}
		repaint();
	}
}



public class GraphicEditor extends JFrame implements ActionListener, WindowListener{


	private static final long serialVersionUID = 3727471814914970170L;


	private final String DESCRIPTION = "OPIS PROGRAMU\n\n" + "Aktywna klawisze:\n"
			+ "   strzalki ==> przesuwanie figur\n"
			+ "   SHIFT + strzalki ==> szybkie przesuwanie figur\n"
			+ "   +,-  ==> powiekszanie, pomniejszanie\n"
			+ "   DEL  ==> kasowanie figur\n"
			+ "   p  ==> dodanie nowego punktu\n"
			+ "   c  ==> dodanie nowego kola\n"
			+ "   t  ==> dodanie nowego trojkata\n"
			+ "   s  ==> dodanie nowego kwadratu\n"
			+ "   e  ==> dodanie nowego odcinka\n"
			+ "   n  ==> dodanie nowego pięciokąta\n"
			+ "   Menu Figury+ oraz klawisze zakończone znakiem + pozwalają na tworzenie\n"
			+ "   figur o zadanym położeniu i wymiarze\n"
			+ "\nOperacje myszka:\n" + "   klik ==> zaznaczanie figur\n"
			+ "   ALT + klik ==> zmiana zaznaczenia figur\n"
			+ "   przeciaganie ==> przesuwanie figur";


	protected Picture picture;

	private JMenu[] menu = { new JMenu("Figury"),
			                 new JMenu("Edytuj"),
			                 new JMenu("Figury+"), 
			                 new JMenu("Pomoc")};

	private JMenuItem[] items = { new JMenuItem("Punkt"),
			                      new JMenuItem("Kolo"),
			                      new JMenuItem("Trojkat"),
			                      new JMenuItem("Kwadrat"),
			                      new JMenuItem("Odcinek"),
			                      new JMenuItem("Pięciokąt"),
			                      new JMenuItem("Wypisz wszystkie"),
			                      new JMenuItem("Przesun w gore"),
			                      new JMenuItem("Przesun w dol"),
			                      new JMenuItem("Powieksz"),
			                      new JMenuItem("Pomniejsz"),
			                      new JMenuItem("Punkt+"),
			                      new JMenuItem("Kolo+"),
			                      new JMenuItem("Trojkat+"),
			                      new JMenuItem("Kwadrat+"),
			                      new JMenuItem("Odcinek+"),
			                      new JMenuItem("Pięciokąt+"),
			                      new JMenuItem("Autor"),
			                      new JMenuItem("Opis")
			                      };

	private JButton buttonPoint = new JButton("Punkt");
	private JButton buttonCircle = new JButton("Kolo");
	private JButton buttonTriangle = new JButton("Trojkat");
	private JButton buttonSquare = new JButton("Kwadrat");
	private JButton buttonSection = new JButton("Odcinek");
	private JButton buttonPentagon = new JButton("Pięciokąt");
	private JButton buttonPointPlaced = new JButton("Punkt+");
	private JButton buttonCirclePlaced = new JButton("Kolo+");
	private JButton buttonTrianglePlaced = new JButton("Trojkat+");
	private JButton buttonSquarePlaced = new JButton("Kwadrat+");
	private JButton buttonSectionPlaced = new JButton("Odcinek+");
	private JButton buttonPentagonPlaced = new JButton("Pięciokąt+");


	public GraphicEditor() throws ClassNotFoundException, IOException
    { super ("Edytor graficzny");
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setSize(600,600);
      
      addWindowListener(this);
      
      for (int i = 0; i < items.length; i++)
      	items[i].addActionListener(this);

      // dodanie opcji do menu "Figury"
      menu[0].add(items[0]);
      menu[0].add(items[1]);
      menu[0].add(items[2]);
      menu[0].add(items[3]);
      menu[0].add(items[4]);
      menu[0].add(items[5]);
      menu[0].addSeparator();
      menu[0].add(items[6]);

      // dodanie opcji do menu "Edytuj"
      menu[1].add(items[7]);
      menu[1].add(items[8]);
      menu[1].addSeparator();
      menu[1].add(items[9]);
      menu[1].add(items[10]);
      
      menu[2].add(items[11]);
      menu[2].add(items[12]);
      menu[2].add(items[13]);
      menu[2].add(items[14]);
      menu[2].add(items[15]);
      menu[2].add(items[16]);
      
      menu[3].add(items[17]);
      menu[3].add(items[18]);

      // dodanie do okna paska menu
      JMenuBar menubar = new JMenuBar();
      for (int i = 0; i < menu.length; i++)
      	menubar.add(menu[i]);
      setJMenuBar(menubar);

      picture=new Picture();
      picture.addKeyListener(picture);
      picture.setFocusable(true);
      picture.addMouseListener(picture);
      picture.addMouseMotionListener(picture);
      picture.addMouseWheelListener(picture);
      picture.setLayout(new FlowLayout());

      buttonPoint.addActionListener(this);
      buttonCircle.addActionListener(this);
      buttonTriangle.addActionListener(this);
      buttonSquare.addActionListener(this);
      buttonSection.addActionListener(this);
      buttonPentagon.addActionListener(this);
      buttonPointPlaced.addActionListener(this);
      buttonCirclePlaced.addActionListener(this);
      buttonTrianglePlaced.addActionListener(this);
      buttonSquarePlaced.addActionListener(this);
      buttonSectionPlaced.addActionListener(this);
      buttonPentagonPlaced.addActionListener(this);

      picture.add(buttonPoint);
      picture.add(buttonCircle);
      picture.add(buttonTriangle);
      picture.add(buttonSquare);
      picture.add(buttonSection);
      picture.add(buttonPentagon);
      picture.add(buttonPointPlaced);
      picture.add(buttonCirclePlaced);
      picture.add(buttonTrianglePlaced);
      picture.add(buttonSquarePlaced);
      picture.add(buttonSectionPlaced);
      picture.add(buttonPentagonPlaced);

      readFromFile();
      
      setContentPane(picture);
      setVisible(true);
    }
    
    @SuppressWarnings("unchecked")
	public void readFromFile() throws ClassNotFoundException, IOException{
    	 try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("figures.bin"))){
 			picture.figures = (Vector<Figure>)ois.readObject();
 	  } catch (Exception e){
 	  }
    }

	public void actionPerformed(ActionEvent evt) {
		Object zrodlo = evt.getSource();

		if (zrodlo == buttonPoint)
			picture.addFigure(new Point());
		if (zrodlo == buttonCircle)
			picture.addFigure(new Circle());
		if (zrodlo == buttonTriangle)
			picture.addFigure(new Triangle());
		if (zrodlo == buttonSquare)
			picture.addFigure(new Square());
		if (zrodlo == buttonSection)
			picture.addFigure(new Section());
		if (zrodlo == buttonPentagon)
			picture.addFigure(new Pentagon());

		if (zrodlo == items[0])
			picture.addFigure(new Point());
		if (zrodlo == items[1])
			picture.addFigure(new Circle());
		if (zrodlo == items[2])
			picture.addFigure(new Triangle());
		if (zrodlo == items[3])
			picture.addFigure(new Square());
		if (zrodlo == items[4])
			picture.addFigure(new Section());
		if (zrodlo == items[5])
			picture.addFigure(new Pentagon());
		if (zrodlo == items[6])
			JOptionPane.showMessageDialog(null, picture.toString());

		if (zrodlo == items[7])
			picture.moveAllFigures(0, -10);
		if (zrodlo == items[8])
			picture.moveAllFigures(0, 10);
		if (zrodlo == items[9])
			picture.scaleAllFigures(1.1f);
		if (zrodlo == items[10])
			picture.scaleAllFigures(0.9f);
		
		if (zrodlo == items[11] || zrodlo == buttonPointPlaced){
			float x,y;
			
			do{
				x = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną X punktu z przedziału 0 - 600"));
				y = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną Y punktu z przedziału 0 - 600"));
			}
			while(x < 0 || x > 600 || y < 0 || y > 600);
			picture.addFigure(new Point(x,y));
		}
		if (zrodlo == items[12] || zrodlo == buttonCirclePlaced){
			float x,y,r;
			
			do{
				x = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną X punktu z przedziału 0 - 600"));
				y = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną Y punktu z przedziału 0 - 600"));
				r = Float.parseFloat(JOptionPane.showInputDialog("Podaj długość promienia z przedziału 1 - 300"));
			}
			while(x < 0 || x > 600 || y < 0 || y > 600 || r < 1 || r > 300);
			picture.addFigure(new Circle(x,y,r));
		}
		if (zrodlo == items[13] || zrodlo == buttonTrianglePlaced){
			float x1,y1,x2,y2,x3,y3;
			
			do{
				x1 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną X punktu 1 z przedziału 0 - 600"));
				y1 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną Y punktu 1 z przedziału 0 - 600"));
				x2 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną X punktu 2 z przedziału 0 - 600"));
				y2 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną Y punktu 2 z przedziału 0 - 600"));
				x3 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną X punktu 3 z przedziału 0 - 600"));
				y3 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną Y punktu 3 z przedziału 0 - 600"));	
			}
			while(x1 < 0 || x1 > 600 || y1 < 0 || y1 > 600 || x2 < 0 || x2 > 600 || y2 < 0 || y2 > 600 || x3 < 0 || x3 > 600 || y3 < 0 || y3 > 600 );
			picture.addFigure(new Triangle(new Point(x1,y1), new Point(x2,y2), new Point(x3,y3)));
		}
		if (zrodlo == items[14] || zrodlo == buttonSquarePlaced){
			float x1,y1,a;
			
			do{
				x1 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną X punktu 1 z przedziału 0 - 600"));
				y1 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną Y punktu 1 z przedziału 0 - 600"));
				a = Float.parseFloat(JOptionPane.showInputDialog("Podaj dlugosc booku z przedzialu 1 - 300"));
			}
			while(x1 < 0 || x1 > 600 || y1 < 0 || y1 > 600 || a < 1 || a > 300 );
			picture.addFigure(new Square(new Point(x1,y1), new Point(x1 + a,y1), new Point(x1,y1 + a), new Point(x1 + a,y1 + a)));
		}
		if (zrodlo == items[15] || zrodlo == buttonSectionPlaced){
			float x1,y1,x2,y2;
			
			do{
				x1 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną X punktu 1 z przedziału 0 - 600"));
				y1 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną Y punktu 1 z przedziału 0 - 600"));
				x2 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną X punktu 2 z przedziału 0 - 600"));
				y2 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną Y punktu 2 z przedziału 0 - 600"));
			}
			while(x1 < 0 || x1 > 600 || y1 < 0 || y1 > 600 || x2 < 0 || x2 > 600 || y2 < 0 || y2 > 600);
			picture.addFigure(new Section(new Point(x1,y1), new Point(x2,y2)));
		}
		if (zrodlo == items[16] || zrodlo == buttonPentagonPlaced){
			float x1,y1,x2,y2,x3,y3,x4,y4,x5,y5;
			
			do{
				x1 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną X punktu 1 z przedziału 0 - 600"));
				y1 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną Y punktu 1 z przedziału 0 - 600"));
				x2 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną X punktu 2 z przedziału 0 - 600"));
				y2 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną Y punktu 2 z przedziału 0 - 600"));
				x3 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną X punktu 3 z przedziału 0 - 600"));
				y3 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną Y punktu 3 z przedziału 0 - 600"));
				x4 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną X punktu 4 z przedziału 0 - 600"));
				y4 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną Y punktu 4 z przedziału 0 - 600"));
				x5 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną X punktu 5 z przedziału 0 - 600"));
				y5 = Float.parseFloat(JOptionPane.showInputDialog("Podaj współrzędną Y punktu 5 z przedziału 0 - 600"));
			}
			while(x1 < 0 || x1 > 600 || y1 < 0 || y1 > 600 || x2 < 0 || x2 > 600 || y2 < 0 || y2 > 600 || x3 < 0 || x3 > 600 || y3 < 0 || y3 > 600 
					|| x4 < 0 || x4 > 600 || y4 < 0 || y4 > 600 || x5 < 0 || x5 > 600 || y5 < 0 || y5 > 600);
			picture.addFigure(new Pentagon(new Point(x1,y1), new Point(x2,y2), new Point(x3,y3), new Point(x4,y4), new Point(x5,y5)));
		}
		if (zrodlo == items[17])
			JOptionPane.showMessageDialog(this, "Program: Edytor graficzny \n" +
 												"Autor: Kamil Kluba 226016 \n" +
												"Wersja: Listopad 2017r.");
		if (zrodlo == items[18])
			JOptionPane.showMessageDialog(this, DESCRIPTION);
		
		picture.requestFocus(); // przywrocenie ogniskowania w celu przywrocenia
								// obslugi zadarez� pd klawiatury
		repaint();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("figures.bin"))) {
			oos.writeObject(picture.figures);
		} catch (IOException e) {
		}
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
	
    public static void main(String[] args) throws ClassNotFoundException, IOException
    { new GraphicEditor();
    }


}
