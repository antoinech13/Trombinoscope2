package com.example.trombinoscope.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trombinoscope.GenericTextWatcher;
import com.example.trombinoscope.MainActivity;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Code_Recup_Pw#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Code_Recup_Pw extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button btn_valid;
    private Vector<String> vectorCode =new Vector<String>(5);
    private String code;
    private EditText case1, case2, case3, case4, case5;
    private JSONObject js = new JSONObject();
    private String email;
    private Vector<EditText> editTexts= new Vector<EditText>(5);

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Code_Recup_Pw() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Code_Recup_Pw.
     */
    // TODO: Rename and change types and number of parameters
    public static Code_Recup_Pw newInstance(String param1, String param2) {
        Code_Recup_Pw fragment = new Code_Recup_Pw();
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
        View view= inflater.inflate(R.layout.fragment_code__recup__pw, container, false);
        ((MainActivity)getActivity()).setDrawer_Locked();
        btn_valid = view.findViewById(R.id.btn_validCode);
        case1 = view.findViewById(R.id.caseCode1);
        case2 = view.findViewById(R.id.caseCode2);
        case3 = view.findViewById(R.id.caseCode3);
        case4 = view.findViewById(R.id.caseCode4);
        case5 = view.findViewById(R.id.caseCode5);
        editTexts.add(case1);
        editTexts.add(case2);
        editTexts.add(case3);
        editTexts.add(case4);
        editTexts.add(case5);
        case1.addTextChangedListener(new GenericTextWatcher(case1, view));
        case2.addTextChangedListener(new GenericTextWatcher(case2, view));
        case3.addTextChangedListener(new GenericTextWatcher(case3, view));
        case4.addTextChangedListener(new GenericTextWatcher(case4, view));
        case5.addTextChangedListener(new GenericTextWatcher(case5, view));


        btn_valid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String Case1 = case1.getText().toString().trim();
                String Case2 = case2.getText().toString().trim();
                String Case3 = case3.getText().toString().trim();
                String Case4 = case4.getText().toString().trim();
                String Case5 = case5.getText().toString().trim();
                vectorCode.add(Case1);
                vectorCode.add(Case2);
                vectorCode.add(Case3);
                vectorCode.add(Case4);
                vectorCode.add(Case5);

                //Verification que les champs sont remplis
                if (Case1.isEmpty() || Case2.isEmpty() || Case3.isEmpty() || Case4.isEmpty() || Case5.isEmpty())
                    Snackbar.make(v, getResources().getString(R.string.Err_Saisie_Chp_Vide), 1000).show();
                else {
                    code = convert(vectorCode);
                    checkCode(v, code);
                }
            }
        });
        return view;
    }

    //Verification du mail
    private void checkCode(View v, String code){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
        update(code);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               try {
                    if (response.getString("res").equals("true"))
                    // res= nom de la clé de la reponse fournie par flask !!! JsonObject doit etre converti en String
                        Navigation.findNavController(v).navigate(R.id.action_code_Recup_Pw_to_reset_Pw, getArguments());//Nav vers page reinitialisation mdp + transfert via bundle de l'email
                    else
                        Snackbar.make(v, getResources().getString(R.string.Err_code), Snackbar.LENGTH_LONG).show();
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
        s.addToRequestQueue(jsonObjectRequest);
    }

    public String convert(Vector<String> vector){
        String res = "";
        for(int i =0; i<vector.size(); i++)
            res+=vector.get(i);
        return res;
    }

    //Envoi les variables a flask
    public void update(String Email){
        try {
            js.put("request", "CheckCode");
            js.put("code",code);
        } catch (JSONException e) {
            e.printStackTrace();
            // Obligatoire avec jsonObject
        }
    }


}