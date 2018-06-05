#include <iostream>
#include <stdlib.h>
#include <time.h>

using namespace std;

char **charSquare(int n);
void drawCharSquare(char **square, int n);

int main(int argc, char **argv)
{
	int n,seed;

	n=atoi(argv[1]);
	seed=atoi(argv[2]);
	
	srand(seed);
	drawCharSquare(charSquare(n), n);	
}

char **charSquare(int n)
{
	char **square;
	square = new char *[n];
	
	for ( int i = 0; i < n; ++i )
 		{
 			square[i] = new char [n];
 			for ( int j = 0; j < n; ++j)
 				square[i][j]=rand();
 		} 
	
	return square;
}

void drawCharSquare(char **square, int n)
{
	for ( int i = 0; i < n; ++i, cout<<endl )
 		for ( int j = 0; j < n; ++j)
 			cout<<square[i][j]<<'\t'; 

}
