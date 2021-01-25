package com.example.trombinoscope.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.trombinoscope.ItemClickSupport;
import com.example.trombinoscope.R;
import com.example.trombinoscope.adapter.EtuAdapter;
import com.example.trombinoscope.dataStructure.Etudiant;
import com.example.trombinoscope.dataStructure.Trombi;
import com.example.trombinoscope.adapter.TrombiAdapter;
import com.example.trombinoscope.view.TrombinoscopesViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTrombi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTrombi extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Etudiant> etudiants;
    private EtuAdapter adapter;
    private RecyclerView recyclerView;
    Button add;

    public EditTrombi() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditTrombi.
     */
    // TODO: Rename and change types and number of parameters
    public static EditTrombi newInstance(String param1, String param2) {
        EditTrombi fragment = new EditTrombi();
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

        View view =  inflater.inflate(R.layout.fragment_edit_trombi, container, false);
        add = view.findViewById(R.id.ajouter);
        this.recyclerView = view.findViewById(R.id.EtuRecyclerView);
        this.configureRecyclerView();
        etudiants.add(new Etudiant("super", "du"));
        etudiants.add(new Etudiant("yolo", "marche"));
        adapter.notifyDataSetChanged();

        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(
                        R.id.action_editTrombi_to_addToTrombi);
                //finish();
            }
        });


        return view;
    }


   private void configureRecyclerView(){
        // 3.1 - Reset list
        this.etudiants = new ArrayList<>();

        // 3.2 - Create adapter passing the list of users
        this.adapter = new EtuAdapter(this.etudiants);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // 3.4 - Set layout manager to position the items
        this.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

    }

}