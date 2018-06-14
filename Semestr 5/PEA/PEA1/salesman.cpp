#include "salesman.h"
#include <list>

using namespace std;

double PCFreq2 = 0.0;
__int64 licznik2 = 0;

std::list<int> lista;
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
	cout << endl;
	return (li.QuadPart - licznik2) / PCFreq2;
}

Salesman::Salesman(int cities) {
	citiesAmount = cities;
	map = new int*[cities];
	visitedMap = new bool*[cities];
	visited = new bool[cities];
	visitedAmount = 0;
	totalCost = 0;
	greedTime = bruteforceTime = dynamicTime = 0;
	visitedCities = new int[cities];
	finalVisitedCities = new int[cities];

	for (int dataCounter = 0; dataCounter < cities; dataCounter++) {
		map[dataCounter] = new int[cities];
		visitedMap[dataCounter] = new bool[cities];
		visited[dataCounter] = false;
		visitedCities[dataCounter] = finalVisitedCities[dataCounter] = 0;
		stack = 0;
		mylist.push_front(cities - dataCounter);
	}
	for (int dataCounter = 0; dataCounter < cities; dataCounter++) 
		for (int dataCounter2 = 0; dataCounter2 < cities; dataCounter2++) {
			map[dataCounter][dataCounter2] = -1;
			visitedMap[dataCounter][dataCounter2] = false;
		}
}

Salesman::~Salesman() {
	for (int dataCounter = 0; dataCounter < citiesAmount; dataCounter++) {
		delete[] map[dataCounter];
		delete[] visitedMap[dataCounter];
	}

	delete[] visitedMap;
	delete[] map;
	delete[] visited;
	delete[] visitedCities;
	delete[] finalVisitedCities;
}

void Salesman::randomSymmetricMap() {
	for (int dataCounter = 0; dataCounter < citiesAmount; dataCounter++)
		for (int dataCounter2 = dataCounter + 1; dataCounter2 < citiesAmount; dataCounter2++)
			map[dataCounter][dataCounter2] = map[dataCounter2][dataCounter] = rand() % 100 + 1;
}

void Salesman::randomUnsymmetricMap() {
	for (int dataCounter = 0; dataCounter < citiesAmount; dataCounter++)
		for (int dataCounter2 = 0; dataCounter2 < citiesAmount; dataCounter2++)
			if (dataCounter != dataCounter2)
				map[dataCounter][dataCounter2] = rand() % 100 + 1;
}

double Salesman::tsp() {
	czasStartSalesman();
	cout << dynamicProgramming(citiesAmount, mylist)<<endl;

	list<int> finalList;
	while(!listResult.empty()) {
		finalList.push_front(listResult.front() - 1);
		listResult.pop_front();
	}
	finalList.push_front(0);

	std::list<int>::iterator iter;
	for (iter = finalList.begin(); iter != finalList.end(); ++iter)
		cout << *iter << " ";

	dynamicTime = pobierzCzasSalesman();
	cout << dynamicTime;
	return dynamicTime;
}

int Salesman::dynamicProgramming(int cities, std::list<int> list) {
	std::list<int>::iterator i;
	int minValue;

	if (cities == 1) {
		i = list.begin();
		minValue = map[*i - 1][0];  
	}
	else {
		int temp;
		i = list.begin();
		temp = *i;
		list.erase(i);
		i = list.begin();
		minValue = map[temp - 1][*(i) - 1] + dynamicProgramming(cities - 1, list);
		for (int j = 2; j<cities; j++) {
			i = list.end();
			i--;
			int tempvalue = *i;
			list.erase(i);
			list.push_front(tempvalue);
			i = list.begin();
			tempvalue = map[temp - 1][*(i) - 1] + dynamicProgramming(cities - 1, list);
			if (tempvalue<minValue) {
				minValue = tempvalue;
			}
		}
	}  

	return minValue;
}

/*int Salesman::dynamicProgramming(int cities, std::list<int> list) {
	std::list<int>::iterator i;
	int minValue;

	if (cities == 1) {
		i = list.begin();
		minValue = map[*i - 1][0];
		if (list.size() == citiesAmount - 1) {
			listResult = list;
			lista.push_front(cities);
		}	  
	}
	else {
		int temp;
		i = list.begin();
		temp = *i;
		list.erase(i);
		i = list.begin();
		minValue = map[temp - 1][*(i) - 1] + dynamicProgramming(cities - 1, list);
		if (list.size() == citiesAmount - 1) {
			listResult = list;
			lista.push_front(cities);
		}
		for (int j = 2; j<cities; j++) {
			i = list.begin();
			for (int k = 1; k<j; k++) {
				i++;
			}
			int tempvalue = *i;
			list.erase(i);
			list.push_front(tempvalue);
			i = list.begin();
			tempvalue = map[temp - 1][*(i) - 1] + dynamicProgramming(cities - 1, list);
			if (tempvalue<minValue) {
				if (list.size() == citiesAmount - 1) {
					listResult = list;
					lista.push_front(cities);
				}
				minValue = tempvalue;
			}

		}
	}  

	return minValue;
}
*/














































