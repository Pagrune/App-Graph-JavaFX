module ensisa.tp {
    requires javafx.controls;
    requires javafx.fxml;


    opens ensisa.tp to javafx.fxml;
    exports ensisa.tp;
}