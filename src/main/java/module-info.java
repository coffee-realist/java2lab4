module com.tyb6.java2lab4 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.tyb6.java2lab4 to javafx.fxml;
    exports com.tyb6.java2lab4;
}