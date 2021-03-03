package com.example.trombinoscope.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trombinoscope.ItemClickSupport;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Trombi;
import com.example.trombinoscope.adapter.TrombiAdapter;
import com.example.trombinoscope.view.TrombinoscopesViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class trombinoscopes extends Fragment {

    private TrombinoscopesViewModel mViewModel;
    protected Button add;
    private RecyclerView recyclerView;
    private List<Trombi> trombis;
    private TrombiAdapter adapter;
    private JSONObject js = new JSONObject();
    public static trombinoscopes newInstance() {
        return new trombinoscopes();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trombinoscopes_fragment, container, false);
        mViewModel= ViewModelProviders.of(this).get(TrombinoscopesViewModel.class);
        mViewModel.initTrrombinoscopesViewModel();
        Log.e("initMV", String.valueOf(mViewModel.getTrombinoscopesViewModel()));
        this.recyclerView = view.findViewById(R.id.fragment_main_recycler_view);
        this.configureOnClickRecyclerView();
        configureRecyclerView();
        Log.e("cpt", String.valueOf(mViewModel.getCpt()));
        if(mViewModel.getCpt()==0)
            RequestTrombis(view);
        adapter.notifyDataSetChanged();

        //adapter.notifyDataSetChanged();
        add = view.findViewById(R.id.addTro);
        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                mViewModel.clear();
                Navigation.findNavController(view).navigate(
                        R.id.action_trombinoscopes3_to_addTrombinoscopes);
                //finish();
            }
        });
        return view;
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.layout_tromb_frag)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // 1 - Get trombi from adapter
                        Trombi trombi = adapter.getTrombi(position);
                        // 2 - save trombi in Bundle and navigate
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("Trombi", trombi);
                        mViewModel.clear();
                        Navigation.findNavController(v).navigate(R.id.action_trombinoscopes3_to_editTrombi, bundle);
                    }
                });
    }


    private void configureRecyclerView(){
        // 3.1 - Reset list
        this.trombis = mViewModel.getTrombinoscopesViewModel();
        Log.e("configureRecyclerView", String.valueOf(trombis));
        // 3.2 - Create adapter passing the list of users
        this.adapter = new TrombiAdapter(this.trombis);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // 3.4 - Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void RequestTrombis(View view) {
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
        update();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray Nom = response.getJSONArray("Nom");
                    JSONArray Date = response.getJSONArray("Date");
                    JSONArray Tag = response.getJSONArray("Tag");
                    JSONArray Idpromo = response.getJSONArray("Idpromo");
                    for(int i = 0; i < Nom.length(); i++) {
                        trombis.add(new Trombi(Nom.getString(i), Date.getString(i), Tag.getString(i), Idpromo.getString(i)));
                        Log.e("trombis : ", String.valueOf(trombis));
                        mViewModel.setTrombinoscopesViewModel(trombis);
                        Log.e("trombisVM : ", String.valueOf(mViewModel.getTrombinoscopesViewModel()));
                    }
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
        s.addToRequestQueue(jsonObjectRequest);
    }

    private void update() {
        try{
            js.put("request", "trombis");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}