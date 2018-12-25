/* Klasa GroupOfCars reprezentująca kolekcje obiektów typu Car
 * 
 * Autor: Kamil Kluba
 * Data: Listopad 2017r.
 */

import java.util.List;
import java.util.Set;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Vector;

/**
 * Typ wyliczeniowy pozawalający na wybór jednego z kontenerów na dane
 *
 * @author Kamil Kluba
 * @version listopad 2017r.
 */
enum GroupType{
	VECTOR("Klasa Vector - lista"),
	ARRAY_LIST("Klasa ArrayList - lista"),
	LINKED_LIST("Klasa LinkedList - lista"),
	HASH_SET("Klasa HashSet - zbiór"),
	TREE_SET("Klasa TreeSet - zbiór");
	
	String typeName;
	
	private GroupType(String type_name){
		typeName = type_name;
	}
	
	/**
	 * Metoda zwracająca wybrany kontener danych jako <code>String</code>
	 */
	public String toString(){
		return typeName;
	}
	
	/**
	 * Metoda przeszukująca cały typ wyliczeniowy w poszukiwaniu określonego kontenera
	 * @param type_name szukany kontener
	 * @return typ kontenera gdy uda się go znalezć lub <code>null</code> gdy się to nie uda
	 */
	public static GroupType find(String type_name){
		for (GroupType type : values())
			if (type.typeName.equals(type_name))
				return type;
		return null;
	}
	/**
	 * Metoda tworząca obiekt będący danym kontenerem na dane
	 * @return zwrócony kontener
	 * @throws CarException wyjątek zwrócony gdy dany kontener nie został zaimplementowany
	 */
	public Collection<Car> createCollection() throws CarException{
		switch (this) {
		case VECTOR:      return new Vector<Car>();
		case ARRAY_LIST:  return new ArrayList<Car>();
		case HASH_SET:    return new HashSet<Car>();
		case LINKED_LIST: return new LinkedList<Car>();
		case TREE_SET:    return new TreeSet<Car>();
		default:          throw new CarException("Podany typ kolekcji nie został zaimplementowany.");
		}
	}
}

/**
 * Klasa reprezentująca grupy obiektów typu Car
 * 
 * @author Kamil
 * @version listopad 2017r.
 */
public class GroupOfCars {
	/** nazwa danej grupy */
	private String name;
	/** typ kontenera na dane danej grupy */
	private GroupType type;
	/** kolekcja obiektów typu Car */
	private Collection<Car> collection;	
	
	/**
	 * Konstruktor klasy 
	 * @param type_name typ kontenera na dane
	 * @param name nazwa grupy
	 * @throws CarException wyjątek zwrócony gdy typ kolekcji jest nieprawidłowy
	 */
	public GroupOfCars(GroupType type_name, String name) throws CarException{
		setName(name);
		if (type_name == null)
			throw new CarException("Nieprawidłowy typ kolekcji.");
		type = type_name;
		collection = type.createCollection();
	}
	
	/**
	 * Konstruktor klasy 
	 * @param type_name typ kontenera na dane
	 * @param name nazwa grupy
	 * @throws CarException wyjątek zwrócony gdy typ kolekcji jest nieprawidłowy
	 */
	public GroupOfCars(String type_name, String name) throws CarException{
		setName(name);
		GroupType type = GroupType.find(type_name);
		if (type == null)
			throw new CarException("Nieprawidłowy typ kolekcji.");
		this.type = type;
		collection = this.type.createCollection();
	}
	
	/**
	 * Metoda zwracająca nazwę grupy
	 * @return zwrócona nazwa grupy
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Metoda ustanawiająca nazwę grupy
	 * @param name nadana nazwa
	 * @throws CarException wyjątek zwrócony gdy nazwa grupy jest pusta
	 */
	public void setName(String name) throws CarException{
		if (name == null || name.equals(""))
			throw new CarException("Nazwa grupy nie może być pusta.");
		this.name = name; 
	}
	
