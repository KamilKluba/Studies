#include "Salesman.h"

double PCFreq2 = 0.0;
__int64 licznik2 = 0;

void czasStartSalesman()
{
	licznik2 = 0;
	LARGE_INTEGER li;
	if (!QueryPerformanceFrequency(&li))
		cout << "B³¹d!\n";

	PCFreq2 = double(li.QuadPart) / 1000.0;

	QueryPerformanceCounter(&li);
	licznik2 = li.QuadPart;
}
double pobierzCzasSalesman()
{
	LARGE_INTEGER li;
	QueryPerformanceCounter(&li);
	return (li.QuadPart - licznik2) / PCFreq2;
}

Salesman::Salesman(int cities, int *set) {
	path = new int[cities];
	copied = false;
	hashcode = 1;
	cost = 0;
	citiesAmount = cities;

	for (int i = 0; i < cities; i++)
		path[i] = set[i];
}

Salesman::Salesman() {
	copied = false;
	hashcode = 1;
	cost = 0;
}

Salesman::~Salesman() {
	delete[] path;
}

void Salesman::reset() {
//	for (int i = 0; i < citiesAmount; i++)
//		path[i] = INT_MAX;
	copied = false;
	hashcode = 1;
	cost = 0;
}

void Salesman::makePath(int cities) {
	citiesAmount = cities;
	path = new int[cities];
	for (int i = 0; i < citiesAmount; i++)
		path[i] = INT_MAX;
}

MainThread::MainThread(int citAmount, int popSize, int tourSize, int mutPercent, int problem) {
	int bOnes = 0;
	int mutMethod = 1;
	citiesAmount = citAmount;
	populationSize = popSize;
	currentPopulationSize = currentChildrenSize = currentParentsSize = 0;
	bestOnes = bOnes;
	childrenSize = (populationSize / tourSize + populationSize % tourSize) * ((populationSize - 1) / tourSize + (populationSize - 1) % tourSize) + bestOnes;
	children = new Salesman[childrenSize]();
	tournamentSize = tourSize;
	mutationPercent = mutPercent;
	mutationMethod = mutMethod;
	int rest = populationSize % tournamentSize;
	if (rest > 0) rest = 1;
	parentsSize = bestOnes + populationSize / tournamentSize + rest;
	parents = new Salesman[parentsSize]();
	generationCounter = 0;
	theBest = INT_MAX;
	theWorst = 0;
	srand(time(NULL));
	if (problem != 0)
		if (problem == 1)
			randomSymmetricMap();
		else
			randomUnsymmetricMap();


	outputPlik.open("Wyniki.txt");
	opened = true;
	map = new float*[citAmount];
	set = new int[citAmount];
	for (int i = 0; i < citAmount; i++) {
		map[i] = new float[citAmount];
		set[i] = i;
	}
	population = new Salesman[populationSize]();
	for (int i = 0; i < childrenSize; i++)
		children[i].makePath(citiesAmount);
	for (int i = 0; i < parentsSize; i++)
		parents[i].makePath(citiesAmount);
	for (int i = 0; i < populationSize; i++)
		population[i].makePath(citiesAmount);

	tabMutation = new int[citiesAmount];
	tabNewGenes = new int[citiesAmount];
	tabGenesA = new bool[citiesAmount];
	tabGenesB = new bool[citiesAmount];
	tabSwathGenes = new bool[citiesAmount];

	//run();
	//deleteAll();
}

void MainThread::doIt(int citAmount, int popSize, int tourSize, int mutPercent, int problem) {
	int bOnes = 0;
	int mutMethod = 1;
	citiesAmount = citAmount;
	populationSize = popSize;
	currentPopulationSize = currentChildrenSize = currentParentsSize = 0;
	bestOnes = bOnes;
	childrenSize = (populationSize / tourSize + populationSize % tourSize) * ((populationSize - 1) / tourSize + (populationSize - 1) % tourSize) + bestOnes;
	tournamentSize = tourSize;
	mutationPercent = mutPercent;
	mutationMethod = mutMethod;
	int rest = populationSize % tournamentSize;
	if (rest > 0) rest = 1;
	parentsSize = bestOnes + populationSize / tournamentSize + rest;
	generationCounter = 0;
	theBest = INT_MAX;
	theWorst = 0;
	opened = false;

	parents = new Salesman[parentsSize]();
	children = new Salesman[childrenSize]();
	population = new Salesman[populationSize]();
	for (int i = 0; i < childrenSize; i++)
		children[i].makePath(citiesAmount);
	for (int i = 0; i < parentsSize; i++)
		parents[i].makePath(citiesAmount);
	for (int i = 0; i < populationSize; i++)
		population[i].makePath(citiesAmount);
}

