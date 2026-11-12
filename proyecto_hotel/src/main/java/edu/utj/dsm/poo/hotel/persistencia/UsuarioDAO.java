package edu.utj.dsm.poo.hotel.persistencia;

import edu.utj.dsm.poo.hotel.modelo.Actividad;
import edu.utj.dsm.poo.hotel.modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioDAO {

  
    public Usuario autenticar(String identificador, String password) {
        String sql = "SELECT id, nombre, correo, password, rol, activo FROM usuarios " +
                     "WHERE (CAST(id AS CHAR) = ? OR correo = ?) AND password = ? AND activo = 1";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, identificador.trim());
            ps.setString(2, identificador.trim());
            ps.setString(3, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNombre(rs.getString("nombre"));
                    u.setCorreo(rs.getString("correo"));
                    u.setPassword(rs.getString("password"));
                    u.setRol(rs.getString("rol"));
                    u.setActivo(rs.getBoolean("activo"));

                    registrarEventoSesion(u.getId(), "INICIO_SESION");
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    public boolean esCuentaInactiva(String identificador, String password) {
        String sql = "SELECT id FROM usuarios WHERE (CAST(id AS CHAR) = ? OR correo = ?) AND password = ? AND activo = 0";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, identificador.trim());
            ps.setString(2, identificador.trim());
            ps.setString(3, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public void registrarEventoSesion(int idUsuario, String accion) {
        String sql = "INSERT INTO bitacora_sesiones (id_usuario, accion) VALUES (?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setString(2, accion);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   
    public List<Actividad> listarBitacora() {
        List<Actividad> lista = new ArrayList<>();
        String sql = "SELECT DATE_FORMAT(b.fecha_hora, '%Y-%m-%d %H:%i:%s') AS fecha, u.nombre, b.accion " +
                     "FROM bitacora_sesiones b " +
                     "INNER JOIN usuarios u ON b.id_usuario = u.id " +
                     "ORDER BY b.fecha_hora DESC LIMIT 25";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Actividad(
                    rs.getString("fecha"),
                    rs.getString("nombre"),
                    rs.getString("accion")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

   
    public String obtenerRolActivo(int idUsuario) {
        String sql = "SELECT rol FROM usuarios WHERE id = ? AND activo = 1";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("rol");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

   
    public Map<String, Boolean> validarPermisosRol(String rol) {
        Map<String, Boolean> permisos = new HashMap<>();
        permisos.put("CREAR_EMPLEADOS", "ADMIN".equals(rol) || "GERENTE".equals(rol));
        permisos.put("ELIMINAR_REGISTROS", "ADMIN".equals(rol));
        permisos.put("EDITAR_HABITACIONES", "ADMIN".equals(rol) || "GERENTE".equals(rol));
        permisos.put("GESTIONAR_RESERVAS", true);
        return permisos;
    }
}