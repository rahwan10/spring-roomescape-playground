package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        String sql = "SELECT id, name, date, time FROM reservation";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new Reservation(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("date"),
                resultSet.getString("time")
        ));
    }

    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO reservation (name, date, time) VALUES (?, ?, ?)",
                    new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getDate());
            ps.setString(3, reservation.getTime());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime());
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }
}
