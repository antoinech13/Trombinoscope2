package com.example.trombinoscope.dataStructure;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

public class Trombi implements Parcelable {
    private String formation;
    private String tag;
    private String date;
    private String idpromo;
    private int right;
    private int r,g ,b;

    public Trombi(String formation, String tag, String date, String idpromo, int right){
        this.formation = formation;
        this.tag = tag;
        this.date = date;
        this.idpromo = idpromo;
        this.right = right;
        this.r = new Random().nextInt(256);
        this.g = new Random().nextInt(256);
        this.b = new Random().nextInt(256);
    }

    protected Trombi(Parcel in) {
        formation = in.readString();
        tag = in.readString();
        date = in.readString();
        idpromo = in.readString();
        right = in.readInt();
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

    public String getId() {
        return this.idpromo;
    }

    public int getRight() {return this.right;}

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
        dest.writeString(idpromo);
        dest.writeInt(right);
        dest.writeInt(r);
        dest.writeInt(g);
        dest.writeInt(b);
    }

    public static String[] mColors = {
            "#02aab0", // beach1
            "#314755", // nimvelo1
            "#2b5876", // seablue1
            "#2193b0", // sexyblue1
            "#1488cc", // skyline1
            "#2193b0", // coolblues1
    };

    public static String[] mColors2 = {
            "#00cdac", //beach2
            "#26a0da", // nimvelo2
            "#4e4376", // seablue2
            "#6dd5ed", // sexyblue2
            "#2b32b2", //skyline2
            "#6dd5ed",  // coolblues2
    };

    public static int[] getGcolors() {
        String color = "";
        String color2 = "";

        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(mColors.length);

        color = mColors[randomNumber];
        color2 = mColors2[randomNumber];
        int colorAsInt = Color.parseColor(color);
        int colorAsInt2 = Color.parseColor(color2);

        return new int[]{colorAsInt, colorAsInt2};
    }
}
