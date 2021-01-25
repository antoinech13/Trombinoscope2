package com.example.trombinoscope.dataStructure;

public class Etudiant {

    private String nom;
    private String prenom;

    public Etudiant(String nom, String prenom){
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getNom(){
        return this.nom;
    }

    public String getPrenom(){
        return this.prenom;
    }

}
