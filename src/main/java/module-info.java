module com.kencuevas.savepassword {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jbcrypt;

    opens com.kencuevas.savepassword to javafx.fxml;
    exports com.kencuevas.savepassword;
    exports com.kencuevas.savepassword.controller;
    opens com.kencuevas.savepassword.controller to javafx.fxml;
}