module com.neduc.neduc {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires jbcrypt;
    requires java.mail; // Déclare la dépendance au module JavaMail
    
    opens com.neduc.neduc to javafx.fxml;
    exports com.neduc.neduc;
}
