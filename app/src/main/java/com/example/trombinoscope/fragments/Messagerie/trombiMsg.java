package com.example.trombinoscope.fragments.Messagerie;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trombinoscope.ItemClickSupport;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;
import com.example.trombinoscope.adapter.TrombMsgAdapter;
import com.example.trombinoscope.adapter.TrombiAdapter;
import com.example.trombinoscope.dataStructure.Trombi;
import com.example.trombinoscope.view.TrombinoscopesViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link trombiMsg#newInstance} factory method to
 * create an instance of this fragment.
 */
public class trombiMsg extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Trombi> trombis;
    private TrombMsgAdapter adapter;
    private TrombinoscopesViewModel mViewModel;
    private RecyclerView recyclerView;
    private JSONObject js= new JSONObject();
    public trombiMsg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment trombiMsg.
     */
    // TODO: Rename and change types and number of parameters
    public static trombiMsg newInstance(String param1, String param2) {
        trombiMsg fragment = new trombiMsg();
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

        View view = inflater.inflate(R.layout.fragment_trombi_msg, container, false);

        mViewModel = ViewModelProviders.of(this).get(TrombinoscopesViewModel.class);
        mViewModel.initTrrombinoscopesViewModel();
        recyclerView = view.findViewById(R.id.rvTrMsg);
        this.configureOnClickRecyclerView();
        getTrombis();

        return view;
    }

    private void configureRecyclerView(){
        // 3.1 - Reset list
        this.trombis = mViewModel.getTrombinoscopesViewModel();
        // 3.2 - Create adapter passing the list of users
        this.adapter = new TrombMsgAdapter(this.trombis);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // 3.4 - Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.layout_tromb_frag)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // 1 - Get user from adapter
                        Trombi trombi = adapter.getTrombi(position);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("Trombi", trombi);
                        Navigation.findNavController(v).navigate(R.id.action_trombiMsg_to_message2, bundle);
                    }
                });
    }

    private void getTrombis(){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
        update();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                configureRecyclerView();
                JSONArray Nom = null;
                try {
                    Nom = response.getJSONArray("Nom");
                    JSONArray Date = response.getJSONArray("Date");
                    JSONArray Tag = response.getJSONArray("Tag");
                    JSONArray Idpromo = response.getJSONArray("Idpromo");
                    JSONArray Droit = response.getJSONArray("Droit");
                    for (int i = 0; i < Nom.length(); i++) {

                        if(Droit.getString(i).equals("2"))
                            trombis.add(new Trombi(Nom.getString(i), Tag.getString(i), Date.getString(i), Idpromo.getString(i), 2));
                        else if(Droit.getString(i).equals("1"))
                            trombis.add(new Trombi(Nom.getString(i), Tag.getString(i), Date.getString(i), Idpromo.getString(i), 1));
                        else if(Droit.getString(i).equals("3"))
                            trombis.add(new Trombi(Nom.getString(i), Tag.getString(i), Date.getString(i), Idpromo.getString(i), 3));
                        mViewModel.setTrombinoscopesViewModel(trombis);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        s.addToRequestQueue(jsonObjectRequest);
    }

    private void update(){
        try {
            js.put("request", "trombis");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}