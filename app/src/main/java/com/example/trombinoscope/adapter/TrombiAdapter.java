package com.example.trombinoscope.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Trombi;
import com.example.trombinoscope.view.TrombViewHolder;


import java.util.List;

public class TrombiAdapter extends RecyclerView.Adapter<TrombViewHolder> {

    // FOR DATA
    private List<Trombi> Trombis;

    // CONSTRUCTOR
    public TrombiAdapter(List<Trombi> Trombis) {
        this.Trombis = Trombis;
    }

    @Override
    public TrombViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_tromb_frag, parent, false);

        return new TrombViewHolder(view);
    }



    @Override
    public void onBindViewHolder(TrombViewHolder viewHolder, int position) {
        viewHolder.updateWithTrombi(this.Trombis.get(position));
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.Trombis.size();
    }

    public Trombi getTrombi(int position){
        return this.Trombis.get(position);
    }
}
