
package edu.utj.dsm.poo.hotel.modelo;

import java.util.Objects;

/**
 *
 * @author Victor Meza
 */
public abstract class Habitacion {

    

   
    protected String numeroHabitacion;
    protected String tipo;
    protected  double precioPorNoche;
    protected int capacidad;
    protected String estado;
    protected int piso;
    
    
    
    public Habitacion(){
    this.numeroHabitacion = "";
    this.tipo = "";
    this.precioPorNoche = 0.0;
    this.capacidad = 0;
    this.estado = "Disponible";
    this.piso = 0;
}
    public Habitacion(String numeroHabitacion, String tipo, int piso) {
    this.numeroHabitacion = numeroHabitacion;
    this.tipo = tipo;
    this.piso = piso;
    this.precioPorNoche = 0.0;
    this.capacidad = 1;
    this.estado = "Disponible";
}
    public void setNumeroHabitacion(String numeroHabitacion){
        this.numeroHabitacion = numeroHabitacion;
    }
    public void setTipo(String tipo){
        this.tipo = tipo;
    }
    public void setPrecioPorNoche(double precioPorNoche){
        this.precioPorNoche = precioPorNoche;
    }
    public void setCapacidad(int capacidad){
        this.capacidad = capacidad;
    }
    public void setEstado(String estado){
        this.estado = estado;
    }
    public void setPiso(int piso){
        this.piso = piso;
    }
    
    public String getNumeroHabitacion(){
        return this.numeroHabitacion;
    }
    public String getTipo(){
        return this.tipo;
    }
    public double getPrecioPorNoche(){
        return this.precioPorNoche;
    }
    public int getCapacidad(){
        return  this.capacidad;
    }
    public String getEstado(){
        return this.estado;
    }
    public int getPiso(){
        return this.piso;
    }
     @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.numeroHabitacion);
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
        final Habitacion other = (Habitacion) obj;
        return Objects.equals(this.numeroHabitacion, other.numeroHabitacion);
    }
    @Override
    public String toString() {
        return "Habitacion{" + "numeroHabitacion=" + numeroHabitacion + ", tipo=" + tipo + ", precioPorNoche=" + precioPorNoche + ", capacidad=" + capacidad + ", estado=" + estado + ", piso=" + piso + '}';
    }
}

