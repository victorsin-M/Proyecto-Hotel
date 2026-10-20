package edu.utj.dsm.poo.hotel.controlador;

import java.time.LocalDate;
import edu.utj.dsm.poo.hotel.validaciones.ValidadorForm;
import edu.utj.dsm.poo.hotel.validaciones.Validaciones;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import edu.utj.dsm.poo.hotel.modelo.Cliente;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Victor Meza
 */
public class GestionUsuariosController implements Initializable {
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
    private TableView<Cliente> tblClientes;
    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtRfc;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TableColumn<Cliente,String> colNombre;
    @FXML
    private TableColumn<Cliente,String> colCorreo;
    @FXML
    private TableColumn<Cliente,String> colTelefono;
    @FXML
    private TableColumn<Cliente,String> colRfc;

    /**
     * Initializes the controller class.
     */
    @Override   
    public void initialize(URL url, ResourceBundle rb) {
        configurarValidaciones();
        colNombre.setCellValueFactory(new  PropertyValueFactory<Cliente,String>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colRfc.setCellValueFactory(new PropertyValueFactory<>("rfc"));
        
        tblClientes.setItems(listaClientes);
        
        tblClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
            Cliente seleccionado =  newSelection;
            txtNombre.setText(seleccionado.getNombre());
            txtEmail.setText(seleccionado.getEmail());
            txtTelefono.setText(seleccionado.getTelefono());
            txtRfc.setText(seleccionado.getRfc());
        }
    });
    }    
    
    @FXML
    private void guardarCliente(ActionEvent event){
        
        String nombre = txtNombre.getText();
        String email = txtEmail.getText();
        String telefono = txtTelefono.getText();
        String rfc = txtRfc.getText();
        Cliente nuevoCliente = new Cliente(rfc, nombre, email, telefono);
       listaClientes.add(nuevoCliente);
       
    }

    @FXML
    private void limpiarCampos(ActionEvent event) {
        txtNombre.clear();
        txtEmail.clear();
        txtRfc.clear();
        txtTelefono.clear();
        validador.limpiarMensajes();
        actualizarBotones();
    }
    
    @FXML
    private void eliminarCliente(ActionEvent event){
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Acción de demostración");
        alerta.setHeaderText(null);
        alerta.setContentText("En esta actividad la acción es solo visual: no se modifica ni se elimina información.");
        alerta.showAndWait();
    }

    @FXML
private void editarCliente(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Acción de demostración");
        alerta.setHeaderText(null);
        alerta.setContentText("En esta actividad la acción es solo visual: no se modifica ni se elimina información.");
        alerta.showAndWait();
    }

    // ---------- Validaciones en tiempo real ----------

    @FXML
    private Label errNombre, errEmail, errRfc, errTelefono;
    @FXML
    private Button btnValidar;

    private ValidadorForm validador;

    private void configurarValidaciones() {
        validador = new ValidadorForm(this::actualizarBotones);

        // Filtros: bloquean caracteres no permitidos al teclear y al pegar
        Validaciones.soloLetras(txtNombre, errNombre);
        Validaciones.rfc(txtRfc, errRfc);
        Validaciones.soloDigitos(txtTelefono, errTelefono, 10);

        // Reglas: se evalúan cada vez que el campo cambia (escribir, borrar o pegar)
        validador.agregar(errNombre, () -> Validaciones.letras(txtNombre.getText()),
                txtNombre.textProperty());
        validador.agregar(errEmail, () -> Validaciones.email(txtEmail.getText()),
                txtEmail.textProperty());
        validador.agregar(errRfc, () -> Validaciones.rfc(txtRfc.getText()),
                txtRfc.textProperty());
        validador.agregar(errTelefono, () -> Validaciones.digitos(txtTelefono.getText(), 10),
                txtTelefono.textProperty());

        tblClientes.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nuevo) -> actualizarBotones());
        actualizarBotones();
    }

    // Guardar solo se habilita si todo es válido; Editar y Eliminar, si hay un registro seleccionado
    private void actualizarBotones() {
        btnGuardar.setDisable(!validador.esValido());
        boolean haySeleccion = tblClientes.getSelectionModel().getSelectedItem() != null;
        btnEditar.setDisable(!haySeleccion);
        btnEliminar.setDisable(!haySeleccion);
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
}