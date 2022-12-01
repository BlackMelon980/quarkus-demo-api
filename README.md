# Customers & Devices

> Come customer care, ho necessità di gestire i dati degli utenti e i device a loro associati.

## Requisiti funzionali

Realizzare un set di api **REST compliant** che permettano di gestire operazioni CRUD di anagrafica utente e device
associati, con le seguenti richieste business:

- L'utente customer ha come set di dati nome, cognome, codice fiscale e indirizzo
- Un device ha un codice **uuid** e uno stato (**ACTIVE, INACTIVE, LOST**)
- Ogni utente può avere fino a 2 device associati
- Di un utente è possibile modificare solo l'indirizzo
- Di un device è possibile modificare solo lo stato
- Dovrà essere possibile recuperare i dati aggregati di utente e device
- Dovrà essere possibile verificare, dato un uuid device, che questo sia presente o meno
- Dovrà essere possibile cancellare un device

------

## Run the application

Run the application in dev mode:

```
mvn quarkus:dev
```

## Testing

Use the following command to test the application:

```
mvn test
```

