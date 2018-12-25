/* Klasa Car reprezentuj�ca samoch�d
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
 * Typ wyliczeniowy pozawalaj�cy na wyb�r jednej z kilku marek samochod�w
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
	 * Metoda zwracaj�ca wybrany model wyliczenoiwy jako <code>String </code>
	 * 
	 * @return tekstowa forma marki samochodu
	 */
	public String toString(){
		return carName;
	}
}

/**
 * Klasa reprezentujaca wyj�tek stworzony na potrzeb� klasy <code>Car</code>
 * 
 * @author Kamil
 * @version pazdziernik 2017r.
 */
class CarException extends Exception{

	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor klasy, wywo�uj�cy klas� bazow� <code>Exception</code> z argumentem
	 * 
	 * @param message wiadomo�� przekazana do klasy bazowej
	 */
	public CarException(String message) {
		super(message);
	}
}

/**
 * Klasa reprezentuje niekt�re marki samochod�w za pomoc� kilku parametr�w
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
	/** Pojemono�� silnika samochodu */
	private float engineCapacity;
	/** Opis samochodu */
	private String description;
	/** Atrybut pozwalaj�cy wczytywa� dane z pliku binarnego */
	private static ObjectInputStream ois;
	/** Atrybut pozwalaj�cy zapisywa� dane do pliku binarnego */
	private static ObjectOutputStream oos;
	

	/**
	 * Metoda nadaj�ca mark� obiektowi
	 * @param name nadana nazwa
	 */
	public void setCarName(Name name){
		carName = name;
	}
	
	/**
	 * Metoda nadaj�ca mark� obiektowi
	 * @param name nadana nazwa
	 * @throws CarException wyj�tek zg�oszony gdy nazwa jest pusta
	 */
	public void setCarName(String name) throws CarException{
		if (name == null || name.equals("")){
			throw new CarException ("Marka samochodu nie mo�e by� pusta.");
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
	 * Metoda zwracaj�ca mark� samochodu
	 * @return carName zwr�cona marka
	 */
	public Name getCarName(){
		return carName;
	}
	
	/**
	 * Metoda ustawiaj�ca atrybut "rok produkcji" obiektu
	 * @param setYear nadany rok
	 * @throws CarException wyj�tek zg�aszany gdy rok produkcji wykracza poza okre�lone granice
	 */
	public void setProductionYear(int setYear) throws CarException{
		if (setYear < 1950 || setYear > 2017)
			throw new CarException("Samoch�d nie m�g� powsta� w tym roku");
		else
			productionYear = setYear;
	}
	
	/**
	 * Metoda ustawiaj�ca atrybut "rok produkcji" obiektu
	 * @param setYear nadany rok
	 * @throws CarException wyj�tek zg�aszany gdy rok produkcji wykracza poza okre�lone granice
	 */
	public void setProductionYear(String setYear) throws CarException{
		if (setYear == null || setYear.equals(""))
			throw new CarException("Rok produkcji nie mo�e by� pusty.");
		try{
			setProductionYear(Integer.parseInt(setYear));
			}
		catch(NumberFormatException e){
			throw new CarException("Rok produkcji musi by� liczb� ca�kowit�.");
		}
	}
	
	/**
	 * Metoda zwracaj�ca atrybut "rok produkcji"
	 * @return productionYear zwr�cony atrybut "rok produkcji"
	 */
	public int getProductionYear(){
		return productionYear;
	}
	
	/**
	 * Metoda ustawiaj�ca atrybut "pojemno�� silnika" obiektu
	 * @param setYear nadana pojemno�� silnika
	 * @throws CarException wyj�tek zg�aszany gdy pojemno�� silnika wykracza poza okre�lone granice
	 */
	public void setEngineCapacity(float setCapacity) throws CarException{
		if (setCapacity < 0 || setCapacity > 6)
			throw new CarException("Silnik nie mo�e mie� takeij pojemno�ci.");
		else
			engineCapacity = setCapacity;
	}
	
	/**
	 * Metoda ustawiaj�ca atrybut "pojemno�� silnika" obiektu
	 * @param setYear nadana pojemno�� silnika
	 * @throws CarException wyj�tek zg�aszany gdy pojemno�� silnika wykracza poza okre�lone granice
	 */
	public void setEngineCapacity(String setCapacity) throws CarException{
		if (setCapacity == null || setCapacity.equals(""))
			throw new CarException("Pojemno�� silnika nie mo�e by� pusta.");
		try{
			setEngineCapacity(Float.parseFloat(setCapacity));
		}
		catch (NumberFormatException e){
			throw new CarException("Pojemno�� musi by� liczb�.");
		}
	}
	
	/**
	 * Metoda zwracaj�ca atrubyt "pojemnosc silnika"
	 * @return zwr�cony atrybut "pojemnosc silnika"
	 */
	public float getEngineCapacity(){
		return engineCapacity;
	}
	
	/**
	 * Metoda nadaj�ca opis danemu obiektowi
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
	 * Metoda zwracaj�da opis obiektu
	 * @return opis obiektu
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * Metoda zwracaj�ca wszystkie dane obiektu przekszta�cone do �a�cucha znak�w
	 * @return zwr�cony �a�cuch znak�w
	 */
	public String toString(){
		return carName + " " + productionYear + " " + engineCapacity + " " + description;
	}
	
	/**
	 * Metoda pozwalaj�ca na zapis danych do pliku
	 * @param writer plik do ktorego bedzie zapisywany obiekt
	 * @param car zapisywany obiekt
	 */
	public static void printToFile(PrintWriter writer, Car car){
		writer.println(car.carName + "#" + car.productionYear + "#" + car.engineCapacity + "#" + car.description);
	}
	
	/**
	 * Metoda pozwalaj�ca na zapis danych do pliku
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
	 * Metoda pozwalaj��a na odczytywanie danych z pliku
	 * @param reader bufor przetrzymuj�cy dane odczytane z pliku
	 * @return zwrocony obiekt typu Car
	 * @throws CarException wyj�tek zwr�cony gdy nie mo�na odczyta� obiektu
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
			throw new CarException("Nie mo�na stworzy� obiektu");
		}
	}
	
	/**
	 * Metoda pozwalaj��a na odczytywanie danych z pliku
	 * @param fileName Nazwa otwieranego pliku
	 * @return zwrocony obiekt typu Car
	 * @throws CarException wyj�tek zwr�cony gdy nie mo�na odczyta� obiektu
	 */
	public static Car readFromFile(String fileName) throws CarException{
		try (BufferedReader reader = new BufferedReader (new FileReader(new File(fileName)))){
			return Car.readFromFile(reader);
			}
		catch(FileNotFoundException e){
			throw new CarException("Nie odnaleziono pliku " + fileName + ".");
		} catch(IOException e){
			throw new CarException("Wyst�pi� b��d podczas odczytu danych w pliku.");
		}
	}
	
	/**
	 * Metoda pozwalaj�ca na zapis do pliku binarnego
	 * @param fileName nazwa pliku do kt�rego b�dzie zapisywany plik
	 * @param car zwr�cony obiekt typu Car
	 * @throws CarException wyj�tke zwr�cony gdy nie mo�na zapisa� do pliku
	 */
	public static void writeBinary(String fileName, Car car) throws CarException{
		try{
			FileOutputStream fos = new FileOutputStream(fileName + ".bin");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(car.carName + "#" + car.productionYear + "#" + car.engineCapacity + "#" + car.description);
		} catch (IOException e){
			throw new CarException("Wyst�pi� b��d podczas zapisu do pliku.");
		}
	}
	
	/**
	 * Metoda pozwalaj�ca na odczyt z plku binarnego
	 * @param fileName nazwa pliku z kt�ergo b�d� odczytywane dane
	 * @return zwr�cony obiekt typu Car
	 * @throws CarException wyj�tek zwr�cony gdt nie mo�na odczyta� danych
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
			throw new CarException("Wyst�pi� b��d podczas odczytu danych z pliku.");
		}
	}
	
	/**
	 * Metoda zwracaj�ca unikaln� warto�� liczbow� obiektu
	 * @return zwr�cona warto��
	 */
	public int hashCode(){
		return carName.hashCode() + Integer.hashCode(productionYear) + Float.hashCode(engineCapacity) + description.hashCode();
	}
	
	/**
	 * Metoda por�wnuj�ca dwa obiekty typu Car
	 * @param car1 pierwszy obiekt do por�wnania
	 * @param car2 drugi obiekt do por�wnania
	 * @return warto�� <code>true</code> gdy por�wnane obiekty s� takie same lub <code>false</code> gdy obiekty s� r�ne
	 */
	public static boolean equals(Car car1, Car car2){
		if (car1.hashCode() == car2.hashCode()) return true;
		return false;
	}

	/**
	 * Metoda pozwalaj�ca por�wna� dane obiektu typu Car z innym obiektem tego samego typu, co pozwala na sortowanie obiekt�w
	 * @param car por�wnywany obiekt
	 * @return zwracana warto�� okre�laj�ca kt�ry obiekt powinien by� w sortowaniu pierwszy
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
