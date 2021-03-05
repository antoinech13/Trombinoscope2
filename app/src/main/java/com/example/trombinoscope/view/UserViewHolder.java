package com.example.trombinoscope.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Trombi;
import com.example.trombinoscope.dataStructure.User;

public class UserViewHolder extends RecyclerView.ViewHolder {

    // @BindView(R.id.fragment_main_item) TextView textView;
    private TextView nom, prenom, email;
    private Switch edit, admin;


    public UserViewHolder(View itemView) {
        super(itemView);

        nom = itemView.findViewById(R.id.nompopup);
        prenom = itemView.findViewById(R.id.prenompopup);
        email = itemView.findViewById(R.id.emailpopup);
        edit = itemView.findViewById(R.id.editionpopup);
        admin = itemView.findViewById(R.id.adminpopup);

        admin.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    edit.setChecked(true);
            }
        });

        edit.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                    admin.setChecked(false);
            }
        });

    }

    public void updateWithTrombi(User tab){
        this.nom.setText(tab.getNom());
        this.prenom.setText(tab.getPrenom());
        this.email.setText(tab.getEmail());

        if(tab.getDroit() == 3)
            admin.setChecked(true);
        if(tab.getDroit() == 2)
            edit.setChecked(true);
    }




}
