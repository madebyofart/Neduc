package com.neduc.neduc;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import com.neduc.neduc.database.DatabaseConnection;

import java.sql.*;

public class UEController {

    @FXML private TextField nomUEField;
    @FXML private TextField codeUEField;
    @FXML private TextField nomECUEField;
    @FXML private TextField coefficientECUEField;
    @FXML private ComboBox<String> filiereComboBox;
    @FXML private ComboBox<String> comboUE;

    @FXML private TableView<UEData> tableUE;
    @FXML private TableColumn<UEData, Integer> colNum;
    @FXML private TableColumn<UEData, String> colNom;
    @FXML private TableColumn<UEData, String> colECUEs;
    @FXML private TableColumn<UEData, String> colFiliere;
    @FXML private TableColumn<UEData, Void> colActions;

    private final ObservableList<UEData> ueList = FXCollections.observableArrayList();
    private boolean isAdmin = false;

    @FXML
    public void initialize() {
        colNum.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colECUEs.setCellValueFactory(new PropertyValueFactory<>("ecues"));
        colFiliere.setCellValueFactory(new PropertyValueFactory<>("filiere"));
        setupActionsColumn();

        checkAdminRole();
        loadFiliereComboBox();
        loadUEs();
        loadUEComboBox();

        if (!isAdmin) {
            disableFormControls();
        }
    }

