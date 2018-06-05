#pragma once

#include <string>
#include <vector>

using namespace std;


struct weaponParameters
{
	//	obrazenia	penetracja pancerza		szanse na trafienie 
	int weapDamage, weapPenetration, weapChance;
};

struct armorParameters
{
	//	hełm		zbroja		tarcza	nogawice	szansa na blok ciosu
	int armHelmet, armArmor, armShield, armLegs, armShieldBlock; 
};

class Slav
{
	static vector <string> names;

	string _name;
	int _hp, _baseHp;
//	int _mana, baseMana;
	int _lvl;
	float _exp;
	int _ultimateSkill, _charge;
	int _weapon;
	string _weaponName, _ultimateName;

	weaponParameters _weapPar, _baseWeapPar;
	armorParameters _armPar, _baseArmPar;

	static void init();

public:
	Slav();
//	~Slav();

	int hp() 				{ return _hp; }
	int baseHp() 			{return _baseHp; }
	string name()			{ return _name; }
	int ultimateSkill()		{ return _ultimateSkill; }
	int Lvl()				{ return _lvl; }

	//bronie
	void Weapon();
	weaponParameters weapParameters()	{ return _weapPar; }
	string WeaponName() 				{ return _weaponName; }
	string UltimateName() 				{ return _ultimateName; }

	//pancerz
	void Armor();
	armorParameters armParameters()		{ return _armPar; }

	//umiejetnosci
	void SlowianskiPrzykuc();
	void KrakowskaMaczetaRozboju();
	void Cwiara();
	void KlatwaPeruna();
	void Kamehameha();

	int charge();
	bool damage(int value);
	void heal();
	double beastRatio();
	void lvlUp(double _ratio);
	//int beastExp();
	
	string description();
};
