package edu.utj.dsm.poo.hotel;

import edu.utj.dsm.poo.hotel.modelo.Habitacion;
import edu.utj.dsm.poo.hotel.modelo.HabitacionSencilla;
import edu.utj.dsm.poo.hotel.modelo.Suite;
import java.net.URL;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Victor Meza
 */
public class GestionHabitacionesController implements Initializable {

    @FXML
    private TextField txtNumero;
    @FXML
    private ComboBox<String> cbTipo;
    @FXML
    private TextField txtPiso;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtCapacidad;
    @FXML
    private TextField txtCamas;
    @FXML
    private ComboBox<String> cbEstado;
    @FXML
    private CheckBox chkBalcon;
    @FXML
    private CheckBox chkVistaAlMar;
    @FXML
    private CheckBox chkJacuzzi;
    @FXML
    private CheckBox chkMiniBar;
    @FXML
    private CheckBox chkDesayuno;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TableView<Habitacion> tblHabitaciones;
    // Fíjate: piso y capacidad son int (Integer), el precio es double (Double)
    @FXML
    private TableColumn<Habitacion, String> colNumero;
    @FXML
    private TableColumn<Habitacion, String> colTipo;
    @FXML
    private TableColumn<Habitacion, Integer> colPiso;
    @FXML
    private TableColumn<Habitacion, Double> colPrecio;
    @FXML
    private TableColumn<Habitacion, Integer> colCapacidad;
    @FXML
    private TableColumn<Habitacion, String> colEstado;

