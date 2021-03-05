package com.example.trombinoscope.view;


import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.User;

public class UserViewHolder extends RecyclerView.ViewHolder {

    // @BindView(R.id.fragment_main_item) TextView textView;
    private TextView nom, prenom, email;
    private Switch edit, admin;

    private User tab;

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
                if(isChecked) {
                    edit.setChecked(true);
                    tab.setDroit(3);
                }
                else
                    tab.setDroit(2);
            }
        });

        edit.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    admin.setChecked(false);
                    tab.setDroit(1);
                }
                else
                    tab.setDroit(2);
            }
        });

    }

    public void updateWithUser(User tab){
        this.tab = tab;
        this.nom.setText(tab.getNom());
        this.prenom.setText(tab.getPrenom());
        this.email.setText(tab.getEmail());

        if(tab.getDroit() == 3)
            admin.setChecked(true);
        if(tab.getDroit() == 2)
            edit.setChecked(true);
    }




}