MainThread::~MainThread() {
	for (int i = 0; i < citiesAmount; i++)
		delete[] map[i];

	delete[] map, set;

	//delete[] children;
}

void MainThread::randomSymmetricMap() {
	for (int dataCounter = 0; dataCounter < citiesAmount; dataCounter++)
		for (int dataCounter2 = dataCounter; dataCounter2 < citiesAmount; dataCounter2++) {
			if (dataCounter == dataCounter2) map[dataCounter][dataCounter2] = 0;
			else map[dataCounter][dataCounter2] = map[dataCounter2][dataCounter] = rand() % 100 + 1;
		}
	/*cout << "MAPA" << endl;
	for (int j = 0; j < citiesAmount; j++) {
		for (int i = 0; i < citiesAmount; i++)
			cout << map[j][i] << " ";
		cout << endl;
	}*/
}

void MainThread::randomUnsymmetricMap() {
	for (int dataCounter = 0; dataCounter < citiesAmount; dataCounter++)
		for (int dataCounter2 = 0; dataCounter2 < citiesAmount; dataCounter2++)
			if (dataCounter != dataCounter2)
				map[dataCounter][dataCounter2] = rand() % 100 + 1;
}

void MainThread::shuffle() {
	int buffer;
	int a, b;

	for (int i = 0; i < citiesAmount; i++) {
		a = rand() % citiesAmount;
		b = rand() % citiesAmount;
		buffer = set[a];
		set[a] = set[b];
		set[b] = buffer;
	}
}

void MainThread::fitness(bool first) {
	theWorst = 0;
	if (first) {
	/*	for (int i = 0; i < populationSize; i++) {
			for (int k = 0; k < citiesAmount; k++)
				cout << population[i].path[k] << " ";
			cout << endl;
		}
		cout << populationSize << " " << currentPopulationSize << endl;*/
		for (int j = 0; j < populationSize; j++) {
			for (int i = 0; i < citiesAmount - 1; i++)
				population[j].cost += map[population[j].path[i]][population[j].path[i + 1]];
			population[j].cost += map[population[j].path[citiesAmount - 1]][population[j].path[0]];
			if (population[j].cost < theBest) theBest = population[j].cost;
			if (population[j].cost > theWorst) theWorst = population[j].cost;

			for (int i = 0; i < citiesAmount; i++) {
				if (population[j].path[i] % 3 == 0) population[j].hashcode += population[j].path[i] + 1 + i;
				else if (population[j].path[i] % 3 == 1) population[j].hashcode /= (population[j].path[i] % 10 + 1);
				else population[j].hashcode *= (population[j].path[i] % 10 + 2);
				//cout << population[j].hashcode << endl;
			}
		}
	}
	else {
/*		cout << "POPULACJA" << endl;
		for (int i = 0; i < populationSize; i++) {
			for (int k = 0; k < citiesAmount; k++)
				cout << population[i].path[k] << " ";
			cout << endl;
		}
		cout << populationSize << " " << currentPopulationSize << endl;
		cout << "DZIECI" << endl;
		for (int i = 0; i < childrenSize; i++) {
			for (int k = 0; k < citiesAmount; k++)
				cout << children[i].path[k] << " ";
			cout << endl;
		}
		cout << childrenSize << " " << currentChildrenSize << endl;*/
		for (int j = bestOnes; j < currentChildrenSize; j++) {
		//	cout << j << " ";
			for (int i = 0; i < citiesAmount - 1; i++) {
			//	cout << j << " " << i << " " << children[j].cost << " " << map[children[j].path[i]][children[j].path[i + 1]] << " " << children[j].path[i] << " " << children[j].path[i + 1] << endl;
				//cout << j << " " << i << "    ";
				children[j].cost += map[children[j].path[i]][children[j].path[i + 1]];
			}
			children[j].cost += map[children[j].path[citiesAmount - 1]][children[j].path[0]];
			if (children[j].cost < theBest) {
				theBest = children[j].cost;
				bestOne = children[j];
			}
			if (children[j].cost > theWorst) theWorst = children[j].cost;

			for (int i = 0; i < citiesAmount; i++) {
				if (children[j].path[i] % 3 == 0) children[j].hashcode += children[j].path[i] + 1 + i;
				else if (children[j].path[i] % 3 == 1) children[j].hashcode /= (children[j].path[i] % 10 + 1);
				else children[j].hashcode *= (children[j].path[i] % 10 + 2);
			}
		}
	}
}

