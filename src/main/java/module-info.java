module com.app.alieninvasion {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.jetbrains.annotations;


    opens com.app.alieninvasion to javafx.fxml;
    exports com.app.alieninvasion;
}