#include <iostream>
#include <stdlib.h>

using namespace std;

int product(int n);
int replacement(int n);

int main(int argc, char **argv)
{
	int n=atoi(argv[1]),amount;
	
	amount=product(n);
	replacement(amount);
}

int product(int n)
{
	int i,k,a=0,licznik=0;
		
	for (k=1;k<1000000;k++)
		{
			i=n*k;
			while (i>0)
				{
					if (i%10==1 || i%10==0) a++;
					i=i/10;
					licznik++;
				}
			if (licznik==a) return k;
			a=0;
			licznik=0;
		}	
}

int replacement(int n)
{
	int rest;
	string word[6] = {"","","","","",""};
	string tablica[5][10] = {"","jeden ","dwa ","trzy ","cztery ","piec ","szesc ","siedem ","osiem ","dziewiec ",
							"dziesiec ","jedenascie ","dwanascie ","trzynascie ","czternascie ","pietnascie ","szesnascie ","siedemnascie ","osiemnascie ","dziewietnascie ",
							"","","dwadziescia ","trzydziesci ","czterdziesci ","piecdziesiat ","szescdziesiat ","siedemdziesiat ","osiemdziesiat ","dziewiecdziesiat ",
							"","sto ","dwiescie ","trzysta ","czterysta ","piecset ","szescset ","siedemset ","osiemset ","dziewiecset ",
							"tysiecy ","jeden tysiecy ","dwa tysiace ","trzy tysiace ","cztery tysiace ","piec tysiecy ","szesc tysiecy ","siedem tysiecy ","osiem tysiecy ","dziewiec tysiecy "};
					
	
	if	(n>0&&n<1000000)
	{
		if (n%100<20)
			{
				rest=n%100;
				word[0]=tablica[1][rest-10];		//przypisuje od 0 do 19
				n=n/10;
			}
		else
			{
				rest=n%10;	
				word[0]=tablica[0][rest];			//przypisuje od 0 do 9 jesli przedostatnia liczba jest 0
				n=n/10;
				rest=n%10;
			}
		
		rest=n%10;
		word[1]=tablica[2][rest];					//przypisuje dziesiatki albo nic jesli przedostatnia liczba jest 0 lub 1
	
		n=n/10;
		rest=n%10;	
		word[2]=tablica[3][rest];					//przypisuje setki
	
		n=n/10;
		if (n%100<20 && n%100>=10)
			{
				rest=n%100;
				word[3]=tablica[1][rest-10]+"tysiecy ";			//przypisuje nastki tysiecy
			}
		else
			{
				rest=n%10;	
				if (n!=0)	word[3]=tablica[4][rest];			//przypisuje jednosci tysiecy a warunek nie pozwala wkraœæ siê tab[4][0] (tysiecy) przy liczbach 1-3cyfrowych
			}
		
		n=n/10;
		rest=n%10;
		word[4]=tablica[2][rest];					//przypisuje dziesiatki tysiecy lub nic jesli dziesiatki sa rowne 0 lub 1  
	
		n=n/10;
		rest=n%10;
		if (rest!=0) word[5]=tablica[3][rest];					//przypisuje setki tysiecy
		if (word[4]=="" && word[5]=="" && word[3]=="jeden tysiecy ")	word[3]="tysiac ";			// w przypadku 4 cyfrowej liczby zamienia "jeden tysiac..." na "tysiac"
			
		for (int i=5;i>=0;i--)	cout<<word[i];		//wyswietla liczbe w postaci pisemnej
	}
	else
		cout<<"Zly zakres, wprowadz liczbe od 0 do 1000000 wylacznie";
}
