package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.Reservations;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.exception.InvalidReservationException;
import roomescape.exception.NotFoundReservationException;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;


@Controller
public class ReservationController {
    private final ReservationDao reservationDao;

    public ReservationController(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }


    @GetMapping("/reservations")
    @ResponseBody // json형식 반환
    public List<ReservationResponse> readReservations() {
        return reservationDao.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(@RequestBody ReservationRequest request) {
        Reservation reservation = new Reservation(null, request.name(), request.date(), request.time());
        if (reservation.isBefore(LocalDate.now())) {
            throw new InvalidReservationException("지난 날짜는 예약할 수 없습니다.");
        }
        Reservation saved = reservationDao.save(reservation);
        return ResponseEntity.created(URI.create("/reservations/" + saved.getId()))
                .body(ReservationResponse.from(saved));
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        int deleted = reservationDao.deleteById(id);
        if (deleted == 0) {
            throw new NotFoundReservationException();
        }
        return ResponseEntity.noContent().build();
    }
}
