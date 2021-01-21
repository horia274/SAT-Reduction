# SAT-Reduction

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
la 1 la k. Variabile FiSj va si setata pe *true*, daca familia i este spionata de
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

* oricare doua familii care se cunosc, trebuie sa aiba spioni diferiti - oricare
doua familii diferite, i != j nu pot avea acelasi spion l, deci valoarea expresiei
!(FiSl ^ FjSl) va fi *true* - se obtin ultimele **clauze de tipul 3** si anume
*!FiSl v !FjSl*, pentru orice familii i si j intre care exista muchie si orice
spion l.

#### Mica explicatie a corectitudinii

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


### Task 2

### Identificarea problemei K clique

Trebuie verificat daca exista un grup de k familii astfel incat oricare doua sa se
cunoasca. Intrucat relatia dintre doua familii este reprezentata la nivelul grafului
prin trasarea muchiei, problema este echivalenta cu **k clique**.

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

* pentru oricare doua familii care nu se cunosc, acestea nu pot fi simultan in clica
- pentru orice familii i != j si oricare pozitii din clica l, m (nu neaprat diferite)
se obtin **clauzele de tipul 3**
*!FiPl v !FjPm*.

#### Mica explicatie a corectitudinii

La fel ca in cazul precedent, corectitudinea rezulta din modul de constructie a
clauzelor. Astfel problema SAT intoarce *true*, daca si numai daca toate clauzele sunt
adevarate, daca si numai daca toate conditiile pentru K clique sunt indeplinite,
adica daca K clique intoarce *true*.

### Encodarea / Decodarea variabilelor

Formula de encodare pentru variabilele de tipul FiPj este exact ca in cazul anterior
si anume k * (i - 1) + j. Decodarea este tot in acelasi fel si anume, pentru o
variabila encodata var, i este catul impartirii lui var la k (la care se adauga 1
dupa caz) si j este restul.

### Raspunsul oracolului

Daca obtin *true*, ma intereseaza sa vad care sunt familiile care intra in clica.
In acest sens, pentru variabilele pozitive, le decodific pentru a afla indicele i
al familiei, pe care il salvez pentru a forma fisierul de output.

## Task 3

### Idee

Luand graful complementar, vreau sa gasesc clica de dimensiune maxima. Arestatii
vor fi familiile care **nu** se afla in clica. Ca explicatie, daca as face din nou
graful complementar (cel initial), dupa ce arestez (scot mafiotii din graf) raman
cu familii care nu se cunosc deloc intre ele (intrucat in graful complementar, ele
formeaza clica, adica se cunosc toti intre ei). Am nevoie de clica cea mai mare
posibila (dimensiune maxima), intrucat vreau sa elimin toate relatiile dintre ei.

### Folosire Task 2

Formez input pentru task-ul 2, adaugand doar, pe langa datele obtinute din input,
dimensiunea clicii k. Obtin apoi rezultatul dat de oracol pentru clica de 
dimensiune k, iar in cazul obtinerii unui raspuns afirmativ, executia se incheie,
afland ce variabile se afla in clica. Ulterior, cele care nu se afla vor fi arestati.

## Bonus

### Identificarea problemei Vertex Cover

Vrem sa identificam familiile (numarul minim de familii) care daca le scoatem din
graf sa nu mai exista relatii intre familiile ramase. Cu alte cuvinte, daca acoperim
/ coloram cu o culoare o parte din varfurile (familiile) din graf, toate muchiile sa
fie acoperite (in sensul ca atunci cand eliminam muchiile acoperite, nu mai ramane
niciuna). Muchie acoperita inseamna ca macar un nod este acoperit. Astfel, arestam
familiile care reprezinta raspunsul problemei **Vertex Cover**.

### Encodare / Decodare

In cadrul acestui task, nici macar nu este nevoie de o encodare, intrucat variabilele
folosite sunt varfurile din graf / indicii familiilor. Astfel, pentru explicatii
folosesc variabile de tipul Fi care au valoarea *true* daca si numai data familia i
este acoperita / colorata.

### Modalitatea de reducere

#### Necesitati - corespondenta in clauze:

* pentru fiecare muchie, cel putin unul din fiecare varf ce formeaza aceassta muchie
trebuie acoperit - **clauzele de tipul 1, cele obligatorii**, sunt
*Fi v Fj*, cu pondere de *sumOfWeights* + 1;

* pentru fiecare varf, se introduce o **clauza de tipul 2, cele soft**, in sensul ca
acesta poate sa nu fie colorat, asignandu-se un *weight* egal cu 1 (pentru simplitatea
calculului) - se obtin clauze de tipul
*!Fi*, cu pondere de 1.

## Observatii

1. Am lasat special cod duplicat prin clasele ce rezolva task-urile, special pentru
a se intelege mai usor cum se formeaza clauzele pentru fiecare reducere in parte,
codul fiind astfel mai usor de citit.

2. Am preferat sa tin de fiecare data o lista cu toate clauzele, pentru a fi usor de
numarat la final. Puteam sa hardcodez acest numar, dar din nou, am ales sa fie mai usor
de inteles (spre exemplu pentru *Task1*, numarul de clauze este 
*n + nk(k-1)/2 + mk*, unde n este numarul de familii, k este numarul de spioni si m
este numarul de relatii dintre familiile implicate).

