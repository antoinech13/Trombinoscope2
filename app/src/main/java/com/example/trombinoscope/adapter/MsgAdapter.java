package com.example.trombinoscope.adapter;

import android.content.Context;
import android.content.res.Resources;
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
    private Context context;
    private Resources resources;

    // CONSTRUCTOR
    public MsgAdapter(List<Msg> msgs, Context context, Resources resources) {
        this.msgs = msgs;
        this.context = context;
        this.resources = resources;
    }

    @Override
    public int getItemViewType(int position){
        if(msgs.get(position).getPerso().equals(msgs.get(position).getUser()))
            return 0;
        return 1;
    }

    @Override
    public MsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        switch (viewType) {
            case 0:
                view = inflater.inflate(R.layout.message_perso, parent, false);
                break;
            default:
                view = inflater.inflate(R.layout.message, parent, false);
        }


        return new MsgViewHolder(view, context, resources);
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
