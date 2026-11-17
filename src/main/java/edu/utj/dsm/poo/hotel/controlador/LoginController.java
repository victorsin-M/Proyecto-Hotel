package edu.utj.dsm.poo.hotel.controlador;

import edu.utj.dsm.poo.hotel.App;
import edu.utj.dsm.poo.hotel.modelo.SesionUsuario;
import edu.utj.dsm.poo.hotel.modelo.Usuario;
import edu.utj.dsm.poo.hotel.persistencia.UsuarioDAO;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void handleEntrar(ActionEvent event) {
        String idOCorreo = txtUsuario.getText().trim();
        String pass = txtPassword.getText();

        if (idOCorreo.isEmpty() || pass.isEmpty()) {
            mostrarAlerta("Campos Vacíos", "Por favor ingresa tu ID o correo electrónico y tu contraseña.", Alert.AlertType.WARNING);
            return;
        }

        Usuario usuario = usuarioDAO.autenticar(idOCorreo, pass);

        if (usuario != null) {
            SesionUsuario.iniciarSesion(usuario);
            abrirPanelPrincipal();
        } else if (usuarioDAO.esCuentaInactiva(idOCorreo, pass)) {
            mostrarAlerta("Cuenta Inactiva", "Esta cuenta se encuentra desactivada. Contacta al Administrador.", Alert.AlertType.ERROR);
        } else {
            mostrarAlerta("Acceso Denegado", "ID, correo electrónico o contraseña incorrectos.", Alert.AlertType.ERROR);
            txtPassword.clear();
        }
    }

    @FXML
    private void handleCerrar(ActionEvent event) {
        Platform.exit();
    }

    private void abrirPanelPrincipal() {
        try {
            App.setRoot("PanelAdmin");

            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setTitle("Grand Hotel - Sistema de Gestión [" + SesionUsuario.getRolActual() + "]");
            stage.setResizable(true);
            stage.setMaximized(true);

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Navegación", "No se pudo cargar el panel principal (PanelAdmin.fxml).", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}