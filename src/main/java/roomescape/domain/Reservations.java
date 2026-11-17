package roomescape.domain;

import roomescape.exception.NotFoundReservationException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Reservations {
    private final List<Reservation> values = new ArrayList<>();
    private final AtomicLong lastId = new AtomicLong(0);

    public Reservation add(String name, String date, String time) {
        Reservation reservation = new Reservation(lastId.incrementAndGet(), name, date, time);
        values.add(reservation);
        return reservation;
    }

    public void removeById(Long id) {
        boolean removed = values.removeIf(reservation -> reservation.hasId(id));
        if (!removed) {
            throw new NotFoundReservationException();
        }
    }

    public List<Reservation> getValues() {
        return List.copyOf(values);   // 밖에서 원본을 못 건드리게 복사본으로
    }
}
