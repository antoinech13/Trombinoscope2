package com.example.trombinoscope.fragments.Nav_drawer_fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.trombinoscope.MainActivity;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class User_profil extends Fragment {

    private TextView lastName;
    private TextView firstName;
    private TextView email;
    private JSONObject js = new JSONObject();
    private ImageView userImg;
    private ImageView camera;
    private View view;
    private Button validate;
    private String imgName;
    private JSONArray Img;
    private Bitmap img;
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://trombi-f6e10.appspot.com");
    private Bundle b;

    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_CODE = 1000;
    public static final int DEFAULT_MAX_RETRIES = 0;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profil, container, false);
        ((MainActivity)getActivity()).setDrawer_UnLocked(); //Gestion du nav drawer
        ((MainActivity)getActivity()).getSupportActionBar().show(); //Gestion de la toolbar
        (((MainActivity)getActivity()).findViewById(R.id.toolbar)).findViewById(R.id.toolbarSearch).setVisibility(View.INVISIBLE);//Gestion de la fonction search de la toolbar

        lastName = view.findViewById(R.id.name_user);
        firstName = view.findViewById(R.id.firstname_user);
        email = view.findViewById(R.id.user_email);
        userImg = view.findViewById(R.id.profil_user);
        camera = view.findViewById(R.id.camera_user);
        validate = view.findViewById(R.id.valide_img);

        if(getArguments()!=null)
        {
            if(getArguments().getString("source").equals("CamFrag"))
            {
                email.setText((getArguments().getString("email")));
                lastName.setText((getArguments().getString("nom")));
                firstName.setText((getArguments().getString("prenom")));
                validate.setVisibility(View.VISIBLE);
                if(getArguments().containsKey("BitmapImage")) {
                    img = getArguments().getParcelable("BitmapImage");
                    if (img != null) {
                        userImg.setImageBitmap(img);
                    }
                }
            }
        }
        else
        {
            b = new Bundle();
            RequestUser();
        }

        camera.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                if (checkPermission()) {
                    openCamera();
                }
                else
                    requestPermission();
            }
        });

        validate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd_hh:mm:ss");
                String strDate = dateFormat.format(date);
                imgName ="trombiImages/"+ lastName.getText() + "_" + firstName.getText() + "_" + strDate + ".jpg";
                StorageReference ref = storage.getReference(imgName);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                byte[] imgByte = outputStream.toByteArray();
                UploadTask uT = ref.putBytes(imgByte);
                uT.addOnCompleteListener( new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    }
                });
                updateUser(view);
                Log.e("Image name :",imgName);
                validate.setVisibility(view.INVISIBLE);
            }
        });
        return view;
    }


    private void RequestUser() {
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
        getUserProfil();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    lastName.setText(response.getString("Nom"));
                    firstName.setText(response.getString("Prenom"));
                    email.setText(response.getString("Email"));
                    imgName = response.getString("Image");
                    Log.e("RequestUser ImgName",imgName);
                    if (!(imgName.equals("null"))) {
                        StorageReference ref;
                        StreamDownloadTask Task;
                        ref = storage.getReference(imgName);
                        Task = ref.getStream();
                        int SDK_INT = android.os.Build.VERSION.SDK_INT;
                        if (SDK_INT > 8)
                        {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                    .permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            //your codes here
                            Task.addOnSuccessListener(new OnSuccessListener<StreamDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(StreamDownloadTask.TaskSnapshot taskSnapshot) {
                                    InputStream inputStream = taskSnapshot.getStream();
                                    Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                                    userImg.setImageBitmap(bmp);
                                }
                            });
                        }
                    }
                    b.putString("email", String.valueOf(email.getText()));
                    b.putString("nom", String.valueOf(lastName.getText()));
                    b.putString("prenom", String.valueOf(firstName.getText()));
                }
                catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        s.addToRequestQueue(jsonObjectRequest);
    }

        private boolean checkPermission() {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                return false;
            return true;
        }

        private void requestPermission() {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CODE);
            if(checkPermission())
                openCamera();
            else
                Snackbar.make(getView(), getResources().getString(R.string.Err_Camera_Acces), Snackbar.LENGTH_SHORT).show();
        }

    private void openCamera() {
        NavController c =  Navigation.findNavController(view) ;
        c.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.camFrag){
                    ((MainActivity)getActivity()).setDrawer_Locked(); //Gestion du nav drawer
                    ((MainActivity)getActivity()).getSupportActionBar().hide(); //Gestion de la toolbar
                }
            }
        });
        b.putString("origine","user_profil");
        Log.e("user_profil",b.getString("origine"));
        c.navigate(R.id.action_user_profil_to_camFrag, b);
    }


    //Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case IMAGE_PICK_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, IMAGE_PICK_CODE);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == IMAGE_PICK_CODE) {
            userImg.setImageURI(data.getData());
            BitmapDrawable drawable = (BitmapDrawable) userImg.getDrawable();
            img = drawable.getBitmap();
        }
    }

    private void updateUser(View v){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
        update();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("res").equals("notAllow"))
                        Snackbar.make(v, "non autorisé", Snackbar.LENGTH_LONG).show();
                    else
                        Snackbar.make(v, getResources().getString(R.string.Msg_Info_Update_Img), Snackbar.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        s.addToRequestQueue(jsonObjectRequest);
    }

    public void getUserProfil(){
        try {
            js.put("request","UserProfil");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void update(){
        try {
            js.put("request", "addImgUser");
            //js.put("img",this.profil);
            //js.put("email", this.email.getText());
            js.put("img",this.imgName);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
