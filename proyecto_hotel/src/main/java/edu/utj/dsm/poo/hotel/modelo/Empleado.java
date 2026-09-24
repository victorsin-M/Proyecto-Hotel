
package edu.utj.dsm.poo.hotel.modelo;


/**
 *
 * @author Victor Meza
 */
public abstract class Empleado  extends Persona{
    protected String usuario;//Por Default los string inician en null
    protected String contraseña;
    
   


public Empleado(){
    this.usuario = "";
    this.contraseña = "";
}
public void setUsuario(String usuario){
        this.usuario = usuario;
        
    }
public void setContraseña(String contraseña){
        this.contraseña = contraseña;
        
    }



public String getUsuario(){
        return this.usuario;
    }
public String getContraseña(){
        return this.contraseña;
}


}