	/**
	 * Metoda zwracająca typ kontenera na dane obiektu
	 * @return zwrócony typ kontenera na dane
	 */
	public GroupType getType(){
		return type;
	}
	
	/**
	 * Metoda ustanawiająca typ kontenera na dane danego obiektu
	 * @param type ustanowiony typ kontenera
	 * @throws CarException wyjątek zwracany gdy nie można ustanowić typu kontenera 
	 */
	public void setType(GroupType type) throws CarException{
		if(type == null)
			throw new CarException("Typ kolekcji nie może być pusty.");
		if(this.type == type)
			return;
		Collection<Car> oldCollection = collection;
		collection = type.createCollection();
		this.type = type;
		for (Car car : oldCollection)
			collection.add(car);
	}
	
	/**
	 * Metoda ustanawiająca typ kontenera na dane danego obiektu
	 * @param type_name ustanowiony typ kontenera
	 * @throws CarException wyjątek zwracany gdy nie można ustanowić typu kontenera 
	 */
	public void setType(String type_name) throws CarException{
		for (GroupType type : GroupType.values())
			if (type.toString().equals(type_name)){
				setType(type);
				return;
			}
		throw new CarException("Nie ma takiego typu kolekcji");
	}
	
	/**
	 * Metoda dodająca obiekt typu Car do danej kolekcji obiektu
	 * @param car dodany obiekt typu Car
	 * @return informacja czy udało się dodać obiekt do kolekcji
	 */
	public boolean add(Car car){
		return collection.add(car);
	}

	/**
	 * Metoda iterująca po kolekcji obiektór typu Car
	 * @return ziterowana kolekcja
	 */
	public Iterator<Car> iterator(){
		return collection.iterator();
	}
	
	/**
	 * Metoda zwracająca rozmiar kolekcji obiektów typu Car
	 * @return zwrócony rozmiar
	 */
	public int getSize(){
		return collection.size();
	}
	
	/**
	 * Metoda zwracająca kolekcję obiektów typu Car danego obiektu
	 * @return zwrócona kolakcja
	 */
	public Collection<Car> getCollection(){
		return collection;
	}
	
	/**
	 * Metoda sortująca kolekcje
	 * @throws CarException wyjątek zwrócony gdy nie można posortować kolekcji
	 */
	public void sortName() throws CarException{
		if (type == GroupType.HASH_SET || type == GroupType.TREE_SET)
			throw new CarException("Kolekcje typu SET nie mogą być sortowane.");
		
		Collections.sort((List<Car>)collection);
	}
	
	/**
	 * Metoda sortująca obiekty typu Car według atrybutu "rok produkcji"
	 * @throws CarException wyjątek zwrócony gdy nie można posortować kolekcji
	 */
	public void sortProductionYear() throws CarException{
		if (type == GroupType.HASH_SET || type == GroupType.TREE_SET)
			throw new CarException("Kolekcje typu SET nie mogą być sortowane.");
		
		Collections.sort((List<Car>)collection, new Comparator<Car>(){
			public int compare(Car car1, Car car2){
				if (car1.getProductionYear() > car2.getProductionYear())
					return 1;
				if (car1.getProductionYear() < car2.getProductionYear())
					return -1;
				return 0;
			}	
		});	
	}
	
	/**
	 * Metoda sortująca obiekty typu Car według atrybutu "pojemność silnika"
	 * @throws CarException wyjątek zwrócony gdy nie można posortować kolekcji
	 */
	public void sortEngineCapacity() throws CarException{
		if (type == GroupType.HASH_SET || type == GroupType.TREE_SET)
			throw new CarException("Kolekcje typu SET nie mogą być sortowane.");
		
		Collections.sort((List<Car>)collection, new Comparator<Car>(){
			public int compare(Car car1, Car car2){
				if (car1.getEngineCapacity() > car2.getEngineCapacity())
					return 1;
				if (car1.getEngineCapacity() < car2.getEngineCapacity())
					return -1;
				return 0;
			}
		});
	}
	
