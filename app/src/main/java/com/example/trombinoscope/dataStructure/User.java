package com.example.trombinoscope.dataStructure;

public class User {
    private String nom, prenom, email;
    private int droit;

    public User(String nom, String prenom, String email, int droit){
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.droit = droit;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail(){
        return email;
    }

    public int getDroit() {
        return droit;
    }

    public void setDroit(int i){
        droit = i;
    }
}
