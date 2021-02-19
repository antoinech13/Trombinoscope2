package com.example.trombinoscope.dataStructure;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class addEtu implements Parcelable {
    private String image;
    private String nom;
    private String prenom;
    private String email;

    public addEtu(String img, String nom, String prenom, String email){
        image = img;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    protected addEtu(Parcel in) {
        image = in.readString();
        nom = in.readString();
        prenom = in.readString();
        email = in.readString();
    }

    public static final Creator<addEtu> CREATOR = new Creator<addEtu>() {
        @Override
        public addEtu createFromParcel(Parcel in) {
            return new addEtu(in);
        }

        @Override
        public addEtu[] newArray(int size) {
            return new addEtu[size];
        }
    };

    public String getImage() { return image;}
    public String getNom(){ return nom; }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail(){
        return email;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeString(email);
        dest.writeString(String.valueOf(image));
    }
}
