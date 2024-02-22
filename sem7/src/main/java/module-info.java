module com.example.sem7 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    opens com.example.sem7.java.domain to javafx.base;
    opens com.example.sem7 to javafx.fxml;
    exports com.example.sem7;
    exports com.example.sem7.java.utils;
    opens com.example.sem7.java.utils to javafx.fxml;


}