package com.neduc.neduc;

public class User {
    private String id;
    private String nom;
    private String prenom;
    private String role;
    private String email;
    private boolean isSuperuser;

    public User(String id, String nom, String prenom, String role, String email, boolean isSuperuser) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.email = email;
        this.isSuperuser = isSuperuser;
    }

    public String getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public boolean isSuperuser() { return isSuperuser; }
}
