package com.neduc.neduc;

import javafx.beans.property.*;

public class BulletinLigne {
    private final StringProperty ue;
    private final StringProperty ecue;
    private final DoubleProperty note1;
    private final DoubleProperty note2;
    private final DoubleProperty note3;
    private final DoubleProperty devoir;
    private final DoubleProperty moyenneEcue;

    public BulletinLigne(String ue, String ecue, double note1, double note2, double note3, double devoir, double moyenneEcue) {
        this.ue = new SimpleStringProperty(ue);
        this.ecue = new SimpleStringProperty(ecue);
        this.note1 = new SimpleDoubleProperty(note1);
        this.note2 = new SimpleDoubleProperty(note2);
        this.note3 = new SimpleDoubleProperty(note3);
        this.devoir = new SimpleDoubleProperty(devoir);
        this.moyenneEcue = new SimpleDoubleProperty(moyenneEcue);
    }

    public StringProperty ueProperty() { return ue; }
    public StringProperty ecueProperty() { return ecue; }
    public DoubleProperty note1Property() { return note1; }
    public DoubleProperty note2Property() { return note2; }
    public DoubleProperty note3Property() { return note3; }
    public DoubleProperty devoirProperty() { return devoir; }
    public DoubleProperty moyenneEcueProperty() { return moyenneEcue; }

    // Optionnel : getters et setters classiques
    public String getUe() { return ue.get(); }
    public void setUe(String ue) { this.ue.set(ue); }

    public String getEcue() { return ecue.get(); }
    public void setEcue(String ecue) { this.ecue.set(ecue); }

    public double getNote1() { return note1.get(); }
    public void setNote1(double note1) { this.note1.set(note1); }

    public double getNote2() { return note2.get(); }
    public void setNote2(double note2) { this.note2.set(note2); }

    public double getNote3() { return note3.get(); }
    public void setNote3(double note3) { this.note3.set(note3); }

    public double getDevoir() { return devoir.get(); }
    public void setDevoir(double devoir) { this.devoir.set(devoir); }

    public double getMoyenneEcue() { return moyenneEcue.get(); }
    public void setMoyenneEcue(double moyenneEcue) { this.moyenneEcue.set(moyenneEcue); }
}
