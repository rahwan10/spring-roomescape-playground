package roomescape;

import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.exception.InvalidReservationException;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ReservationTest {

    @Test
    void 빈_값으로_예약을_만들면_예외가_발생한다() {
        assertThatThrownBy(() -> new Reservation(1L, "", "", ""))
                .isInstanceOf(InvalidReservationException.class);
    }

    @Test
    void 이름이_15자를_넘으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Reservation(null, "가".repeat(16), "2099-08-05", "10:00"))
                .isInstanceOf(InvalidReservationException.class);
    }

    @Test
    void 날짜_형식이_올바르지_않으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Reservation(null, "브라운", "2023-13-45", "10:00"))
                .isInstanceOf(InvalidReservationException.class);
    }

    @Test
    void 기준_날짜보다_이전이면_지난_예약이다() {
        Reservation reservation = new Reservation(null, "브라운", "2024-01-01", "10:00");
        assertThat(reservation.isBefore(LocalDate.of(2024, 1, 2))).isTrue();
    }
}
