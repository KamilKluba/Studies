#include "Slav.h"
#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <unistd.h>
#include <iostream>
#include <queue>

#define HIT_FORCE 50
#define HIT_INTERVAL 1000000
/*
**********************************************************************
program cały czas w trakcie pisania, ciągle szukam nowych pomysłów
**********************************************************************
*/


int main(int argc, char const *argv[])
{


	srand(time(NULL));
	//argc == 2 
	//int count = atoi(argv[1]) / 2;  
	int count = 11;
	if (count%2 != 0) count -= 1;
	//tworze okresloną PARZYSTĄ ilość Slowian
	Slav slavs[count];

	int amount=0;
	//wrzucam wszystkich Slowian do kolejki
	queue <Slav> queueOfSlavs;
	while (amount < count)
	{
		queueOfSlavs.push(slavs[amount]);
		amount++;
	}

	//prezentacja wszystkich Slowian
	for (int i=0 ; i< count ; i++)	cout<<slavs[i].description()<<endl;

	int j=1;
	string winner;
	//rżną się aż zostanie tylko jeden
	while (queueOfSlavs.size() > 1)
	{
		//wyswietlam ktora to walka
		cout<<endl<<"WALKA "<<j<<endl;
		j++;

		//wyciagam dwóch Słowian z koeljki
		for (int i = 0; i < 2; ++i)
		{
			slavs[i] = queueOfSlavs.front();
			queueOfSlavs.pop();
			printf("%s\n", slavs[i].description().c_str());
		}

		usleep(HIT_INTERVAL);

		srand(time(NULL));
		//tablica wszystkich miejsc w ktore mozna uderzyc
		string target[4] = {"w głowę", "w tułów", "po tarczy", "w nogi"};
		while(1)
		{
			usleep(HIT_INTERVAL);
			// Kto uderza?
			double attack[2];
			for (int i = 0; i < 2; ++i)
				attack[i] = (rand() % 100) < slavs[i].weapParameters().weapChance
					? (slavs[i].weapParameters().weapDamage) / 100. * HIT_FORCE
					: 0;



			int whoHits = (int)(attack[1] > attack[0]);

			// Jak mocno?
			int damage = attack[whoHits] - attack[1 - whoHits] / 2;
 
			// Obrona
			int _block = rand() % 100;
			//proba sparowania ciosu tarczą
			if (slavs[1-whoHits].armParameters().armShieldBlock > _block)
			{
				cout<<" ["<<slavs[0].hp()<<", "<<slavs[1].hp()<<"] "<<
						slavs[1-whoHits].name()<<" paruje cios!"<<endl;	
			}
			else
			{
				//w zaleznosci od tego gdzie obiekt uderzy i od penetracji pancerza 
				//atakującego inaczej przeliczane są obrazenia
				int _target = rand() % 101;
				int _target2;
				if 		(_target < 15) {		 
					damage = damage * (100 - slavs[whoHits].weapParameters().weapPenetration 
							 * slavs[1-whoHits].armParameters().armHelmet / 100) / 100;
					_target2 = 0;	}
				else if (_target < 50) {
					damage = damage * (100 - slavs[whoHits].weapParameters().weapPenetration
							 * slavs[1-whoHits].armParameters().armArmor / 100) / 100;
					_target2 = 1;	}
				else if (_target < 75) {	 
					damage = damage * (100 - slavs[whoHits].weapParameters().weapPenetration
							 * slavs[1-whoHits].armParameters().armShield / 100) / 100;
					_target2 = 2;	}
				else {
					 damage = damage * (100 - slavs[whoHits].weapParameters().weapPenetration
							 * slavs[1-whoHits].armParameters().armLegs / 100) / 100;
					 _target2 = 3;	}


				// Uderz
				bool isLethal = slavs[1 - whoHits].damage(damage);

				printf(" [%2i, %2i] uderza %s %s z siłą %i\n", 
					slavs[0].hp(), slavs[1].hp(), 
					slavs[whoHits].name().c_str(),
					target[_target2].c_str(),
					damage);

				//sprawdzenie warunków i uzycie superumiejetnosci
				for(int i=0;i<2;i++)
				{
					if ((slavs[i].hp() <= 20 || slavs[i].hp() < slavs[i].baseHp() / 5)
						 && slavs[i].charge() > 0)
						switch (slavs[i].ultimateSkill())
						{
							case 1:	slavs[i].SlowianskiPrzykuc();
							break;
							case 2:	slavs[i].KrakowskaMaczetaRozboju();
							break;
							case 3: slavs[i].Cwiara();
							break;
							case 4:	slavs[1-i].KlatwaPeruna();
							break;
							case 5: slavs[1-i].Kamehameha();

							default:
							break;
						}
				}	

				if (isLethal)
				{
					printf("%s wygrywa\n", slavs[whoHits].name().c_str());
					winner = slavs[whoHits].name();
					if (slavs[whoHits].WeaponRatio() > slavs[1-whoHits].WeaponRatio())
						slavs[whoHits].GetWeapon(slavs[1-whoHits].weapParameters2(), slavs[1-whoHits].WeaponName());
					slavs[whoHits].lvlUp(slavs[1-whoHits].beastRatio());
					slavs[whoHits].heal();
					queueOfSlavs.push(slavs[whoHits]);
					break;
				}	
			}
		}

	}


	cout<<endl<<"ZWYCIEZCĄ TURNIEJU ZOSTAJE: "<<winner<<endl<<endl;
} 	