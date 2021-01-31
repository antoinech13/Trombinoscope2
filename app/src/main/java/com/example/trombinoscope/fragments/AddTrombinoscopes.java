package com.example.trombinoscope.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trombinoscope.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTrombinoscopes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTrombinoscopes extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView formation, tag, date;
    private Button validate;
    private Spinner spinner;
    private JSONObject js= new JSONObject();
    private List<String> categories = new ArrayList<String>();
    private String Univ, CP, Ville, Pays;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddTrombinoscopes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddTrombinoscopes.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTrombinoscopes newInstance(String param1, String param2) {
        Log.d("csio","dddinfdon2");
        AddTrombinoscopes fragment = new AddTrombinoscopes();
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



        Log.d("csio","dddinfdon");
        View view = inflater.inflate(R.layout.fragment_add_trombinoscopes, container, false);
        categories.clear();
        formation = view.findViewById(R.id.formation);
        tag = view.findViewById(R.id.tag);
        date = view.findViewById(R.id.date);
        Bitmap img = StringToBitMap(tag.getText().toString());
        validate = view.findViewById(R.id.val);
        spinner = view.findViewById(R.id.spinnerRegion);
        request(view, true);

        validate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                String[] str = spinner.getSelectedItem().toString().split(" ");
                Univ = str[0];
                CP = str[1];
                Ville = str[2];
                Pays = str[3];
                request(view, false);

                //finish();
            }
        });



        return view;
    }
    Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    Bitmap drawImg(final String text) {
        final Paint textPaint = new Paint() {
            {
                setColor(Color.GREEN);
                setTextAlign(Paint.Align.LEFT);
                setTextSize(20f);
                setAntiAlias(true);
            }
        };
        final Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);

        final Bitmap bmp = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.RGB_565); //use ARGB_8888 for better quality
        final Canvas canvas = new Canvas(bmp);
        canvas.drawText(text, 0, 20f, textPaint);

        return bmp;
    }



    private void request (View view, boolean flag){
        String url = "https://192.168.43.82:5000/";
        update(flag);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(flag){
                    try {
                        JSONArray univ = response.getJSONArray("Univ");
                        JSONArray cp = response.getJSONArray("CP");
                        JSONArray ville = response.getJSONArray("Ville");
                        JSONArray pays = response.getJSONArray("Pays");

                        for(int i = 0; i < univ.length(); i++){
                            categories.add(univ.getString(i) + " " + cp.getString(i) + " " + ville.getString(i) + " " + pays.getString(i));
                        }
                        Collections.sort(categories);
                        categories.add("+ Ajouter une université");
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(dataAdapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                        {
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                            {
                                if(categories.get(position).equals("+ Ajouter une université"))
                                    Navigation.findNavController(view).navigate(R.id.action_addTrombinoscopes_to_addUniversityFragment);
                            } // to close the onItemSelected
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{

                    try {
                        if(response.getString("res") == "true"){
                            Snackbar.make(view, "Promotion ajouté", 1000).show();
                            Navigation.findNavController(view).navigate(R.id.action_addTrombinoscopes_to_trombinoscopes3);}
                        else if(response.getString("res") == "none") {
                            Snackbar.make(view, "Promotion déjà éxistante. Contactez l'administrateur de la promotion.", 2000).show();
                            Navigation.findNavController(view).navigate(R.id.action_addTrombinoscopes_to_trombinoscopes3);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });


        queue.add(jsonObjectRequest);
    }


    public void update(boolean flag){

        if(flag){
            try {
                js.put("request", "infoUnives");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else{
            try {
                js.put("request", "addTrombi");
                js.put("formation", formation.getText());
                js.put("tag", tag.getText());
                js.put("date", date.getText());
                js.put("univ", Univ);
                js.put("cp", CP);
                js.put("ville", Ville);
                js.put("pays", Pays);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }


}