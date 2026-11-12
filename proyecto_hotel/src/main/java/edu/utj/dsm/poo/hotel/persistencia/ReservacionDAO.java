package edu.utj.dsm.poo.hotel.persistencia;

import edu.utj.dsm.poo.hotel.modelo.Cliente;
import edu.utj.dsm.poo.hotel.modelo.Reservacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservacionDAO {

    public boolean guardar(Reservacion res) {
        String sql = "INSERT INTO reservaciones (folio, nombre_cliente, telefono_cliente, habitacion, fecha_entrada, fecha_salida, costo_total, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, res.getFolio());
            ps.setString(2, res.getCliente() != null ? res.getCliente().getNombre() : "");
            ps.setString(3, res.getCliente() != null ? res.getCliente().getTelefono() : "");
            ps.setString(4, res.getHabitacion() != null ? res.getHabitacion().toString() : "Habitación Genérica");

            if (res.getFechaEntrada() != null) {
                ps.setDate(5, Date.valueOf(res.getFechaEntrada()));
            } else {
                ps.setNull(5, Types.DATE);
            }

            if (res.getFechaSalida() != null) {
                ps.setDate(6, Date.valueOf(res.getFechaSalida()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.setDouble(7, res.getCostoTotal());
            ps.setString(8, res.getEstado());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(Reservacion res) {
        String sql = "UPDATE reservaciones SET nombre_cliente=?, telefono_cliente=?, habitacion=?, fecha_entrada=?, fecha_salida=?, costo_total=?, estado=? WHERE folio=?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, res.getCliente() != null ? res.getCliente().getNombre() : "");
            ps.setString(2, res.getCliente() != null ? res.getCliente().getTelefono() : "");
            ps.setString(3, res.getHabitacion() != null ? res.getHabitacion().toString() : "Habitación Genérica");

            if (res.getFechaEntrada() != null) {
                ps.setDate(4, Date.valueOf(res.getFechaEntrada()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            if (res.getFechaSalida() != null) {
                ps.setDate(5, Date.valueOf(res.getFechaSalida()));
            } else {
                ps.setNull(5, Types.DATE);
            }

            ps.setDouble(6, res.getCostoTotal());
            ps.setString(7, res.getEstado());
            ps.setString(8, res.getFolio());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(String folio) {
        String sql = "DELETE FROM reservaciones WHERE folio=?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, folio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Reservacion> listarTodas() {
        List<Reservacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservaciones";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Reservacion res = new Reservacion();
                res.setFolio(rs.getString("folio"));

                Cliente cliente = new Cliente("", rs.getString("nombre_cliente"), "", rs.getString("telefono_cliente"));
                res.setCliente(cliente);

                Date fEntrada = rs.getDate("fecha_entrada");
                if (fEntrada != null) {
                    res.setFechaEntrada(fEntrada.toLocalDate());
                }

                Date fSalida = rs.getDate("fecha_salida");
                if (fSalida != null) {
                    res.setFechaSalida(fSalida.toLocalDate());
                }

                res.setCostoTotal(rs.getDouble("costo_total"));
                res.setEstado(rs.getString("estado"));

                lista.add(res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}