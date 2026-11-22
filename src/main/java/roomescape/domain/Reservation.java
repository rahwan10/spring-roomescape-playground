package roomescape.domain;

import roomescape.exception.InvalidReservationException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Reservation {
    private static final int MAX_NAME_LENGTH = 15;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private final Long id;
    private final String name;
    private final String date;
    private final String time;

    public Reservation(Long id, String name, String date, String time) {
        if (isNullOrBlank(name) || isNullOrBlank(date) || isNullOrBlank(time)) {
            throw new InvalidReservationException("예약 정보가 비어 있습니다.");
        }
        validateName(name);
        validateDate(date);
        validateTime(time);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }



    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public String getTime() { return time; }

    public boolean hasId(Long id) {
        return this.id.equals(id);
    }

    public boolean isBefore(LocalDate criteria) {
        return LocalDate.parse(date).isBefore(criteria);
    }

    private static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }
    private static void validateDate(String date) {
        try {
            LocalDate.parse(date);              // "2023-13-45" 같은 값이면 예외
        } catch (DateTimeParseException e) {
            throw new InvalidReservationException("날짜 형식이 올바르지 않습니다.");
        }
    }

    private static void validateTime(String time) {
        try {
            LocalTime.parse(time, TIME_FORMAT);
        } catch (DateTimeParseException e) {
            throw new InvalidReservationException("시간 형식이 올바르지 않습니다.");
        }
    }

    private static void validateName(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidReservationException("이름은 " + MAX_NAME_LENGTH + "자 이하여야 합니다.");
        }
    }

}
