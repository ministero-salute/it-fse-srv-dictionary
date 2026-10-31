
# Fascicolo Sanitario 2.0

# _it-fse-srv-dictionary_

Il microservizio in oggetto espone API che consentono la creazione, lettura, aggiornamento ed eliminazione di dizionari e codifiche terminologiche. Il microservizio, inoltre, espone API necessarie al Gateway per effettuare l'allineamento dei nuovi dizionari.

<br/>

## Test isolati

Richiede JDK 21 e Maven. La suite opt-in non richiede MongoDB, container o
servizi esterni:

```sh
mvn -PISOLATION clean test
```

Il profilo seleziona esclusivamente i test JUnit con tag `ISOLATION`, esclusi
dall'esecuzione Maven predefinita. I test verificano controller HTTP e risposte
di errore, logica applicativa, parsing CSV, snapshot/chunk, query MongoDB e
configurazione OpenAPI. MongoTemplate e i confini esterni sono mock; il contesto
Spring di configurazione è minimale e usa il profilo `ISOLATION`, senza avviare
l'applicazione o l'auto-configurazione MongoDB.

JaCoCo produce `target/site/jacoco/index.html` e `target/site/jacoco/jacoco.xml`.

Regressione nota preservata: con Spring 6, gli handler dei parametri/parti
mancanti modificano header di sola lettura; MVC restituisce quindi HTTP 400
senza il corpo problem-details. La suite verifica il comportamento attuale;
non modifica il codice di produzione.

## Repos
- [*it-fse-support*](https://github.com/ministero-salute/it-fse-support) - Pagina di supporto FSE 2.0
- [*it-fse-landing*](https://github.com/ministero-salute/it-fse-landing) - Landing page dei repository FSE 2.0

<br/>

## License

[![License: AGPL v3](https://img.shields.io/badge/License-AGPL_v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)