void Salesman::travelGreed() {
	int minCost;
	int data;
	finalCost = INT_MAX;

	czasStartSalesman();

	for (int dataCounter5 = 0; dataCounter5 < citiesAmount; dataCounter5++) {
		int counter = dataCounter5;
		visitedAmount = 0;
		totalCost = 0;

		for (int dataCounter3 = 0; dataCounter3 < citiesAmount; dataCounter3++) {
			visited[dataCounter3] = false;
			for (int dataCounter4 = 0; dataCounter4 < citiesAmount; dataCounter4++) visitedMap[dataCounter3][dataCounter4] = false;
		}
		for (int dataCounter = 0; dataCounter < citiesAmount; dataCounter++) visitedMap[dataCounter][dataCounter] = true;

		while (visitedAmount < citiesAmount) {
			minCost = INT_MAX;
			if (visitedAmount == citiesAmount - 1) {
				totalCost += map[counter][dataCounter5];
				//cout << map[data][dataCounter5] << endl;
				break;
			}
			for (int dataCounter = 0; dataCounter < citiesAmount; dataCounter++)
				if (map[counter][dataCounter] > 0 && map[counter][dataCounter] < minCost && !visitedMap[counter][dataCounter] && !visited[dataCounter]) {
					minCost = map[counter][dataCounter];
					data = dataCounter;
				}
			visitedMap[counter][data] = visitedMap[data][counter] = true;
			totalCost += minCost;
			//cout << minCost << endl;
			visited[counter] = true;
			visitedCities[visitedAmount] = counter;
			visitedAmount++;
			counter = data;
		}
		//cout << totalCost << "---"<<endl;
		if (finalCost > totalCost) {
			finalCost = totalCost;
			for (int dataCounter = 0; dataCounter < citiesAmount; dataCounter++) finalVisitedCities[dataCounter] = visitedCities[dataCounter];
			finalVisitedCities[citiesAmount - 1] = counter;
		}
		//	for (int dataCounter = 0; dataCounter < citiesAmount; dataCounter++) cout << visitedCities[dataCounter] << " ";
		//	cout << endl;
	}

	greedTime = pobierzCzasSalesman();
	cout << greedTime << endl;

	cout << "Odwiedzone miasta: ";
	for (int dataCounter = 0; dataCounter < citiesAmount; dataCounter++) cout << finalVisitedCities[dataCounter] << " ";
	cout << endl << "Calkowity koszt: " << finalCost;
}

void Salesman::travelBruteforce() {
	int *permTab = new int[citiesAmount];
	int data = 0;
	finalCost = INT_MAX;

	czasStartSalesman();

	for (int dataCounter = 0; dataCounter < citiesAmount; dataCounter++) permTab[dataCounter] = dataCounter;

	permutation(permTab, data);

	bruteforceTime = pobierzCzasSalesman();
	cout << bruteforceTime << endl;

	cout << "Odwiedzone miasta: ";
	for (int dataCounter = 0; dataCounter < citiesAmount; dataCounter++) cout << finalVisitedCities[dataCounter] << " ";
	cout << endl << "Calkowity koszt: " << finalCost;
}

inline void swap(int &a, int &b) {
	int c = a; a = b; b = c;
}

void Salesman::permutation(int permTab[], int data) {
	if (data < citiesAmount - 1)
		for (int dataCounter = data; dataCounter < citiesAmount; dataCounter++) {
			swap(permTab[data], permTab[dataCounter]);
			permutation(permTab, data + 1);
			swap(permTab[data], permTab[dataCounter]);
		}
	else {
		int *tempTab = new int[citiesAmount];
		int counter = permTab[0];
		int visitedAmount = 0;
		int totalCost = 0;
		int data = permTab[1];
		int tabCounter = 2;

		for (int dataCounter2 = 0; dataCounter2 < citiesAmount; dataCounter2++) tempTab[dataCounter2] = permTab[dataCounter2];

		while (visitedAmount < citiesAmount) {
			if (visitedAmount == citiesAmount - 1) {
				totalCost += map[permTab[citiesAmount - 1]][permTab[0]];
				//cout << map[permTab[citiesAmount - 1]][permTab[0]] << endl;
				break;
			}
			totalCost += map[counter][data];
			//cout<<map[counter][data]<<" ";
			counter = data;
			data = permTab[tabCounter];
			tabCounter++;
			visitedAmount++;
		}
		//cout << totalCost << endl;
		if (finalCost > totalCost) {
			finalCost = totalCost;
			for (int dataCounter = 0; dataCounter < citiesAmount; dataCounter++) finalVisitedCities[dataCounter] = permTab[dataCounter];
		}
		delete[] tempTab;
	}
}
