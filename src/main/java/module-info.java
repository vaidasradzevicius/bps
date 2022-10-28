module lt.vilniustech.bps {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    requires MaterialFX;
    requires morphia.core;
    requires org.mongodb.bson;
    requires com.google.guice;
    requires org.slf4j;
    requires org.apache.commons.lang3;
    requires org.apache.commons.collections4;
    requires commons.validator;
    requires itextpdf;

    opens lt.vilniustech.bps to javafx.fxml;
    opens lt.vilniustech.bps.dto to morphia.core;
    opens lt.vilniustech.bps.controller to javafx.fxml;
    exports lt.vilniustech.bps;
    exports lt.vilniustech.bps.controller;
    exports lt.vilniustech.bps.dao;
    exports lt.vilniustech.bps.dto;
    exports lt.vilniustech.bps.service;
}