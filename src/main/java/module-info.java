module com.mycompany.proyecto_hotel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; 
    requires java.base;

    opens edu.utj.dsm.poo.hotel to javafx.fxml;
    opens edu.utj.dsm.poo.hotel.controlador to javafx.fxml;
    opens edu.utj.dsm.poo.hotel.modelo to javafx.base;

    exports edu.utj.dsm.poo.hotel;
}