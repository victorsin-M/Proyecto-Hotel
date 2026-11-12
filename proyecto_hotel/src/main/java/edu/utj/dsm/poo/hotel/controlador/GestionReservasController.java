package edu.utj.dsm.poo.hotel.controlador;

import edu.utj.dsm.poo.hotel.modelo.Cliente;
import edu.utj.dsm.poo.hotel.modelo.Reservacion;
import edu.utj.dsm.poo.hotel.persistencia.ReservacionDAO;
import edu.utj.dsm.poo.hotel.validaciones.ValidadorForm;
import edu.utj.dsm.poo.hotel.validaciones.Validaciones;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;


public class GestionReservasController implements Initializable {

    @FXML private ImageView imgLogo;
    @FXML private Button btnGuardarReserva;
    @FXML private Button btnCancelar;
    @FXML private TextField txtNombreCompleto;
    @FXML private TextField txtTelefono;
    @FXML private ComboBox<String> cbHabitacion;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;

    @FXML private Label errNombreCompleto, errTelefono, errHabitacion, errFechaInicio, errFechaFin;
    @FXML private Button btnNuevo, btnValidar;

    private ObservableList<Reservacion> listaReservaciones = FXCollections.observableArrayList();
    private ReservacionDAO reservacionDAO = new ReservacionDAO();
    private ValidadorForm validador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarValidaciones();
        cbHabitacion.setItems(FXCollections.observableArrayList(
            "101 - Sencilla ($500/noche)",
            "102 - Doble ($800/noche)",
            "201 - Suite Presidencial ($1500/noche)"
        ));

        cargarDatosDesdeBD();
    }

    private void cargarDatosDesdeBD() {
        listaReservaciones.setAll(reservacionDAO.listarTodas());
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

        if (reservacionDAO.guardar(nuevaReserva)) {
            cargarDatosDesdeBD();
            mostrarAlerta("Reserva Guardada", 
                "Folio: " + nuevaReserva.getFolio() + 
                "\nCliente: " + cliente.getNombre() + 
                "\nTotal (" + noches + " noches): $" + costoTotal, 
                Alert.AlertType.INFORMATION);
            limpiarCampos(event);
        } else {
            mostrarAlerta("Error de BD", "No se pudo guardar la reserva en MySQL.", Alert.AlertType.ERROR);
        }
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
        validador.limpiarMensajes();
        actualizarBotones();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // ---------- Validaciones en tiempo real ----------
private void configurarValidaciones() {
    validador = new ValidadorForm(this::actualizarBotones);

    // 1. Filtros de entrada en tiempo real (bloquean caracteres no válidos al teclear)
    Validaciones.soloLetras(txtNombreCompleto, errNombreCompleto);
    Validaciones.soloDigitos(txtTelefono, errTelefono, 10);

    // 2. Reglas de validación agregadas al validador de formulario
    validador.agregar(errNombreCompleto, 
            () -> Validaciones.letras(txtNombreCompleto.getText()),
            txtNombreCompleto.textProperty());

    validador.agregar(errTelefono, 
            () -> Validaciones.digitos(txtTelefono.getText(), 10),
            txtTelefono.textProperty());

    validador.agregar(errHabitacion, 
            () -> Validaciones.seleccion(cbHabitacion.getValue()),
            cbHabitacion.valueProperty());

    // Validación de fecha de entrada (no puede ser anterior a la fecha actual)
    validador.agregar(errFechaInicio, 
            () -> {
                LocalDate inicio = dpFechaInicio.getValue();
                if (inicio == null) return "Selecciona una fecha";
                if (inicio.isBefore(LocalDate.now())) return "La entrada no puede ser anterior a hoy";
                return null;
            },
            dpFechaInicio.valueProperty());

    // Validación de fecha de salida (depende de que exista fecha de entrada y sea posterior)
    validador.agregar(errFechaFin, 
            () -> {
                LocalDate inicio = dpFechaInicio.getValue();
                LocalDate fin = dpFechaFin.getValue();
                if (fin == null) return "Selecciona una fecha";
                if (inicio == null) return "Selecciona primero la fecha de entrada";
                if (!fin.isAfter(inicio)) return "Debe ser posterior a la fecha de entrada";
                return null;
            },
            dpFechaFin.valueProperty(), dpFechaInicio.valueProperty());

    actualizarBotones();
}

    private void actualizarBotones() {
        btnGuardarReserva.setDisable(!validador.esValido());
    }

    @FXML
    private void validarFormulario(ActionEvent event) {
        if (validador.validarTodo()) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Validación");
            alerta.setHeaderText(null);
            alerta.setContentText("Todos los datos cumplen las validaciones.");
            alerta.showAndWait();
        }
        actualizarBotones();
    }

    @FXML
    private void nuevo(ActionEvent event) {
        limpiarCampos(event);
    }
}