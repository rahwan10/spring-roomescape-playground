package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundReservationException.class)
    public ResponseEntity<Void> handleNotFound(NotFoundReservationException e) {
        return ResponseEntity.notFound().build();        // 404
    }

    @ExceptionHandler(InvalidReservationException.class)
    public ResponseEntity<Void> handleBadRequest(InvalidReservationException e) {
        return ResponseEntity.badRequest().build();      // 400
    }

}
