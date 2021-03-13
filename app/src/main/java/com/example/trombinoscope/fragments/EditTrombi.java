package com.example.trombinoscope.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.transition.Transition;
import android.util.Base64;
import android.util.Log;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Etudiant> etudiants;
    private EtuAdapter adapter;
    private RecyclerView recyclerView;
    private JSONObject js = new JSONObject();
    private Trombi promo;
    private JSONArray Link, Email, Img, Prenom, Nom;
    private int cpt;
    //public FtpConnection co;



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
        ((MainActivity)getActivity()).setDrawer_UnLocked();
        add = view.findViewById(R.id.ajouter);
        this.recyclerView = view.findViewById(R.id.EtuRecyclerView);
        this.promo = getArguments().getParcelable("Trombi");
        if(this.promo.getRight() < 2)
            add.setVisibility(View.INVISIBLE);


        // Log.e("this.promo", String.valueOf(promo)); // afficher la valeur de promo
        //this.configureRecyclerView();

        this.configureOnClickRecyclerView(); //methode pour passer dans la fiche profile
        update();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
            requestEtu();
        }


        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Log.e("onClick", getArguments().toString());
                Navigation.findNavController(view).navigate(R.id.action_editTrombi_to_addToTrombi, getArguments());
                //finish();
            }
        });

        return view;
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
                Log.e("link", "icii");
                Nom = null;
                try {
                    Nom = response.getJSONArray("Nom");
                    Prenom = response.getJSONArray("Prenom");
                    Img = response.getJSONArray("Img");
                    Email = response.getJSONArray("Email");
                    //Link = response.getJSONArray("Link");
                    Log.e("EditTrombi", "onResponse");
                    /*for(int i = 0; i < Nom.length(); i++){
                        Log.e("i", String.valueOf(i));
                        requestImg(Link.getString(i), Img.optString(i), Nom.getString(i), Prenom.getString(i), Email.getString(i));
                    }*/
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
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                adapter.notifyDataSetChanged();

                            }
                        });
                    }

                   /* for(int i = 0; i < Prenom.length(); i++){
                        Log.d("huip", Img.getString(i));
                        byte[] decodedString = Base64.decode(Image.getString(i), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        etudiants.add(new Etudiant(Nom.getString(i), Prenom.getString(i), Img.getString(i), decodedByte, Email.getString(i)));
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("je me fais catche", "walla2");
                }
                //etudiants.add(new Etudiant(Nom.getString(i), Prenom.getString(i), Img.getString(i), co.getImage(Img.getString(i)), Email.getString(i)));



            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("link", "volley marche pas");
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy( DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        s.addToRequestQueue(jsonObjectRequest);
    }

  /*  private void requestImg(String mImageURLString, String img, String nom, String prenom, String email) {

        Glide.with(this)
                .asBitmap()
                .load(mImageURLString)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new CustomTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                        etudiants.add(new Etudiant(nom, prenom, img, resource, email));
                        adapter.notifyDataSetChanged();
                        Log.e("link", " cfgjvhbj");
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        Log.e("test","yolo");
                    }
                });


    }*/

    private void requestImg(String mImageURLString, String img, String nom, String prenom, String email){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();

        ImageRequest imageRequest = new ImageRequest(mImageURLString, new Response.Listener<Bitmap>() { // Bitmap listener
            @Override
            public void onResponse(Bitmap response) {
                Log.e("on", "good");
                etudiants.add(new Etudiant(nom, prenom, img, response, email));
                adapter.notifyDataSetChanged();
            }
        }, 0, 0, null, new Response.ErrorListener() { // Error listener
            @Override
            public void onErrorResponse(VolleyError error) {
                // Do something with error response
                error.printStackTrace();
                //requestImg(mImageURLString, img, nom, prenom, email);
                Log.e("err", "pas good");
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
            Log.e("[id.promo]",id);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


//Log.e("promo : ", String.valueOf(promo)); chercher le promo
        //test flo pour la navigation entre les 2 fragments

    //methode pour naviguer entre trombi a edit trombi c est celle qui va m interesser
    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.layout_etu_frag)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        // 1 - Get user from adapter
                        Etudiant etu = adapter.getEtu(position);
                        // 2 - Show result in a Toast
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("Etu",etu);
                        bundle.putParcelable("idTrombi",promo); //testons
                        //Log.e("Id Trombi","[id.promo]");
                        Navigation.findNavController(v).navigate(R.id.action_editTrombi_to_profile,bundle);
                    }
                });
    }






}