	/**
	 * Metoda sortująca obiekty typu Car według atrybutu "marka samochodu"
	 * @throws CarException wyjątek zwrócony gdy nie można posortować kolekcji
	 */
	public void sortCarName() throws CarException{
		if (type == GroupType.HASH_SET || type == GroupType.TREE_SET)
			throw new CarException("Kolekcje typu SET nie mogą być sortowane.");
		
		Collections.sort((List<Car>)collection, new Comparator<Car>(){
			public int compare (Car car1, Car car2){
				return car1.getCarName().toString().compareTo(car2.getCarName().toString());
			}
		});
	}
	
	/**
	 * Metoda zwracająca nazwę danej danego obiektu oraz typ kolekcji obiektu w postaci łańcucha znaków
	 * @return zwrócone dane
	 */
	public String toString(){
		return name + "[" + type + "]";
	}
	
	/**
	 * Metoda pozwalająca na zapis danych do pliku
	 * @param writer plik do ktorego bedzie zapisywany obiekt
	 * @param group zapisywany obiekt
	 */
	public static void printToFile(PrintWriter writer, GroupOfCars group) {
		writer.println(group.getName());
		writer.println(group.getType());
		for (Car car : group.collection)
			Car.printToFile(writer, car);
	}
	
	/** Metoda pozwalająca na zapis danych do pliku
	 * @param writer plik do ktorego bedzie zapisywany obiekt
	 * @param group zapisywany obiekt
	 */
	public static void printToFile(String file_name, GroupOfCars group) throws CarException{
		try (PrintWriter writer = new PrintWriter(file_name)){
			printToFile(writer, group);
		} catch (FileNotFoundException e){
			throw new CarException("Nie odnaleziono pliku " + file_name);
		}
	}
	
	/**
	 * Metoda pozwalająća na odczytywanie danych z pliku
	 * @param reader bufor przetrzymujący dane odczytane z pliku
	 * @return zwrocony obiekt typu GroupOfCars
	 * @throws CarException wyjątek zwrócony gdy nie można odczytać obiektu
	 */
	public static GroupOfCars readFromFile(BufferedReader reader) throws CarException{
		try {
			String group_name = reader.readLine();
			String type_name = reader.readLine();
			GroupOfCars groupOfCars = new GroupOfCars(type_name, group_name);

			Car car;
			while((car = Car.readFromFile(reader)) != null)
				groupOfCars.collection.add(car);
			return groupOfCars;
		} catch(IOException e){
			throw new CarException("Wystąpił błąd podczas odczytu danych z pliku.");
		}
	}


