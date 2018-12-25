/* Klasa Car reprezentuj¹ca samochód
 * 
 * Autor: Kamil Kluba
 * Data: Pazdziernik 2017r.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * Typ wyliczeniowy pozawalaj¹cy na wybór jednej z kilku marek samochodów
 *
 * @author Kamil Kluba
 * @version pazdziernik 2017r.
 */
enum Name {
	VOLKSWAGEN("Volkswagen"),
	AUDI("Audi"),
	BMW("BMW"),
	MERCEDES("Mercedes"),
	OPEL("Opel");
	
	String carName;
	
	private Name(String name){
		carName = name;
	}
	
	/**
	 * Metoda zwracaj¹ca wybrany model wyliczenoiwy jako <code>String </code>
	 * 
	 * @return tekstowa forma marki samochodu
	 */
	public String toString(){
		return carName;
	}
}

/**
 * Klasa reprezentujaca wyj¹tek stworzony na potrzebê klasy <code>Car</code>
 * 
 * @author Kamil
 * @version pazdziernik 2017r.
 */
class CarException extends Exception{

	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor klasy, wywo³uj¹cy klasê bazow¹ <code>Exception</code> z argumentem
	 * 
	 * @param message wiadomoœæ przekazana do klasy bazowej
	 */
	public CarException(String message) {
		super(message);
	}
}

/**
 * Klasa reprezentuje niektóre marki samochodów za pomoc¹ kilku parametrów
 * 
 * @author Kamil
 * @version pazdziernik 2017r.
 */
public class Car implements Serializable, Comparable<Car>{

	private static final long serialVersionUID = 1L;
	/** Marka samochodu */
	private Name carName;
	/** Rok produkcji samochodu */
	private int productionYear;
	/** Pojemonoœæ silnika samochodu */
	private float engineCapacity;
	/** Opis samochodu */
	private String description;
	/** Atrybut pozwalaj¹cy wczytywaæ dane z pliku binarnego */
	private static ObjectInputStream ois;
	/** Atrybut pozwalaj¹cy zapisywaæ dane do pliku binarnego */
	private static ObjectOutputStream oos;
	

	/**
	 * Metoda nadaj¹ca markê obiektowi
	 * @param name nadana nazwa
	 */
	public void setCarName(Name name){
		carName = name;
	}
	
	/**
	 * Metoda nadaj¹ca markê obiektowi
	 * @param name nadana nazwa
	 * @throws CarException wyj¹tek zg³oszony gdy nazwa jest pusta
	 */
	public void setCarName(String name) throws CarException{
		if (name == null || name.equals("")){
			throw new CarException ("Marka samochodu nie mo¿e byæ pusta.");
		}
		else{
			for (Name data: Name.values()){
				if (data.carName.equals(name)){
					carName = data;
				}
			}
		}
	}
	
	/**
	 * Metoda zwracaj¹ca markê samochodu
	 * @return carName zwrócona marka
	 */
	public Name getCarName(){
		return carName;
	}
	
	/**
	 * Metoda ustawiaj¹ca atrybut "rok produkcji" obiektu
	 * @param setYear nadany rok
	 * @throws CarException wyj¹tek zg³aszany gdy rok produkcji wykracza poza okreœlone granice
	 */
	public void setProductionYear(int setYear) throws CarException{
		if (setYear < 1950 || setYear > 2017)
			throw new CarException("Samochód nie móg³ powstaæ w tym roku");
		else
			productionYear = setYear;
	}
	
	/**
	 * Metoda ustawiaj¹ca atrybut "rok produkcji" obiektu
	 * @param setYear nadany rok
	 * @throws CarException wyj¹tek zg³aszany gdy rok produkcji wykracza poza okreœlone granice
	 */
	public void setProductionYear(String setYear) throws CarException{
		if (setYear == null || setYear.equals(""))
			throw new CarException("Rok produkcji nie mo¿e byæ pusty.");
		try{
			setProductionYear(Integer.parseInt(setYear));
			}
		catch(NumberFormatException e){
			throw new CarException("Rok produkcji musi byæ liczb¹ ca³kowit¹.");
		}
	}
	
