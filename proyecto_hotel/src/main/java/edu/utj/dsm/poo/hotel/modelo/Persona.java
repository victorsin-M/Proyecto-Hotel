/*
  Nombre Victor Uriel Meza Arias
 */
package edu.utj.dsm.poo.hotel.modelo;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Victor Meza
 */
public abstract class  Persona  {
 private int id;
private String rfc;
private String nombre;
private String primerApellido;
private String segundoApellido;
private Date nacimiento;
private String rutaImagen;
    
    /**
     * Sirve para Inicializar Los atributos
     * O Bien Para Recibir Datos Externos de Inicializacion
     */
   public Persona() {
        this.rfc = rfc;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.nacimiento = nacimiento;
        this.segundoApellido = "";
        this.rutaImagen = "";
    }
    public void setRfc(String rfc){
        this.rfc = rfc;
        
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setPrimerApellido(String primerApellido){
        this.primerApellido = primerApellido;
    }
    public void setSegunfoApellido(String segungoApellido){
        this.segundoApellido = segungoApellido;
    }
    public void setNacimiento(Date nacimiento){
        this.nacimiento = nacimiento;
    }
    public void setRutaImagen(String rutaImagen){
        this.rutaImagen = rutaImagen;
    }
    
    
    
    public String getRfc(){
        return this.rfc;
    }
    public String getNombre(){
        return  this.nombre;
    }
    public String getPrimerApellido(){
        return this.primerApellido;
    }
    public String getSegundoApelllido(){
        return this.segundoApellido;
    }
    public Date getNacimiento(){
        return  this.nacimiento;
    }
    public String getRutaImagen(){
        return  this.rutaImagen;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.rfc);
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
        final Persona other = (Persona) obj;
        return Objects.equals(this.rfc, other.rfc);
    }

    @Override
    public String toString() {
        return "Persona{" + "rfc=" + rfc + ", nombre=" + nombre + ", primerApellido=" + primerApellido + ", segundoApellido=" + segundoApellido + ", nacimiento=" + nacimiento + ", rutaImagen=" + rutaImagen + '}';
    }
    
}
