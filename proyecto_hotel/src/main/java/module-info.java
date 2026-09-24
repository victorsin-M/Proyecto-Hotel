module com.mycompany.proyecto_hotel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens edu.utj.dsm.poo.hotel to javafx.fxml;
    exports edu.utj.dsm.poo.hotel;
}
