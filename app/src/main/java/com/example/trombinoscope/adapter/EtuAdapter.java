package com.example.trombinoscope.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Etudiant;
import com.example.trombinoscope.view.EtuViewHolder;


import java.util.List;

public class EtuAdapter extends RecyclerView.Adapter<EtuViewHolder> {

    // FOR DATA
    private List<Etudiant> etudiants;

    // CONSTRUCTOR
    public EtuAdapter(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    @Override
    public EtuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_etu_frag, parent, false);

        return new EtuViewHolder(view);
    }



    @Override
    public void onBindViewHolder(EtuViewHolder viewHolder, int position) {
        viewHolder.updateWithEtu(this.etudiants.get(position));
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.etudiants.size();
    }

    public Etudiant getEtu(int position){
        return this.etudiants.get(position);
    }
}

