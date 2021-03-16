package com.example.trombinoscope.fragments.Messagerie;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trombinoscope.ItemClickSupport;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;
import com.example.trombinoscope.adapter.MsgAdapter;
import com.example.trombinoscope.dataStructure.Msg;
import com.example.trombinoscope.dataStructure.Trombi;
import com.example.trombinoscope.view.MsgViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Message#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Message extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Msg> msgs;
    private Msg msg;
    private MsgAdapter adapter;
    private RecyclerView recyclerView;
    private EditText input, date;
    private Button btn;
    private String idTrombi, perso = "nothing";
    private Thread t;
    private JSONObject js = new JSONObject();
    private boolean running, first;
    private boolean show = false;
    private ImageView dots;

    public Message() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment message.
     */
    // TODO: Rename and change types and number of parameters
    public static Message newInstance(String param1, String param2) {
        Message fragment = new Message();
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
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        this.recyclerView = view.findViewById(R.id.recyclerView);
        this.input = view.findViewById(R.id.editText);
        this.btn = view.findViewById(R.id.button);
        Trombi trombi = getArguments().getParcelable("Trombi");
        idTrombi = trombi.getId();
        configureRecyclerView();
        this.configureOnClickRecyclerView();

        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String strDate = dateFormat.format(date);
                update(strDate);
                input.setText("");
                setMsg();
            }
        });

        t = new Thread() {
            public void run() {
                running = true;
                first = true;
                MySingleton s = MySingleton.getInstance(getContext());
                String url = s.getUrl();
                try {

                        js.put("request", "getMsg");
                        js.put("idTrombi", idTrombi);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray msg = response.getJSONArray("Msg");
                            JSONArray pseudo = response.getJSONArray("Pseudo");
                            JSONArray date = response.getJSONArray("Date");
                            if(first) {
                                perso = response.getString("Perso");
                                Log.e("persoT", perso);
                                Log.e("resp", response.getString("Perso"));
                                first = false;
                            }
                            msgs.clear();
                            for(int i = 0; i < msg.length(); i++)
                                msgs.add(new Msg(msg.getString(i), date.getString(i), idTrombi, pseudo.getString(i), perso));
                            adapter.notifyDataSetChanged();

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


                /*try {
                    js.put("request", "getMsgBis");
                    js.put("idTrombi", idTrombi);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                while (running) {
                    try {
                        Log.e("REFRESH", "run");
                        s.addToRequestQueue(jsonObjectRequest);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();


        return view;
    }

    public void onDestroyView () {
        super.onDestroyView();
        running = false;
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.layout_tromb_frag)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // 1 - Get user from adapter
                        Msg message = adapter.getMsg(position);
                        MsgViewHolder vh = (MsgViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                        Log.e("perso",perso);
                        vh.setVisible(!show, perso);
                        if(show)
                            show = false;
                        else
                            show = true;

                    }
                });
    }

    private void configureRecyclerView(){
        // 3.1 - Reset list
        this.msgs = new ArrayList<>();
        // 3.2 - Create adapter passing the list of users
        this.adapter = new MsgAdapter(this.msgs);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // 3.4 - Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setMsg(){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        s.addToRequestQueue(jsonObjectRequest);
    }

    private void update(String date){
        try {
            js.put("request", "setMsg");
            js.put("msg", this.input.getText().toString());
            js.put("idTrombi", idTrombi);
            js.put("date", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}

