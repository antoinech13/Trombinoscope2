package com.example.trombinoscope;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.trombinoscope.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {


    private static final int LOGIN_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(this, LoginActivity.class);
        startActivityForResult(i, LOGIN_CODE);

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_CODE) {
            if(resultCode == Activity.RESULT_OK){

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Intent i = new Intent(this, LoginActivity.class);
                startActivityForResult(i, LOGIN_CODE);
            }
        }
    }
}