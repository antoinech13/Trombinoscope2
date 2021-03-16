package com.example.trombinoscope.view;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Etudiant;
import com.example.trombinoscope.dataStructure.Msg;

public class MsgViewHolder extends RecyclerView.ViewHolder {

    // @BindView(R.id.fragment_main_item) TextView textView;
    TextView msg, date, pseudo;
    ImageView dots;


    public MsgViewHolder(View itemView) {
        super(itemView);
        msg = itemView.findViewById(R.id.message_text);
        date = itemView.findViewById(R.id.dateMsg);
        pseudo = itemView.findViewById(R.id.pseudoUser);
        dots = itemView.findViewById(R.id.dots);


    }

    public void updateWithMsg(Msg msg) {
        this.msg.setText(msg.getMsg());
        this.date.setText(msg.getDate());
        this.pseudo.setText(msg.getUser());

    }

    public void setVisible(boolean flag, String perso){
        if (flag && pseudo.getText().toString().equals(perso)){
            date.setVisibility(View.VISIBLE);
            dots.setVisibility(View.VISIBLE);
        }
        else if (flag)
            date.setVisibility(View.VISIBLE);
        else{
            date.setVisibility(View.INVISIBLE);
            dots.setVisibility(View.INVISIBLE);

        }
    }

    public String getPseudo(){
        return pseudo.getText().toString();
    }

}
    /*public interface OnClickEtu{
        void onTrombiClick(Trombi tab);
    }*/
