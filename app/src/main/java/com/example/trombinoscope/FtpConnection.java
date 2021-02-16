package com.example.trombinoscope;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FtpConnection {
    private FTPClient con = new FTPClient();
    private String path = "/Web/imageTrombi/";

   public FtpConnection(){
// NE PAS TOUCHER AUX IP !!!!
        try {
            con.connect("86.211.99.209",21);
            con.enterLocalPassiveMode();
            con.login("bsd1", "bsd1cci");
            con.setDefaultTimeout(6000);
            con.setFileType(FTP.BINARY_FILE_TYPE);
//Modif ajouter du prof
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getImage(String name){

        ByteArrayOutputStream out = null;
        ByteArrayOutputStream  bitO;
        Log.d("aasd", name);
        byte[] bitA = null;

        try {
            out = new ByteArrayOutputStream();
            boolean result = con.retrieveFile(path + name, out);

            bitA = out.toByteArray();
            out.close();


            if (result) Log.v("download result", "succeeded");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return BitmapFactory.decodeByteArray(bitA, 0, bitA.length);
    }

    public void sendImage(Bitmap img, String filename){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100 , bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        try {
            con.storeFile(path + filename, bs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try {
            con.logout();
            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(){
        try {
            con.connect("192.168.2.57");
            con.login("bsd1", "bsd1cci");
            con.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


