package com.neduc.neduc;

import com.neduc.neduc.database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BulletinsController {

    @FXML private TextField matriculeField;
    @FXML private TableView<BulletinLigne> bulletinTable;

    @FXML private TableColumn<BulletinLigne, String> colUE;
    @FXML private TableColumn<BulletinLigne, String> colECUE;
    @FXML private TableColumn<BulletinLigne, Double> colNote1;
    @FXML private TableColumn<BulletinLigne, Double> colNote2;
    @FXML private TableColumn<BulletinLigne, Double> colNote3;
    @FXML private TableColumn<BulletinLigne, Double> colDevoir;
    @FXML private TableColumn<BulletinLigne, Double> colMoyenneECUE;

    private ObservableList<BulletinLigne> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colUE.setCellValueFactory(cell -> cell.getValue().ueProperty());
        colECUE.setCellValueFactory(cell -> cell.getValue().ecueProperty());
        colNote1.setCellValueFactory(cell -> cell.getValue().note1Property().asObject());
        colNote2.setCellValueFactory(cell -> cell.getValue().note2Property().asObject());
        colNote3.setCellValueFactory(cell -> cell.getValue().note3Property().asObject());
        colDevoir.setCellValueFactory(cell -> cell.getValue().devoirProperty().asObject());
        colMoyenneECUE.setCellValueFactory(cell -> cell.getValue().moyenneEcueProperty().asObject());
    }

    @FXML
    private void handleChargerBulletin() {
        data.clear();
        String matricule = matriculeField.getText().trim();
        String query = 
            "SELECT ue.nom AS ue, ec.nom AS ecue, " +
            "       n.note1, n.note2, n.note3, n.devoir, " +
            "       m.moyenne_ecue " +
            "FROM etudiants e " +
            "JOIN notes n ON e.id = n.etudiant_id " +
            "JOIN ecues ec ON n.ecue_id = ec.id " +
            "JOIN ues ue ON ec.ue_id = ue.id " +
            "JOIN moyennes m ON m.ecue_id = ec.id AND m.etudiant_id = e.id " +
            "WHERE e.matricule = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, matricule);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                data.add(new BulletinLigne(
                        rs.getString("ue"),
                        rs.getString("ecue"),
                        rs.getDouble("note1"),
                        rs.getDouble("note2"),
                        rs.getDouble("note3"),
                        rs.getDouble("devoir"),
                        rs.getDouble("moyenne_ecue")
                ));
            }

            bulletinTable.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
