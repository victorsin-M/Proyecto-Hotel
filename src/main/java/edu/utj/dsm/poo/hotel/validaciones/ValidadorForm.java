package edu.utj.dsm.poo.hotel.validaciones;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;


public class ValidadorForm {

    private static class Regla {
        Label error;
        Supplier<String> regla;
    }

    private final List<Regla> reglas = new ArrayList<>();
    private final Runnable alCambiar; 

    public ValidadorForm(Runnable alCambiar) {
        this.alCambiar = alCambiar;
    }

    public void agregar(Label error, Supplier<String> regla, ObservableValue<?>... origenes) {
        Regla r = new Regla();
        r.error = error;
        r.regla = regla;
        reglas.add(r);
        for (ObservableValue<?> origen : origenes) {
            origen.addListener((obs, anterior, nuevo) -> {
                mostrar(r);
                alCambiar.run();
            });
        }
    }

    public boolean esValido() {
        for (Regla r : reglas) {
            if (r.regla.get() != null) {
                return false;
            }
        }
        return true;
    }

    public boolean validarTodo() {
        boolean valido = true;
        for (Regla r : reglas) {
            mostrar(r);
            if (r.regla.get() != null) {
                valido = false;
            }
        }
        return valido;
    }

    public void limpiarMensajes() {
        for (Regla r : reglas) {
            r.error.setText("");
        }
    }

    private void mostrar(Regla r) {
        String mensaje = r.regla.get();
        r.error.setText(mensaje == null ? "" : mensaje);
    }
}