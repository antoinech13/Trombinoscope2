package com.example.trombinoscope.view;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.example.trombinoscope.MySingleton;


public class AddToTrombiViewModel {
    private Uri imageUri;
    private static AddToTrombiViewModel instance;


    public static synchronized AddToTrombiViewModel getInstance(){
        if (instance == null) {
            instance = new AddToTrombiViewModel();
        }
        return instance;
    }

    public Uri getUri(){
        return imageUri;
    }

    public void setUri(Uri image){
        imageUri = image;
    }

}
