# Customers & Devices
> Come customer care, ho necessità di gestire i dati degli utenti e i device a loro associati.

## Requisiti funzionali
Realizzare un set di api **REST compliant** che permettano di gestire operazioni CRUD di anagrafica utente e device associati, con le seguenti richieste business:
- L'utente customer ha come set di dati nome, cognome, codice fiscale e indirizzo
- Un device ha un codice **uuid** e uno stato (**ACTIVE, INACTIVE, LOST**)
- Ogni utente può avere fino a 2 device associati
- Di un utente è possibile modificare solo l'indirizzo
- Di un device è possibile modificare solo lo stato
- Dovrà essere possibile recuperare i dati aggregati di utente e device
- Dovrà essere possibile verificare, dato un uuid device, che questo sia presente o meno
- Dovrà essere possibile cancellare un device

## Requisiti tecnici
Per la persistenza dei dati va bene utilizzare una semplice hashmap o un db embedded.

Dal punto di vista delle tecnologie, gli unici requisiti sono l'utilizzo di **Java 17**, di junit 5 e di maven.
Libera scelta per quanto riguarda framework, ide.
Il codice deve essere testato tramite test unitari.
Per condividere il codice sorgente, è preferibile utilizzare un repository online su piattaforme come Github o simili, **mantenendo la storia dei commit**, procedendo nello sviluppo con feature atomiche.

Ci aspettiamo che il codice compili e i test passino. Va allegato anche un **readme** con le indicazioni per fare il build e lanciare l'applicazione.

**La presenza di swagger/openApi è un plus.**
**La presenza di ulteriori tipologie di test è un plus.**