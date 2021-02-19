package com.example.trombinoscope.fragments;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trombinoscope.FtpConnection;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Trombi;
import com.example.trombinoscope.view.AddToTrombiViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    private Button photo, gallerie, suivant, ocr;
    private ImageView image;
    private TextInputEditText nom, prenom, email;
    private Bitmap img;
    private JSONObject js = new JSONObject();
    private Trombi promo;
    private FtpConnection ftp = new FtpConnection();
    private String imgName;
    private AddToTrombiViewModel mViewModel;


    private Uri imageUri;
    private ContentValues values;
    private String imageurl;
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
        mViewModel = ViewModelProviders.of(this).get(AddToTrombiViewModel.class);
        imageUri = mViewModel.getUri();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_to_trombi, container, false);
        Log.e("dsnj", "création");
        photo = view.findViewById(R.id.photo);
        gallerie = view.findViewById(R.id.galleryBtn);
        suivant = view.findViewById(R.id.suivant);
        ocr = view.findViewById(R.id.ocr);

        nom = view.findViewById(R.id.nomEtu);
        prenom = view.findViewById(R.id.prenomEtu);
        email = view.findViewById(R.id.email);

        image = view.findViewById(R.id.image);
        this.promo = getArguments().getParcelable("Trombi");

        ocr.setOnClickListener(new View.OnClickListener(){
           public void onClick(View view){
               runTextRecognition();
               Log.e("dji", imageurl);
           }
        });


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
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd_hh:mm:ss");
                String strDate = dateFormat.format(date);
                imgName = nom.getText() + "_" + prenom.getText() + "_" + strDate + ".jpg";
                ftp.sendImage(img, imgName);
                addStudent(view);
                clear();
            }
        });

        return view;

    }

    private void clear() {
        this.nom.getText().clear();
        this.email.getText().clear();
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
            Toast.makeText(getContext(), "Vous devez autoriser l'accès à la caméra pour utiliser cette fonctionalité", Toast.LENGTH_SHORT).show();
    }

    private void openCamera() {
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        mViewModel.setUri(imageUri);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);

        //Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {


            img = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(img);
        }
    }*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case CAMERA_REQUEST_CODE:
                if (requestCode == CAMERA_REQUEST_CODE)
                    if (resultCode == getActivity().RESULT_OK) {
                        try {
                            img = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                            imageurl = getFilePath(imageUri);
                            File fdelete = new File(imageurl);
                            fdelete.delete();
                            Matrix matrix = new Matrix();
                            matrix.postRotate(90);
                            img = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);


                            image.setImageBitmap(img);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
        }
    }

    private String getFilePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null
            cursor.close();
            return picturePath;
        }
        return null;
    }

    private void addStudent(View v){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
        update();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Snackbar.make(v, R.string.student_add, Snackbar.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });


        s.addToRequestQueue(jsonObjectRequest);
    }

    public void update(){
        try {
            js.put("request", "add");
            js.put("prenom",this.prenom.getText());
            js.put("nom",this.nom.getText());
            js.put("promo",this.promo.getId());
            js.put("img",this.imgName);
            js.put("email", this.email.getText());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void runTextRecognition(){
        FirebaseVisionImage imageOcr = FirebaseVisionImage.fromBitmap(this.img);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        detector.processImage(imageOcr).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                processTextRecognitionResult(firebaseVisionText);
            }
        });
    }

    private void processTextRecognitionResult(FirebaseVisionText texts){
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if(blocks.size() == 0){
            Log.e("dn", "pas de text");
            return;
        }
        for (int i = 0; i < blocks.size(); i++){
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for(int j = 0; j < lines.size(); j++){
                Log.e("cndsj", lines.get(j).getText());
            }
        }


    }
}