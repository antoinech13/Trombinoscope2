package com.example.trombinoscope.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trombinoscope.FtpConnection;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Trombi;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
   // private FtpConnection ftp = new FtpConnection();
    private String imgName, Image = "null";
    private View view;


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
        view = inflater.inflate(R.layout.fragment_add_to_trombi, container, false);
        image = view.findViewById(R.id.image);

        if(getArguments() != null){
            this.promo = getArguments().getParcelable("Trombi");
            if(getArguments().containsKey("BitmapImage")) {
                img = getArguments().getParcelable("BitmapImage");
                Log.e("img", img.toString() );
                if (img != null) {

                    Image = bitmapToBase64(img);
                    image.setImageBitmap(img);
                }
            }
        }

        photo = view.findViewById(R.id.photo);
        gallerie = view.findViewById(R.id.galleryBtn);
        suivant = view.findViewById(R.id.suivant);
        ocr = view.findViewById(R.id.ocr);

        nom = view.findViewById(R.id.nomEtu);


        prenom = view.findViewById(R.id.prenomEtu);

        email = view.findViewById(R.id.email);







        ocr.setOnClickListener(new View.OnClickListener(){
           public void onClick(View view){
               runTextRecognition();

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
               // ftp.sendImage(img, imgName);
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

    //Permission
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
        Bundle b = new Bundle();
        b.putParcelable("Trombi", this.promo);
        Navigation.findNavController(view).navigate(R.id.action_addToTrombi_to_camFrag2, b);
    }


    private void addStudent(View v){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
        update();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Snackbar.make(v, R.string.student_add, Snackbar.LENGTH_SHORT).show();
                Log.e("snackbar", "etu ajout");
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
            js.put("image", this.Image);
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
                Log.e("line", lines.get(j).getText());
                String[] elements = lines.get(j).getText().split(":");
                Map<String, String> map = ocrPlacement(elements);
                if(map.size() < 3){
                    elements = null;
                    elements = lines.get(j).getText().split(" ");
                    Map<String, String> map2 = ocrPlacement(elements);
                    if(map2.size() > map.size()){
                        setText(map2);
                    }
                    else{
                        setText(map);
                    }
                }
                else{
                    setText(map);
                }

            }
        }
    }


    public Map<String, String> ocrPlacement(String[] tab){
        Map<String, String> m = new HashMap<String, String>();
        for(int i = 0; i < tab.length; i++){
            if(tab[i].toLowerCase().equals("email") || tab[i].toLowerCase().equals("email:")){
                m.put("email", tab[i+1]);
            }
            else if(tab[i].toLowerCase().equals("prenom") || tab[i].toLowerCase().equals("prenom:") || tab[i].toLowerCase().equals("prénom") || tab[i].toLowerCase().equals("prénom:")  || tab[i].toLowerCase().equals("prènom") || tab[i].toLowerCase().equals("prènom:")){
                m.put("prenom", tab[i+1]);
            }
            else if(tab[i].toLowerCase().equals("nom") || tab[i].toLowerCase().equals("nom:")){
                m.put("nom", tab[i+1]);
            }
        }
        return m;
    }

    public void setText(Map<String, String> m){
        if(m.containsKey("email"))
            email.setText(m.get("email"));
        else if(m.containsKey("prenom"))
            prenom.setText(m.get("prenom"));
        else if(m.containsKey("nom"))
            nom.setText(m.get("nom"));

    }

    private String bitmapToBase64(Bitmap bitmap) {
        Bitmap bitmap2= bitmap.copy(bitmap.getConfig(), true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}