package edu.utj.dsm.poo.hotel;

import edu.utj.dsm.poo.hotel.modelo.Administrador;
import edu.utj.dsm.poo.hotel.modelo.Empleado;
import edu.utj.dsm.poo.hotel.modelo.Mantenimiento;
import edu.utj.dsm.poo.hotel.modelo.Recepcion;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Victor Meza
 */
public class GestionEmpleadosController implements Initializable {

    @FXML
    private ImageView imgEmpleado;
    @FXML
    private Button btnImagen;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtPrimerApellido;
    @FXML
    private TextField txtSegundoApellido;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtRfc;
    @FXML
    private TextField txtTelefono;
    @FXML
    private DatePicker dpNacimiento;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField pwdContrasena;
    @FXML
    private ComboBox<String> cbRol;
    @FXML
    private TableView<Empleado> tblEmpleados;
    @FXML
    private TableColumn<Empleado, String> colNombre;
    @FXML
    private TableColumn<Empleado, String> colApellido;
    @FXML
    private TableColumn<Empleado, String> colUsuario;
    @FXML
    private TableColumn<Empleado, String> colRol;
    @FXML
    private TableColumn<Empleado, String> colCorreo;
    @FXML
    private TableColumn<Empleado, String> colTelefono;
    @FXML
    private TableColumn<Empleado, String> colRfc;

    private ObservableList<Empleado> listaEmpleados = FXCollections.observableArrayList();

