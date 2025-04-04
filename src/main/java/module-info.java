module amelya.yeah1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.swing;

    opens amelya.yeah1 to javafx.fxml;
    exports amelya.yeah1;
}