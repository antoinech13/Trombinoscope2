package com.example.trombinoscope.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trombinoscope.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddUniversityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddUniversityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView ville, univ, cp, pays;
    private Button val;
    private JSONObject js = new JSONObject();

    public AddUniversityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddUniversityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddUniversityFragment newInstance(String param1, String param2) {
        AddUniversityFragment fragment = new AddUniversityFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_university, container, false);

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
        String url = "https://192.168.43.244:5000/";
        update();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
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


        queue.add(jsonObjectRequest);
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