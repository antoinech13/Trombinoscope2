package com.example.trombinoscope.fragments;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.transition.Transition;
import android.util.Base64;
import android.util.Log;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trombinoscope.FtpConnection;
import com.example.trombinoscope.ItemClickSupport;
import com.example.trombinoscope.MainActivity;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;
import com.example.trombinoscope.adapter.EtuAdapter;
import com.example.trombinoscope.dataStructure.Etudiant;
import com.example.trombinoscope.dataStructure.Trombi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTrombi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTrombi extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int WRITE_EXTERNAL_PERM = 23;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //Instances
    private List<Etudiant> etudiants;
    private List<Etudiant> membresCopy;
    private EtuAdapter adapter;
    private RecyclerView recyclerView;
    private JSONObject js = new JSONObject();
    private Trombi promo;
    private JSONArray Link, Email, Img, Prenom, Nom;
    private int cpt;
    private ImageView save;
    private  SearchView search;
    private Button add;
    // Get a non-default Storage bucket
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://trombi-f6e10.appspot.com");

    public EditTrombi() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditTrombi.
     */
    // TODO: Rename and change types and number of parameters
    public static EditTrombi newInstance(String param1, String param2) {
        EditTrombi fragment = new EditTrombi();
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
        View view =  inflater.inflate(R.layout.fragment_edit_trombi, container, false);
        ((MainActivity)getActivity()).setDrawer_UnLocked(); //Gestion du nav drawer
        ((MainActivity)getActivity()).getSupportActionBar().show(); //Gestion de la toolbar
        //Gestion de la recherche de membres
        search = (((MainActivity)getActivity()).findViewById(R.id.toolbar)).findViewById(R.id.toolbarSearch);
        search.setQueryHint(getString(R.string.Hint_member));
        search.setVisibility(View.VISIBLE);
        membresCopy=new ArrayList<>();
        //Fermeture de la barre de recherche
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                etudiants= new ArrayList<>(membresCopy);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        //Export pdf
        save = view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(isReadStoragePermissionGranted()){
                }
                if(isWriteStoragePermissionGranted()){
                }
            }
        });

        //Ajouter un étudiant
        add = view.findViewById(R.id.ajouter);
        this.recyclerView = view.findViewById(R.id.EtuRecyclerView);
        this.promo = getArguments().getParcelable("Trombi");
        if(this.promo.getRight() < 2)
            add.setVisibility(View.INVISIBLE);
        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_editTrombi_to_addToTrombi, getArguments());
                //finish();
            }
        });

        //Profil membre
        this.configureOnClickRecyclerView(); //methode pour passer dans la fiche profil
        update();

        //Affichage liste membres
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            requestEtu();
        }

        return view;
    }

    public boolean searchMember() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                etudiants.clear();
                Log.e("onsearch", String.valueOf(membresCopy.size()));
                for (Etudiant membre : membresCopy) {
                    if (membre.getPrenom().toLowerCase().trim().contains(newText) ||
                            membre.getNom().toLowerCase().trim().contains(newText) )
                        etudiants.add(membre);
                }
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    private void export(){
        PdfDocument objDocument  = new PdfDocument();
        Paint myPaint = new Paint();
        int width = 842, height = 595, margingTextTop = 13, imageDim = 80;
        int cellsCentre = (int)137/2 - (int)imageDim/2;
        String nom, prenom;

        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(width, height, (int)(((double)etudiants.size()/18)*0.999999999999)).create();
        PdfDocument.Page firstPage = objDocument.startPage(myPageInfo);
        Canvas canvas = firstPage.getCanvas();
        Bitmap scaleBitmap;
        int j = 0, k = 1;
        scaleBitmap = Bitmap.createScaledBitmap(etudiants.get(0).getImg(), imageDim,imageDim,false);
        canvas.drawBitmap(scaleBitmap, cellsCentre,cellsCentre, myPaint);
        canvas.drawText("Nom: " + etudiants.get(0).getNom(),cellsCentre, cellsCentre+margingTextTop + imageDim, myPaint);
        canvas.drawText("Prénom: " + etudiants.get(0).getPrenom(), cellsCentre, cellsCentre + 2*margingTextTop + imageDim, myPaint);
        Log.e("sire",String.valueOf(etudiants.size()));
        for(int i = 1; i < etudiants.size(); i++){

            scaleBitmap = Bitmap.createScaledBitmap(etudiants.get(i).getImg(), imageDim,imageDim,false);
            nom = etudiants.get(i).getNom();
            prenom = etudiants.get(i).getPrenom();

            if(i%6 == 0) {
                j++;
                k = 0;
            }
            Log.e("j", String.valueOf(j));
            canvas.drawBitmap(scaleBitmap, 137*k + cellsCentre ,143*j + cellsCentre, myPaint);
            canvas.drawText("Nom: " + nom,cellsCentre + 137*k, cellsCentre+margingTextTop + 143*j + imageDim, myPaint);
            canvas.drawText("Prénom: " + prenom, cellsCentre + 137*k, cellsCentre + 2*margingTextTop + imageDim + 143*j, myPaint);
            k++;
            if(j == 4){
                j = 0;
            }
        }

        objDocument.finishPage(firstPage);
        ContextWrapper cw = new ContextWrapper(getContext());
        File directory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        Log.e("path", String.valueOf(directory));
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd_hh:mm:ss");
        String strDate = dateFormat.format(date);
        File file = new File(directory, "/"+promo.getFormation().replace(" ","")+ "_"+strDate+".pdf");
        try {
            objDocument.writeTo(new FileOutputStream(file));
            Snackbar.make(getView(), getResources().getString(R.string.dir_pdf) + directory.toString(), 1000).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        objDocument.close();
    }

    //Permissions pour export pdf
    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else if(Build.VERSION.SDK_INT >= 30){
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                export();
                return true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_PERM);
                return false;
            }
        }
        else if(Build.VERSION.SDK_INT >= 30) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
                return false;
            }
        }
        else{ //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_EXTERNAL_PERM:
                export();
                break;
        }
    }

    private void configureRecyclerView(){
        // 3.1 - Reset list
        this.etudiants = new ArrayList<>();
        // 3.2 - Create adapter passing the list of users
        this.adapter = new EtuAdapter(this.etudiants);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // 3.4 - Set layout manager to position the items
        this.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
    }

    private void requestEtu(){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                configureRecyclerView();
                Nom = null;
                try {
                    Nom = response.getJSONArray("Nom");
                    Prenom = response.getJSONArray("Prenom");
                    Img = response.getJSONArray("Img");
                    Email = response.getJSONArray("Email");
                    StorageReference ref;
                    StreamDownloadTask Task;
                    for (int i = 0; i < Nom.length(); i++) {
                        ref = storage.getReference(Img.getString(i));
                        Task = ref.getStream();
                        int finalI = i;
                        Task.addOnSuccessListener(new OnSuccessListener<StreamDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(StreamDownloadTask.TaskSnapshot taskSnapshot) {
                                InputStream jean = taskSnapshot.getStream();
                                Bitmap bmp = BitmapFactory.decodeStream(jean);
                                try {
                                    etudiants.add(new Etudiant(Nom.getString(finalI), Prenom.getString(finalI), Img.getString(finalI), bmp, Email.getString(finalI)));
                                    membresCopy.add(new Etudiant(Nom.getString(finalI), Prenom.getString(finalI), Img.getString(finalI), bmp, Email.getString(finalI)));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                    searchMember();
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy( DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        s.addToRequestQueue(jsonObjectRequest);
    }


    private void requestImg(String mImageURLString, String img, String nom, String prenom, String email){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
        ImageRequest imageRequest = new ImageRequest(mImageURLString, new Response.Listener<Bitmap>() { // Bitmap listener
            @Override
            public void onResponse(Bitmap response) {
                etudiants.add(new Etudiant(nom, prenom, img, response, email));
                adapter.notifyDataSetChanged();
            }
        }, 0, 0, null, new Response.ErrorListener() { // Error listener
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        imageRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        s.addToRequestQueue(imageRequest);
    }

    private void update(){
        String id = this.promo.getId();
        js = new JSONObject();
        try {
            js.put("request", "etu");
            js.put("idpromo", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.layout_etu_frag)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // 1 - Get user from adapter
                        Etudiant etu = adapter.getEtu(position);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("Etu",etu);
                        bundle.putParcelable("idTrombi",promo);
                        bundle.putParcelable("Trombi",getArguments().getParcelable("Trombi"));
                        Navigation.findNavController(v).navigate(R.id.action_editTrombi_to_Member_Profil,bundle);
                    }
                });
    }

}