module org.example.weatherapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires unirest.java;
    requires jdk.jsobject;
    requires json;
    requires com.fasterxml.jackson.databind;
    requires java.sql;

    opens org.example.weatherapp to javafx.fxml;
    exports org.example.weatherapp;
}