void MainThread::createNewGeneration() {
	if (generationCounter % 1000 == 0) cout << "Generacja: " << generationCounter << "      Najlepszy osobnik: " << theBest << "      Najgorszy osobnik: " << theWorst << endl;
	generationCounter++;
}

void MainThread::run() {
	bool s1 = false, s5 = false, s10 = false, s30 = false, s60 = false, s100 = false, s200 = false;
	bool finish = false;
	for (int j = 0; j < 1; j++) {
		for (int i = 0; i < 9 ; i++) {
			if (j == 0) readMap("asym124.txt");
			if (i == 0) doIt(citiesAmount, 10, 2, 1, 0);
			if (i == 1) doIt(citiesAmount, 10, 2, 1, 0);
			if (i == 2) doIt(citiesAmount, 10, 2, 5, 0);
			if (i == 3) doIt(citiesAmount, 20, 3, 1, 0);
			if (i == 4) doIt(citiesAmount, 20, 3, 5, 0);
			if (i == 5) doIt(citiesAmount, 50, 4, 1, 0);
			if (i == 6) doIt(citiesAmount, 50, 4, 5, 0);
			if (i == 7) doIt(citiesAmount, 100, 5, 1, 0);
			if (i == 8) doIt(citiesAmount, 100, 5, 5, 0);
			czasStartSalesman();
			while (pobierzCzasSalesman() < 200000) {
				createPopulation();
				fitness(true);
				while (!finish) {
					createNewGeneration();
					reset();
					copyTheBest();
					findParents();
					crossAndMutate();
					fitness(false);
					deleteUseless();
					checkForTheSame();
					mixNewPopulation();
					if (pobierzCzasSalesman() > 1000 && !s1) {
						s1 = true;
						outputPlik << "Liczba miast: ;" << citiesAmount << "; Wielkosc populacji: ;" << populationSize << "; Wielkosc turnieju: ;" << tournamentSize << "; Szansa mutacji: ;" << mutationPercent << "; Czas pomiaru: ;" << pobierzCzasSalesman() << "; Generacja: ;" << generationCounter << "; Najlepszy wynik: ;" << theBest << endl;
					}
					if (pobierzCzasSalesman() > 5000 && !s5) {
						s5 = true;
						outputPlik << "Liczba miast: ;" << citiesAmount << "; Wielkosc populacji: ;" << populationSize << "; Wielkosc turnieju: ;" << tournamentSize << "; Szansa mutacji: ;" << mutationPercent << "; Czas pomiaru: ;" << pobierzCzasSalesman() << "; Generacja: ;" << generationCounter << "; Najlepszy wynik: ;" << theBest << endl;
					}
					if (pobierzCzasSalesman() > 10000 && !s10) {
						s10 = true;
						outputPlik << "Liczba miast: ;" << citiesAmount << "; Wielkosc populacji: ;" << populationSize << "; Wielkosc turnieju: ;" << tournamentSize << "; Szansa mutacji: ;" << mutationPercent << "; Czas pomiaru: ;" << pobierzCzasSalesman() << "; Generacja: ;" << generationCounter << "; Najlepszy wynik: ;" << theBest << endl;
					}
					if (pobierzCzasSalesman() > 30000 && !s30) {
						s30 = true;
						outputPlik << "Liczba miast: ;" << citiesAmount << "; Wielkosc populacji: ;" << populationSize << "; Wielkosc turnieju: ;" << tournamentSize << "; Szansa mutacji: ;" << mutationPercent << "; Czas pomiaru: ;" << pobierzCzasSalesman() << "; Generacja: ;" << generationCounter << "; Najlepszy wynik: ;" << theBest << endl;
					}
					if (pobierzCzasSalesman() > 100000 && !s100) {
						s100 = true;
						outputPlik << "Liczba miast: ;" << citiesAmount << "; Wielkosc populacji: ;" << populationSize << "; Wielkosc turnieju: ;" << tournamentSize << "; Szansa mutacji: ;" << mutationPercent << "; Czas pomiaru: ;" << pobierzCzasSalesman() << "; Generacja: ;" << generationCounter << "; Najlepszy wynik: ;" << theBest << endl;
					}
					if (pobierzCzasSalesman() > 200000 && !s200) {
						s200 = true;
						finish = true;
						outputPlik << "Liczba miast: ;" << citiesAmount << "; Wielkosc populacji: ;" << populationSize << "; Wielkosc turnieju: ;" << tournamentSize << "; Szansa mutacji: ;" << mutationPercent << "; Czas pomiaru: ;" << pobierzCzasSalesman() << "; Generacja: ;" << generationCounter << "; Najlepszy wynik: ;" << theBest << endl;
					}
				}
			}
			s1 = false, s5 = false, s10 = false, s30 = false, s60 = false, s100 = false, s200 = false, finish = false, opened = true;
		}
	}
}

