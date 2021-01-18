package com.example.trombinoscope.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.trombinoscope.ItemClickSupport;
import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Trombi;
import com.example.trombinoscope.adapter.TrombiAdapter;
import com.example.trombinoscope.view.TrombinoscopesViewModel;

import java.util.ArrayList;
import java.util.List;

public class trombinoscopes extends Fragment {

    private TrombinoscopesViewModel mViewModel;
    protected Button add;

    //@BindView(R.id.fragment_main_recycler_view) RecyclerView recyclerView;
    private RecyclerView recyclerView;

    private List<Trombi> trombis;
    private TrombiAdapter adapter;

    public static trombinoscopes newInstance() {
        return new trombinoscopes();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trombinoscopes_fragment, container, false);
        this.recyclerView = view.findViewById(R.id.fragment_main_recycler_view);
        this.configureRecyclerView();
        trombis.add(new Trombi("Compétences complémentaire en informatique", "CCI", "2019-2020"));
        trombis.add(new Trombi("Compétences complémentaire en informatique", "CCI", "2020-2021"));
        this.configureOnClickRecyclerView();
        adapter.notifyDataSetChanged();
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


                        Navigation.findNavController(v).navigate(
                                R.id.action_trombinoscopes3_to_trombiOptions, bundle);
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
        this.trombis = new ArrayList<>();

        // 3.2 - Create adapter passing the list of users
        this.adapter = new TrombiAdapter(this.trombis);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // 3.4 - Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

}