
package edu.utj.dsm.poo.hotel;

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
    }
    
    @FXML
    private void eliminarCliente(ActionEvent event){
       Cliente seleccionado =  tblClientes.getSelectionModel().getSelectedItem(); 
       
       if(seleccionado != null){
           listaClientes.remove(seleccionado);
       }
    }

    @FXML
private void editarCliente(ActionEvent event) {
    Cliente seleccionado =  tblClientes.getSelectionModel().getSelectedItem();
    
    if (seleccionado != null) {
        seleccionado.setNombre(txtNombre.getText());
        seleccionado.setEmail(txtEmail.getText());
        seleccionado.setTelefono(txtTelefono.getText());
        seleccionado.setRfc(txtRfc.getText());
        
        tblClientes.refresh();
        
        limpiarCampos(event);
    }
}
}