void MainThread::createPopulation() {
	for (int i = 0; i < populationSize; i++) {
		shuffle();
		currentPopulationSize++;
		for (int k = 0; k < citiesAmount; k++)
			population[i].path[k] = set[k];
	}
	/*for (int i = 0; i < populationSize; i++) {
	for (int j = 0; j < citiesAmount; j++)
	cout << population[i].path[j]<<" ";
	cout <<"    "<<population[i].hashcode<< " "<<population[i].cost<<endl;
	}*/
}

void MainThread::reset() {
	for (int i = 0; i < parentsSize; i++)
		parents[i].reset();
	currentParentsSize = 0;
	for (int i = 0; i < childrenSize; i++)
		children[i].reset();
	currentChildrenSize = 0;
}

void MainThread::copyTheBest() {
	int minCost;
	int currentIndex;

	for (int i = 0; i < bestOnes; i++) {
		minCost = INT_MAX;
		for (int j = 0; j < populationSize; j++) {
			if (population[j].cost < minCost && !population[j].copied) {
				minCost = population[j].cost;
				currentIndex = j;
			}
		}
		population[currentIndex].copied = true;
		children[currentChildrenSize] = population[currentIndex];
		currentChildrenSize++;
	}
	for (int i = 0; i < bestOnes; i++) children[i].copied = false;
}

void MainThread::findParents() {
	int minCost;
	int currentIndex;
	Salesman *parBuffer;
	for (int j = 0; j <= populationSize - tournamentSize; j += tournamentSize) {
		minCost = INT_MAX;
		for (int i = 0; i < tournamentSize; i++) {
			parBuffer = &population[j + i];
			if (parBuffer->cost < minCost) {
				currentIndex = j + i;
				minCost = parBuffer->cost;
			}
		}
		parents[currentParentsSize] = population[currentIndex];
		currentParentsSize++;
	}
	if (populationSize % tournamentSize != 0) {
		minCost = INT_MAX;
		for (int i = populationSize - populationSize % tournamentSize; i < populationSize; i++) {
			parBuffer = &population[i];
			if (parBuffer->cost < minCost) {
				currentIndex = i;
				minCost = parBuffer->cost;
			}
		}
		parents[currentParentsSize] = population[currentIndex];
		currentParentsSize++;
	}
}

void MainThread::crossAndMutate() {
	Salesman *parentA, *parentB, *parBuffer;
	double dominationRatio;
	int copiedGenesSize;
	int copyStart;
	int buffer;
	int mutate;


	for (int i = 0; i < parentsSize; i++) {
		for (int j = i + 1; j < parentsSize; j++) {
			parentA = &parents[i];
			parentB = &parents[j];
			dominationRatio = (double)parentB->cost / (double)(parentA->cost + parentB->cost);
			if (dominationRatio == 0) dominationRatio = 0.5; 
			copiedGenesSize = citiesAmount * dominationRatio;
			for (int h = 0; h < 2; h++) {
				copyStart = rand() % (citiesAmount - copiedGenesSize);
				for (int k = 0; k < citiesAmount; k++) {
					tabGenesA[k] = false;
					tabGenesB[k] = false;
					tabSwathGenes[k] = false;
					tabNewGenes[k] = INT_MAX;
				}
				for (int k = copyStart; k < copiedGenesSize + copyStart; k++) {
					tabNewGenes[k] = parentA->path[k];
					tabGenesA[k] = true;
				}
				for (int k = 0; k < citiesAmount; k++)
					for (int l = copyStart; l < copyStart + copiedGenesSize; l++) {
						if (parentB->path[k] == parentA->path[l]) {
							tabGenesB[k] = true;
							tabSwathGenes[l] = true;
						}
					}
				for (int k = copyStart; k < copyStart + copiedGenesSize; k++) {
					if (tabSwathGenes[k] && !tabGenesB[k]) {
						buffer = parentA->path[k];
						int counter = 0;
						int z = 0;
						while (true) {
							if (parentB->path[counter] == buffer) {
								if (!tabSwathGenes[counter]) {
									tabSwathGenes[counter] = true;
									tabGenesB[k] = true;
									tabNewGenes[counter] = parentB->path[k];
									break;
								}
								else {
									buffer = parentA->path[counter];
									counter = 0;
								}
							}
							else
								counter++;
						}
					}
				}
				for (int k = 0; k < citiesAmount; k++)
					if (!tabGenesB[k]) {
						tabGenesB[k] = true;
						tabNewGenes[k] = parentB->path[k];
					}
				mutate = rand() % 100;
				if (mutate < mutationPercent) {
					int geneA = rand() % citiesAmount;
					int geneB = rand() % citiesAmount;
					while (geneB == geneA)	geneB = rand() % citiesAmount;
					if (mutationMethod == 1) {
						buffer = tabNewGenes[geneA];
						tabNewGenes[geneA] = tabNewGenes[geneB];
						tabNewGenes[geneB] = buffer;
					}
					else {
						if (geneA > geneB) {
							buffer = geneA;
							geneA = geneB;
							geneB = buffer;
						}
						
						for (int k = geneA; k < geneB; k++)
							tabMutation[k - geneA] = tabNewGenes[k];
						for (int k = geneB - 1; k >= geneA; k--)
							tabNewGenes[k] = tabMutation[k - geneA];
					}
				}
				for (int k = 0; k < citiesAmount; k++)
					children[currentChildrenSize].path[k] = tabNewGenes[k];
	//			for (int k = 0; k < citiesAmount; k++)
		//			cout<<children[currentChildrenSize].path[k]<<" ";
				currentChildrenSize++;

				parBuffer = parentA;
				parentA = parentB;
				parentB = parBuffer;
			}
		}
	}

}

