package com.example.trombinoscope.view;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Etudiant;
import com.example.trombinoscope.dataStructure.Msg;
import com.example.trombinoscope.fragments.Messagerie.Message;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class MsgViewHolder extends RecyclerView.ViewHolder {

    // @BindView(R.id.fragment_main_item) TextView textView;
    private TextView msg, date, pseudo;
    private ImageView dots;
    private JSONObject js = new JSONObject();
    private Context context;
    private Resources resources;
    private String idTrombi;

    public MsgViewHolder(View itemView, Context context, Resources resources) {
        super(itemView);
        this.context = context;
        this.resources = resources;
        msg = itemView.findViewById(R.id.message_text);
        date = itemView.findViewById(R.id.dateMsg);
        pseudo = itemView.findViewById(R.id.pseudoUser);
        dots = itemView.findViewById(R.id.dots);
        dots.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    js.put("request", "deletMsg");
                    js.put("date", date.getText().toString());
                    js.put("pseudo", pseudo.getText().toString());
                    js.put("msg", msg.getText().toString());
                    js.put("idTrombi", idTrombi );
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MySingleton s = MySingleton.getInstance(context);
                String url = s.getUrl();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("Flag").equals("true"))
                                Snackbar.make(v, resources.getString(R.string.delet_msg), Snackbar.LENGTH_LONG).show();
                            else if(response.getString("Flag").equals("false"))
                                Snackbar.make(v, resources.getString(R.string.action_impossible), Snackbar.LENGTH_LONG).show();
                            else if(response.getString("Flag").equals("notAllow"))
                                Snackbar.make(v, resources.getString(R.string.action_not_allow), Snackbar.LENGTH_LONG).show();



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
        });


    }

    public void updateWithMsg(Msg msg) {
        this.msg.setText(msg.getMsg());
        this.date.setText(msg.getDate());
        this.pseudo.setText(msg.getUser());
        idTrombi = msg.getIdTrombi();

    }

    public void setVisible(boolean flag, String perso){
        if (flag && pseudo.getText().toString().equals(perso)){
            date.setVisibility(View.VISIBLE);
            dots.setVisibility(View.VISIBLE);
        }
        else if (flag)
            date.setVisibility(View.VISIBLE);
        else{
            date.setVisibility(View.INVISIBLE);
            dots.setVisibility(View.INVISIBLE);

        }
    }

    public String getPseudo(){
        return pseudo.getText().toString();
    }

}
    /*public interface OnClickEtu{
        void onTrombiClick(Trombi tab);
    }*/
