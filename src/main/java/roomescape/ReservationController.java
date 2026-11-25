package roomescape;

import java.net.URI;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    private final ReservationRepository reservationRepository;

    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * GET /reservations 요청을 처리한다.
     * 데이터베이스에서 읽은 예약 목록을 Spring이 JSON 배열로 응답한다.
     */
    @GetMapping("/reservations")
    public List<Reservation> readReservations() {
        return reservationRepository.findAll();
    }

    /**
     * POST /reservations 요청을 처리한다.
     * {@code @RequestBody}가 요청 JSON을 ReservationRequest로 변환한다.
     */
    @PostMapping("/reservations")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest request) {
        // 1. 요청을 먼저 검사한다. 실패하면 INSERT하지 않는다.
        request.validate();

        // 2. 저장 후 데이터베이스가 생성한 ID를 포함한 예약을 받는다.
        Reservation reservation = reservationRepository.save(request);

        // 3. 201 Created, Location 헤더, 생성된 예약 JSON을 응답한다.
        URI location = URI.create("/reservations/" + reservation.id());
        return ResponseEntity.created(location).body(reservation);
    }

    /**
     * DELETE /reservations/{id} 요청을 처리한다.
     * {@code @PathVariable}이 URL의 id 부분을 메서드의 id 파라미터로 전달한다.
     */
    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
        // DELETE가 지운 행이 없다면 요청한 예약이 존재하지 않는다.
        if (!reservationRepository.deleteById(id)) {
            return ResponseEntity.notFound().build();
        }

        // 삭제 성공 시 응답 본문 없이 204 No Content를 보낸다.
        return ResponseEntity.noContent().build();
    }

    /** 예약 요청 검증 실패만 400으로 바꾸고, 서버의 다른 예외는 감추지 않는다. */
    @ExceptionHandler(InvalidReservationRequestException.class)
    public ResponseEntity<Map<String, String>> handleInvalidReservation(
            InvalidReservationRequestException exception) {
        return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
    }
}