void MainThread::deleteUseless() {
	int minCost;
	int currentIndex;

	currentPopulationSize = 0;
	for (int i = 0; i < bestOnes; i++) {
		population[i] = children[i];
		currentPopulationSize++;
	}
	int searchStart = currentPopulationSize;
	for (int i = searchStart; i < populationSize; i++) {
		minCost = INT_MAX;
		for (int l = searchStart; l < currentChildrenSize; l++)
			if (children[l].cost < minCost && !children[l].copied) {
				minCost = children[l].cost;
				currentIndex = l;
			}
		population[i] = children[currentIndex];
		children[currentIndex].copied = true;
		currentPopulationSize++;
	}
	childrenQuality.clear();
}

void MainThread::mixNewPopulation(){
	int a, b;
	Salesman *salesmanBuffer;

	for (int i = 0; i < populationSize; i++) {
		a = rand() % populationSize;
		b = rand() % populationSize;
		while (a == b) b = rand() % populationSize;
		salesmanBuffer = &population[a];
		population[a] = population[b];
		population[b] = *salesmanBuffer;
	}
	population[0] = bestOne;
}

void MainThread::checkForTheSame() {
	/*for (int i = 0; i < populationSize; i++) {
		for (int l = i + i; l < populationSize; l++) {
			if (population[l].cost < population[i].cost) {
				Salesman buffer;
				buffer = population[i];
				population[i] = population[l];
				population[l] = buffer;
			}
		}
	}*/
	for (int i = 0; i < populationSize - 1; i++)
		for (int j = i + 1; j < populationSize; j++) {
			if (population[i].hashcode == population[j].hashcode) {
				int a = rand() % citiesAmount;
				int b = rand() % citiesAmount;
				while (a == b) b = rand() % citiesAmount;
				int buffer = population[j].path[a];
				population[j].path[a] = population[j].path[b];
				population[j].path[b] = buffer;
				population[j].cost = 0;
				population[j].hashcode = 1;

				for (int k = 0; k < citiesAmount - k; k++)
					population[j].cost += map[population[j].path[k]][population[j].path[k + 1]];
				population[j].cost += map[population[j].path[citiesAmount - 1]][population[j].path[0]];
				if (population[j].cost < theBest) theBest = population[j].cost;
				if (population[j].cost > theWorst) theWorst = population[j].cost;
			}
		}/**/
}








void MainThread::deleteAll() {
	cout << "Parents: " << parentsSize << " " << currentParentsSize<<endl;
	cout << "Popoula: " << populationSize << " " << currentPopulationSize << endl;
	cout << "Childre: " << childrenSize << " " << currentChildrenSize << endl;

	cin >> parentsSize;
	for (int i = 0; i < childrenSize; i++) {
		Salesman *salesman = &children[i];
		delete salesman;
	}
	cin >> mutationMethod;
}

void MainThread::readMap(string name) {
	inputPlik.open(name);
	inputPlik >> citiesAmount;

	for (int dataCounter = 0; dataCounter < citiesAmount; dataCounter++)
		for (int dataCounter2 = 0; dataCounter2 < citiesAmount; dataCounter2++)
			inputPlik >> map[dataCounter][dataCounter2];
}