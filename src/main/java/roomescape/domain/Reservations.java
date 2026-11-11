package roomescape.domain;

import roomescape.exception.NotFoundReservationException;

import java.util.ArrayList;
import java.util.List;

public class Reservations {
    private final List<Reservation> values = new ArrayList<>();

    public void add(Reservation reservation) {
        values.add(reservation);
    }

    public void removeById(Long id) {
        boolean removed = values.removeIf(reservation -> reservation.getId().equals(id));
        if (!removed) {
            throw new NotFoundReservationException();
        }
    }

    public List<Reservation> getValues() {
        return List.copyOf(values);   // 밖에서 원본을 못 건드리게 복사본으로
    }
}
