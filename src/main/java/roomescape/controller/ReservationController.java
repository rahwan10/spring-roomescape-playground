package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Reservation;
import roomescape.domain.Reservations;
import roomescape.dto.ReservationRequest;

import java.net.URI;
import java.util.List;


@Controller
public class ReservationController {
    private final Reservations reservations = new Reservations();


    @GetMapping("/reservation")
    public String reservationPage() {
        return "reservation";
    }

    @GetMapping("/reservations")
    @ResponseBody // json형식 반환
    public List<Reservation> readReservations() {
        return reservations.getValues();
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> create(@RequestBody ReservationRequest request) {
        Reservation reservation = reservations.add(request.name(), request.date(), request.time());
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId()))
                .body(reservation);
    }
    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservations.removeById(id);
        return ResponseEntity.noContent().build();
    }
}
