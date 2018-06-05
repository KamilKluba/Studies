#Lab1
##Kwadrat znaków
Wynikiem pracy dzisiejszego programu ma być generator kwadratów, zawierających losowe znaki, wykorzystujący tak zwane *seed value*.

###Ocena dostateczna
- uzupełnij niniejszy kod, w funkcji `charSquare` tworząc, a w funkcji `drawCharSquare` wyświetlając dwuwymiarową, dynamicznie alokowaną tablicę znaków,
- wielkość kwadratu pobieraj z parametru wiersza poleceń,
- znaki w tablicy mają być generowane losowo, przy użyciu funkcji `rand()`,
- nie możesz modyfikować prototypów funkcji.

###Ocena dobra
- wprowadź drugi parametr wiersza poleceń, będący *seed value*, wykorzystywanym w funkcji [`srand()`](http://www.cplusplus.com/reference/cstdlib/srand/).

###Ocena bardzo dobra
- tak wypełnianie, jak wyświetlanie tablicy `square` zrealizuj przy użyciu pojedynczej pętli `for`, ograniczając jej ciało do jednej instrukcji.

##Poprawny wynik pracy programu
Poprawnym wynikiem pracy programu, po wykonaniu pliku `Makefile`, jest poniższy rezultat. Utworzone kwadraty mogą być lustrzanymi odbiciami zaprezentowanych. Dla rozwiązania na ocenę dostateczną, wartości mogą być inne.

	g++ main.cpp -o lab1
	./lab1 1 666
	m
	./lab1 2 646
	g g
	w w
	./lab1 3 123
	d j f
	s k a
	c c l
	./lab1 4 1234
	l f n r
	d e u d
	e p i c
	d v q n
