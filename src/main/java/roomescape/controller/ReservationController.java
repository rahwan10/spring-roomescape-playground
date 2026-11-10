package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.domain.Reservation;
import roomescape.domain.Reservations;
import roomescape.dto.ReservationRequest;

import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class ReservationController {
    private final Reservations reservations = new Reservations();
    private final AtomicLong index = new AtomicLong(0);

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/reservations")
    @ResponseBody // json형식 반환
    public List<Reservation> reservations() {
        return reservations.getValues();
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> create(@RequestBody ReservationRequest request) {
        Reservation reservation = new Reservation(
                index.incrementAndGet(),                 // A: 1, 2, 3 ... 번호 발급
                request.name(), request.date(), request.time());
        reservations.add(reservation);                   // 목록에 추가

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId()))   // B: 201 + Location
                .body(reservation);
    }
}