	/**
	 * Metoda pozwalająća na odczytywanie danych z pliku
	 * @param reader bufor przetrzymujący dane odczytane z pliku
	 * @return zwrocony obiekt typu GroupOfCars
	 * @throws CarException wyjątek zwrócony gdy nie można odczytać obiektu
	 */
	public static GroupOfCars readFromFile(String file_name) throws CarException {
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(file_name)))) {
			return GroupOfCars.readFromFile(reader);
		} catch (FileNotFoundException e){
			throw new CarException("Nie odnaleziono pliku " + file_name);
		} catch(IOException e){
			throw new CarException("Wystąpił błąd podczas odczytu danych z pliku.");
		}
	}
	
	/**
	 * Metoda tworząca obiekt typu GroupOfCars zawierający wszystkie obiekty
	 * typu Car dwóch innych obiektów tego samego typu
	 * @param g1 pierwszy obiekt do tworzenia nowego
	 * @param g2 drugi obiekt do tworzenia nowego
	 * @return utworzony nowy obiekt typu GroupOfCars
	 * @throws CarException wyjatek zgłaszany gdy nie da się utworzyć nowego obiektu
	 */
	public static GroupOfCars createGroupUnion(GroupOfCars g1,GroupOfCars g2) throws CarException {
		String name = "(" + g1.name + " OR " + g2.name +")";
		GroupType type;
		if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
			type = g2.type;
		} else {
			type = g1.type;
		}
		GroupOfCars group = new GroupOfCars(type, name);
		group.collection.addAll(g1.collection);
		group.collection.addAll(g2.collection);
		return group;
	}

	/**
	 * Metoda tworząca obiekt typu GroupOfCars zawierający wszystkie obiekty typu Car
	 * dwóch innych obiektów tego samego typu
	 * @param g1 pierwszy obiekt do tworzenia nowego
	 * @param g2 drugi obiekt do tworzenia nowego
	 * @return utworzony nowy obiekt typu GroupOfCars
	 * @throws CarException wyjatek zgłaszany gdy nie da się utworzyć nowego obiektu
	 */
	public static GroupOfCars createGroupIntersection(GroupOfCars g1,GroupOfCars g2) throws CarException {
		String name = "(" + g1.name + " AND " + g2.name +")";
		GroupType type;
		if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
			type = g2.type;
		} else {
			type = g1.type;
		}
		GroupOfCars group = new GroupOfCars(type, name);
		for (Car gr1 : g1.collection){
			for(Car gr2 : g2.collection)
				if (gr1.hashCode() == gr2.hashCode())
					group.add(gr1);
		}
		return group;
	}

	/**
	 * Metoda tworząca obiekt typu GroupOfCars zawierający obiekty typu Car będące w
	 * jednym, ale jednocześnie nie będące w drugim z dwóch innych obiektów tego samego typu
	 * @param g1 pierwszy obiekt do tworzenia nowego
	 * @param g2 drugi obiekt do tworzenia nowego
	 * @return utworzony nowy obiekt typu GroupOfCars
	 * @throws CarException wyjatek zgłaszany gdy nie da się utworzyć nowego obiektu
	 */
	public static GroupOfCars createGroupDifference(GroupOfCars g1,GroupOfCars g2) throws CarException {
		String name = "(" + g1.name + " SUB " + g2.name +")";
		GroupType type;
		if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
			type = g2.type;
		} else {
			type = g1.type;
		}
		GroupOfCars group = new GroupOfCars(type, name);
		for (Car gr1 : g1.collection){
			boolean contains = false;
			for(Car gr2 : g2.collection)
				if (gr1.hashCode() == gr2.hashCode())
					contains = true;
			if (!contains)
				group.add(gr1);
		}
		return group;
	}

	/**
	 * Metoda tworząca obiekt typu GroupOfCars zawierający obiekty typu Car będące w
	 * jednym, ale jednocześnie nie będące w drugim, oraz będące w drugim, a nie będące
	 * w pierwszym obiekcie z dwóch innych obiektów tego samego typu
	 * @param g1 pierwszy obiekt do tworzenia nowego
	 * @param g2 drugi obiekt do tworzenia nowego
	 * @return utworzony nowy obiekt typu GroupOfCars
	 * @throws CarException wyjatek zgłaszany gdy nie da się utworzyć nowego obiektu
	 */
	public static GroupOfCars createGroupSymmetricDiff(GroupOfCars g1,GroupOfCars g2) throws CarException {
		String name = "(" + g1.name + " XOR " + g2.name +")";
		GroupType type;
		if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
			type = g2.type;
		} else {
			type = g1.type;
		}

		GroupOfCars group = new GroupOfCars(type, name);
		for (Car gr1 : g1.collection){
			boolean contains = false;
			for(Car gr2 : g2.collection)
				if (gr1.hashCode() == gr2.hashCode())
					contains = true;
			if (!contains)
				group.add(gr1);
		}
		for (Car gr2 : g2.collection){
			boolean contains = false;
			for(Car gr1 : g1.collection)
				if (gr1.hashCode() == gr2.hashCode())
					contains = true;
			if (!contains)
				group.add(gr2);
		}
		return group;
	}
}