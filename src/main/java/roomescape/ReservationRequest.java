package roomescape;

/** 클라이언트가 예약 생성 요청의 JSON 본문에 담는 데이터. */
public record ReservationRequest(String name, String date, String time) {

    /** 필수 값이 비어 있으면 예약을 만들기 전에 요청 오류를 알린다. */
    public void validate() {
        if (isMissing(name) || isMissing(date) || isMissing(time)) {
            throw new InvalidReservationRequestException("예약자, 날짜, 시간을 입력해 주세요.");
        }
    }

    private static boolean isMissing(String value) {
        return value == null || value.isBlank();
    }
}
