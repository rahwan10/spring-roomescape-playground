package roomescape.dto;

public record ReservationRequest(String name, String date, String time) {

    public ReservationRequest {
        if (isBlank(name) || isBlank(date) || isBlank(time)) {
            throw new IllegalArgumentException("예약 정보가 비어 있습니다.");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}