    private ObservableList<Habitacion> listaHabitaciones = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroHabitacion"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colPiso.setCellValueFactory(new PropertyValueFactory<>("piso"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioPorNoche"));
        colCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        cbTipo.getItems().addAll("Sencilla", "Suite");
        cbEstado.getItems().addAll("Disponible", "Ocupada", "Mantenimiento");
        cbEstado.setValue("Disponible");

        tblHabitaciones.setItems(listaHabitaciones);

        // Cada vez que cambia el tipo, se activan solo los extras que le corresponden
        cbTipo.valueProperty().addListener((obs, tipoAnterior, tipoNuevo) -> {
            ajustarExtras(tipoNuevo);
        });
        ajustarExtras(null);

        tblHabitaciones.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                llenarFormulario(newSelection);
            }
        });
    }

    @FXML
    private void guardarHabitacion(ActionEvent event) {
        Habitacion nueva = construirDesdeFormulario();
        if (nueva == null) {
            return; // ya se mostró el mensaje de error
        }

        if (buscarPorNumero(nueva.getNumeroHabitacion()) != -1) {
            mostrarAlerta(Alert.AlertType.WARNING, "Número repetido", "Ya existe una habitación con ese número.");
            return;
        }

        listaHabitaciones.add(nueva);
        limpiarCampos(event);
        mostrarAlerta(Alert.AlertType.INFORMATION, "Listo", "Habitación guardada correctamente.");
    }

    @FXML
    private void editarHabitacion(ActionEvent event) {
        int indice = tblHabitaciones.getSelectionModel().getSelectedIndex();
        if (indice < 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin selección", "Selecciona una habitación de la tabla para editarla.");
            return;
        }

        Habitacion actualizada = construirDesdeFormulario();
        if (actualizada == null) {
            return;
        }

        int indiceNumero = buscarPorNumero(actualizada.getNumeroHabitacion());
        if (indiceNumero != -1 && indiceNumero != indice) {
            mostrarAlerta(Alert.AlertType.WARNING, "Número repetido", "Ese número ya pertenece a otra habitación.");
            return;
        }

        // Se reemplaza el objeto completo (el tipo pudo cambiar); set() avisa a la tabla
        listaHabitaciones.set(indice, actualizada);
        limpiarCampos(event);
    }

    @FXML
    private void eliminarHabitacion(ActionEvent event) {
        int indice = tblHabitaciones.getSelectionModel().getSelectedIndex();
        if (indice < 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin selección", "Selecciona una habitación de la tabla para eliminarla.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar habitación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Seguro que quieres eliminar esta habitación?");
        Optional<ButtonType> respuesta = confirmacion.showAndWait();

        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            listaHabitaciones.remove(indice);
            limpiarCampos(event);
        }
    }

    @FXML
    private void limpiarCampos(ActionEvent event) {
        txtNumero.clear();
        txtPiso.clear();
        txtPrecio.clear();
        txtCapacidad.clear();
        txtCamas.clear();
        cbTipo.setValue(null);
        cbEstado.setValue("Disponible");
        chkBalcon.setSelected(false);
        chkVistaAlMar.setSelected(false);
        chkJacuzzi.setSelected(false);
        chkMiniBar.setSelected(false);
        chkDesayuno.setSelected(false);
        tblHabitaciones.getSelectionModel().clearSelection();
    }

    

    
    private Habitacion construirDesdeFormulario() {
        if (txtNumero.getText().trim().isEmpty()
                || txtPiso.getText().trim().isEmpty()
                || txtPrecio.getText().trim().isEmpty()
                || txtCapacidad.getText().trim().isEmpty()
                || txtCamas.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Faltan datos", "Llena todos los campos.");
            return null;
        }
        if (cbTipo.getValue() == null || cbEstado.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Faltan datos", "Elige el tipo y el estado de la habitación.");
            return null;
        }

        try {
            
            int piso = Integer.parseInt(txtPiso.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int capacidad = Integer.parseInt(txtCapacidad.getText().trim());
            int camas = Integer.parseInt(txtCamas.getText().trim());

            if (piso < 0 || precio <= 0 || capacidad <= 0 || camas <= 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Datos inválidos",
                        "El precio, la capacidad y las camas deben ser mayores que cero, y el piso no puede ser negativo.");
                return null;
            }

            Habitacion h = crearHabitacionPorTipo(cbTipo.getValue());
            h.setNumeroHabitacion(txtNumero.getText().trim());
            h.setTipo(cbTipo.getValue());
            h.setPiso(piso);
            h.setPrecioPorNoche(precio);
            h.setCapacidad(capacidad);
            h.setEstado(cbEstado.getValue());

            
            if (h instanceof HabitacionSencilla) {
                HabitacionSencilla sencilla = (HabitacionSencilla) h;
                sencilla.setNumeroDeCamas(camas);
                sencilla.setBalcon(chkBalcon.isSelected());
                sencilla.setVistaAlMar(chkVistaAlMar.isSelected());
            } else if (h instanceof Suite) {
                Suite suite = (Suite) h;
                suite.setNumeroDeCamas(camas);
                suite.setJacuzzi(chkJacuzzi.isSelected());
                suite.setMiniBar(chkMiniBar.isSelected());
                suite.setIncluyeDesayuno(chkDesayuno.isSelected());
            }
            return h;

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos inválidos",
                    "Piso, precio, capacidad y camas deben ser números.");
            return null;
        }
    }

    private Habitacion crearHabitacionPorTipo(String tipo) {
        Habitacion nueva = null;
        if (tipo.equals("Sencilla")) {
            nueva = new HabitacionSencilla();
        } else if (tipo.equals("Suite")) {
            nueva = new Suite();
        }
        return nueva;
    }

    private void llenarFormulario(Habitacion h) {
        txtNumero.setText(h.getNumeroHabitacion());
        txtPiso.setText(String.valueOf(h.getPiso()));
        txtPrecio.setText(String.valueOf(h.getPrecioPorNoche()));
        txtCapacidad.setText(String.valueOf(h.getCapacidad()));
        cbEstado.setValue(h.getEstado());
        cbTipo.setValue(h.getTipo()); 

        if (h instanceof HabitacionSencilla) {
            HabitacionSencilla sencilla = (HabitacionSencilla) h;
            txtCamas.setText(String.valueOf(sencilla.getNumeroDeCamas()));
            chkBalcon.setSelected(sencilla.isBalcon());
            chkVistaAlMar.setSelected(sencilla.isVistaAlMar());
        } else if (h instanceof Suite) {
            Suite suite = (Suite) h;
            txtCamas.setText(String.valueOf(suite.getNumeroDeCamas()));
            chkJacuzzi.setSelected(suite.isJacuzzi());
            chkMiniBar.setSelected(suite.isMiniBar());
            chkDesayuno.setSelected(suite.isIncluyeDesayuno());
        }
    }

    private void ajustarExtras(String tipo) {
        boolean esSencilla = "Sencilla".equals(tipo); 
        boolean esSuite = "Suite".equals(tipo);

        chkBalcon.setDisable(!esSencilla);
        chkVistaAlMar.setDisable(!esSencilla);
        chkJacuzzi.setDisable(!esSuite);
        chkMiniBar.setDisable(!esSuite);
        chkDesayuno.setDisable(!esSuite);

        if (!esSencilla) {
            chkBalcon.setSelected(false);
            chkVistaAlMar.setSelected(false);
        }
        if (!esSuite) {
            chkJacuzzi.setSelected(false);
            chkMiniBar.setSelected(false);
            chkDesayuno.setSelected(false);
        }
    }

    private int buscarPorNumero(String numero) {
        for (int i = 0; i < listaHabitaciones.size(); i++) {
            if (listaHabitaciones.get(i).getNumeroHabitacion().equalsIgnoreCase(numero)) {
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