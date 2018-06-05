#include <iostream>
#include <stdio.h>
#include <assert.h>
#include <ctime>

using namespace std;

//int gcd (int a, int b);

struct fraction
{
	int nominator, denominator;
	
	bool is_correct (int nominator, int denominator)
	{
		if (denominator!=0 && abs(nominator) < abs(denominator));
			return true;
		return false;		
	}

};

int main(int argc, char **argv)
{
	int n;

	n=atoi(argv[1]);
	fraction *fractions = new fraction[n];
	srand(time(NULL));

	for (int j=0; j<n; j++)
	{
			do
			{
				(fractions + j)-> nominator = rand() % 19 - 9;
				(fractions + j)-> denominator = rand() % 19 - 9;
			} 
			while (fractions[j].denominator==0 || abs(fractions[j].nominator) > abs(fractions[j].denominator));
	}

	
	for (int i = 0; i < n; assert(fractions[i].is_correct(fractions[i].nominator, fractions[i].denominator)))
		{
			printf("[%i] %2i / %2i\n",i,fractions[i].nominator,fractions[i].denominator);
			i++;
		}
	delete[] fractions;
}
