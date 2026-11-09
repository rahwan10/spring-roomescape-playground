package roomescape;

/** 클라이언트가 예약 생성 요청의 JSON 본문에 담는 데이터. */
public record ReservationRequest(String name, String date, String time) {
}
