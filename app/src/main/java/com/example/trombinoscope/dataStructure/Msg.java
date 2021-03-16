package com.example.trombinoscope.dataStructure;

public class Msg {
    private String msg, date, idTrombi, User, perso;

    public Msg(String msg, String date, String idTrombi, String User, String perso) {
        this.msg = msg;
        this.date = date;
        this.idTrombi = idTrombi;
        this.User = User;
        this.perso = perso;
    }

    public String getMsg() {return msg;}
    public String getDate(){return date;}
    public String getIdTrombi(){return idTrombi;}
    public String getUser(){return User;}
    public String getPerso(){return perso;}

    public void setMsg(String msg){this.msg = msg;}
    public void setDate(String date){this.date = date;}
    public void setIdTrombi(String idTrombi){this.idTrombi = idTrombi;}
    public void setUser(String idUser){this.User=idUser;}



}
