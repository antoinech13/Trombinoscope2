package com.example.trombinoscope;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HachageMDP {

    public String hachageMDP(String password) {
        //Creates a message digest with the specified algorithm name SHA6256.
        String s = "";
        MessageDigest md;
        {
            try {
                md = MessageDigest.getInstance("SHA-256");
                try {
                    //Updates the digest using the specified byte
                    md.update(password.getBytes());
                    byte byteData[] = md.digest();
                    //convertir le tableau de bits en une format hexadécimal
                    for (int i = 0; i < byteData.length; i++) {
                        s += (Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return s;
    }
}
