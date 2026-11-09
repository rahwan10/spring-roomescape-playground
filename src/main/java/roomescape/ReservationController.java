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

/**
 * 예약 관리 화면의 JavaScript가 호출하는 JSON API.
 * {@code @RestController}는 메서드가 반환한 객체를 HTTP 응답 본문으로 변환한다.
 */
@RestController
public class ReservationController {

    // 데이터베이스 대신 서버 메모리에 보관한다. 키는 예약 ID이며, ID 순서로 조회된다.
    private final ConcurrentSkipListMap<Long, Reservation> reservations =
            new ConcurrentSkipListMap<>();

    // 새 예약의 ID를 만든다. 서버를 재시작하면 이 값도 초기화된다.
    private final AtomicLong nextId = new AtomicLong();

    /**
     * GET /reservations 요청을 처리한다.
     * 저장된 예약 목록의 복사본을 반환하면 Spring이 JSON 배열로 응답한다.
     */
    @GetMapping("/reservations")
    public List<Reservation> readReservations() {
        return List.copyOf(reservations.values());
    }

    /**
     * POST /reservations 요청을 처리한다.
     * {@code @RequestBody}가 요청 JSON을 ReservationRequest로 변환한다.
     */
    @PostMapping("/reservations")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest request) {
        // 1. 서버가 새 ID를 만들고, 요청 데이터를 예약 객체로 묶는다.
        long id = nextId.incrementAndGet();
        Reservation reservation = new Reservation(
                id, request.name(), request.date(), request.time());

        // 2. ID를 키로 예약을 저장한다.
        reservations.put(id, reservation);

        // 3. 201 Created, Location 헤더, 생성된 예약 JSON을 응답한다.
        URI location = URI.create("/reservations/" + id);
        return ResponseEntity.created(location).body(reservation);
    }

    /**
     * DELETE /reservations/{id} 요청을 처리한다.
     * {@code @PathVariable}이 URL의 id 부분을 메서드의 id 파라미터로 전달한다.
     */
    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
        // remove는 해당 ID가 없으면 null을 돌려준다.
        if (reservations.remove(id) == null) {
            return ResponseEntity.notFound().build();
        }

        // 삭제 성공 시 응답 본문 없이 204 No Content를 보낸다.
        return ResponseEntity.noContent().build();
    }
}
