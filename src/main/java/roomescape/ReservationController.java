package roomescape;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** 예약 관리 화면의 JavaScript가 호출하는 JSON API. */
@RestController
public class ReservationController {

    @GetMapping("/reservations")
    public List<Reservation> readReservations() {
        return List.of();
    }
}
