package edu.utj.dsm.poo.hotel.persistencia;

import edu.utj.dsm.poo.hotel.modelo.Habitacion;
import edu.utj.dsm.poo.hotel.modelo.HabitacionSencilla;
import edu.utj.dsm.poo.hotel.modelo.Suite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitacionDAO {

    public boolean guardar(Habitacion h) {
        String sql = "INSERT INTO habitaciones (numero_habitacion, tipo, piso, precio_noche, capacidad, camas, estado, balcon, vista_mar, jacuzzi, minibar, desayuno, ruta_imagen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, h.getNumeroHabitacion());
            ps.setString(2, h.getTipo());
            ps.setInt(3, h.getPiso());
            ps.setDouble(4, h.getPrecioPorNoche());
            ps.setInt(5, h.getCapacidad());

            int camas = 0;
            boolean balcon = false, vistaMar = false, jacuzzi = false, minibar = false, desayuno = false;

            if (h instanceof HabitacionSencilla) {
                HabitacionSencilla s = (HabitacionSencilla) h;
                camas = s.getNumeroDeCamas();
                balcon = s.isBalcon();
                vistaMar = s.isVistaAlMar();
            } else if (h instanceof Suite) {
                Suite st = (Suite) h;
                camas = st.getNumeroDeCamas();
                jacuzzi = st.isJacuzzi();
                minibar = st.isMiniBar();
                desayuno = st.isIncluyeDesayuno();
            }

            ps.setInt(6, camas);
            ps.setString(7, h.getEstado());
            ps.setBoolean(8, balcon);
            ps.setBoolean(9, vistaMar);
            ps.setBoolean(10, jacuzzi);
            ps.setBoolean(11, minibar);
            ps.setBoolean(12, desayuno);
            ps.setString(13, h.getRutaImagen());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(Habitacion h) {
        String sql = "UPDATE habitaciones SET tipo=?, piso=?, precio_noche=?, capacidad=?, camas=?, estado=?, balcon=?, vista_mar=?, jacuzzi=?, minibar=?, desayuno=?, ruta_imagen=? WHERE numero_habitacion=?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, h.getTipo());
            ps.setInt(2, h.getPiso());
            ps.setDouble(3, h.getPrecioPorNoche());
            ps.setInt(4, h.getCapacidad());

            int camas = 0;
            boolean balcon = false, vistaMar = false, jacuzzi = false, minibar = false, desayuno = false;

            if (h instanceof HabitacionSencilla) {
                HabitacionSencilla s = (HabitacionSencilla) h;
                camas = s.getNumeroDeCamas();
                balcon = s.isBalcon();
                vistaMar = s.isVistaAlMar();
            } else if (h instanceof Suite) {
                Suite st = (Suite) h;
                camas = st.getNumeroDeCamas();
                jacuzzi = st.isJacuzzi();
                minibar = st.isMiniBar();
                desayuno = st.isIncluyeDesayuno();
            }

            ps.setInt(5, camas);
            ps.setString(6, h.getEstado());
            ps.setBoolean(7, balcon);
            ps.setBoolean(8, vistaMar);
            ps.setBoolean(9, jacuzzi);
            ps.setBoolean(10, minibar);
            ps.setBoolean(11, desayuno);
            ps.setString(12, h.getRutaImagen());
            ps.setString(13, h.getNumeroHabitacion());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(String numeroHabitacion) {
        String sql = "DELETE FROM habitaciones WHERE numero_habitacion=?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, numeroHabitacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Habitacion> listarTodas() {
        List<Habitacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM habitaciones";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String tipo = rs.getString("tipo");
                Habitacion h;
                if ("Sencilla".equalsIgnoreCase(tipo)) {
                    HabitacionSencilla sencilla = new HabitacionSencilla();
                    sencilla.setNumeroDeCamas(rs.getInt("camas"));
                    sencilla.setBalcon(rs.getBoolean("balcon"));
                    sencilla.setVistaAlMar(rs.getBoolean("vista_mar"));
                    h = sencilla;
                } else {
                    Suite suite = new Suite();
                    suite.setNumeroDeCamas(rs.getInt("camas"));
                    suite.setJacuzzi(rs.getBoolean("jacuzzi"));
                    suite.setMiniBar(rs.getBoolean("minibar"));
                    suite.setIncluyeDesayuno(rs.getBoolean("desayuno"));
                    h = suite;
                }

                h.setNumeroHabitacion(rs.getString("numero_habitacion"));
                h.setTipo(tipo);
                h.setPiso(rs.getInt("piso"));
                h.setPrecioPorNoche(rs.getDouble("precio_noche"));
                h.setCapacidad(rs.getInt("capacidad"));
                h.setEstado(rs.getString("estado"));
                h.setRutaImagen(rs.getString("ruta_imagen"));

                lista.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}