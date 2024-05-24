# Rowerek - Aplikacja do wypożyczania rowerów

## Opis aplikacji
**Rowerek** to aplikacja do wypożyczania rowerów, która została podzielona na 5 głównych modułów. Każdy z tych modułów zawiera funkcjonalności wykonywane przez różnych "aktorów":

- **Customer**: Zarządzanie danymi klientów.
- **Bike**: Szczegóły dotyczące rowerów, w tym ustalanie cen - obsługiwane przez firmę zarządzającą zasobami.
- **Maintenance**: Usługi naprawcze rowerów - realizowane przez zewnętrzną firmę.
- **Restriction**: Regulacje prawne lub konkurencyjne nakładane przez miasto lub inne organy.
- **Reservation**: Proces rezerwacji dokonywany przez klientów.

## Implementacja funkcjonalności
W obecnej chwili brakuje implementacji metody [ReservationService#reserve](./src/main/java/pl/rowerek/reservation/ReservationService.java) umożliwiającej rezerwację rowerów.
Zaimplementowanie tej funkcji, po jej implmentacji testy [ReservationServiceTest](./src/test/java/pl/rowerek/reservation/ReservationServiceTest.java) powinny świecić się na zielono.

# Zad 2 - Moduł dostępności 

## Wprowadzenie
Po poprzednim podziału aplikacji na moduły zidentyfikowano jeden z problemów związanych z modularyzacją, a mianowicie brak pojedynczego źródła prawdy dla dostępności rowerów.
Moduł `availability` został już dodany i utworzono dla jego metod odpowiednie API.

Dodano również funkcjonalność automatycznego tworzenia [BikeAvailability](./src/main/java/pl/rowerek/availability/BikeAvailability.java) za pomocą zdarzenia emitowanego przez [BikeService](./src/main/java/pl/rowerek/bike/BikeService.java) w momencie tworzenia roweru.
Zdarzenie to jest przechwytywane przez  [BikeEventHandler](./src/main/java/pl/rowerek/availability/BikeEventHandler.java). 
Pozwala to na dodanie encji odpowiedzialnej za dostępność roweru jednocześnie z jego tworzeniem.
W przyszłości rozważamy zmianę tego mechanizmu na podejście asynchroniczne i zabezpieczymy się przed ewentualnym gubieniem eventów za pomocą wzorców OutBox i Inbox Pattern.

## Zadanie

Twoim zadaniem jest zaimplementowanie metod w module dostępności `availability` oraz dostosowanie pozostałych modułów tak, aby korzystały z nowego modułu.
Po prawidłowej implementacji wszystkie testy powinny świecić się na zielono :)

Powodzenia!
