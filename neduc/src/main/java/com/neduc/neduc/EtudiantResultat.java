package com.neduc.neduc;

import javafx.beans.property.*;

public class EtudiantResultat {

    private final IntegerProperty rang;
    private final StringProperty matricule;
    private final StringProperty nom;
    private final StringProperty filiere;
    private final StringProperty annee;
    private final DoubleProperty moyenneGenerale;

    public EtudiantResultat(int rang, String matricule, String nom, String filiere, String annee, double moyenneGenerale) {
        this.rang = new SimpleIntegerProperty(rang);
        this.matricule = new SimpleStringProperty(matricule);
        this.nom = new SimpleStringProperty(nom);
        this.filiere = new SimpleStringProperty(filiere);
        this.annee = new SimpleStringProperty(annee);
        this.moyenneGenerale = new SimpleDoubleProperty(moyenneGenerale);
    }

    public int getRang() {
        return rang.get();
    }

    public IntegerProperty rangProperty() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang.set(rang);
    }

    public String getMatricule() {
        return matricule.get();
    }

    public StringProperty matriculeProperty() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule.set(matricule);
    }

    public String getNom() {
        return nom.get();
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getFiliere() {
        return filiere.get();
    }

    public StringProperty filiereProperty() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere.set(filiere);
    }

    public String getAnnee() {
        return annee.get();
    }

    public StringProperty anneeProperty() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee.set(annee);
    }

    public double getMoyenneGenerale() {
        return moyenneGenerale.get();
    }

    public DoubleProperty moyenneGeneraleProperty() {
        return moyenneGenerale;
    }

    public void setMoyenneGenerale(double moyenneGenerale) {
        this.moyenneGenerale.set(moyenneGenerale);
    }
}