	/**
	 * Metoda zwracaj¹ca atrybut "rok produkcji"
	 * @return productionYear zwrócony atrybut "rok produkcji"
	 */
	public int getProductionYear(){
		return productionYear;
	}
	
	/**
	 * Metoda ustawiaj¹ca atrybut "pojemnoœæ silnika" obiektu
	 * @param setYear nadana pojemnoœæ silnika
	 * @throws CarException wyj¹tek zg³aszany gdy pojemnoœæ silnika wykracza poza okreœlone granice
	 */
	public void setEngineCapacity(float setCapacity) throws CarException{
		if (setCapacity < 0 || setCapacity > 6)
			throw new CarException("Silnik nie mo¿e mieæ takeij pojemnoœci.");
		else
			engineCapacity = setCapacity;
	}
	
	/**
	 * Metoda ustawiaj¹ca atrybut "pojemnoœæ silnika" obiektu
	 * @param setYear nadana pojemnoœæ silnika
	 * @throws CarException wyj¹tek zg³aszany gdy pojemnoœæ silnika wykracza poza okreœlone granice
	 */
	public void setEngineCapacity(String setCapacity) throws CarException{
		if (setCapacity == null || setCapacity.equals(""))
			throw new CarException("Pojemnoœæ silnika nie mo¿e byæ pusta.");
		try{
			setEngineCapacity(Float.parseFloat(setCapacity));
		}
		catch (NumberFormatException e){
			throw new CarException("Pojemnoœæ musi byæ liczb¹.");
		}
	}
	
	/**
	 * Metoda zwracaj¹ca atrubyt "pojemnosc silnika"
	 * @return zwrócony atrybut "pojemnosc silnika"
	 */
	public float getEngineCapacity(){
		return engineCapacity;
	}
	
	/**
	 * Metoda nadaj¹ca opis danemu obiektowi
	 * @param setDesc nadany opis
	 */
	public void setDescription(String setDesc){
		if (setDesc == null || setDesc.equals("")){
			description = "Brak opisu.";
			return;
		}
		description = setDesc;
	}
	
	/**
	 * Metoda zwracaj¹da opis obiektu
	 * @return opis obiektu
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * Metoda zwracaj¹ca wszystkie dane obiektu przekszta³cone do ³añcucha znaków
	 * @return zwrócony ³añcuch znaków
	 */
	public String toString(){
		return carName + " " + productionYear + " " + engineCapacity + " " + description;
	}
	
	/**
	 * Metoda pozwalaj¹ca na zapis danych do pliku
	 * @param writer plik do ktorego bedzie zapisywany obiekt
	 * @param car zapisywany obiekt
	 */
	public static void printToFile(PrintWriter writer, Car car){
		writer.println(car.carName + "#" + car.productionYear + "#" + car.engineCapacity + "#" + car.description);
	}
	
	/**
	 * Metoda pozwalaj¹ca na zapis danych do pliku
	 * @param fileName plik do ktorego bedzie zapisywany obiekt
	 * @param car zapisywany obiekt
	 */
	public static void printToFile(String fileName, Car car) throws CarException{
		try (PrintWriter writer = new PrintWriter(fileName)){
			printToFile(writer, car);
		}
		catch (FileNotFoundException e) {
			throw new CarException("Nie odnaleziono pliku " + fileName + ".");
		}
	}
	
	/**
	 * Metoda pozwalaj¹æa na odczytywanie danych z pliku
	 * @param reader bufor przetrzymuj¹cy dane odczytane z pliku
	 * @return zwrocony obiekt typu Car
	 * @throws CarException wyj¹tek zwrócony gdy nie mo¿na odczytaæ obiektu
	 */
	public static Car readFromFile(BufferedReader reader) throws CarException{
		try {
			String line = reader.readLine();
			String[] text = line.split("#");
			Car car = new Car();
			car.setCarName(text[0]);
			car.setProductionYear(text[1]);
			car.setEngineCapacity(text[2]);
			car.setDescription(text[3]);
			return car;
		}
		catch (IOException e ){
			throw new CarException("Nie mo¿na stworzyæ obiektu");
		}
	}
	
