package com.neduc.neduc;

import com.neduc.neduc.database.DatabaseConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;

public class PrimaryController implements Initializable {

    @FXML
    private AnchorPane bordMenu, etudiantsMenu, evaluationsMenu, resultatsMenu, parametresMenu;
    @FXML
    private AnchorPane bordMenu2, etudiantsMenu2, evaluationsMenu2, resultatsMenu2, parametresMenu2;
    @FXML
    private AnchorPane menu1, menu2, bordPane, etudiantsPane, evaluationsPane, resultatsPane, parametresPane;
    @FXML
    private ComboBox<String> semestreBox, filieresBox, anneesBox, etudiantBox, anneeBox;
    @FXML
    private TableView<Etudiant> etudiantTable;
    @FXML
    private TableColumn<Etudiant, Number> numeroCol;
    @FXML
    private TableColumn<Etudiant, String> nomCol, prenomCol, sexeCol, dateCol, lieuCol, matriculeCol;
    @FXML
    private TableColumn<Etudiant, Void> actionCol;

    @FXML
    private Label leftLabel;
    @FXML
    private Label currentLabel;
    @FXML
    private Label rightLabel;
    @FXML
    private Label nombreEtu;

    private int currentStartYear = 2025;

    //EvaluationsController
    @FXML
    private ComboBox<String> filiereComboBox;
    @FXML
    private ComboBox<String> anneeComboBox;
    @FXML
    private ComboBox<String> semestreComboBox;
    @FXML
    private ComboBox<String> ecueComboBox;

    @FXML
    private TableView<EvaluationRow> tableNotes;
    @FXML
    private TableColumn<EvaluationRow, String> colEtudiant;
    @FXML
    private TableColumn<EvaluationRow, Double> colNote1;
    @FXML
    private TableColumn<EvaluationRow, Double> colNote2;
    @FXML
    private TableColumn<EvaluationRow, Double> colNote3;
    @FXML
    private TableColumn<EvaluationRow, Double> colDevoir;
    @FXML
    private TableColumn<EvaluationRow, Double> colMoyenne;

    private void Annees() {

        updateLabels();

        leftLabel.setOnMouseClicked(event -> {
            currentStartYear--;
            updateLabels();
        });

        rightLabel.setOnMouseClicked(event -> {
            currentStartYear++;
            updateLabels();
        });

    }

    private void updateLabels() {

        leftLabel.setText((currentStartYear - 1) + " – " + currentStartYear);
        currentLabel.setText(currentStartYear + " – " + (currentStartYear + 1));
        rightLabel.setText((currentStartYear + 1) + " – " + (currentStartYear + 2));

    }

    public String getSelectedAnnee() {
        return currentStartYear + " – " + (currentStartYear + 1);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Annees();
        setupComboBoxes();
        loadData();
        setupTable();

    }

    private void setupComboBoxes() {

        semestreBox.setItems(FXCollections.observableArrayList("1", "2"));
        etudiantBox.setItems(FXCollections.observableArrayList("1", "2"));
        filieresBox.setItems(FXCollections.observableArrayList("SIL", "SI"));
        anneesBox.setItems(FXCollections.observableArrayList("1 année", "2 année", "3 année"));
        anneeBox.setItems(FXCollections.observableArrayList("1 année", "2 année", "3 année"));

        semestreBox.setValue("1");

    }

    // Methode for the navigation a Menu1
    @FXML
    private void handleClickMenu1(MouseEvent event) {

        if (event.getTarget() == etudiantsMenu) {

            menu1.setVisible(false);
            menu2.setVisible(true);
            bordPane.setVisible(false);
            etudiantsPane.setVisible(true);

            etudiantsMenu2.setStyle(
                    "-fx-background-color:  linear-gradient(to right,#FF8E72 0%, #EE6221 100%); -fx-background-radius: 10px");
            evaluationsMenu2.setStyle("white");
            resultatsMenu2.setStyle("white");
            parametresMenu2.setStyle("white");

        } else if (event.getTarget() == evaluationsMenu) {

            menu1.setVisible(false);
            menu2.setVisible(true);
            bordPane.setVisible(false);
            evaluationsPane.setVisible(true);

            etudiantsMenu2.setStyle("white");
            evaluationsMenu2.setStyle(
                    "-fx-background-color:  linear-gradient(to right,#FF8E72 0%, #EE6221 100%); -fx-background-radius: 10px");
            resultatsMenu2.setStyle("white");
            parametresMenu2.setStyle("white");

        } else if (event.getTarget() == resultatsMenu) {

            menu1.setVisible(false);
            menu2.setVisible(true);
            bordPane.setVisible(false);
            resultatsPane.setVisible(true);

            etudiantsMenu2.setStyle("white");
            evaluationsMenu2.setStyle("white");
            resultatsMenu2.setStyle(
                    "-fx-background-color:  linear-gradient(to right,#FF8E72 0%, #EE6221 100%); -fx-background-radius: 10px");
            parametresMenu2.setStyle("white");

        } else if (event.getTarget() == parametresMenu) {

            menu1.setVisible(false);
            menu2.setVisible(true);
            bordPane.setVisible(false);
            parametresPane.setVisible(true);

            etudiantsMenu2.setStyle("white");
            evaluationsMenu2.setStyle("white");
            resultatsMenu2.setStyle("white");
            parametresMenu2.setStyle(
                    "-fx-background-color:  linear-gradient(to right,#FF8E72 0%, #EE6221 100%); -fx-background-radius: 10px");

        } else {

            menu1.setVisible(true);
            bordPane.setVisible(true);

        }

    }

