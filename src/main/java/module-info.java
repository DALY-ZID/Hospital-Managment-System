module com.example.miniprojet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.pdfbox;
    requires java.desktop;



    exports com.example.miniprojet.home;
    opens com.example.miniprojet.home to javafx.fxml;
}