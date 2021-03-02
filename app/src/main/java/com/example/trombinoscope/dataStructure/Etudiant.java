package com.example.trombinoscope.dataStructure;

import android.graphics.Bitmap;

import android.os.Parcel; //ajouter
import android.os.Parcelable;  //ajouter


public class Etudiant implements Parcelable {  //ajouter

    private String nom, prenom, imgName, email;
    private Bitmap img;

    public Etudiant(String nom, String prenom, String imgName, Bitmap img, String email){
        this.nom = nom;
        this.prenom = prenom;
        this.imgName = imgName;
        this.img = img;
        this.email = email;

    }

    protected Etudiant(Parcel in) {
        nom = in.readString();
        prenom = in.readString();
        imgName = in.readString();
        email = in.readString();
        img = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeString(imgName);
        dest.writeString(email);
        dest.writeParcelable(img, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Etudiant> CREATOR = new Creator<Etudiant>() {
        @Override
        public Etudiant createFromParcel(Parcel in) {
            return new Etudiant(in);
        }

        @Override
        public Etudiant[] newArray(int size) {
            return new Etudiant[size];
        }
    };

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

    public String getEMail(){
        return this.email;
    }

}
