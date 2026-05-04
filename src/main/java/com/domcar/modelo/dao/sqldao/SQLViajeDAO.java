package com.domcar.modelo.dao.sqldao;

import com.domcar.exceptions.ViajeNotFoundException;
import com.domcar.modelo.dao.interfaces.ViajeDAO;
import com.domcar.modelo.dto.viaje.Viaje;
import com.domcar.modelo.dto.viaje.EstadoViaje;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class SQLViajeDAO implements ViajeDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Viaje> viajeRowMapper = (rs, rowNum) -> {
        int cod = rs.getInt("codViaje");
        String propietario = rs.getString("propietario");
        String ruta = rs.getString("ruta");
        LocalDateTime fechaSalida = rs.getTimestamp("fechaSalida").toLocalDateTime();
        int duracion = rs.getInt("duracion");
        Float precio = rs.getFloat("precio");
        int plazasOfertadas = rs.getInt("plazasOfertadas");
        String estadoViajeString = rs.getString("estadoViaje");
        EstadoViaje estadoViaje = EstadoViaje.valueOf(estadoViajeString);
        return new Viaje(cod, propietario, ruta, fechaSalida, duracion, precio, plazasOfertadas, estadoViaje);
    };

    @Override
    public Set<Viaje> findAll() {
        String sql = "SELECT * FROM viajes";
        return new HashSet<>(jdbcTemplate.query(sql, viajeRowMapper));
    }

    @Override
    public Set<Viaje> findAll(String city) {
        String sql = "SELECT * FROM viajes WHERE ruta LIKE ?";
        return new HashSet<>(jdbcTemplate.query(sql, viajeRowMapper, "%-" + city + "%"));
    }

    @Override
    public Set<Viaje> findAll(EstadoViaje estadoViaje) {
        String sql = "SELECT * FROM viajes WHERE estadoViaje = ?";
        return new HashSet<>(jdbcTemplate.query(sql, viajeRowMapper, estadoViaje.toString()));
    }

    @Override
    public Set<Viaje> findAll(Class<? extends Viaje> viajeClass) {
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public Viaje findById(int codViaje) {
        String sql = "SELECT * FROM viajes WHERE codViaje = ?";
        List<Viaje> viajes = jdbcTemplate.query(sql, viajeRowMapper, codViaje);
        return viajes.isEmpty() ? null : viajes.get(0);
    }

    @Override
    public Viaje getById(int codViaje) throws ViajeNotFoundException {
        Viaje viaje = findById(codViaje);
        if (viaje == null) {
            throw new ViajeNotFoundException("Viaje no encontrado");
        }
        return viaje;
    }

    @Override
    public void add(Viaje viaje) {
        String sql = "INSERT INTO viajes (propietario, ruta, fechaSalida, duracion, precio, plazasOfertadas, estadoViaje) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, viaje.getPropietario());
            ps.setString(2, viaje.getRuta());
            ps.setTimestamp(3, Timestamp.valueOf(viaje.getFechaSalida()));
            ps.setLong(4, viaje.getDuracion());
            ps.setFloat(5, viaje.getPrecio());
            ps.setInt(6, viaje.getPlazasOfertadas());
            ps.setString(7, viaje.getEstado().toString());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            viaje.setCodViaje(keyHolder.getKey().intValue());
        }
    }

    @Override
    public void update(Viaje viaje) throws ViajeNotFoundException {
        String sql = "UPDATE viajes SET propietario = ?, ruta = ?, fechaSalida = ?, duracion = ?, precio = ?, plazasOfertadas = ?, estadoViaje = ? WHERE codViaje = ?";
        int filasAfectadas = jdbcTemplate.update(sql,
                viaje.getPropietario(),
                viaje.getRuta(),
                Timestamp.valueOf(viaje.getFechaSalida()),
                viaje.getDuracion(),
                viaje.getPrecio(),
                viaje.getPlazasOfertadas(),
                viaje.getEstado().toString(),
                viaje.getCodViaje());

        if (filasAfectadas == 0) {
            throw new ViajeNotFoundException("El viaje con id: " + viaje.getCodViaje() + " no ha sido encontrado.");
        }
    }

    @Override
    public void remove(Viaje viaje) throws ViajeNotFoundException {
        String sql = "DELETE FROM viajes WHERE codViaje = ?";
        int filasAfectadas = jdbcTemplate.update(sql, viaje.getCodViaje());
        if (filasAfectadas == 0) {
            throw new ViajeNotFoundException("El viaje con id: " + viaje.getCodViaje() + " no ha sido encontrado.");
        }
    }
}