	/**
	 * Metoda pozwalaj¹æa na odczytywanie danych z pliku
	 * @param fileName Nazwa otwieranego pliku
	 * @return zwrocony obiekt typu Car
	 * @throws CarException wyj¹tek zwrócony gdy nie mo¿na odczytaæ obiektu
	 */
	public static Car readFromFile(String fileName) throws CarException{
		try (BufferedReader reader = new BufferedReader (new FileReader(new File(fileName)))){
			return Car.readFromFile(reader);
			}
		catch(FileNotFoundException e){
			throw new CarException("Nie odnaleziono pliku " + fileName + ".");
		} catch(IOException e){
			throw new CarException("Wyst¹pi³ b³¹d podczas odczytu danych w pliku.");
		}
	}
	
	/**
	 * Metoda pozwalaj¹ca na zapis do pliku binarnego
	 * @param fileName nazwa pliku do którego bêdzie zapisywany plik
	 * @param car zwrócony obiekt typu Car
	 * @throws CarException wyj¹tke zwrócony gdy nie mo¿na zapisaæ do pliku
	 */
	public static void writeBinary(String fileName, Car car) throws CarException{
		try{
			FileOutputStream fos = new FileOutputStream(fileName + ".bin");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(car.carName + "#" + car.productionYear + "#" + car.engineCapacity + "#" + car.description);
		} catch (IOException e){
			throw new CarException("Wyst¹pi³ b³¹d podczas zapisu do pliku.");
		}
	}
	
	/**
	 * Metoda pozwalaj¹ca na odczyt z plku binarnego
	 * @param fileName nazwa pliku z któergo bêd¹ odczytywane dane
	 * @return zwrócony obiekt typu Car
	 * @throws CarException wyj¹tek zwrócony gdt nie mo¿na odczytaæ danych
	 */
	public static Car readBinary(String fileName) throws CarException{
		try{
			FileInputStream fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			String line = ois.readObject().toString();
			String[] text = line.split("#");
			Car car = new Car();
			car.setCarName(text[0]);
			car.setProductionYear(text[1]);
			car.setEngineCapacity(text[2]);
			car.setDescription(text[3]);
			return car;
		} catch (IOException | ClassNotFoundException e){
			throw new CarException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
		}
	}
	
	/**
	 * Metoda zwracaj¹ca unikaln¹ wartoœæ liczbow¹ obiektu
	 * @return zwrócona wartoœæ
	 */
	public int hashCode(){
		return carName.hashCode() + Integer.hashCode(productionYear) + Float.hashCode(engineCapacity) + description.hashCode();
	}
	
	/**
	 * Metoda porównuj¹ca dwa obiekty typu Car
	 * @param car1 pierwszy obiekt do porównania
	 * @param car2 drugi obiekt do porównania
	 * @return wartoœæ <code>true</code> gdy porównane obiekty s¹ takie same lub <code>false</code> gdy obiekty s¹ ró¿ne
	 */
	public static boolean equals(Car car1, Car car2){
		if (car1.hashCode() == car2.hashCode()) return true;
		return false;
	}

	/**
	 * Metoda pozwalaj¹ca porównaæ dane obiektu typu Car z innym obiektem tego samego typu, co pozwala na sortowanie obiektów
	 * @param car porównywany obiekt
	 * @return zwracana wartoœæ okreœlaj¹ca który obiekt powinien byæ w sortowaniu pierwszy
	 */
	@Override
	public int compareTo(Car car) {
		int compared_descriptions = description.compareTo(car.description);
		
		if (compared_descriptions != 0) return compared_descriptions;
		else{
			if (productionYear > car.productionYear) return 1;
			else return -1;
		}
	}
}
