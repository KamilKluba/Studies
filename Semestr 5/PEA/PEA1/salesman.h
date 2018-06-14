#pragma once
#include <iostream>
#include <windows.h>
#include <list>

using namespace std;

class Salesman {
public:
	int citiesAmount;
	int **map;
	bool **visitedMap;
	bool *visited;
	int visitedAmount;
	int stack;
	int totalCost;
	int *visitedCities;
	int *finalVisitedCities;
	int finalCost;
	double greedTime;
	double bruteforceTime;
	double dynamicTime;
	list<int> listResult;
	list<int> mylist;

	Salesman(int cities);
	~Salesman();
	void randomSymmetricMap();
	void randomUnsymmetricMap();
	int dynamicProgramming(int, list<int>);
	double tsp();
	void travelGreed();
	void travelBruteforce();
	void permutation(int permTab[], int data);
};
