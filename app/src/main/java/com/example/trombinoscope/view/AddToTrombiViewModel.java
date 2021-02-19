package com.example.trombinoscope.view;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

public class AddToTrombiViewModel extends ViewModel {
    private Uri imageUri;

    public Uri getUri(){
        return imageUri;
    }

    public void setUri(Uri image){
        imageUri = image;
    }

}
