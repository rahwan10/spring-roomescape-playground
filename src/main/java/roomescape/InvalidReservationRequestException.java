package roomescape;

/** 예약 생성에 필요한 값이 빠진 요청을 나타낸다. */
public class InvalidReservationRequestException extends RuntimeException {

    public InvalidReservationRequestException(String message) {
        super(message);
    }
}
