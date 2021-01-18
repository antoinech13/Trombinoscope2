package com.example.trombinoscope.dataStructure;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

public class Trombi implements Parcelable {
    private String formation;
    private String tag;
    private String date;
    private int r,g ,b;

    public Trombi(String formation, String tag, String date){
        this.formation = formation;
        this.tag = tag;
        this.date = date;
        this.r = new Random().nextInt(256);
        this.g = new Random().nextInt(256);
        this.b = new Random().nextInt(256);
    }

    protected Trombi(Parcel in) {
        formation = in.readString();
        tag = in.readString();
        date = in.readString();
        r = in.readInt();
        g = in.readInt();
        b = in.readInt();
    }

    public static final Creator<Trombi> CREATOR = new Creator<Trombi>() {
        @Override
        public Trombi createFromParcel(Parcel in) {
            return new Trombi(in);
        }

        @Override
        public Trombi[] newArray(int size) {
            return new Trombi[size];
        }
    };

    public String getFormation() {
        return this.formation;
    }

    public String getTag() {
        return this.tag;
    }

    public String getDate() {
        return this.date;
    }

    public int getR() {return this.r;}

    public int getG() { return this.g; }

    public int getB(){ return this.b;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(formation);
        dest.writeString(tag);
        dest.writeString(date);
        dest.writeInt(r);
        dest.writeInt(g);
        dest.writeInt(b);
    }
}
