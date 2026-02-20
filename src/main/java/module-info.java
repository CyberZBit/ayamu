module com.cyberz.ayamu {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires MathParser.org.mXparser;

    opens com.cyberz.ayamu to javafx.fxml;
    exports com.cyberz.ayamu;
    exports com.cyberz.ayamu.Controllers;
    opens com.cyberz.ayamu.Controllers to javafx.fxml;
}