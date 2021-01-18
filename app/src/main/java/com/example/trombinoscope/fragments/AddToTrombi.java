package com.example.trombinoscope.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.trombinoscope.R;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddToTrombi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddToTrombi extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int CAMERA_REQUEST_CODE = 102;
    private static final int PERMISSION_REQUEST_CODE = 200;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button photo, gallerie, suivant;
    private ImageView image;
    private TextInputEditText nom, prenom;
    private Bitmap img;

    public AddToTrombi() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddToTrombi.
     */
    // TODO: Rename and change types and number of parameters
    public static AddToTrombi newInstance(String param1, String param2) {
        AddToTrombi fragment = new AddToTrombi();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_to_trombi, container, false);

        photo = view.findViewById(R.id.photo);
        gallerie = view.findViewById(R.id.galleryBtn);
        suivant = view.findViewById(R.id.suivant);

        nom = view.findViewById(R.id.nom);
        prenom = view.findViewById(R.id.prenom);

        image = view.findViewById(R.id.image);

        photo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                if (checkPermission())
                    openCamera();
                else
                    requestPermission();

                //finish();
            }
        });

        suivant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                clear();
            }
        });

        return view;

    }

    private void clear() {
        this.nom.getText().clear();
        this.prenom.getText().clear();
        this.image.setImageResource(android.R.color.transparent);;
        this.img.recycle();

    }


    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
        if(checkPermission())
            openCamera();
        else
            Toast.makeText(getContext(), "Vous devez autoriser l'accée a la caméra pour utiliser cette fonctionalité", Toast.LENGTH_SHORT).show();
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            img = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(img);
        }
    }
}