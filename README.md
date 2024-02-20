[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/le3-5Dzg)
Dobre Emilia Iliana - 323CB

---
Design Patterns - flow program:
-

Aplicatia contine useri care o folosesc, care sunt ascultatori sau streameri si are o baza de date unde tine evidenta tuturor streamurilor pe care un streamer le-a postat in aplicatie. Are nevoie de un Dispatcher de comenzi care se ocupa cu identificarea si executarea comenzilor.

---


1) FACTORY PATTERN:

    Aplicatia trebuie initializata cu datele primite din fisiere, iar pentru ca exista 4 tipuri de fisiere care descriu aplicatia, este nevoie de o parsare speciala, astfel am folosit factory de parsere.


2) SINGLETON PATTERN:

    Duplicitatea parserului ar afecta aplicatia, astfel folosesc Singleton design pentru a nu compromite datele cu parsari din alte obiecte parser.


3) STRATEGY PATTERN:
   
    In cadrul initializarii aplicatiei, la analiza comenzilor apar comenzi diferite care au la randul lor nevoia de o alta parsare. Identificand aceasta problema am folosit un strategy design pattern in cadrul parsarii pentru comenzi, care va returna comanda dorita.


4)  COMMAND PATTERN:

    Dupa ce datele aplicatiei au fost initializate si comenzile identificate, acestea executa fiecare ceva diferit si actualizeaza aplicatia, astfel am identificat nevoia de un command design.


5) OBSERVER PATTERN:

    La anumite comenzi, aplicatia trebuie notificata de schimbarile facute, aplicatia actualizeaza datele sale fiind notificata de userii sai. Din aceasta nevoie de notificari intre obiecte, am folosit oberver design pattern.

---


---
Parsarea Datelor:
-

- parserul reprezinta o parte foarte importnata dintr o aplicatie si astfel trebuie sa asigur flexibilitatea sa, corectiduinea inputului si siguranta ca parserul e unic si nu se compromit date
- de aceea parserFacatory este singleton si in plus parseaza diferit in functie de fisierele primite, astfel daca vor aparea noi fisiere din care sa citeasca, codul este reutilizabil
- in Factory se face cate un obiect Parser pentru fiecare scop, clasa abstracta citeste datele din orice fisier si pentru fiecare linie initializeaza diferit datele aplicatiei

---
Initializare Date: user/stream/streamer:
-

-----Inregistrare streamer/user-----:
- se creeaza obiecte auxiliare initializate cu datele citite, la crearea unui utilizator (streamer sau user normal) se apeleaza metoda de registerObserver
- dupa ideea designului observer, utilizatorii aplicatiei sunt observatorii, userul si streamerul pot notifica aplicatia si astfel implementeaza interfata observer cu metoda de update
- aplicatia este subiectul observat care are metoda de register pentru observerii sai si de notifyObserver care intercepteaza notificarea primita de la userii sai

-----Adaugare Stream-----:
- la adaugarea unui stream se observa design patternul implementat, se apeleaza metoda de postareSTream unde se identifica streamerul ce vrea sa posteze si se notifica astfel aplicatia
- dupa ce se identifica in metoda de notificare ce fel de update se face pentru aplicatie, se apeleaza update ul in acest caz pentru streamer 
- update ul streamerului depinde de actiunea sa: stergere sau adaugare care este identificata printr-o clasa enum
- la adaugarea unui nou stream se adauga streamul in lista streameri a streamerului dar si in aplicatie


---
Initializare Date: comenzi:
-

- Ulitmul tip de parser din factory este parserCommand in care in metoda de initializareDate se vor initializa obiectele de tip Command
- Pentru ca exista mai multe comenzi, si acestea au o parsare diferita, am ales astfel o strategie de a trata inputurile de aceeasi lungime si a initializa obiectul corect
- Folosesc astfel strategy pattern pentru a alege startegia de parsare, dupa ce a fost aleasa se apeleaza metoda acesteia de parsare care va intoarce direct comanda
- Comanda se va adauga apoi in lista de comenzi a dispatcherului de comenzi.

---
Dispatcher Comenzi:
-

- Are o lista de comenzi care se actualizeaza la initializarea datelor, la final urmand sa se apeleze metoda de executare in ordine a comenzilor 


- Comenzile respecta command pattern astfel am mai multe tipuri de comenzi fiecare avand o metoda ce executa si face sau nu modificari asupra aplicatiei


- Comenzile care schimba aplicatia sunt:

    - streamer: adauga/sterg stream: aplicatia va fi notificata se respecta observer pattern aici
  
    - userul asculta un stream


- Recomandarile pentru 5:
  
    - se gaseste userul care vrea recomandarea
    - pentru fiecare stream ascultat caut id-ul in aplicatie, gasesc si creatorul si din lista streamurilor acelui creator care sunt diferite
    - de cele ascultate de user, le adaug intr-o coada de prioritate dupa popularitate 


- Recomandarile pentru 3 surprize:

    - se gaseste userul care vrea recomandarea
    - pentru fiecare id din lista de streamuri, gasesc streamul din aplicatie
    - fac o lista de id uri cu streamerii pe care i-a ascultat userul
    - pentru toate streamurile aplicatiei verific sa nu fie streamerul cunoscut deja de user
    - daca nu e stiut de user adaug streamurile creatorului intr-o coada de prioritati dupa data sau popularitate
        