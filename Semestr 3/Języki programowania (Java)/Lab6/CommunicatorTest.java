/*
 * Komunikator - test
 * 
 * Kamil Kluba
 * 02.01.2017r.
 * 
 */

public class CommunicatorTest {
	public static void main(String args[]) throws InterruptedException{
		new CommunicatorServer();
		
		Thread.sleep(1000);
		
		new CommunicatorUser("Jerzy", "localhost");
		new CommunicatorUser("Jan", "localhost");
		new CommunicatorUser("Jurek", "localhost");
		new CommunicatorUser("Jurgen", "localhost");
	/*	new CommunicatorUser("Jurg", "localhost");
		new CommunicatorUser("Jureczek", "localhost");
		new CommunicatorUser("Jurij", "localhost");
		new CommunicatorUser("Jurk", "localhost");*/
	}
}