    // Methode for the navigation a Menu1
    @FXML
    private void handleClickMenu2(MouseEvent event) {

        if (event.getTarget() == bordMenu2) {

            menu1.setVisible(true);
            menu2.setVisible(false);
            bordPane.setVisible(true);
            etudiantsPane.setVisible(false);
            evaluationsPane.setVisible(false);
            resultatsPane.setVisible(false);
            parametresPane.setVisible(false);

        } else if (event.getTarget() == etudiantsMenu2) {

            menu1.setVisible(false);
            menu2.setVisible(true);
            bordPane.setVisible(false);
            etudiantsPane.setVisible(true);
            evaluationsPane.setVisible(false);
            resultatsPane.setVisible(false);
            parametresPane.setVisible(false);

            etudiantsMenu2.setStyle(
                    "-fx-background-color:  linear-gradient(to right,#FF8E72 0%, #EE6221 100%); -fx-background-radius: 10px");
            evaluationsMenu2.setStyle("white");
            resultatsMenu2.setStyle("white");
            parametresMenu2.setStyle("white");

        } else if (event.getTarget() == evaluationsMenu2) {

            menu1.setVisible(false);
            menu2.setVisible(true);
            bordPane.setVisible(false);
            etudiantsPane.setVisible(false);
            evaluationsPane.setVisible(true);
            resultatsPane.setVisible(false);
            parametresPane.setVisible(false);

            etudiantsMenu2.setStyle("white");
            evaluationsMenu2.setStyle(
                    "-fx-background-color:  linear-gradient(to right,#FF8E72 0%, #EE6221 100%); -fx-background-radius: 10px");
            resultatsMenu2.setStyle("white");
            parametresMenu2.setStyle("white");

        } else if (event.getTarget() == resultatsMenu2) {

            menu1.setVisible(false);
            menu2.setVisible(true);
            bordPane.setVisible(false);
            etudiantsPane.setVisible(false);
            resultatsPane.setVisible(true);
            evaluationsPane.setVisible(false);
            parametresPane.setVisible(false);

            etudiantsMenu2.setStyle("white");
            evaluationsMenu2.setStyle("white");
            resultatsMenu2.setStyle(
                    "-fx-background-color:  linear-gradient(to right,#FF8E72 0%, #EE6221 100%); -fx-background-radius: 10px");
            parametresMenu2.setStyle("white");

        } else if (event.getTarget() == parametresMenu2) {

            menu1.setVisible(false);
            menu2.setVisible(true);
            bordPane.setVisible(false);
            evaluationsPane.setVisible(false);
            resultatsPane.setVisible(false);
            parametresPane.setVisible(true);

            etudiantsMenu2.setStyle("white");
            evaluationsMenu2.setStyle("white");
            resultatsMenu2.setStyle("white");
            parametresMenu2.setStyle(
                    "-fx-background-color:  linear-gradient(to right,#FF8E72 0%, #EE6221 100%); -fx-background-radius: 10px");

        } else {

            menu2.setVisible(true);
            bordPane.setVisible(true);

        }

    }

