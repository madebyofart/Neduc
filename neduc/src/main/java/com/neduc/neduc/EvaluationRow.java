package com.neduc.neduc;

import javafx.beans.property.*;

public class EvaluationRow {
    private final int etudiantId;
    private final int ecueId;
    private final StringProperty nomEtudiant;
    private final DoubleProperty note1;
    private final DoubleProperty note2;
    private final DoubleProperty note3;
    private final DoubleProperty devoir;
    private final DoubleProperty moyenne;

    public EvaluationRow(int etudiantId, int ecueId, String nomEtudiant,
                         double note1, double note2, double note3, double devoir) {
        this.etudiantId = etudiantId;
        this.ecueId = ecueId;
        this.nomEtudiant = new SimpleStringProperty(nomEtudiant);
        this.note1 = new SimpleDoubleProperty(note1);
        this.note2 = new SimpleDoubleProperty(note2);
        this.note3 = new SimpleDoubleProperty(note3);
        this.devoir = new SimpleDoubleProperty(devoir);
        this.moyenne = new SimpleDoubleProperty();
        recalcMoyenne();

        // Recalculer la moyenne à chaque modification de note
        this.note1.addListener((obs, oldV, newV) -> recalcMoyenne());
        this.note2.addListener((obs, oldV, newV) -> recalcMoyenne());
        this.note3.addListener((obs, oldV, newV) -> recalcMoyenne());
        this.devoir.addListener((obs, oldV, newV) -> recalcMoyenne());
    }

    private void recalcMoyenne() {
        double moyInterro = (note1.get() + note2.get() + note3.get()) / 3.0;
        double moy = 0.3 * moyInterro + 0.7 * devoir.get();
        moyenne.set(Math.round(moy * 100.0) / 100.0);
    }

    // Getters properties pour le binding TableView
    public StringProperty nomEtudiantProperty() { return nomEtudiant; }
    public DoubleProperty note1Property() { return note1; }
    public DoubleProperty note2Property() { return note2; }
    public DoubleProperty note3Property() { return note3; }
    public DoubleProperty devoirProperty() { return devoir; }
    public DoubleProperty moyenneProperty() { return moyenne; }

    public int getEtudiantId() { return etudiantId; }
    public int getEcueId() { return ecueId; }
}
