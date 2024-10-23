module undibe.hu.SFMProject{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.persistence;
    requires com.h2database;

    opens unideb.hu.SFMProject to javafx.fxml;
    exports unideb.hu.SFMProject;
}