    private void setupTable() {

        numeroCol.setCellValueFactory(cellData -> {

            return new ReadOnlyObjectWrapper<>(etudiantTable.getItems().indexOf(cellData.getValue()) + 1);

        });

        nomCol.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        prenomCol.setCellValueFactory(cellData -> cellData.getValue().prenomProperty());
        sexeCol.setCellValueFactory(cellData -> cellData.getValue().sexeProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        lieuCol.setCellValueFactory(cellData -> cellData.getValue().lieuProperty());
        matriculeCol.setCellValueFactory(cellData -> cellData.getValue().matriculeProperty());

        nomCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nomCol.setOnEditCommit(event -> {
            Etudiant e = event.getRowValue();
            e.setNom(event.getNewValue());
        });

        prenomCol.setCellFactory(TextFieldTableCell.forTableColumn());
        prenomCol.setOnEditCommit(event -> {
            Etudiant e = event.getRowValue();
            e.setPrenom(event.getNewValue());
        });

        sexeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        sexeCol.setOnEditCommit(event -> {
            Etudiant e = event.getRowValue();
            e.setSexe(event.getNewValue());
        });

        dateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dateCol.setOnEditCommit(event -> {
            Etudiant e = event.getRowValue();
            e.setDate(event.getNewValue());
        });

        lieuCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lieuCol.setOnEditCommit(event -> {
            Etudiant e = event.getRowValue();
            e.setLieu(event.getNewValue());
        });

        matriculeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        matriculeCol.setOnEditCommit(event -> {
            Etudiant e = event.getRowValue();
            e.setMatricule(event.getNewValue());
        });

        etudiantTable.setEditable(true);
        addActionButtons();
        updateNombreEtu();

    }

    private void updateNombreEtu() {

        nombreEtu.setText(String.valueOf(etudiantTable.getItems().size()));

        etudiantTable.getItems().addListener((ListChangeListener<Etudiant>) change -> {

            nombreEtu.setText(String.valueOf(etudiantTable.getItems().size()));

        });

    }

    @FXML
    private void handleAjouter() {

        Etudiant newEtudiant = new Etudiant("", "", "", "", "", "");

        etudiantTable.getItems().add(newEtudiant);

        int newRowIndex = etudiantTable.getItems().size() - 1;
        etudiantTable.edit(newRowIndex, nomCol);

    }

    private void loadData() {
        ObservableList<Etudiant> data = FXCollections.observableArrayList(
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("DIALLO", "Moussa", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"),
                new Etudiant("ADJA", "Alice", "F", "01/01/2025", "Cotonou", "1223325647QD"));

        etudiantTable.setItems(data);
    }

    // Implementation to icon in the table
    private void addActionButtons() {

        actionCol.setCellFactory(col -> new TableCell<>() {

            private final HBox buttons = new HBox(25);

            private final ImageView editIcon = new ImageView(
                    new Image(getClass().getResourceAsStream("/com/mycompany/university/images/update1.png")));

            private final ImageView deleteIcon = new ImageView(
                    new Image(getClass().getResourceAsStream("/com/mycompany/university/images/delete1.png")));

            {

                buttons.getStyleClass().add("buttons-box");
                editIcon.getStyleClass().add("icon-clickable");
                deleteIcon.getStyleClass().add("icon-clickable");

                // EDIT ICON
                ScaleTransition edit = new ScaleTransition(Duration.millis(150), editIcon);
                editIcon.setOnMouseEntered(e -> {
                    edit.setToX(1.2);
                    edit.setToY(1.2);
                    edit.playFromStart();
                });

                editIcon.setOnMouseExited(e -> {
                    edit.setToX(1.0);
                    edit.setToY(1.0);
                    edit.playFromStart();
                });

                editIcon.setOnMouseClicked(e -> {
                    int rowIndex = getIndex();
                    etudiantTable.edit(rowIndex, nomCol);
                    Etudiant etu = getCurrentEtudiant();
                    System.out.println("Édition de : " + etu.getNom());
                });

                // DELETE ICON
                ScaleTransition delete = new ScaleTransition(Duration.millis(150), deleteIcon);
                deleteIcon.setOnMouseEntered(e -> {
                    delete.setToX(1.2);
                    delete.setToY(1.2);
                    delete.playFromStart();
                });
                deleteIcon.setOnMouseExited(e -> {
                    delete.setToX(1.0);
                    delete.setToY(1.0);
                    delete.playFromStart();
                });

                deleteIcon.setOnMouseClicked(e -> {
                    int rowIndex = getIndex();
                    etudiantTable.getItems().remove(rowIndex);
                    etudiantTable.refresh();
                    System.out.println("Ligne supprimée à l'index : " + rowIndex);
                });

                editIcon.setFitHeight(20);
                editIcon.setFitWidth(20);
                deleteIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);

                buttons.getChildren().addAll(editIcon, deleteIcon);

            }

            private Etudiant getCurrentEtudiant() {

                return (Etudiant) getTableView().getItems().get(getIndex());

            }

            @Override
            protected void updateItem(Void item, boolean empty) {

                super.updateItem(item, empty);

                setGraphic(empty ? null : buttons);

            }

        });

    }

    //EtudiantsController
    private final DatabaseConnection db = new DatabaseConnection();

    @FXML
    public void initialize() {
        loadComboBox(filiereComboBox, "SELECT nom FROM filieres");
        loadComboBox(anneeComboBox, "SELECT DISTINCT annee FROM inscriptions");
        loadComboBox(semestreComboBox, "SELECT DISTINCT semestre FROM inscriptions");

        filiereComboBox.setOnAction(e -> updateEcueComboBox());

        SetupTable();
    }

    private void loadComboBox(ComboBox<String> comboBox, String query) {
        try (Connection conn = db.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

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
            try (Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT nom FROM ecues WHERE filiere = ?")) {

                stmt.setString(1, filiere);
                ResultSet rs = stmt.executeQuery();
                ObservableList<String> ecues = FXCollections.observableArrayList();
                while (rs.next()) {
                    ecues.add(rs.getString("nom"));
                }
                ecueComboBox.setItems(ecues);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void SetupTable() {
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
        try (Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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
                if (rsEcue.next()) {
                    ecueId = rsEcue.getInt("id");
                } else {
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
