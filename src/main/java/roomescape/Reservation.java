package roomescape;

/** 예약 목록과 생성 응답에 사용하는 데이터. */
public record Reservation(long id, String name, String date, String time) {
}
