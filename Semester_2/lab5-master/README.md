# Najbliżsi sąsiedzi

## Klasa `Sample`
- Stwórz klasę `Sample`, zawierającą:
    - zmienną całkowitą `label`,
    - wektor liczb zmiennoprzecinkowych `features`,
- w konstruktorze klasy `Sample` przyjmuj oba te parametry,
- stwórz też dla nich *gettery*.

## Klasa `Dataset`
- Stwórz klasę `Dataset`, zawierającą:
    - wektor obiektów klasy `Sample`,
    - metodę `push_back()`, dodającą nową próbkę do wektora:
        - metoda ta powinna sprawdzać, czy wektor `features` dla każdej z wprowadzanych próbek jest tej samej długości.

## Klasa `Prediction`
- Stwórz klasę `Prediction`, dziedziczącą po klasie `Sample`, dodającą do niej zmienną `prediction`,
- Rozszerz ją o metodę statyczną `accuracy()`, przyjmującą wektor obiektów klasy `Prediction`, informującą o tym, w jakim odsetku ich zmienne `label` i `prediction` mają tę samą wartość.

## Program obliczeniowy
- Jako argumenty wiersza poleceń programu, przekaż nazwę pliku oraz liczbę całkowitą `k`,
- dla celów testowych, w repozytorium znajduje się plik `wine.csv`,
- wczytaj plik `wine.csv` do wektora obiektów typu `Sample` tak, aby pierwsza jego kolumna określała `label`, a pozostałe znalazły się w wektorze `features`,
- stwórz dwa obiekty klasy `Dataset`. Pierwszy nazwij `train`, drugi `test`,
- do `train` wprowadź losowe 20% wczytanych z pliku obiektów, a do `test`, resztę,
- zaimplementuj funkcję `knn()`, zwracającą wektor obiektów klasy `Prediction` przyjmującą jako argumenty dwa obiekty typu `Dataset` (`train` i `test`) i zmienną całkowitą `k`:
    - utwórz w niej wektor `predictions`:
    - do utworzonego wektora wprowadź kopie wszystkich obiektów (mają typ `Sample`) znajdujących się w `test`, uzupełniając dla nich wartość `prediction`, według następującego schematu:

>   Dla każdego obiektu ze zbioru testowego znajdź `k` obiektów ze zbioru uczącego, którego cechy (`features`) znajdują się w najmniejszej od niego [odległości](https://pl.wikipedia.org/wiki/Odległość). Za `prediction` uznaj `label`, który powtarza się najczęściej wśród znalezionych.
>   

- Wyświetl użytkownikowi informacje o zbiorze (nazwa pliku, liczba próbek, liczba cech) oraz jakość klasyfikacji (`accuracy()`).
