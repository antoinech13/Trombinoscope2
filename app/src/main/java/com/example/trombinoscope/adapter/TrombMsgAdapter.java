package com.example.trombinoscope.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Trombi;
import com.example.trombinoscope.view.TrombMsgViewHolder;


import java.util.List;

public class TrombMsgAdapter extends RecyclerView.Adapter<TrombMsgViewHolder> {

    // FOR DATA
    private List<Trombi> Trombis;

    // CONSTRUCTOR
    public TrombMsgAdapter(List<Trombi> Trombis) {
        this.Trombis = Trombis;
    }

    private int position;


    public TrombMsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trombi_msg_layout, parent, false);

        return new TrombMsgViewHolder(view);
    }




    @Override
    public void onBindViewHolder(TrombMsgViewHolder viewHolder, int position) {
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
}