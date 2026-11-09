package roomescape;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** 예약 관리 화면의 JavaScript가 호출하는 JSON API. */
@RestController
public class ReservationController {

    private final ConcurrentSkipListMap<Long, Reservation> reservations = new ConcurrentSkipListMap<>();
    private final AtomicLong nextId = new AtomicLong();

    /** 저장된 예약을 ID 순서대로 JSON 목록에 담아 응답한다. */
    @GetMapping("/reservations")
    public List<Reservation> readReservations() {
        return List.copyOf(reservations.values());
    }

    /** JSON 본문을 읽어 예약을 생성하고, 201과 새 예약의 주소를 응답한다. */
    @PostMapping("/reservations")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest request) {
        long id = nextId.incrementAndGet();
        Reservation reservation = new Reservation(id, request.name(), request.date(), request.time());
        reservations.put(id, reservation);
        return ResponseEntity.created(URI.create("/reservations/" + id)).body(reservation);
    }

    /** URL의 id에 해당하는 예약을 삭제하고, 본문 없이 204를 응답한다. */
    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
        if (reservations.remove(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
