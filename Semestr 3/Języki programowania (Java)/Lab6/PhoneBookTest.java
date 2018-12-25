/*
 * Ksi¹¿ka telefoniczna - test
 * 
 * Kamil Kluba
 * 02.01.2017r.
 * 
 */

public class PhoneBookTest {
	public static void main(String args[]) throws InterruptedException{
		new PhoneBook();
		
		Thread.sleep(1000);
		
		new PhoneBookClient("Jerzy", "localhost");
		new PhoneBookClient("Jan", "localhost");
	//	new PhoneBookClient("Jurek", "localhost");
	//	new PhoneBookClient("Jurgen", "localhost");
	}
}
