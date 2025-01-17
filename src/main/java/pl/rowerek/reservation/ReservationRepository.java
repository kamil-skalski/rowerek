package pl.rowerek.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface ReservationRepository extends JpaRepository<Reservation, ReservationId> {

    List<Reservation> findByReservationIdBikeIdAndStatus(UUID bikeId, Status status);

}
