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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trombinoscope.ItemClickSupport;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;
import com.example.trombinoscope.adapter.EtuAdapter;
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

    private TrombinoscopesViewModel mViewModel;
    protected Button add;
    private AlertDialog.Builder builder;
    //@BindView(R.id.fragment_main_recycler_view) RecyclerView recyclerView;
    private RecyclerView recyclerView;

    private EditText email;
    private Switch edit;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<Trombi> trombis;
    private TrombiAdapter adapter;
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://trombi-f6e10.appspot.com");
    private List<User> users;
    private JSONObject js = new JSONObject();
    private JSONArray EmailU;


    public static trombinoscopes newInstance() {
        return new trombinoscopes();
    }

    // ca c est pour trombi a addtrombi
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trombinoscopes_fragment, container, false);
        mViewModel= ViewModelProviders.of(this).get(TrombinoscopesViewModel.class);
        mViewModel.initTrrombinoscopesViewModel();
        this.recyclerView = view.findViewById(R.id.fragment_main_recycler_view);
        registerForContextMenu(recyclerView);
       /* trombis.add(new Trombi("Compétences complémentaire en informatique", "CCI", "2019-2020"));
        trombis.add(new Trombi("Compétences complémentaire en informatique", "CCI", "2020-2021"));*/
        this.configureOnClickRecyclerView();
        update(0, null);
        configureRecyclerView();
        Log.e("cpt", String.valueOf(mViewModel.getCpt()));
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
                                                 }
        );



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
        }
        Log.e("trombi:",trombis.get(position).getFormation());

        return super.onContextItemSelected(item);
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
        builder.setTitle("Partager");
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


    //methode pour naviguer entre trombi a edit trombi c est celle qui va m interesser
    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.layout_tromb_frag)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // 1 - Get user from adapter
                        Trombi trombi = adapter.getTrombi(position);
                        // 2 - Show result in a Toast
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
        Log.e("configureRecyclerView", String.valueOf(trombis));
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

                            Log.e("trombis : ", String.valueOf(trombis));
                            mViewModel.setTrombinoscopesViewModel(trombis);
                            Log.e("trombisVM : ", String.valueOf(mViewModel.getTrombinoscopesViewModel()));
                        }
                        adapter.notifyDataSetChanged();
                    }
                    else if(flag == 1){
                        if(response.getString("Flag").equals("true")) {
                            JSONArray img = response.getJSONArray("Img");
                            StorageReference ref;

                            for(int j = 0; j < img.length(); j++) {
                                ref = storage.getReference(img.getString(j));
                                ref.delete();
                            }

                            Snackbar.make(view, "suppression total réussie", 1000).show();
                            trombis.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                        else if(response.getString("Flag").equals("localDelete")){
                            Snackbar.make(view, "vous n'avez plus accée a ce trombinoscope", 1000).show();
                            trombis.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                        else if(response.getString("Flag").equals("false"))
                            Snackbar.make(view, "supression échoué", 1000).show();
                        else if(response.getString("Flag").equals("notAllow"))
                            Snackbar.make(view, "action non autorisé", 1000).show();
                    }

                    else if(flag == 2) {
                        if (response.getString("Flag").equals("invit"))
                            Snackbar.make(view, "invitation validé", 1000).show();
                        else if (response.getString("Flag").equals("add"))
                            Snackbar.make(view, "utilisateur ajouté", 1000).show();
                        else if (response.getString("Flag").equals("existe"))
                            Snackbar.make(view, "déjà ajouté", 1000).show();
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
                           Snackbar.make(view, "action non autorisé", 1000).show();
                       else if(response.getString("Flag").equals("true"))
                           Snackbar.make(view, "changement de statue validé", 1000).show();
                       else if(response.getString("Flag").equals("noAdmin"))
                           Snackbar.make(view, "Action impossible: pas d'Admin", 1000).show();
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}