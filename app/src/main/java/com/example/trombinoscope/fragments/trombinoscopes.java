package com.example.trombinoscope.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;


import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.trombinoscope.ItemClickSupport;
import com.example.trombinoscope.MainActivity;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;

import com.example.trombinoscope.adapter.UserAdapter;
import com.example.trombinoscope.dataStructure.Trombi;
import com.example.trombinoscope.adapter.TrombiAdapter;
import com.example.trombinoscope.dataStructure.User;
import com.example.trombinoscope.view.TrombinoscopesViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class trombinoscopes extends Fragment {

    //Instances
    private TrombinoscopesViewModel mViewModel;
    protected Button add;
    private AlertDialog.Builder builder;
    //@BindView(R.id.fragment_main_recycler_view) RecyclerView recyclerView;
    private RecyclerView recyclerView;
    private EditText email, formation, tag, date;
    private Switch edit;
    private Spinner s;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Trombi> trombis;
    private TrombiAdapter adapter;
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://trombi-f6e10.appspot.com");
    private List<User> users;
    private JSONObject js = new JSONObject();
    private JSONArray EmailU;
    private  SearchView search;

    public static trombinoscopes newInstance() {
        return new trombinoscopes();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trombinoscopes_fragment, container, false);
        ((MainActivity)getActivity()).setDrawer_UnLocked(); //Gestion du nav drawer
        ((MainActivity)getActivity()).getSupportActionBar().show(); //Gestion de la toolbar
        //Gestion de la recherche de trombi
        search = (((MainActivity)getActivity()).findViewById(R.id.toolbar)).findViewById(R.id.toolbarSearch);
        search.setQueryHint(getString(R.string.Hint_Trombi));
        search.setVisibility(View.VISIBLE);

        mViewModel = ViewModelProviders.of(this).get(TrombinoscopesViewModel.class);
        mViewModel.initTrrombinoscopesViewModel();
        this.recyclerView = view.findViewById(R.id.fragment_main_recycler_view);
        registerForContextMenu(recyclerView);
        this.configureOnClickRecyclerView();
        update(0, null);
        configureRecyclerView();
        if(mViewModel.getCpt()==0)
            RequestTrombis(view, 0, 0, null);
        adapter.notifyDataSetChanged();

        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.clear();
                mViewModel.initTrrombinoscopesViewModel();
                update(0, null);
                if(mViewModel.getCpt()==0)
                    RequestTrombis(view, 0, 0, null);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        searchTrombi();

        //adapter.notifyDataSetChanged();
        add = view.findViewById(R.id.addTro);
        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(
                        R.id.action_trombinoscopes3_to_addTrombinoscopes);
                //finish();
            }
         });
        return view;
    }

    public boolean searchTrombi() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                configureRecyclerView();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = this.adapter.getPosition();
        switch (item.getItemId()){
            case 1:
                createPopupShar(trombis.get(position));
                builder.show();
                break;
            case 2:
                update(1, trombis.get(position).getId());
                RequestTrombis(getView(), 1, position, null);
                break;
            case 3:
                update(3, trombis.get(position).getId());
                users = new ArrayList<User>();
                createPopupRight(trombis.get(position).getId());
                break;
            case 4:
                createPopupDel(trombis.get(position), position);
                builder.show();
        }
        return super.onContextItemSelected(item);
    }


    private void createPopupModify(Trombi tr){
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Partager");
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.popup_modify_trombi, (ViewGroup) getView(), false);
        formation = (EditText) viewInflated.findViewById(R.id.popup_form);
        tag = (EditText) viewInflated.findViewById(R.id.popup_tag);
        date = (EditText) viewInflated.findViewById(R.id.popup_date);
        s = (Spinner) viewInflated.findViewById(R.id.spinnerPopup);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                update(2, tr.getId());
                RequestTrombis(getView(),2, -1, null);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }


    private void createPopupDel(Trombi tr, int position){
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Supprimer");
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.popup_supprimer, (ViewGroup) getView(), false);

        email = (EditText) viewInflated.findViewById(R.id.inputAddress);
        edit = (Switch) viewInflated.findViewById(R.id.switchEdit);
        if(tr.getRight() < 3)
            edit.setVisibility(View.INVISIBLE);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                update(5, tr.getId());
                RequestTrombis(getView(),5, position, null);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    private void createPopupShar(Trombi tr){
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Partager");
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.popup_share, (ViewGroup) getView(), false);
        email = (EditText) viewInflated.findViewById(R.id.inputAddress);
        edit = (Switch) viewInflated.findViewById(R.id.switchEdit);
        if(tr.getRight() < 3)
            edit.setVisibility(View.INVISIBLE);
        builder.setView(viewInflated);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                update(2, tr.getId());
                RequestTrombis(getView(),2, -1, null);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    private void createPopupRight(String id){

        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Gestion des droits");
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.popup_droits, (ViewGroup) getView(), false);
        RecyclerView rV = viewInflated.findViewById(R.id.popupRV);

        UserAdapter userAdapter;

        userAdapter = new UserAdapter(users);
        rV.setAdapter(userAdapter);
        rV.setLayoutManager(new LinearLayoutManager(getActivity()));
        RequestTrombis(getView(),3, -1, userAdapter);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                update(4, id);
                RequestTrombis(getView(),4, -1, null);
                dialog.dismiss();
            }
        });
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
                        Navigation.findNavController(v).navigate(R.id.action_trombinoscopes3_to_editTrombi, bundle);
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TrombinoscopesViewModel.class);
        // TODO: Use the ViewModel
    }

    private void configureRecyclerView(){
        // 3.1 - Reset list
        this.trombis = mViewModel.getTrombinoscopesViewModel();
        // 3.2 - Create adapter passing the list of users
        this.adapter = new TrombiAdapter(this.trombis);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // 3.4 - Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void RequestTrombis(View view, int flag, int position, UserAdapter u) {
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();

        //A QUOI SERT CE TRY ??!!!
        try {
            Log.e("request", js.getString("request"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(flag == 0) {
                        configureRecyclerView();
                        JSONArray Nom = response.getJSONArray("Nom");
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
                        adapter.notifyDataSetChanged();
                    }
                    else if(flag == 1){

                        if(response.getString("Flag").equals("localDelete")){
                            Snackbar.make(view, getResources().getString(R.string.Msg_Info_Trombi_Acces_Denied), 1000).show();
                            trombis.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                        else if(response.getString("Flag").equals("false"))
                            Snackbar.make(view, getResources().getString(R.string.Msg_Info_Echec), 1000).show();
                        else if(response.getString("Flag").equals("notAllow"))
                            Snackbar.make(view, getResources().getString(R.string.Msg_Info_Invalid_Action), 1000).show();
                        else if(response.getString("Flag").equals("LastAdmin"))
                            Snackbar.make(view, getResources().getString(R.string.Msg_Info_Invalid_Action_Last_Admin), 1000).show();
                    }

                    else if(flag == 2) {
                        if (response.getString("Flag").equals("invit"))
                            Snackbar.make(view, getResources().getString(R.string.Msg_Info_Valid_Invit), 1000).show();
                        else if (response.getString("Flag").equals("add"))
                            Snackbar.make(view, getResources().getString(R.string.Msg_Info_User_Add), 1000).show();
                        else if (response.getString("Flag").equals("existe"))
                            Snackbar.make(view, getResources().getString(R.string.Msg_Info_User_Already_Add), 1000).show();
                    }

                    else if (flag == 3){
                        JSONArray nomU = response.getJSONArray("Nom");
                        JSONArray prenomU = response.getJSONArray("Prenom");
                        EmailU = response.getJSONArray("Email");
                        JSONArray DroitU = response.getJSONArray("Droits");

                        for(int j = 0; j < nomU.length(); j++){
                            if(DroitU.getString(j).equals("1"))
                                users.add(new User(nomU.getString(j), prenomU.getString(j), EmailU.getString(j), 1));
                            else if(DroitU.getString(j).equals("2"))
                                users.add(new User(nomU.getString(j), prenomU.getString(j), EmailU.getString(j), 2));
                            else if(DroitU.getString(j).equals("3"))
                                users.add(new User(nomU.getString(j), prenomU.getString(j), EmailU.getString(j), 3));
                        }
                        u.notifyDataSetChanged();
                        builder.show();
                    }

                    else if(flag == 4){
                       if(response.getString("Flag").equals("notAllow"))
                           Snackbar.make(view, getResources().getString(R.string.Msg_Info_Invalid_Action), 1000).show();
                       else if(response.getString("Flag").equals("true"))
                           Snackbar.make(view, getResources().getString(R.string.Msg_Info_Status_Changed_Valid), 1000).show();
                       else if(response.getString("Flag").equals("noAdmin"))
                           Snackbar.make(view, getResources().getString(R.string.Msg_Info_No_Admin), 1000).show();

                    }

                    else if(flag == 5){
                        if(response.getString("Flag").equals("notAllow"))
                            Snackbar.make(view, getResources().getString(R.string.Msg_Info_Invalid_Action), 1000).show();
                        else if(response.getString("Flag").equals("true")){
                            JSONArray img = response.getJSONArray("Img");
                            StorageReference ref;

                            for(int j = 0; j < img.length(); j++) {
                                ref = storage.getReference(img.getString(j));
                                ref.delete();
                            }
                        }
                        trombis.remove(position);
                        adapter.notifyDataSetChanged();
                        Log.e("position",String.valueOf(position));
                        Snackbar.make(view, getResources().getString(R.string.Msg_Info_Deleted), 1000).show();
                    }
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

    private void update(int flag, String idTrombi) {

        try{
            js = new JSONObject();
            if(flag == 0)
                js.put("request", "trombis");
            else if(flag == 1) {
                js.put("request", "deletTrombi");
                js.put("idTrombi", idTrombi);
            }
            else if(flag == 2) {
                js.put("request", "shareTrombi");
                js.put("idTrombi", idTrombi);
                js.put("email", email.getText().toString());
                if(edit.isChecked())
                    js.put("edit", 1);
                else
                    js.put("edit", 0);
            }

            else if(flag == 3){
                js.put("request", "getDroits");
                js.put("id", idTrombi);
            }

            else if(flag == 4){
                js.put("request", "setDroits");
                js.put("email", EmailU);
                js.put("id", idTrombi);

                JSONArray Droit = new JSONArray();
                for (int i = 0; i < users.size(); i++){
                    Droit.put(users.get(i).getDroit());
                }

                js.put("droit", Droit);
            }

            else if(flag == 5) {
                js.put("request", "delFor");
                js.put("idTrombi", idTrombi);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}