#include <iostream>
#include <cstdlib>
#include <conio.h>
#include <fstream>
#include <string>
#include "salesman.h"
#include <ctime>



using namespace std;

int main() {
	int choice;
	string name;
	ifstream plik;
	ofstream plik2;
	int cities;
	Salesman *salesman;
	int counter, counter2;
	double dynamicTime3s = 0;
	double dynamicTime4s = 0;
	double dynamicTime5s= 0;
	double dynamicTime6s = 0;
	double dynamicTime7s = 0;
	double dynamicTime8s= 0;
	double dynamicTime9s = 0;
	double dynamicTime10s = 0;
	double dynamicTime11s = 0;
	double dynamicTime12s = 0;
	double dynamicTime13s = 0;
	double dynamicTime3u = 0;
	double dynamicTime4u = 0;
	double dynamicTime5u = 0;
	double dynamicTime6u = 0;
	double dynamicTime7u = 0;
	double dynamicTime8u = 0;
	double dynamicTime9u = 0;
	double dynamicTime10u = 0;
	double dynamicTime11u = 0;
	double dynamicTime12u = 0;
	double dynamicTime13u = 0;


	srand(time(0));
	do {
		cout << endl;
		cout << "==== MENU KOMIWOJAZER ===" << endl;
		cout << "1. Wczytaj dane z pliku" << endl;
		cout << "2. Stwórz dane" << endl;
		cout << "3. Wyswietl dane " << endl;
		cout << "0. Powrot do menu glownego" << endl;
		choice = _getche();
		system("CLS");

		switch (choice) {
		case '1':
			cout << "Podaj nazwe pliku: ";
			cin >> name;
			name += ".txt";
			plik.open(name);
			if (!plik) {
				cout << "Bledna nazwa pliku!" << endl;
				break;
			}
			else {
				plik >> cities;
				salesman = new Salesman(cities);
				counter = counter2 = 0;
				while (!plik.eof()) {
					if (counter2 == cities) {
						counter++;
						counter2 = 0;
					}
					plik >> salesman->map[counter][counter2];
					counter2++;
				}
				cout << "----------ALGORYTM ZACHLANNY----------" << endl;
				salesman->travelGreed();
				cout << endl << "----------PRZEGLAD ZUPELNY----------" << endl;
				salesman->travelBruteforce();
				cout << endl;
				plik.close();
				name.clear();
			}
			break;
		case '2':
		/*	do {
				cout << "Podaj ilosc miast: ";
				cin >> cities;
				system("CLS");
				if (cities < 2)	cout << endl << "Za malo miast!" << endl;
			} while (cities < 2);*/
			plik2.open("Wyniki.txt");
			counter = 0;salesman = new Salesman(3);
				cities = salesman->citiesAmount;
				salesman->randomUnsymmetricMap();
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime3u += salesman->tsp();
				delete salesman;
			while (counter < 100) {
				counter++;

				

				salesman = new Salesman(4);
				salesman->randomUnsymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime4u += salesman->tsp();
				delete salesman;

				salesman = new Salesman(5);
				salesman->randomUnsymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime5u += salesman->tsp();
				delete salesman;

				salesman = new Salesman(6);
				salesman->randomUnsymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime6u += salesman->tsp();
				delete salesman;

				salesman = new Salesman(7);
				salesman->randomUnsymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime7u += salesman->tsp();
				delete salesman;

				salesman = new Salesman(8);
				salesman->randomUnsymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime8u += salesman->tsp();
				delete salesman;

				salesman = new Salesman(9);
				salesman->randomUnsymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime9u += salesman->tsp();
				delete salesman;

				salesman = new Salesman(10);
				salesman->randomUnsymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime10u += salesman->tsp();
				delete salesman;

				salesman = new Salesman(11);
				salesman->randomUnsymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime11u += salesman->tsp();
				delete salesman;



				salesman = new Salesman(3);
				salesman->randomSymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime3s += salesman->tsp();
				delete salesman;

				salesman = new Salesman(4);
				salesman->randomSymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime4s += salesman->tsp();
				delete salesman;

				salesman = new Salesman(5);
				salesman->randomSymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime5s += salesman->tsp();
				delete salesman;

				salesman = new Salesman(6);
				salesman->randomSymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime6s += salesman->tsp();
				delete salesman;

				salesman = new Salesman(7);
				salesman->randomSymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime7s += salesman->tsp();
				delete salesman;

				salesman = new Salesman(8);
				salesman->randomSymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime8s += salesman->tsp();
				delete salesman;

				salesman = new Salesman(9);
				salesman->randomSymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime9s += salesman->tsp();
				delete salesman;

				salesman = new Salesman(10);
				salesman->randomSymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime10s += salesman->tsp();
				delete salesman;

				salesman = new Salesman(11);
				salesman->randomSymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime11s += salesman->tsp();
				delete salesman;
			}

			
			counter = 0;
			while (counter < 15) {
				counter++;

				salesman = new Salesman(12);
				salesman->randomUnsymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime12u += salesman->tsp();
				delete salesman;

				salesman = new Salesman(12);
				salesman->randomSymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime12s += salesman->tsp();
				delete salesman;
			}

			counter = 0;
			while (counter < 10) {
				counter++;

				salesman = new Salesman(1);
				salesman->randomUnsymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime13u += salesman->tsp();
				delete salesman;

				salesman = new Salesman(13);
				salesman->randomSymmetricMap();
				cities = salesman->citiesAmount;
				for (int dataCounter3 = 0; dataCounter3 < cities; dataCounter3++) {
					for (int dataCounter4 = 0; dataCounter4 < cities; dataCounter4++) cout << salesman->map[dataCounter3][dataCounter4] << " ";
					cout << endl;
				}
				cout << endl << "----------PROGRAMOWANIE DYNAMICZNE----------" << endl;
				dynamicTime13s += salesman->tsp();
				delete salesman;
			}
			plik2 << "Wynik dla 3 miast symetrycznie: " << dynamicTime3s / 100 << endl;
			plik2 << "Wynik dla 4 miast symetrycznie: " << dynamicTime4s / 100 << endl;
			plik2 << "Wynik dla 5 miast symetrycznie: " << dynamicTime5s / 100 << endl;
			plik2 << "Wynik dla 6 miast symetrycznie: " << dynamicTime6s / 100 << endl;
			plik2 << "Wynik dla 7 miast symetrycznie: " << dynamicTime7s / 100 << endl;
			plik2 << "Wynik dla 8 miast symetrycznie: " << dynamicTime8s / 100 << endl;
			plik2 << "Wynik dla 9 miast symetrycznie: " << dynamicTime9s / 100 << endl;
			plik2 << "Wynik dla 10 miast symetrycznie: " << dynamicTime10s / 100 << endl;
			plik2 << "Wynik dla 11 miast symetrycznie: " << dynamicTime11s / 100 << endl;
			plik2 << "Wynik dla 12 miast symetrycznie: " << dynamicTime12s / 15 << endl;
			plik2 << "Wynik dla 13 miast symetrycznie: " << dynamicTime13s / 10 << endl;
			plik2 << "---------------------------------" << endl;
			plik2 << "Wynik dla 3 miast niesymetrycznie: " << dynamicTime3u / 100 << endl;
			plik2 << "Wynik dla 4 miast niesymetrycznie: " << dynamicTime4u / 100 << endl;
			plik2 << "Wynik dla 5 miast niesymetrycznie: " << dynamicTime5u / 100 << endl;
			plik2 << "Wynik dla 6 miast niesymetrycznie: " << dynamicTime6u / 100 << endl;
			plik2 << "Wynik dla 7 miast niesymetrycznie: " << dynamicTime7u / 100 << endl;
			plik2 << "Wynik dla 8 miast niesymetrycznie: " << dynamicTime8u / 100 << endl;
			plik2 << "Wynik dla 9 miast niesymetrycznie: " << dynamicTime9u / 100 << endl;
			plik2 << "Wynik dla 10 miast niesymetrycznie: " << dynamicTime10u / 100 << endl;
			plik2 << "Wynik dla 11 miast niesymetrycznie: " << dynamicTime11u / 100 << endl;
			plik2 << "Wynik dla 12 miast niesymetrycznie: " << dynamicTime12u / 15 << endl;
			plik2 << "Wynik dla 13 miast niesymetrycznie: " << dynamicTime13u / 10 << endl;

			break;
		}
	} while (true);
}