package com.neduc.neduc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import com.neduc.neduc.database.DatabaseConnection;

import java.sql.*;

public class EvaluationsController {

    @FXML private ComboBox<String> filiereComboBox;
    @FXML private ComboBox<String> anneeComboBox;
    @FXML private ComboBox<String> semestreComboBox;
    @FXML private ComboBox<String> ecueComboBox;

    @FXML private TableView<EvaluationRow> tableNotes;
    @FXML private TableColumn<EvaluationRow, String> colEtudiant;
    @FXML private TableColumn<EvaluationRow, Double> colNote1;
    @FXML private TableColumn<EvaluationRow, Double> colNote2;
    @FXML private TableColumn<EvaluationRow, Double> colNote3;
    @FXML private TableColumn<EvaluationRow, Double> colDevoir;
    @FXML private TableColumn<EvaluationRow, Double> colMoyenne;

    private final DatabaseConnection db = new DatabaseConnection();

    @FXML
    public void initialize() {
        loadComboBox(filiereComboBox, "SELECT nom FROM filieres");
        loadComboBox(anneeComboBox, "SELECT DISTINCT annee FROM inscriptions");
        loadComboBox(semestreComboBox, "SELECT DISTINCT semestre FROM inscriptions");

        filiereComboBox.setOnAction(e -> updateEcueComboBox());

        setupTable();
    }

    private void loadComboBox(ComboBox<String> comboBox, String query) {
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ObservableList<String> items = FXCollections.observableArrayList();
            while (rs.next()) {
                items.add(rs.getString(1));
            }
            comboBox.setItems(items);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateEcueComboBox() {
        String filiere = filiereComboBox.getValue();
        if (filiere != null) {
            try (Connection conn = db.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT nom FROM ecues WHERE filiere = ?")) {

                stmt.setString(1, filiere);
                ResultSet rs = stmt.executeQuery();
                ObservableList<String> ecues = FXCollections.observableArrayList();
                while (rs.next()) ecues.add(rs.getString("nom"));
                ecueComboBox.setItems(ecues);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupTable() {
        tableNotes.setEditable(true);

        colEtudiant.setCellValueFactory(cellData -> cellData.getValue().nomEtudiantProperty());
        colNote1.setCellValueFactory(cellData -> cellData.getValue().note1Property().asObject());
        colNote2.setCellValueFactory(cellData -> cellData.getValue().note2Property().asObject());
        colNote3.setCellValueFactory(cellData -> cellData.getValue().note3Property().asObject());
        colDevoir.setCellValueFactory(cellData -> cellData.getValue().devoirProperty().asObject());
        colMoyenne.setCellValueFactory(cellData -> cellData.getValue().moyenneProperty().asObject());

        setEditable(colNote1, "note1");
        setEditable(colNote2, "note2");
        setEditable(colNote3, "note3");
        setEditable(colDevoir, "devoir");
    }

    private void setEditable(TableColumn<EvaluationRow, Double> col, String fieldName) {
        col.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        col.setOnEditCommit(event -> {
            EvaluationRow row = event.getRowValue();
            Double newValue = event.getNewValue() != null ? event.getNewValue() : 0.0;

            switch (fieldName) {
                case "note1":
                    row.note1Property().set(newValue);
                    break;
                case "note2":
                    row.note2Property().set(newValue);
                    break;
                case "note3":
                    row.note3Property().set(newValue);
                    break;
                case "devoir":
                    row.devoirProperty().set(newValue);
                    break;
            }

            updateNoteInDatabase(row, fieldName, newValue);
            tableNotes.refresh();
        });
    }

    private void updateNoteInDatabase(EvaluationRow row, String fieldName, Double value) {
        String sql = "UPDATE notes SET " + fieldName + " = ? WHERE etudiant_id = ? AND ecue_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, value);
            stmt.setInt(2, row.getEtudiantId());
            stmt.setInt(3, row.getEcueId());
            int updated = stmt.executeUpdate();

            // Si aucune ligne mise à jour (notes absentes), insérer
            if (updated == 0) {
                String insertSql = "INSERT INTO notes (etudiant_id, ecue_id, note1, note2, note3, devoir) VALUES (?, ?, 0, 0, 0, 0)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, row.getEtudiantId());
                    insertStmt.setInt(2, row.getEcueId());
                    insertStmt.executeUpdate();
                }
                // puis update de la note modifiée
                try (PreparedStatement updateStmt = conn.prepareStatement(sql)) {
                    updateStmt.setDouble(1, value);
                    updateStmt.setInt(2, row.getEtudiantId());
                    updateStmt.setInt(3, row.getEcueId());
                    updateStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChargerNotes() {
        String filiere = filiereComboBox.getValue();
        String annee = anneeComboBox.getValue();
        String semestre = semestreComboBox.getValue();
        String ecue = ecueComboBox.getValue();

        if (filiere == null || annee == null || semestre == null || ecue == null) {
            showAlert("Veuillez remplir tous les filtres.");
            return;
        }

        ObservableList<EvaluationRow> data = FXCollections.observableArrayList();

        try (Connection conn = db.getConnection()) {
            // On récupère l'id de l'ECUE pour utiliser dans la requête
            int ecueId = -1;
            try (PreparedStatement psEcue = conn.prepareStatement("SELECT id FROM ecues WHERE nom = ?")) {
                psEcue.setString(1, ecue);
                ResultSet rsEcue = psEcue.executeQuery();
                if (rsEcue.next()) ecueId = rsEcue.getInt("id");
                else {
                    showAlert("ECUE introuvable.");
                    return;
                }
            }

            String sql = "SELECT e.id AS etudiant_id, e.nom, "
                       + "COALESCE(n.note1, 0) AS note1, "
                       + "COALESCE(n.note2, 0) AS note2, "
                       + "COALESCE(n.note3, 0) AS note3, "
                       + "COALESCE(n.devoir, 0) AS devoir "
                       + "FROM etudiants e "
                       + "JOIN inscriptions i ON e.id = i.etudiant_id "
                       + "LEFT JOIN notes n ON n.etudiant_id = e.id AND n.ecue_id = ? "
                       + "WHERE i.filiere = ? AND i.annee = ? AND i.semestre = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ecueId);
            stmt.setString(2, filiere);
            stmt.setString(3, annee);
            stmt.setString(4, semestre);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int etudiantId = rs.getInt("etudiant_id");
                String nom = rs.getString("nom");
                double n1 = rs.getDouble("note1");
                double n2 = rs.getDouble("note2");
                double n3 = rs.getDouble("note3");
                double devoir = rs.getDouble("devoir");

                data.add(new EvaluationRow(etudiantId, ecueId, nom, n1, n2, n3, devoir));
            }

            tableNotes.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur lors du chargement des notes.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
