# DoIt - Sistema di Gestione Obiettivi Personali

## Descrizione Generale
DoIt è un'applicazione Android nativa sviluppata per facilitare la pianificazione e il monitoraggio degli obiettivi personali. L'applicativo si distingue per un'interfaccia utente essenziale e un'architettura software modulare, progettata seguendo i moderni standard di sviluppo per la piattaforma Android.

## Requisiti Funzionali
L'applicazione implementa le seguenti funzionalità core:
- **Gestione Obiettivi:** Creazione di record relativi ad attività o traguardi personali con descrizione testuale.
- **Pianificazione Temporale:** Possibilità di associare una data di scadenza (deadline) a ciascun obiettivo.
- **Monitoraggio dello Stato:** Sistema di tracciamento per contrassegnare gli obiettivi come completati, con registrazione automatica della data di raggiungimento.
- **Ordinamento Dinamico:** Gestione manuale dell'ordine di priorità degli elementi all'interno della lista.
- **Persistenza Locale:** Memorizzazione permanente dei dati su file system locale per garantire la disponibilità delle informazioni tra diverse sessioni d'uso.

## Architettura del Software
La struttura del codice è suddivisa in moduli logici per garantire manutenibilità e scalabilità:
- **Modello dei Dati (`Goal.kt`):** Definizione della struttura dell'informazione.
- **Logica di Gestione (`GoalManager.kt`):** Orchestrazione dei dati, operazioni CRUD e gestione della persistenza.
- **Livello UI (`ui/`):** 
    - `GoalApp.kt`: Entry point dell'interfaccia e gestione dello stato globale della vista.
    - `components/`: Libreria di componenti grafici atomici e riutilizzabili.
    - `theme/`: Configurazioni globali di stile, colori e tipografia.
