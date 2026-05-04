package com.domcar.modelo.dao.sqldao;

import com.domcar.exceptions.ReservaAlreadyExistsException;
import com.domcar.exceptions.ReservaNotFoundException;
import com.domcar.modelo.dao.interfaces.ReservaDAO;
import com.domcar.modelo.dto.Reserva;
import com.domcar.modelo.dto.viaje.Viaje;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class SQLReservaDAO implements ReservaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Reserva> reservaRowMapper = (rs, rowNum) -> {
        String codigoReserva = rs.getString("codigoReserva");
        String usuario = rs.getString("usuario");
        int plazasSolicitadas = rs.getInt("plazasSolicitadas");
        LocalDateTime fechaRealizacion = rs.getTimestamp("fechaRealizacion").toLocalDateTime();
        int codViaje = rs.getInt("viaje");
        Viaje viaje = new Viaje(codViaje);
        return new Reserva(codigoReserva, usuario, plazasSolicitadas, fechaRealizacion, viaje);
    };

    @Override
    public Set<Reserva> findAll() {
        String sql = "SELECT * FROM reservas";
        return new HashSet<>(jdbcTemplate.query(sql, reservaRowMapper));
    }

    @Override
    public Reserva findById(String id) {
        String sql = "SELECT * FROM reservas WHERE codigoReserva= ?";
        List<Reserva> reservas = jdbcTemplate.query(sql, reservaRowMapper, id);
        return reservas.isEmpty() ? null : reservas.get(0);
    }

    @Override
    public ArrayList<Reserva> findAllByUser(String user) {
        String sql = "SELECT * FROM reservas WHERE usuario = ?";
        return new ArrayList<>(jdbcTemplate.query(sql, reservaRowMapper, user));
    }

    @Override
    public List<Reserva> findAllByTravel(Viaje viaje) {
        String sql = "SELECT * FROM reservas WHERE viaje = ?";
        return jdbcTemplate.query(sql, reservaRowMapper, viaje.getCodViaje());
    }

    @Override
    public Reserva getById(String id) throws ReservaNotFoundException {
        Reserva reserva = findById(id);
        if (reserva == null) {
            throw new ReservaNotFoundException("Reserva no encontrada");
        }
        return reserva;
    }

    @Override
    public List<Reserva> findAllBySearchParams(Viaje viaje, String searchParams) {
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public void add(Reserva reserva) throws ReservaAlreadyExistsException {
        String sql = "INSERT INTO reservas (codigoReserva, usuario, plazasSolicitadas, fechaRealizacion, viaje) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                reserva.getCodigoReserva(),
                reserva.getUsuario(),
                reserva.getPlazasSolicitadas(),
                Timestamp.valueOf(reserva.getFechaRealizacion()),
                reserva.getViaje().getCodViaje());
    }

    @Override
    public void update(Reserva reserva) throws ReservaNotFoundException {
        String sql = "UPDATE reservas SET usuario = ?, plazasSolicitadas = ?, fechaRealizacion = ?, viaje = ? WHERE codigoReserva = ?";
        int filasAfectadas = jdbcTemplate.update(sql,
                reserva.getUsuario(),
                reserva.getPlazasSolicitadas(),
                Timestamp.valueOf(reserva.getFechaRealizacion()),
                reserva.getViaje().getCodViaje(),
                reserva.getCodigoReserva());

        if (filasAfectadas == 0) {
            throw new ReservaNotFoundException(
                    "La reserva con código: " + reserva.getCodigoReserva() + " no ha sido encontrada.");
        }
    }

    @Override
    public void remove(Reserva reserva) throws ReservaNotFoundException {
        String sql = "DELETE FROM reservas WHERE codigoReserva = ?";
        int filasAfectadas = jdbcTemplate.update(sql, reserva.getCodigoReserva());

        if (filasAfectadas == 0) {
            throw new ReservaNotFoundException(
                    "La reserva con código: " + reserva.getCodigoReserva() + " no ha sido encontrada.");
        }
    }

    @Override
    public int getNumPlazasReservadasEnViaje(Viaje viaje) {
        String sql = "SELECT SUM(plazasSolicitadas) AS totalPlazasReservadas FROM reservas WHERE viaje = ?";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, viaje.getCodViaje());
        return total != null ? total : 0;
    }

    @Override
    public Reserva findByUserInTravel(String usuario, Viaje viaje) {
        String sql = "SELECT * FROM reservas WHERE usuario = ? AND viaje = ?";
        List<Reserva> reservas = jdbcTemplate.query(sql, reservaRowMapper, usuario, viaje.getCodViaje());
        return reservas.isEmpty() ? null : reservas.get(0);
    }
}
