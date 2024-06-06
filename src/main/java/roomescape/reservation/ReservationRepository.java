package roomescape.reservation;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservationTime.ReservationTime;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ReservationRepository {
	private final JdbcTemplate jdbcTemplate;

	public ReservationRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Reservation> find() {
		String sql = "SELECT \n" +
				"    r.id as reservation_id, \n" +
				"    r.name as reservation_name, \n" +
				"    r.date as reservation_date, \n" +
				"    t.id as time_id, \n" +
				"    t.start_at as time_start_at \n" +
				"FROM reservation as r \n" +
				"inner join reservation_time as t \n" +
				"on r.time_id = t.id";
		return jdbcTemplate.query(sql,
				(rs, rowNum) ->
						new Reservation(rs.getLong("reservation_id"),
								rs.getString("reservation_name"),
								rs.getString("reservation_date"),
								new ReservationTime(rs.getLong("time_id"), rs.getString("time_start_at")))
		);
	}

	public Reservation findByKey(Long id) {
		String sql = "SELECT \n" +
				"    r.id as reservation_id, \n" +
				"    r.name as reservation_name, \n" +
				"    r.date as reservation_date, \n" +
				"    t.id as time_id, \n" +
				"    t.start_at as time_start_at \n" +
				"FROM reservation as r \n" +
				"inner join reservation_time as t \n" +
				"on r.time_id = t.id \n" +
				"WHERE r.id = ?";
		RowMapper<Reservation> rowMapper = (rs, rowNum) -> new Reservation(
				rs.getLong(1),
				rs.getString(2),
				rs.getString(3),
				new ReservationTime(rs.getLong(4), rs.getString(5)));
		return jdbcTemplate.queryForObject(sql, rowMapper, id);
	}

	public Long save(String name, String date, Long timeId) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement("INSERT INTO reservation(name, date, time_id) VALUES (?, ?, ?)", new String[]{"id"});
			ps.setString(1, name);
			ps.setString(2, date);
			ps.setLong(3, timeId);

			return ps;
		}, keyHolder);

		return keyHolder.getKey().longValue();
	}

	public void delete(long id) {
		jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
	}
}
