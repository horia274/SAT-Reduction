# SAT-Reduction

## Precizari

Implementarea propriu-zisa a acestui proiect este reprezentata de sursele *java*,
cele de *python* reprezinta checker-ul care valideaza corectitudinea solutiilor
fiecarui task.

Ne propunem sa reducem cateva probleme din clasa **NP-complete** la problema **SAT**.
Mai multe delatii despre enunt se gasesc [aici](https://curs.upb.ro/pluginfile.php/508188/mod_resource/content/1/AA___Tema_2__Copy_%20%284%29.pdf).


## Cadru general

In fiecare task este vorba despre un numar de familii mafioate care se pot
cunoaste intre ele sau nu. Practic este vorba de un graf, unde fiecare varf
reprezinta o familie, iar muchia dintre doua varfuri sugereaza ca cele doua
familii se cunosc.


## Task1

### Identificarea problemei K coloring

Spionii care trebuie introdusi in fiecare familie, pot fi asociati unei **culori**.
Astfel, fiecare nod din graf va trebui sa fie colorat cu o singura culoare, in
asa fel incat doua varfuri care sunt legate de o muchie sa aiba culori diferite
(la familiile care se cunosc intre ele exista spioni diferiti). Asadar, problema
pe care vrem sa o reducem SAT este **K coloring**.

### Modalitatea de reducere

Acum ca am recunoscut problema K coloring, voi explica cum se reduce aceasta la
problema SAT.

Fie n, numarul de familii (varfuri) si k numarul de spioni (culori). Consider
variabile boolene de tipul **FiSj**, pentru orice i de la 1 la n si orice j de
la 1 la k. Variabila FiSj va fi setata pe *true*, daca familia i este spionata de
spionul j, altfel va avea valoarea *false*.

#### Necesitati - corespondenta in clauze:

* fiecare familie trebuie sa aiba un spion - pentru orice familiei i, macar una
din variabilele FiS1, FiS2, ..., FiSk este *true* - obtinem **clauze de tipul 1**
*FiS1 v FiS2 v ... v FiSk*, pentru orice familie i;

* fiecare familie trebuie sa aiba exact un spion, cu alte cuvinte nu exista doi
spioni care sa spioneze aceeasi familie - pentru orice familie i si orice spioni
diferiti j != l, valoarea !(FiSj ^ FiSl) trebuie sa fie *true* - folosind regula
lui De Morgen, obtinem **clauze de tipul 2**
*!FiSj v !FiSl*, pentru orice familie i si oricare doi spioni diferiti j si l;

* oricare doua familii care se cunosc, trebuie sa aiba spioni diferiti - doua
familii diferite, i != j nu pot avea acelasi spion l, deci valoarea expresiei
!(FiSl ^ FjSl) va fi *true* - se obtin ultimele **clauze**, cele de **tipul 3** si anume
*!FiSl v !FjSl*, pentru orice familii i si j intre care exista muchie si orice
spion l.

#### Scurta explicatie a corectitudinii

Din modul de constructie a clauzelor, daca problema K coloring intoarce *true*
pe un input introdus, toate clauzele vor fi adevarate, intrucat reprezinta exact
conditiile problemei, deci problema SAT va intoarce tot *true*. In aceeasi
maniera, daca problema SAT intoarce *true* cu clauzele formate, inseamna ca 
toate acestea sunt adevarate, de unde rezulta si ca problema K coloring este
adevarata, deci intoarce *true*.

### Encodarea / Decodarea variabilelor

Intrucat nu pot sa folosesc in cod variabilele de tipul FiSj, acestea trebuie
transformate *controlat* in numere mai mari sau egale ca 1, astfel, pentru a
*encoda* o variabila de tipul FiSj, folosesc formula k * (i - 1) + j (variabilele
FiSj formeaza o matrice cu n linii si k coloane; formula enuntata mai sus este de
fapt, al catelea element este FiSj din aceasta matrice, numarand de la stanga la
dreapta, de sus in jos).

Pentru a decoda variabilele (vrem sa stim i-ul si j-ul, adica indicele familiei
si indicele spionului) se face un mic calcul matematic si se obtine din nou o
astfel de formula.

### Raspunsul oracolului

Daca oracolul intoarce *False*, problema nu are solutii, iar daca nu, voi putea
afla ce spion va fi introdus in fiecare familie, prin decodificarea variabilelor
ce au valoarea *true* (sunt pozitive) date de oracol.

### Complexitate

#### Temporala

Aceasta este data de functia *formulateOracleQuestion()*, caci ea contine cele mai
multe for-uri imbricate. Astfel, complexitatea este de tipul *O(nk + nk^2 + mk)*,
unde n este numarul de varfuri (familii), k este numarul de culori (spioni), m
este numarul de muchii (relatii dintre familii). Astfel, cum numarul total de
muchii respecta relatia m <= n(n-1)/2 (acesta e numarul maxim de muchii dintr-un
graf cu n noduri) si presupunand ca avem input-uri cu k <= n (intrucat nu ne
intereseaza mai multi spioni decat familii), complexitatea este de **O(n^3)**,
n fiind numarul de familii.

#### Spatiala

In rezolvarea acestei probleme, sunt folositi doi vectori, unul de dimensiune n
si inca unul de dimensiune m * 2. Astfel, pentru un graf mai *gol*, cu putine
muchii, complexitatea va fi **O(n)**, iar pentru unul *incarcat*, cu mai multe
muchii, se obtine complexitatea de **O(m)**. Daca folosim relatia m <= n(n-1)/2,
se obtine o complexitate maxima de **O(n^2)**.


## Task 2

### Identificarea problemei K clique

Trebuie verificat daca exista un grup de k familii astfel incat oricare doua sa se
cunoasca. Intrucat relatia dintre doua familii este reprezentata la nivelul grafului
prin trasarea muchiei, problema este echivalenta cu **K clique**.

### Modalitatea de reducere

Acum ca am recunoscut problema K clique, voi explica cum se reduce aceasta la
problema SAT.

Pastrez aceeasi semnificatie pentru n (numarul de familii), iar k va fi acum
dimensiunea cliclii. Folosesc acum variabilele de tipul FiPj, ce reprezinta
valoarea de adevar a afirmatiei *familia i este pe pozitia j in clica* (considerand
o ordine a nodurilor / familiilor in clica)

#### Necesitati - corespondenta in clauze:

* fiecare pozitie din clica trebuie sa fie ocupata de o familie - pentru orice pozitie
i din clica, cel putin una din variabilele F1Pi, F2Pi, ..., FnPi este *true* - se
formeaza astfel **clauzele de tipul 1**
*F1Pi v F2Pi v ... v FnPi*, pentru orice pozitie i din clica;

* fiecare pozitie din clica trebuie sa contina o singura familie - nu exista doua
pozitii din clica j != l, care sa contina aceeasi familie i - **clauzele de tipul 2**
*!FiPj v !FiPl*, pentru orice familie i si oricare doua pozitii diferite, j si l;

* pentru oricare doua familii care nu se cunosc, acestea nu pot fi simultan in clica -
pentru orice familii i != j, care nu se cunosc si oricare pozitii din clica l, m
(nu neaprat diferite) se obtin **clauzele de tipul 3**
*!FiPl v !FjPm*, (i, j) nefiind muchie in graf.

#### Mica explicatie a corectitudinii

La fel ca in cazul precedent, corectitudinea rezulta din modul de constructie a
clauzelor. Astfel problema SAT intoarce *true*, daca si numai daca toate clauzele sunt
adevarate, daca si numai daca toate conditiile pentru K clique sunt indeplinite,
adica daca K clique intoarce *true*.

### Encodarea / Decodarea variabilelor

Formula de encodare pentru variabilele de tipul FiPj este exact ca in cazul anterior
si anume k * (i - 1) + j. Decodarea este tot in acelasi fel si anume, pentru o
variabila encodata *var*, i-ul este catul impartirii lui var la k (la care se adauga 1
dupa caz) si j-ul este restul.

### Raspunsul oracolului

Daca obtin *true*, ma intereseaza sa vad care sunt familiile care intra in clica.
In acest sens, pentru variabilele pozitive, le decodific pentru a afla indicele i
al familiei, pe care il salvez pentru a forma fisierul de output.

### Complexitate

#### Temporala

La fel ca la task-ul precedent, complexitatea temporala este data tot de functia
*formulateOracleQuestion()* si aceasta va fi de tipul *O(nk + nk^2 + n^2k^2)*,
unde n reprezinta numarul de varfuri (familii) si k reprezinta dimensiunea clicii.
Cum clica poate avea o dimensiune maxima egala cu numarul de varfuri, adica k <= n
si considerand ca input-urile respecta aceasta proprietate, se obine complexitatea
temporala egala cu **O(n^4)**, n fiind numarul de familii.

#### Spatiala

Din motive de eficienta a timpului, am folosit matricea de adiacenta a grafului.
Astfel, complexitatea spatiala este de **O(n^2)**, unde n reprezinta numarul de
familii


## Task 3

### Idee

Luand graful complementar, vreau sa gasesc clica de dimensiune maxima. Arestatii
vor fi familiile care **nu** se afla in clica. Ca explicatie, daca as face din nou
graful complementar (cel initial), dupa ce arestez / scot mafiotii din graf, raman
cu familii care nu se cunosc deloc intre ele (intrucat in graful complementar, ele
formeaza clica, adica se cunosc toti intre ei). Am nevoie de clica cea mai mare
posibila (dimensiune maxima), intrucat vreau sa elimin toate relatiile dintre aceste
familii.

### Folosire Task 2

Formez input pentru task-ul 2, adaugand doar, pe langa datele obtinute din input,
dimensiunea clicii k. Obtin apoi rezultatul dat de oracol pentru clica de 
dimensiune k, iar in cazul obtinerii unui raspuns afirmativ, executia se incheie,
afland ce variabile se afla in clica. Ulterior, cele care nu se afla vor fi arestati.

### Complexitate

#### Temporala

Fiecare reducere la task-ul 2 vine cu o complexitate de *O(n^2)* pentru construirea
inputului, n fiind numarul de familii. Ulterior se aplica metoda *solve()* a task-ului
2, avand complexitatea de *O(n^4)*, asa cum am demonstrat anterior. Aceasta metoda
se aplica pana cand se gaseste o clica de o anumita lungime, deci se aplica de maxim
n ori. Asadar complexitatea temporala totala va fi de **O(n^5)** si se obtine pentru
un graf complet.

#### Spatiala

Specific acestui task, este doar matricea de adiacenta folosita, care ocupa *O(n^2)*
spatiu. In schimb, la fiecare apel al functiei *solve()* a task-ului 2, se foloseste
inca *O(n^2)* memorie. Astfel ca se poate folosi maxim **O(n^3)** spatiu. 


## Bonus

### Identificarea problemei Vertex Cover

Vrem sa identificam familiile (numarul minim de familii) pe care, daca le scoatem din
graf sa nu mai exista relatii intre familiile ramase. Cu alte cuvinte, daca acoperim
o parte din varfurile (familiile) din graf, toate muchiile sa fie acoperite (in
sensul ca atunci cand eliminam muchiile acoperite, nu mai ramane niciuna). Muchie
acoperita inseamna ca macar un nod este acoperit. Astfel, arestam familiile care
reprezinta raspunsul problemei **Vertex Cover**.

### Encodare / Decodare

In cadrul acestui task, nici macar nu este nevoie de o encodare, intrucat variabilele
folosite sunt varfurile din graf / indicii familiilor. Astfel, pentru explicatii,
folosesc variabile de tipul Fi care au valoarea *true* daca si numai data familia i
este acoperita.

### Modalitatea de reducere

#### Necesitati - corespondenta in clauze:

* pentru fiecare muchie, cel putin unul din fiecare varf ce formeaza aceasta muchie
trebuie acoperit - **clauzele de tipul 1, cele obligatorii**, sunt
*Fi v Fj*, cu pondere de *sumOfWeights* + 1;

* pentru fiecare varf, se introduce o **clauza de tipul 2, soft**, in sensul ca acesta
poate sa nu fie acoperit, asignandu-se un *weight* egal cu 1 (pentru simplitatea
calculului) - se obtin clauze de tipul
*!Fi*, cu pondere de 1.

### Raspunsul oracolului

Oracolul va afisa variabilele pozitive, cele care au valoarea *true*, adica cele care
trebuie acoperite pentru a acoperi tot graful. Astfel, aceste variabile vor fi
interpretate ca familiile arestate.

### Complexitate

#### Temporala

Instructiunile repetitive folosite in cadrul acestui cadru, au un numar de n si
respectiv m repetari, unde n este numarul de noduri / familii, iar m este numarul de
muchii / relatii. Astfel se obtine o complexitate de **O(max(m, n))**, iar daca tinem
cont de relatia m <= n(n-1)/2, complexitatea temporala maxima este **O(n^2)**.

#### Spatiala

In rezolvarea acestei probleme, sunt folositi doi vectori, unul de dimensiune maxim
n si inca unul de dimensiune m * 2. Astfel, pentru un graf mai *gol*, cu putine
muchii, complexitatea va fi **O(n)**, iar pentru unul *incarcat*, cu mai multe
muchii, se obtine complexitatea de **O(m)**. Punem combina cele doua rezultate,
folosind relatia m <= n(n-1)/2 si obtine o complexitate de **O(n^2)**.


## Observatii

1. Am lasat cod duplicat prin clasele ce rezolva task-urile, special pentru a se
intelege mai bine cum se formeaza clauzele pentru fiecare reducere in parte, codul
fiind mai usor de parcurs.

2. Am calculat de fiecare data numarul clauzelor cu ajutorul unor formule matematice
usor de vazut, tocmai pentru a nu fi nevoit sa tin o lista a tututor clauzelor, fapt
ce ar fi consumat enorm de multa memorie, in mod inutil.


