/*
 * Victor Uriel Meza Arias
 */
package edu.utj.dsm.poo.hotel.modelo;

import java.util.Objects;

/**
 *
 * @author Victor Meza
 */
public final class Administrador extends Empleado{

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.rol);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Administrador other = (Administrador) obj;
        return Objects.equals(this.rol, other.rol);
    }
    private String rol;
    
        
}
