package com.example.trombinoscope.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Trombi;
import com.example.trombinoscope.view.TrombViewHolder;


import java.util.ArrayList;
import java.util.List;

public class TrombiAdapter extends RecyclerView.Adapter<TrombViewHolder> implements Filterable {

    // FOR DATA
    private List<Trombi> Trombis;
    private List<Trombi> TrombiCopy;

    // CONSTRUCTOR
    public TrombiAdapter(List<Trombi> Trombis) {

        this.Trombis = Trombis;
        TrombiCopy =new ArrayList<>(Trombis);
    }

    private int position;


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
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(viewHolder.getPosition());
                return false;
            }
        });
    }
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.Trombis.size();
    }

    public Trombi getTrombi(int position){
        return this.Trombis.get(position);
    }

    @Override
    public Filter getFilter() {
        return trombiFilter;
    }

    private Filter trombiFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Trombi> filteredListTrombi = new ArrayList<>();
            if(constraint == null || constraint.length() == 0)
                filteredListTrombi.addAll(TrombiCopy);
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Trombi trombi : TrombiCopy){
                    if(trombi.getFormation().toLowerCase().trim().contains(filterPattern))
                        filteredListTrombi.add(trombi);
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredListTrombi;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Trombis.clear();
            Trombis.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