    private String rutaImagenActual = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("primerApellido"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colRfc.setCellValueFactory(new PropertyValueFactory<>("rfc"));

        cbRol.getItems().addAll("Administrador", "Recepción", "Mantenimiento");

        tblEmpleados.setItems(listaEmpleados);

        tblEmpleados.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                llenarFormulario(newSelection);
            }
        });
    }

    @FXML
    private void guardarEmpleado(ActionEvent event) {
        if (!camposValidos()) {
            return;
        }

        String rfc = txtRfc.getText().trim();
        if (buscarPorRfc(rfc) != -1) {
            mostrarAlerta(Alert.AlertType.WARNING, "RFC repetido", "Ya existe un empleado con ese RFC.");
            return;
        }

        Empleado nuevo = crearEmpleadoPorRol(cbRol.getValue());
        pasarDatosDelFormulario(nuevo);
        listaEmpleados.add(nuevo);

        limpiarCampos(event);
        mostrarAlerta(Alert.AlertType.INFORMATION, "Listo", "Empleado guardado correctamente.");
    }

    @FXML
    private void limpiarCampos(ActionEvent event) {
        txtNombre.clear();
        txtPrimerApellido.clear();
        txtSegundoApellido.clear();
        txtEmail.clear();
        txtRfc.clear();
        txtTelefono.clear();
        txtUsuario.clear();
        pwdContrasena.clear();
        dpNacimiento.setValue(null);
        cbRol.setValue(null);
        rutaImagenActual = "";
        imgEmpleado.setImage(null);
        tblEmpleados.getSelectionModel().clearSelection();
    }

    @FXML
    private void editarEmpleado(ActionEvent event) {
        int indice = tblEmpleados.getSelectionModel().getSelectedIndex();
        if (indice < 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin selección", "Selecciona un empleado de la tabla para editarlo.");
            return;
        }
        if (!camposValidos()) {
            return;
        }

        int indiceRfc = buscarPorRfc(txtRfc.getText().trim());
        if (indiceRfc != -1 && indiceRfc != indice) {
            mostrarAlerta(Alert.AlertType.WARNING, "RFC repetido", "Ese RFC ya pertenece a otro empleado.");
            return;
        }

        
        Empleado actualizado = crearEmpleadoPorRol(cbRol.getValue());
        pasarDatosDelFormulario(actualizado);
        listaEmpleados.set(indice, actualizado);

        limpiarCampos(event);
    }

    @FXML
    private void eliminarEmpleado(ActionEvent event) {
        int indice = tblEmpleados.getSelectionModel().getSelectedIndex();
        if (indice < 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin selección", "Selecciona un empleado de la tabla para eliminarlo.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar empleado");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Seguro que quieres eliminar a este empleado?");
        Optional<ButtonType> respuesta = confirmacion.showAndWait();

        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            listaEmpleados.remove(indice); 
            limpiarCampos(event);
        }
    }

    @FXML
    private void seleccionarImagen(ActionEvent event) {
        FileChooser selector = new FileChooser();
        selector.setTitle("Elige la foto del empleado");
        selector.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));

        File archivo = selector.showOpenDialog(imgEmpleado.getScene().getWindow());
        if (archivo != null) {
            rutaImagenActual = archivo.getAbsolutePath();
            mostrarImagen(rutaImagenActual);
        }
    }

    
    private Empleado crearEmpleadoPorRol(String rol) {
        Empleado nuevo = null;
        if (rol.equals("Administrador")) {
            nuevo = new Administrador();
        } else if (rol.equals("Recepción")) {
            nuevo = new Recepcion();
        } else if (rol.equals("Mantenimiento")) {
            nuevo = new Mantenimiento();
        }
        return nuevo;
    }

    private void pasarDatosDelFormulario(Empleado e) {
        e.setNombre(txtNombre.getText().trim());
        e.setPrimerApellido(txtPrimerApellido.getText().trim());
        e.setSegundoApellido(txtSegundoApellido.getText().trim());
        e.setEmail(txtEmail.getText().trim());
        e.setRfc(txtRfc.getText().trim());
        e.setTelefono(txtTelefono.getText().trim());
        e.setUsuario(txtUsuario.getText().trim());
        e.setContraseña(pwdContrasena.getText()); 
        e.setRutaImagen(rutaImagenActual);

        LocalDate fecha = dpNacimiento.getValue();
        if (fecha != null) {
            e.setNacimiento(Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
    }

    private void llenarFormulario(Empleado e) {
        txtNombre.setText(e.getNombre());
        txtPrimerApellido.setText(e.getPrimerApellido());
        txtSegundoApellido.setText(e.getSegundoApellido());
        txtEmail.setText(e.getEmail());
        txtRfc.setText(e.getRfc());
        txtTelefono.setText(e.getTelefono());
        txtUsuario.setText(e.getUsuario());
        pwdContrasena.setText(e.getContraseña());
        cbRol.setValue(e.getRol());

        if (e.getNacimiento() != null) {
            dpNacimiento.setValue(e.getNacimiento().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dpNacimiento.setValue(null);
        }

        rutaImagenActual = e.getRutaImagen();
        mostrarImagen(rutaImagenActual);
    }

    private void mostrarImagen(String ruta) {
        if (ruta != null && !ruta.isEmpty()) {
            File archivo = new File(ruta);
            if (archivo.exists()) {
                imgEmpleado.setImage(new Image(archivo.toURI().toString()));
                return;
            }
        }
        imgEmpleado.setImage(null);
    }

    private boolean camposValidos() {
        if (txtNombre.getText().trim().isEmpty()
                || txtPrimerApellido.getText().trim().isEmpty()
                || txtEmail.getText().trim().isEmpty()
                || txtTelefono.getText().trim().isEmpty()
                || txtRfc.getText().trim().isEmpty()
                || txtUsuario.getText().trim().isEmpty()
                || pwdContrasena.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Faltan datos", "Llena todos los campos obligatorios.");
            return false;
        }
        if (cbRol.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Falta el rol", "Elige un rol para el empleado.");
            return false;
        }
        if (!txtEmail.getText().contains("@")) {
            mostrarAlerta(Alert.AlertType.WARNING, "Correo inválido", "El correo debe contener un @.");
            return false;
        }
        if (!txtTelefono.getText().trim().matches("\\d{10}")) {
            mostrarAlerta(Alert.AlertType.WARNING, "Teléfono inválido", "El teléfono debe tener 10 dígitos.");
            return false;
        }
        return true;
    }

    private int buscarPorRfc(String rfc) {
        for (int i = 0; i < listaEmpleados.size(); i++) {
            if (listaEmpleados.get(i).getRfc().equalsIgnoreCase(rfc)) {
                return i;
            }
        }
        return -1;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}