/*
 *  Program do obliczania pierwiastk�w
 *  r�wnania kwadratowego
 *
 *  Autor: Pawe� Rogalinski
 *   Data: 1 pa�dziernika 2017 r.
 */

/**
 * Klasa reprezentuje r�wnania kwadratowe postaci: <br>
 * <center>A x^2 + B x + C = 0.</center>
 *
 * Program demonstruje nast�puj�ce zagadnienia:
 * <ul>
 * <li>definiowanie prostej klasy zawieraj�cej kilka atrybut�w i metood,
 * <li>wy�wietlanie danych r�nych typ�w w oknie konsoli,</li>
 * <li>wykorzystywanie klasy <code>ConsoleUserDialog</code> 
 *     do wprowadzania danych.</li>
 * </ul>
 *
 * @author Pawe� Rogali�ski
 * @version 1 pa�dziernika 2017 r.
 */
public class QuadraticEquation {

	/**
     * Metoda uruchamia program, kt�ry prosi u�ytkownika o wprowadzenie
     * parametr�w r�wnania kwadratowego, a nast�pnie oblicza i wy�wietla 
     * pierwiastki tego r�wnania.
     *
     * @param args tablica parametr�w z lini polece�, przekazanych 
     * przy uruchamianiu programu do wirtualnej maszyny javy.
     */
    public static void main(String[] args) {
    	ConsoleUserDialog user_dialog = new ConsoleUserDialog();
        user_dialog.printInfoMessage("Demonstracja\n"+
        		             "Program oblicza pierwiastki r�wnania kwadratowego\n" +
                             "     A x^2 + B x + C = 0\n" +
        		             "\nAutor: Pawe� Rogali�ski");
    	QuadraticEquation equation = new QuadraticEquation();
        equation.enterEquation(user_dialog);
        try{
        	 equation.calculateRoots();
           } catch(IllegalStateException e)
           {
               user_dialog.printErrorMessage("UWAGA:\n Wsp�czynnik 'A' r�wnania kwadratowego musi byc r�ny od zera!");
               System.exit(1);
           }
        equation.printRoots(user_dialog);
        user_dialog.printInfoMessage("KONIEC PROGRAMU");
        System.exit(0);
    }

    
	/**
	 * wsp�czynnik A.
	 *
	 * <p>
	 * <b>Uwaga:</b> Wsp�czynnik A musi by� r�ny od 0.
	 * </p>
	 */
	private double a;

	/** wsp�czynnik B. */
	private double b;
	
	/** wsp�czynnik C. */
	private double c;

	/**
	 * wyr�nik r�wnania kwadratowego.
	 *
	 * <br><b>Uwaga:</b> Od warto�ci wyr�nika zale�y liczba pierwiastk�w r�wnania
	 * kwadratowego:
	 * <ul>
	 * <li><code> delta &gt; 0 </code> - istniej� dwa pierwiastki
	 * <code> x1, x2.</code></li>
	 * <li><code> delta = 0 </code> - istnieje jeden (podw�jny) pierwiastek
	 * <code> x1, </code></li>
	 * <li><code> delta &lt; 0 </code> - brak pierwiastk�w.</li>
	 * </ul>
	 */
	private double delta;

	/**
	 * pierwszy pierwiastek.
	 *
	 * <p><b>Uwaga:</b> Pierwiastek istnieje tylko je�li 
	 * <code>delta</code> jest wi�ksza lub r�wna zero.</p>
	 */
	private double x1;

	/**
	 * drugi pierwiastek.
	 *
	 * <p><b>Uwaga:</b> Pierwiastek istnieje tylko je�li 
	 * <code>delta</code> jest wi�ksza od zera.</p>
	 */
	private double x2;

	/**
	 * Metoda zwraca posta� r�wnania kwadratowego w formie tekstowej.
	 *
	 * <p><b>Uwaga:</b> Metoda jest wywo�ywana niejawnie, je�li zachodzi potrzeba
	 * przedstawienia r�wnania w formie tekstowej.
	 * @return tekstowa forma r�wnania kwadratowego.
	 */
	public String toString() {
		String equation = a + " x^2 + " + b + " x + " + c + " = 0";
		return equation;
	}

	/**
	 * Metoda oblicza pierwiastki r�wnania kwadratowego.
	 *
	 * @exception IllegalStateException
	 *            wyj�tek zg�aszany gdy wsp�czynnik A jest r�wny zero.
	 */
	public void calculateRoots() throws IllegalStateException {
		if (a == 0)
			throw new IllegalStateException("Parametr a nie mo�e byc r�wny zero.");
		delta = calculateDiscriminant ();
		if (delta > 0) {
			x1 = (-b - Math.sqrt(delta)) / (2 * a);
			x2 = (-b + Math.sqrt(delta)) / (2 * a);
		} else if (delta == 0) {
			x1 = -b / (2 * a);
		}
	}

	/**
	 * Metoda oblicza wyr�nik r�wnania kwadratowego.
	 *
	 * @return Wyr�nik.
	 */
	public double calculateDiscriminant() {
		return b * b - 4 * a * c;
	}

	/**
	 * Metoda umo�liwia wprowadzenie wsp�czynnik�w r�wnania.
	 * 
	 * @param dialog referencja do obiektu klasy <code>ConsoleUserDialog</code>
	 *        wykorzystywanego do realizacji dialogu z u�ytkownikiem.
	 */
	public void enterEquation(ConsoleUserDialog dialog) {
		a = dialog.enterDouble("Wprowad� wsp�czynniki r�wnania\n Podaj A: ");
		b = dialog.enterDouble("Wprowad� wsp�czynniki r�wnania\n Podaj B: ");
		c = dialog.enterDouble("Wprowad� wsp�czynniki r�wnania\n Podaj C: ");
	}

	/**
	 * Metoda wy�wietla pierwiastki.
	 *
	 * @param dialog referencja do obiektu klasy <code>ConsoleUserDialog</code>
	 *        wykorzystywanego do realizacji dialogu z u�ytkownikiem.
	 */
	public void printRoots(ConsoleUserDialog dialog) {
		String message;
		message = "R�wnanie: " + this + "\n    Delta = " + delta;
		if (delta > 0) {
			message = message + "\nR�wnanie ma dwa pierwiastki:" + "\n     x1 = " + x1 + "\n     x2 = " + x2;
		} else if (delta == 0) {
			
			message = message + "\nR�wnanie ma jeden pierwiastek:" + "\n     x1 = " + x1;
		} else
			message = message + "\nR�wnanie nie ma pierwiastk�w.";
		dialog.printInfoMessage("Rozwi�zanie:\n" + message);
	}

} // koniec klasy QuadraticEquation