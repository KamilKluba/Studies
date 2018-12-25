import java.util.Arrays;

public class CarConsoleApp {
	private static final String GREETING_MESSAGE = "Program Car - wersja konsolowa \n" +
												   "Autor: Kamil Kluba \n" + 
												   "Data: paziernik 2017r. \n";
	
	private static final String MENU = 	"    M E N U   G £ Ó W N E \n" +
										"1 - Podaj dane nowego samochodu \n" +
										"2 - Usuñ dane samochodu \n" +
										"3 - Modyfikuj dane samochodu   \n" +
										"4 - Wczytaj dane z pliku \n" +
										"5 - Zapisz dane do pliku \n" +
										"0 - Zakoñcz program \n";
	
	private static final String CHANGE_MENU = "   Co zmieniæ? \n" + 
											  "1 - Nazwê \n" + 
											  "2 - Datê produkcji \n" + 
											  "3 - Pojemnoœæ silnika \n" + 
											  "4 - Opis \n" +
											  "0 - Powrót do menu g³ównego \n";
	
	private static ConsoleUserDialog UI = new ConsoleUserDialog();
	
	private Car currentCar = null;
	
	public void infiniteLoop(){
		UI.printMessage(GREETING_MESSAGE);
		
		while (true){
			UI.clearConsole();
			showCurrentCar();
			
			try{
				switch(UI.enterInt(MENU + "-> ")){
				case 1:
					currentCar = createNewCar();
					break;
				case 2:
					currentCar = null;
					UI.printMessage("Dane samochodu zosta³y usuniête.");
					break;
				case 3:
					if (currentCar == null)
						throw new CarException("Nie utworzono ¿adnego samochodu.");
					changeCarData(currentCar);
					break;
				case 4:
				{
					String fileName = UI.enterString("Podaj nazwe pliku: ");
					currentCar = Car.readFromFile(fileName);
					UI.printInfoMessage("Dane aktualnego samochodu zosta³y wczytane z pliku " + fileName);
					break;
				}
				case 5:
				{
					String fileName = UI.enterString("Podaj nazwe pliku: ");
					Car.printToFile(fileName, currentCar);
					UI.printInfoMessage("Dane samochodu zosta³y zapisane do pliku " + fileName);
					break;
				} 
				case 0:
					UI.printInfoMessage("Program zakoñczy³ dzia³anie.");
					System.exit(0);
				}
			} catch (CarException e){
				UI.printErrorMessage(e.getMessage());
			}
		}
	}
	
	public void showCurrentCar(){
		showCar(currentCar);
	}
	
	public static void showCar(Car car){
		StringBuilder builder = new StringBuilder();
		
		if (car != null){
			builder.append("Aktualny samochód: \n");
			builder.append("Marka: " + car.getCarName() + "\n");
			builder.append("Rok produkcji: " + car.getProductionYear()+ "\n");
			builder.append("Pojemnosc silnika: " + car.getEngineCapacity() + "\n");
			builder.append("Opis: " + car.getDescription() + "\n");
		}
		else
			builder.append("Brak danych samochodu \n");
		UI.printMessage(builder.toString());
	}
	
	public static Car createNewCar(){
		UI.printMessage("Dozwolone marki: " + Arrays.deepToString(Name.values()));
		String name = UI.enterString("Podaj markê: " );
		String year = UI.enterString("Podaj rok produkcji: ");
		String capacity = UI.enterString("Podaj pojemnoœæ silnika: ");
		String description = UI.enterString("Podaj opis: ");
		
		Car car;
		try { 
			car = new Car();
			car.setCarName(name);
			car.setProductionYear(year);
			car.setEngineCapacity(capacity);
			car.setDescription(description);
		} catch (CarException e) {
			UI.printErrorMessage(e.getMessage());
			return null;
		}
		return car;
	}
	
	public static void changeCarData(Car car){
		while (true){
			UI.clearConsole();
			showCar(car);
			
			try{
				switch (UI.enterInt(CHANGE_MENU + "-> ")){
				case 1:
					UI.printMessage("Dozwolone marki: " + Arrays.deepToString(Name.values()));
					car.setCarName(UI.enterString("Podaj markê: "));
					break;
				case 2:
					car.setProductionYear(UI.enterInt("Podaj rok produkcji: "));
					break;
				case 3:
					car.setEngineCapacity(UI.enterString("Podaj pojemnoœæ silnika: "));
					break;
				case 4:
					car.setDescription(UI.enterString("Podaj opis: "));
				}
			} catch (CarException e){
				UI.printErrorMessage(e.getMessage());
			}
		}
	}
	
	
	public static void main(String[] args){
		CarConsoleApp app  = new CarConsoleApp();
		app.infiniteLoop();
	}
}
