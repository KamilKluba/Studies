#Lab 3 - Kontenery i adaptery

## Zadania
Na dzisiejszy kod początkowy składa się klasa **Slav** i częściowo uzupełniony już plik `main.cpp`. Klasa **Slav** opisuje abstrakcję Słowianina/Słowianki, w konstruktorze domyślnym przypisując mu imię (dostępne za pośrednictwem funkcji `name()`) i unikalny numer identyfikacyjny (`id()`). Ma ona również funkcję `description()`, która pozwoli nam łatwo wykorzystywać `cout` do wyświetlania informacji o obiektach tej klasy.

Funkcja `main()` pobiera z wiersza poleceń liczbę `n` i tworzy dynamiczną `n * 2` elementową tablicę obiektów klasy **Slav**. Czyli po prostu parzystą liczbę Słowian.

Państwa zadaniem będzie użycie utworzonych w ten sposób obiektów do *zaludniania* poszczególnych *kontenerów* i *adapterów*. Są one już zadeklarowane, na początku funkcji `containers()` i `adapters()`. Proszę zwrócić uwagę, że w każdym z nich przechowujemy jedynie wskaźniki na obiekty, a nie ich kopie. I właśnie na wskaźnikach będziemy operować.

Po każdej zadanej operacji (w komentarzach są wprost zaznaczone miejsca, w których należy je implementować) wywołujemy makra do raportowania liczby elementów w każdym z kontenerów i liczby obiektów klasy **Slav**, rezydujących w pamięci. Efekt ich pracy wygląda jak poniżej.

	[vector_size = 0, set_size = 0, map_size = 0, existingSlavs = 6]
	
Aby zadanie było wykonane poprawnie, ostatnia z wartości (`existingSlavs`) nie może zmienić się przez cały czas pracy programu, a makro to pozwala Państwu na łatwą weryfikację tego, czy kolejny krok implementacji został wykonany dobrze.

Powodzenia!

### Zadanie na 3
Wewnątrz funkcji `containers()`:

- Umieść **Słowian** w losowej kolejności w wektorze,
- Wykorzystując iterator i funkcję `description()`, wyświetl wszystkich **Słowian** w wektorze,
- Przenieś wszystkich **Słowian** z wektoru do zbioru,
- Stwórz słownik tworzący pary **Słowian**, z tych znajdujących się w zbiorze, czyszcząc zbiór,
- Wykorzystując iterator, wyświetl wszystkie pary **Słowian**.

### Zadanie na 4
Wewnątrz funkcji `adapters()`:

- Umieść **Słowian** w kolejce.
- Przenieś **Słowian** z kolejki do stosu.
- Wyświetl **Słowian** zdejmowanych ze stosu.

### Zadanie na 5
Uzupełnij klasę `Slav` o metodę rozpoznającą płeć Słowianina/Słowianki, zwracając ją jako `enum`. Stwórz słownik (`map`) zwracający tablicę (`vector`) wskaźników na wszystkich mężczyzn, gdy podamy mu klucz odpowiadający mężczyźnie i tablicę wszystkich kobiet, gdy podamy mu klucz odpowiadający kobiecie.

### Zadanie na 5.5
Wyjaśnij, co dzieje się w `21` linijce pliku `Slav.cpp`. Jak nazywa się ten proces?

## Dokumentacja
- [Dokumentacja biblioteki STL](http://www.cplusplus.com/reference/stl/)
