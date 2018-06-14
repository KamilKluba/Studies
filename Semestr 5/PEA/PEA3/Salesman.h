#include <vector>
#include <deque>
#include <iostream>
#include <ctime>
#include <climits>
#include <fstream>
#include <windows.h>

using namespace std;

class Salesman {
public:
	int citiesAmount;
	int *path;
	int cost;
	int fitnessStart, fitnessEnd;
	unsigned long long int hashcode;
	bool copied;

public:
	Salesman();
	Salesman(int cities, int *set);
	~Salesman();
	void createPath(int cities);
	void reset();
	void makePath(int cities);
};

class MainThread {
public:
	int citiesAmount;
	int currentPopulationSize;
	int populationSize;
	int currentChildrenSize;
	int childrenSize;
	int currentParentsSize;
	int parentsSize;
	int bestOnes;
	int newGenerationSize;
	int tournamentSize;
	int mutationPercent;
	int mutationMethod;
	int generationCounter;
	int theBest, theWorst;
	float **map;
	int *set;
	//deque<Salesman> population;
	Salesman *parents;
	Salesman *children;
	Salesman *population;
	deque<int> childrenQuality;
	Salesman bestOne;
	ifstream inputPlik;
	ofstream outputPlik;
	bool opened;
	int *tabMutation;;
	int *tabNewGenes;
	bool *tabGenesA, *tabGenesB, *tabSwathGenes;

public:
	MainThread(int citAmount, int popSize, int tourSize, int mutPercent, int problem);
	~MainThread();
	void randomSymmetricMap();
	void randomUnsymmetricMap();
	void createPopulation();
	void reset();
	void shuffle();
	void fitness(bool first);
	void createNewGeneration();
	void copyTheBest();
	void findParents();
	void crossAndMutate();
	void deleteUseless();
	void mixNewPopulation();
	void deleteAll();
	void run();
	void checkForTheSame();
	void readMap(string name);
	void doIt(int citAmount, int popSize, int tourSize, int mutPercent, int problem);
};
