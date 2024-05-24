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
