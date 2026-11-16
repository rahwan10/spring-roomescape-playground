package roomescape.dto;

import roomescape.exception.InvalidReservationException;

public record ReservationRequest(String name, String date, String time) {
}