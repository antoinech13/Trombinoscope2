package com.example.trombinoscope;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

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

        try {
            con.connect("86.211.99.209",21);
            con.enterLocalPassiveMode();
            con.login("bsd1", "bsd1cci");
            con.setDefaultTimeout(5000);
            con.setFileType(FTP.BINARY_FILE_TYPE);


            FTPFile[] ftpFiles = con.mlistDir(path);
            for (int i = 0; i < ftpFiles.length; i++) {
                String name = ftpFiles[i].getName();
                Log.d("aasd", name );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getImage(String name){

        OutputStream out = null;
        ByteArrayOutputStream  bitO;
        byte[] bitA = null;

        try {
            out = new ByteArrayOutputStream();
            boolean result = con.retrieveFile(path + name, out);
            bitO = (ByteArrayOutputStream)out;
            bitA = bitO.toByteArray();
            out.close();


            if (result) Log.v("download result", "succeeded");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeByteArray(bitA, 0, bitA.length);
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


