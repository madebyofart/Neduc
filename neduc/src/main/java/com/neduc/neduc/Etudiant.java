package com.neduc.neduc;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Othniel
 */
public class Etudiant {

    private final SimpleStringProperty nom;
    private final SimpleStringProperty prenom;
    private final SimpleStringProperty sexe;
    private final SimpleStringProperty date;
    private final SimpleStringProperty lieu;
    private final SimpleStringProperty matricule;

    public Etudiant(String nom, String prenom, String sexe, String date, String lieu, String matricule) {
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.sexe = new SimpleStringProperty(sexe);
        this.date = new SimpleStringProperty(date);
        this.lieu = new SimpleStringProperty(lieu);
        this.matricule = new SimpleStringProperty(matricule);

    }

    //NOM
    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty nomProperty() {
        return nom;
    }

    //PRENOM
    public String getPrenom() {
        return prenom.get();
    }

    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }

    public StringProperty prenomProperty() {
        return prenom;
    }

    //SEXE
    public String getSexe() {

        return sexe.get();

    }

    public void setSexe(String sexe) {

        this.sexe.set(sexe);

    }
    
    public StringProperty sexeProperty() {
        
        return sexe;
        
    }
    
    //DATE
    public String getDate() {
        
        return date.get();
        
    }
    
    public void setDate(String date) {
        
        this.date.set(date);
        
    }
    
    public StringProperty dateProperty() {
        
        return date;
    }
    
    //LIEU
    public String getLieu() {
        
        return lieu.get();
        
    }
    
    public void setLieu(String lieu) {
        
        this.lieu.set(lieu);
        
    }
    
    public StringProperty lieuProperty() {
        
        return lieu;
        
    }
    
    //MATRICULE
    public String getMatricule() {
        
        return matricule.get();
        
    }
    
    public void setMatricule(String matricule) {
        
        this.matricule.set(matricule);
        
    }
    
    public StringProperty matriculeProperty() {
        
        return matricule;
        
    }
}
