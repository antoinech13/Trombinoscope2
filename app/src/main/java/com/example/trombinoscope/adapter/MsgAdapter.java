package com.example.trombinoscope.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trombinoscope.R;

import com.example.trombinoscope.dataStructure.Msg;
import com.example.trombinoscope.view.MsgViewHolder;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgViewHolder> {

    // FOR DATA
    private List<Msg> msgs;

    // CONSTRUCTOR
    public MsgAdapter(List<Msg> msgs) {
        this.msgs = msgs;
    }

    @Override
    public MsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.message, parent, false);

        return new MsgViewHolder(view);
    }



    @Override
    public void onBindViewHolder(MsgViewHolder viewHolder, int position) {
        viewHolder.updateWithMsg(this.msgs.get(position));
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.msgs.size();
    }

    public Msg getMsg(int position){
        return this.msgs.get(position);
    }
}
