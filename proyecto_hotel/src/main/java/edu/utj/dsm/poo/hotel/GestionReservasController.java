package edu.utj.dsm.poo.hotel;

import edu.utj.dsm.poo.hotel.modelo.Cliente;
import edu.utj.dsm.poo.hotel.modelo.Reservacion;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class GestionReservasController implements Initializable {

    @FXML
    private ImageView imgLogo;
    @FXML
    private Button btnGuardarReserva;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField txtNombreCompleto;
    @FXML
    private TextField txtTelefono;
    @FXML
    private ComboBox<String> cbHabitacion;
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaFin;

    private ObservableList<Reservacion> listaReservaciones = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbHabitacion.setItems(FXCollections.observableArrayList(
            "101 - Sencilla ($500/noche)",
            "102 - Doble ($800/noche)",
            "201 - Suite Presidencial ($1500/noche)"
        ));
    }

    @FXML
    private void guardarReserva(ActionEvent event) {
        String nombre = txtNombreCompleto.getText();
        String telefono = txtTelefono.getText();
        String habitacionSeleccionada = cbHabitacion.getValue();
        LocalDate fechaEntrada = dpFechaInicio.getValue();
        LocalDate fechaSalida = dpFechaFin.getValue();

        
        if (nombre.trim().isEmpty() || telefono.trim().isEmpty() || habitacionSeleccionada == null || fechaEntrada == null || fechaSalida == null) {
            mostrarAlerta("Campos Incompletos", "Por favor llena todos los campos para continuar.", Alert.AlertType.WARNING);
            return;
        }

    
        if (fechaSalida.isBefore(fechaEntrada) || fechaSalida.isEqual(fechaEntrada)) {
            mostrarAlerta("Fechas Inválidas", "La fecha de salida debe ser posterior a la fecha de entrada.", Alert.AlertType.ERROR);
            return;
        }

        
        Cliente cliente = new Cliente("", nombre, "", telefono);

        long noches = ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
        double costoTotal = noches * 600.0;

        Reservacion nuevaReserva = new Reservacion();
        nuevaReserva.setFolio("RES-" + (System.currentTimeMillis() % 10000));
        nuevaReserva.setCliente(cliente);
        nuevaReserva.setHabitacion(null); 
        nuevaReserva.setFechaEntrada(fechaEntrada);
        nuevaReserva.setFechaSalida(fechaSalida);
        nuevaReserva.setCostoTotal(costoTotal);
        nuevaReserva.setEstado("CONFIRMADA");

        listaReservaciones.add(nuevaReserva);

        mostrarAlerta("Reserva Guardada", 
            "Folio: " + nuevaReserva.getFolio() + 
            "\nCliente: " + cliente.getNombre() + 
            "\nTotal (" + noches + " noches): $" + costoTotal, 
            Alert.AlertType.INFORMATION);

        limpiarCampos(event);
    }

    @FXML
    private void cancelar(ActionEvent event) {
        limpiarCampos(event);
    }

    private void limpiarCampos(ActionEvent event) {
        txtNombreCompleto.clear();
        txtTelefono.clear();
        cbHabitacion.getSelectionModel().clearSelection();
        dpFechaInicio.setValue(null);
        dpFechaFin.setValue(null);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}