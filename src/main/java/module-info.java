module undibe.hu.SFMProject{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.persistence;
    requires com.h2database;
    requires org.hibernate.orm.core;

    opens unideb.hu.SFMProject to org.hibernate.orm.core, javafx.fxml;

    exports unideb.hu.SFMProject;
}