#include "Slav.h"
#include <stdlib.h>
#include <fstream>
#include <iterator>
#include <iostream>
#include <unistd.h>

#define HIT_INTERVAL 1000000

//tablica nazw wszystkich superumiejetnosci
string skills[5]={"Slowianski przykuc", "Krakowska maczeta rozboju", "Ćwiartka ognistego napoju", "Klątwa Peruna", "Kamehameha"};

vector <string> Slav::names;

void Slav::init()
{
	ifstream file("names.dat");
	copy(istream_iterator<string>(file),
		istream_iterator<string>(),
		back_inserter(names));
	file.close();
}

int randomValue()
{
	return 25 + rand() % 65;
}

Slav::Slav()
{
	static int amountOfNames = (init(), names.size());
	_name = names[rand() % amountOfNames];
	_hp = _baseHp = randomValue() + 60;
//	_mana = baseMana = randomValue();
	_exp = 0;
	_ultimateSkill = rand() %5 +1;
	_charge = 1;
	_lvl = 1;
	_weapon = rand() %3 +1;
	Weapon();
	_armPar = _baseArmPar = { rand() % 20, rand() % 50,
			  rand() % 30 + 55, rand() % 70, rand() %10 + 5};
}

string Slav::description()
{
	return _name + "\n\tHP: " + to_string(_hp) + " Lvl: " + to_string(Lvl()) + 
		" BR: " + to_string(beastRatio()) +
		"\n\tUltimate Skill: " + skills[_ultimateSkill-1] + 
		"\n\tWeapon: " + WeaponName() + 
		" DMG: " + to_string(_weapPar.weapDamage) +  
		" PEN: " + to_string(_weapPar.weapPenetration) + 
		" ACH: " + to_string(_weapPar.weapChance) + 
		"\n\tArmor : HEL: " + to_string(_armPar.armHelmet) + 
		" ARM: " + to_string(_armPar.armArmor) + 
		" SHI: " + to_string(_armPar.armShield) + 
		" LEG: " + to_string(_armPar.armLegs);
}

bool Slav::damage(int value)
{
	_hp -= value;
	return _hp <= 0;
}

//przywaracam wszystkie parametry po wygranej walce
void Slav::heal()
{
	_hp = _baseHp;
	_charge = 1;
	_armPar = _baseArmPar;
	_weapPar = _baseWeapPar;
}

double Slav::beastRatio()
{
	double parametersRatio = 
		((_baseHp + _weapPar.weapDamage) * (_armPar.armHelmet +
		 _armPar.armArmor + 4 * _armPar.armShield +
		_armPar.armLegs + _armPar.armShieldBlock + 
		_weapPar.weapPenetration)) * _weapPar.weapChance / 500000.;
	return  parametersRatio;
}

//awans postaci na wyższy lvl
void Slav::lvlUp(double _ratio)
{
	_exp += _ratio;
	while (_exp >= 10 * _lvl)
	{
		_baseHp += 30;
		_weapPar.weapDamage = _baseWeapPar.weapDamage = _baseWeapPar.weapDamage + 10;
		_exp -= 10 * _lvl;
		_lvl++;
		cout<<name()<<" awansuje na poziom: "<<_lvl<<endl;
	}
}

//zabieranie ładunku superumiejętności
int Slav::charge()
{
	_charge--;
	return _charge+1;
}

//działanie wszystkich superumiejętności
void Slav::SlowianskiPrzykuc()
{
	cout<<name()<<" uzywa umiejetnosci Slowianski przykuc!"<<endl;
	_armPar.armShieldBlock = 50;
	usleep(HIT_INTERVAL);
}

void Slav::KrakowskaMaczetaRozboju()
{
	cout<<name()<<" uzywa Krakowskiej maczety rozboju!"<<endl;	
	_weapPar.weapDamage += 30;
	_weapPar.weapPenetration += 20;
	_weapPar.weapChance += 10;
	usleep(HIT_INTERVAL);
}

void Slav::Cwiara()
{
	cout<<name()<<" wypija ćwiare ognistego napoju!"<<endl;
	_hp = _hp + 60 + 30*_lvl;
	usleep(HIT_INTERVAL);
}

void Slav::KlatwaPeruna()
{
	cout<<name()<<" zostaje przeklety przez Peruna!"<<endl;
	_armPar.armHelmet -= 10;
	_armPar.armArmor -= 10;
	_armPar.armShield -= 10;
	_armPar.armLegs -= 10;
	_armPar.armShield -= 10;
	_weapPar.weapDamage -= 10;
	_weapPar.weapPenetration -= 10;
	_weapPar.weapChance -= 10;
	usleep(HIT_INTERVAL);
}

void Slav::Kamehameha()
{
	cout<<name()<<" zostaje uderzony za pomocą Kamehameha!"<<endl;
	_hp = _hp - (40 + 30 * _lvl) ;
}

//przydzielanie obiektowi broni
void Slav::Weapon()
{
	switch (_weapon)
	{
		//miecz
		case 1:
		_weapPar = _baseWeapPar = {rand() % 21 + 40, rand() % 16 + 50, rand() % 31 + 60};
		_weaponName = "Miecz";
		break;
		//Topór
		case 2:
		_weapPar = _baseWeapPar = {rand() %31 + 60, rand() % 31 + 55, rand() % 31 + 40};
		_weaponName = "Topór";
		break;
		//Mlot
		case 3:
		_weapPar = _baseWeapPar = {rand() % 31 + 70, rand() % 21 + 70, rand() % 16 + 35};
		_weaponName = "Młot bojowy";
	}
}
