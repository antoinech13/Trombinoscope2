package com.example.trombinoscope.dataStructure;

import android.graphics.Bitmap;


public class Etudiant {

    private String nom, prenom, imgName, email;
    private Bitmap img;

    public Etudiant(String nom, String prenom, String imgName, Bitmap img, String email){
        this.nom = nom;
        this.prenom = prenom;
        this.imgName = imgName;
        this.img = img;
        this.email = email;

    }

    public String getNom(){
        return this.nom;
    }

    public String getPrenom(){
        return this.prenom;
    }

    public String getImgName(){
        return this.imgName;
    }

    public Bitmap getImg(){ return this.img;}

    public void setImg(Bitmap img){ this.img = img;}

}
