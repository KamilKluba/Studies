#include <iostream>
#include <fstream>
#include <conio.h>
#include "Salesman.h"
#include <string>

using namespace std;

int main(int argc, char* argv[]) {
	int choice;
	int citiesAmount;
	int populationSize;
	int bestOnes;
	int newGenerationSize;
	int tournamentSize;
	int mutationPercent;
	int mutationMethod;
	int problem;
	MainThread *genetic;
	ifstream inputPlik;

	do {
		cout << endl;
		cout << "==== MENU KOMIWOJAZER ===" << endl;
		cout << "1. Stworz dane" << endl;
		cout << "2. Testy" << endl;
		cout << "0. Wyjscie" << endl;
		choice = _getche();
		system("CLS");
		switch (choice) {
		case '1':
			/*
			cout << endl << "Podaj liczbe miast: ";
			cin >> citiesAmount;
			cout << "Podaj wielkosc populacji: ";
			cin >> populationSize;
			cout << "Podaj ile najlepszych osobnikow ma przechodzic do nowej generacji: ";
			cin >> bestOnes;
			cout << "Podaj wielkosc kazdej kolejnej generacji: ";
			cin >> newGenerationSize;
			cout << "Podaj wielkosc turnieju: ";
			cin >> tournamentSize;
			cout << "Podaj procent mutacji: ";
			cin >> mutationPercent;
			cout << "Podaj sposob mutacji: " << endl;
			do {
				cout << "1. Swap" << endl;
				cout << "2. Invert" << endl;
				mutationMethod = getche();
			} while (!(mutationMethod == '1' || mutationMethod == '2'));
			do {
				cout << endl << "1. Symetryczny" << endl;
				cout << "2. Niesymetryczny" << endl;
				problem = getche();
			} while (!(problem == '1' || problem == '2'));
			*/

			//genetic = new MainThread(citiesAmount, populationSize, bestOnes, newGenerationSize, tournamentSize, mutationPercent, mutationMethod, problem);
			genetic = new MainThread(124, 1, 1, 1, 0);
			genetic->run();
			//while (true) genetic->createNewGeneration();
			//delete genetic;
			//genetic->shuffle();

			break;
		case '2':
			string file = "";
			cout << "Podaj nazwe pliku z map¹ miast: ";
			cin >> file;
			inputPlik.open(file);
			if (!inputPlik) {
				cout << "Nie mozna odnalezc pliku!" << endl;
				break;
			}
			cout << "Podaj wielkosc populacji: ";
			cin >> populationSize;
			cout << "Podaj wielkosc turnieju: ";
			cin >> tournamentSize;
			cout << "Podaj procent mutacji: ";
			cin >> mutationPercent;
			inputPlik >> citiesAmount;
			genetic = new MainThread(citiesAmount, populationSize, tournamentSize, mutationPercent, 0);

			for (int dataCounter = 0; dataCounter < citiesAmount; dataCounter++)
				for (int dataCounter2 = 0; dataCounter2 < citiesAmount; dataCounter2++)
					inputPlik >> genetic->map[dataCounter][dataCounter2];

			inputPlik.close();
			genetic->run();
		}
	} while (choice != '0');
}


/*
void MainThread::run() {
createPopulation();
fitness(true);
while (true) {
	createNewGeneration();
	reset();
	copyTheBest();
	findParents();
	crossAndMutate();
	fitness(false);
	deleteUseless();
	mixNewPopulation();
}*/