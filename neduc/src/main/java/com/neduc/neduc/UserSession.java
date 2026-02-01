package com.neduc.neduc;

public class UserSession {
    private static String nom;
    private static String prenom;
    private static String role;

    public static String getNom() {
        return nom;
    }

    public static void setNom(String nom) {
        UserSession.nom = nom;
    }

    public static String getPrenom() {
        return prenom;
    }

    public static void setPrenom(String prenom) {
        UserSession.prenom = prenom;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        UserSession.role = role;
    }

    public static void clear() {
        nom = null;
        prenom = null;
        role = null;
    }
}
