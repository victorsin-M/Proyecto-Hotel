package edu.utj.dsm.poo.hotel.modelo;

public class SesionUsuario {
    private static Usuario usuarioActual;

    public static void iniciarSesion(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }

    public static String getRolActual() {
        return usuarioActual != null ? usuarioActual.getRol() : "";
    }



    public static boolean puedeAccederModulo(String modulo) {
        String rol = getRolActual();
        switch (modulo) {
            case "DASHBOARD":
            case "HABITACIONES":
            case "RESERVAS":
            case "CLIENTES":
                return rol.equals("ADMIN") || rol.equals("GERENTE") || rol.equals("RECEPCIONISTA");
            case "EMPLEADOS":
                return rol.equals("ADMIN") || rol.equals("GERENTE");
            case "CONFIGURACION":
                return rol.equals("ADMIN");
            default:
                return false;
        }
    }

    public static boolean puedeCrear(String modulo) {
        String rol = getRolActual();
        if (rol.equals("ADMIN")) return true;
        if (rol.equals("GERENTE")) return !modulo.equals("CONFIGURACION");
        if (rol.equals("RECEPCIONISTA")) return modulo.equals("RESERVAS") || modulo.equals("CLIENTES");
        return false;
    }

    public static boolean puedeEditar(String modulo) {
        String rol = getRolActual();
        if (rol.equals("ADMIN")) return true;
        if (rol.equals("GERENTE")) return !modulo.equals("CONFIGURACION");
        if (rol.equals("RECEPCIONISTA")) return modulo.equals("RESERVAS") || modulo.equals("CLIENTES");
        return false;
    }

    public static boolean puedeEliminar(String modulo) {
        return getRolActual().equals("ADMIN");
    }
}