package com.example.trombinoscope.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Etudiant;
import com.example.trombinoscope.dataStructure.Msg;

public class MsgViewHolder extends RecyclerView.ViewHolder {

    // @BindView(R.id.fragment_main_item) TextView textView;
    TextView msg, date, pseudo;



    public MsgViewHolder(View itemView) {
        super(itemView);
        msg = itemView.findViewById(R.id.message_text);
        date = itemView.findViewById(R.id.dateMsg);
        pseudo = itemView.findViewById(R.id.pseudoUser);
        //ButterKnife.bind(this, itemView);
    }

    public void updateWithMsg(Msg msg) {
        this.msg.setText(msg.getMsg());
        this.date.setText(msg.getDate());
        this.pseudo.setText(msg.getUser());

    }
}
    /*public interface OnClickEtu{
        void onTrombiClick(Trombi tab);
    }*/
