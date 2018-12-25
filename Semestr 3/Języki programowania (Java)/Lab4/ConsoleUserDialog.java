/*
 * Program: Prosta biblioteka metod do realizacji dialogu z u�ytkownikiem
 *          w prostych aplikacjach bez graficznego interfejsu u�ytkownika.
 *    Plik: ConsoleUserDialog.java
 *          
 *   Autor: Pawe� Rogalinski
 *    Data: pazdziernik 2017 r.
 *
 */

import java.util.Scanner;

/**
 * Klasa <code> ConsoleUserDialog </code> implementuje pomocnicze
 * metody do tworzenia w programie tekstowego interfejsu u�ytkownika.
 * 
 * Do realizacji dialogu z u�ytkownikiem wykorzystywane s� standardowe
 * strumienie wej�cia <code>System.in</code>, wyj�cia <code> System.out </code> 
 * oraz b��d�w <code> System.err </code>. 
 * 
 * Program demonstruje nast�puj�ce zagadnienia:
 * <ul>
 *  <li> wy�wietlanie komunikat�w tekstowych w oknie konsoli, </li>
 *  <li> czytanie w oknie konsoli danych r�nych typ�w ze standardowego
 *       strumienia wejsciowego za pomoc� klasy <code> Scanner </code></li>
 *  <li> konwersj� obiekt�w klasy <code>String</code> na znaki lub liczby
 *       typu <code>char, int, double</code>,
 *  <li> obs�ug� wyj�tk�w przy konwersji danych</li>
 * </ul>
 *
 * @author Pawel Rogali�ski
 * @version 1 pa�dziernik 2017
 */

public class ConsoleUserDialog {
	
	/** Komunikat o b��dnym formacie wprowadzonych danych. */
	private final String ERROR_MESSAGE = "Nieprawid�owe dane!\nSpr�buj jeszcze raz.";

	/** Pomocniczy obiekt klasy <code> Scanner </code> 
	 *  do czytania danych w oknie konsoli.
	 *  
	 *  Domy�lnie <code>scanner</code> pod��czony jest do standardowego 
	 *  strumienia wej�ciowego.
	 */
	private Scanner scanner = new Scanner(System.in);

		
	/**
	 * Metoda drukuje komunikat do standardowego strumienia wyj�ciowego.
	 * 
	 * @param message tekst komunikatu.
	 */
	public void printMessage(String message) {
			System.out.println(message);
		}
		
		
	/**
	 * Metoda drukuje komunikat do standardowego strumienia wyj�ciowego
	 * i czeka na potwierdzenie od u�ytkownika.
	 * 
	 * Po wydrukowaniu komunikatu program jest wstrzymywany do momentu
	 * naci�ni�cia klawisza ENTER.
	 * 
	 * @param message tekst komunikatu.
	 */	
	public void printInfoMessage(String message) {
			System.out.println(message);
			enterString("Nacisnij ENTER");
		}
		
		
	/**
	 * Metoda drukuje komunikat do standardowego strumienia b��d�w
	 * i czeka na potwierdzenie od u�ytkownika.
	 * 
	 * Po wydrukowaniu komunikatu program jest wstrzymywany do momentu
	 * naci�ni�cia klawisza ENTER.
	 * @param message tekst komunikatu.
	 */	
	public void printErrorMessage(String message) {
			System.err.println(message);
			System.err.println("Nacisnij ENTER");
			enterString("");
		}
		
		/**
	     * Metoda czy�ci konsol� tekstow�.
	     * 
	     * Metoda faktycznie drukuje trzy puste linie, kt�re
	     * tworz� dodatkowy odst�p przed kolejnymi komunikatami. 
	     */
		public void clearConsole(){
			System.out.println("\n\n");
		}

		/**
	     * Metoda umo�liwia u�ytkownikowi wprowadzenie �a�cucha znak�w.
	     *
	     * @param prompt tekst zach�ty do wprowadzania danych.
	     * @return obiekt reprezentuj�cy wprowadzony ci�g znak�w.
	     */
		public String enterString(String prompt) {
			System.out.print(prompt);
			return scanner.nextLine();
		}
		
		/**
	     * Metoda umo�liwia u�ytkownikowi wprowadzenie pojedy�czego znaku.
	     *
	     * Metoda faktycznie czyta ca�y �a�cuch znak�w, z kt�rego
	     * wybierany jest tylko pierwszy znak.
	     * @param prompt tekst zach�ty do wprowadzania danych.
	     * @return wprowadzony znak.
	     */ 
		public char enterChar(String prompt) {
			boolean isError;
			char c = ' ';
			do {
				isError = false;
				try {
					c = enterString(prompt).charAt(0);
				} catch (IndexOutOfBoundsException e) {
					System.err.println(ERROR_MESSAGE);
					isError = true;
				}
			} while (isError);
			return c;
		}

		/**
	     * Metoda umo�liwia u�ytkownikowi wprowadzenie liczby ca�kowitej.
	     *
	     * Metoda faktycznie czyta ca�y �a�cuch znak�w, kt�ry
	     * nast�pnie jest kowertowany na liczb� ca�kowit�.
	     * @param prompt tekst zach�ty do wprowadzania danych.
	     * @return wprowadzona liczba.
	     */
		public int enterInt(String prompt) {
	        boolean isError;
	        int i = 0;
	        do{
	            isError = false;
	            try{ 
	                i = Integer.parseInt(enterString(prompt));
	            } catch(NumberFormatException e){
	            	System.err.println(ERROR_MESSAGE);
	            	isError = true;
	            }
	        }while(isError);
	        return i;
	    }
		
		/**
	     * Metoda umo�liwia u�ytkownikowi wprowadzenie liczby rzeczywistej.
	     *
	     * Metoda faktycznie czyta ca�y �a�cuch znak�w, kt�ry
	     * nast�pnie jest kowertowany na liczb� rzeczywist�.
	     * @param prompt tekst zach�ty do wprowadzania danych.
	     * @return wprowadzona liczba.
	     */
		public float enterFloat(String prompt) {
	        boolean isError;
	        float d = 0;
	        do{
	            isError = false;
	            try{
	                d = Float.parseFloat(enterString(prompt));
	            } catch(NumberFormatException e){
	            	System.err.println(ERROR_MESSAGE);
	                isError = true;
	            }
	        } while(isError);
	        return d;
	    }   
		
		/**
	     * Metoda umo�liwia u�ytkownikowi wprowadzenie liczby 
	     * rzeczywistej podw�jnej precyzji.
	     *
	     * Metoda faktycznie czyta ca�y �a�cuch znak�w, kt�ry
	     * nast�pnie jest kowertowany na liczb� rzeczywist� 
	     * podw�jnej precyzji.
	     * @param prompt tekst zach�ty do wprowadzania danych.
	     * @return wprowadzona liczba.
	     */
		public double enterDouble(String prompt) {
	        boolean isError;
	        double d = 0;
	        do{
	            isError = false;
	            try{
	                d = Double.parseDouble(enterString(prompt));
	            } catch(NumberFormatException e){
	            	System.err.println(ERROR_MESSAGE);
	                isError = true;
	            }
	        }while(isError);
	        return d;
	    }   
		
} // koniec kasy ConsoleUserDialog