    private void setupActionsColumn() {
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<UEData, Void> call(final TableColumn<UEData, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Supprimer");

                    {
                        btn.setOnAction(event -> {
                            UEData ue = getTableView().getItems().get(getIndex());
                            supprimerUE(ue.getId(), ue.getNom());
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty || !isAdmin ? null : btn);
                    }
                };
            }
        });
    }

    private void checkAdminRole() {
        String role = getCurrentUserRole();
        isAdmin = "admin".equals(role);
    }

    private String getCurrentUserRole() {
        return "admin"; // à remplacer plus tard par la vraie authentification
    }

    private void disableFormControls() {
        nomUEField.setDisable(true);
        codeUEField.setDisable(true);
        nomECUEField.setDisable(true);
        coefficientECUEField.setDisable(true);
        filiereComboBox.setDisable(true);
        comboUE.setDisable(true);
    }

    private void loadFiliereComboBox() {
        String sql = "SELECT nom FROM filieres ORDER BY nom";
        ObservableList<String> filieres = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                filieres.add(rs.getString("nom"));
            }

            filiereComboBox.setItems(filieres);
        } catch (SQLException e) {
            showAlert("Erreur", "Chargement des filières échoué : " + e.getMessage());
        }
    }

    private void loadUEComboBox() {
        String sql = "SELECT nom FROM ues ORDER BY nom";
        ObservableList<String> ues = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ues.add(rs.getString("nom"));
            }

            comboUE.setItems(ues);
        } catch (SQLException e) {
            showAlert("Erreur", "Chargement des UEs échoué : " + e.getMessage());
        }
    }

    @FXML
    public void ajouterUE() {
        if (!isAdmin) {
            showAlert("Accès refusé", "Seuls les administrateurs peuvent ajouter des UEs.");
            return;
        }

        String nom = nomUEField.getText().trim();
        String code = codeUEField.getText().trim();
        String filiere = filiereComboBox.getValue();

        if (nom.isEmpty() || code.isEmpty() || filiere == null) {
            showAlert("Erreur", "Tous les champs UE sont obligatoires.");
            return;
        }

        String sqlFiliereId = "SELECT id FROM filieres WHERE nom = ?";
        String sql = "INSERT INTO ues (nom, coefficient, filiere_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement filiereStmt = conn.prepareStatement(sqlFiliereId)) {

            filiereStmt.setString(1, filiere);
            ResultSet rs = filiereStmt.executeQuery();

            if (rs.next()) {
                int filiereId = rs.getInt("id");

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nom);
                    stmt.setInt(2, Integer.parseInt(code));
                    stmt.setInt(3, filiereId);
                    stmt.executeUpdate();
                    loadUEs();
                    loadUEComboBox(); // mise à jour du ComboBox ECUE
                }
            } else {
                showAlert("Erreur", "Filière non trouvée.");
            }

        } catch (SQLException e) {
            showAlert("Erreur", "Ajout UE échoué : " + e.getMessage());
        }
    }

    @FXML
    public void ajouterECUE() {
        if (!isAdmin) {
            showAlert("Accès refusé", "Seuls les administrateurs peuvent ajouter des ECUEs.");
            return;
        }

        String nom = nomECUEField.getText().trim();
        String coefficient = coefficientECUEField.getText().trim();
        String nomUE = comboUE.getValue();

        if (nom.isEmpty() || coefficient.isEmpty() || nomUE == null) {
            showAlert("Erreur", "Tous les champs ECUE sont obligatoires.");
            return;
        }

        String findUEIdSQL = "SELECT id FROM ues WHERE nom = ?";
        String insertECUESQL = "INSERT INTO ecues (nom, coefficient, ue_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement findUEStmt = conn.prepareStatement(findUEIdSQL)) {

            findUEStmt.setString(1, nomUE);
            ResultSet rs = findUEStmt.executeQuery();

            if (rs.next()) {
                int ueId = rs.getInt("id");

                try (PreparedStatement insertStmt = conn.prepareStatement(insertECUESQL)) {
                    insertStmt.setString(1, nom);
                    insertStmt.setInt(2, Integer.parseInt(coefficient));
                    insertStmt.setInt(3, ueId);
                    insertStmt.executeUpdate();
                    loadUEs();
                }

            } else {
                showAlert("Erreur", "UE non trouvée. Veuillez l’ajouter d’abord.");
            }

        } catch (SQLException e) {
            showAlert("Erreur", "Ajout ECUE échoué : " + e.getMessage());
        }
    }

    private void supprimerUE(int ueId, String nomUE) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer l’UE \"" + nomUE + "\" et ses ECUEs ?", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                String deleteUESQL = "DELETE FROM ues WHERE id = ?";

                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(deleteUESQL)) {
                    stmt.setInt(1, ueId);
                    stmt.executeUpdate();
                    loadUEs();
                    loadUEComboBox();
                } catch (SQLException e) {
                    showAlert("Erreur", "Suppression échouée : " + e.getMessage());
                }
            }
        });
    }

    private void loadUEs() {
        ueList.clear();
        String sql =
            "SELECT ues.id AS ue_id, ues.nom AS ue_nom, filieres.nom AS filiere_nom, " +
            "STRING_AGG(ecues.nom, ', ') AS liste_ecues " +
            "FROM ues " +
            "LEFT JOIN ecues ON ecues.ue_id = ues.id " +
            "LEFT JOIN filieres ON ues.filiere_id = filieres.id " +
            "GROUP BY ues.id, ues.nom, filieres.nom " +
            "ORDER BY ues.id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("ue_id");
                String nom = rs.getString("ue_nom");
                String ecues = rs.getString("liste_ecues");
                String filiere = rs.getString("filiere_nom");
                ueList.add(new UEData(id, nom, ecues != null ? ecues : "", filiere));
            }

            tableUE.setItems(ueList);
        } catch (SQLException e) {
            showAlert("Erreur", "Chargement échoué : " + e.getMessage());
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static class UEData {
        private final Integer id;
        private final String nom;
        private final String ecues;
        private final String filiere;

        public UEData(Integer id, String nom, String ecues, String filiere) {
            this.id = id;
            this.nom = nom;
            this.ecues = ecues;
            this.filiere = filiere;
        }

        public Integer getId() { return id; }
        public String getNom() { return nom; }
        public String getEcues() { return ecues; }
        public String getFiliere() { return filiere; }
    }
}
