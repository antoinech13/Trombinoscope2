package com.example.trombinoscope.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trombinoscope.MainActivity;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddOrganism#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddOrganism extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //Instances
    private TextView ville, univ, cp, pays;
    private Button val;
    private JSONObject js = new JSONObject();

    public AddOrganism() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddOrganism.
     */
    // TODO: Rename and change types and number of parameters
    public static AddOrganism newInstance(String param1, String param2) {
        AddOrganism fragment = new AddOrganism();
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
        View view = inflater.inflate(R.layout.fragment_add_organism, container, false);
        ((MainActivity)getActivity()).setDrawer_UnLocked(); //Gestion du nav drawer
        ((MainActivity)getActivity()).getSupportActionBar().show(); //Gestion de la toolbar
        (((MainActivity)getActivity()).findViewById(R.id.toolbar)).findViewById(R.id.toolbarSearch).setVisibility(View.INVISIBLE);//Gestion de la fonction search de la toolbar

        univ = view.findViewById(R.id.universityName);
        pays = view.findViewById(R.id.pays);
        cp = view.findViewById(R.id.cp);
        ville = view.findViewById(R.id.city);
        val = view.findViewById(R.id.subUniv);

        val.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request(v);
            }
        });

        return view;
    }

    public void request(View view){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
        update();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Navigation.findNavController(view).navigate(R.id.action_addUniversityFragment_to_addTrombinoscopes);
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
            js.put("request", "addUniv");
            js.put("univ", univ.getText());
            js.put("ville", ville.getText());
            js.put("pays", pays.getText());
            js.put("cp", cp.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}