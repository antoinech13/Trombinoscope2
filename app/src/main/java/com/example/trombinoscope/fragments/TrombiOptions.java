package com.example.trombinoscope.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Trombi;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrombiOptions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrombiOptions extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";




    private Button add, edit;
    private TextView test;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TrombiOptions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrombiOptions.
     */
    // TODO: Rename and change types and number of parameters
    public static TrombiOptions newInstance(String param1, String param2) {
        TrombiOptions fragment = new TrombiOptions();
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
        View view = inflater.inflate(R.layout.fragment_trombi_options, container, false);
        add = view.findViewById(R.id.add);
        edit = view.findViewById(R.id.edit);
        test = view.findViewById(R.id.test);
        Trombi trombi = getArguments().getParcelable("Trombi");
        test.setText(trombi.getDate());
        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(
                        R.id.action_trombiOptions_to_addToTrombi);
                //finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(
                        R.id.action_trombiOptions_to_editTrombi);
                //finish();
            }
        });


        return view;
    }
}