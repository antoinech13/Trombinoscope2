package com.example.trombinoscope.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Etudiant;
import com.example.trombinoscope.dataStructure.Trombi;

public class EtuViewHolder extends RecyclerView.ViewHolder {

    // @BindView(R.id.fragment_main_item) TextView textView;
    TextView nom;
    TextView prenom;
    ImageView img;

    public EtuViewHolder(View itemView) {
        super(itemView);
        nom = itemView.findViewById(R.id.nomEtu);
        img = itemView.findViewById(R.id.profilePictureEtu);
        prenom = itemView.findViewById(R.id.prenomEtu);
        //ButterKnife.bind(this, itemView);
    }

    public void updateWithEtu(Etudiant tab) {
        this.nom.setText(tab.getNom());
        this.prenom.setText(tab.getPrenom());
        this.img.setImageBitmap(tab.getImg());
    }
}
    /*public interface OnClickEtu{
        void onTrombiClick(Trombi tab);
    }*/


