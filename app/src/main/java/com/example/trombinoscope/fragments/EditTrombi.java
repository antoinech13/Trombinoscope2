package com.example.trombinoscope.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trombinoscope.dataStructure.Etudiant;
import com.example.trombinoscope.R;

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

    private List<Etudiant> Etudiants;
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
        return inflater.inflate(R.layout.fragment_edit_trombi, container, false);
    }


   /* private void configureRecyclerView(){
        // 3.1 - Reset list
        this.Etudiants = new ArrayList<>();

        // 3.2 - Create adapter passing the list of users
        this.adapter = new TrombiAdapter(this.Etudians);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // 3.4 - Set layout manager to position the items
        this.recyclerView.setLayoutManager(new GridLayoutManager(getActivity()));

    }*/

}