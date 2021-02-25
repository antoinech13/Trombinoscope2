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
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Forgot_Pw#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Forgot_Pw extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button btn_sendCode;
    private EditText email;
    private JSONObject js = new JSONObject();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Forgot_Pw() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForgotPw.
     */
    // TODO: Rename and change types and number of parameters
    public static Forgot_Pw newInstance(String param1, String param2) {
        Forgot_Pw fragment = new Forgot_Pw();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Verification args pas nuls
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_forgot__pw, container, false);
        btn_sendCode = view.findViewById(R.id.SendCode);
        email = view.findViewById(R.id.emailToSend);

        btn_sendCode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                //Verification que les champs sont remplis
                if (Email.isEmpty())
                    Snackbar.make(v, getResources().getString(R.string.chp_email_vide), 1000).show();
                else {
                    checkValidEmail(v, Email);
                }
            }
        });


        return view;
    }


    //Verification du mail
    private void checkValidEmail(View v, String Email){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
        update(Email);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("res").equals("true")) {// res= nom de la clé de la reponse fournie par flask !!! JsonObject doit etre converti en String
                        Snackbar.make(v, getResources().getString(R.string.code_Send), Snackbar.LENGTH_LONG).show();
                        //Création bundle
                        Bundle bundle = new Bundle();
                        bundle.putString("Email", Email); // Recupération email dans le bundle
                        Navigation.findNavController(v).navigate(R.id.action_forgot_Pw_to_code_Recup_Pw, bundle);//Nav vers page code verification + transfert via bundle de l'eamil
                    }
                    else
                        Snackbar.make(v, getResources().getString(R.string.Err_Account_Doesnt_Exist), Snackbar.LENGTH_LONG).show();
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

    //Envoi les variables a flask
    public void update(String Email){
        try {
            js.put("request", "CheckValidMail");
            js.put("email",this.email.getText());
        } catch (JSONException e) {
            e.printStackTrace();
            // Obligatoire avec jsonObject
        }
    }


}