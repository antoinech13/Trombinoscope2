package com.example.trombinoscope.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Trombi;


public class TrombViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

   // @BindView(R.id.fragment_main_item) TextView textView;
    private TextView textView, dateView;
    private TextView imgText;
    private ImageView img, adminStar, editStar;
    private Trombi tab;

    public TrombViewHolder(View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.nompopup);
        img = itemView.findViewById(R.id.fragment_main_item_image);
        imgText = itemView.findViewById(R.id.myImageViewText);
        dateView = itemView.findViewById(R.id.emailpopup);
        adminStar = itemView.findViewById(R.id.adminStar);
        editStar = itemView.findViewById(R.id.editStar);
        itemView.setOnCreateContextMenuListener(this);
        //ButterKnife.bind(this, itemView);
    }

    public void updateWithTrombi(Trombi tab){
        this.tab = tab;
        this.textView.setText(tab.getFormation());
        this.dateView.setText(tab.getDate());
        this.imgText.setBackgroundColor(Color.rgb(tab.getR(), tab.getG(), tab.getB()));
        this.imgText.setText(tab.getTag());
        this.imgText.setTextColor(Color.WHITE);
        this.imgText.setTypeface(null, Typeface.BOLD);
        if(tab.getRight() == 3)
            adminStar.setVisibility(View.VISIBLE);
        if(tab.getRight() == 2)
            editStar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        //menuInfo is null
        menu.add(Menu.NONE, 1,
                1, "Partager");
        if(tab.getRight() == 3)
            menu.add(Menu.NONE, 3, 2,"Gérer les droits");
        menu.add(Menu.NONE, 2,
                3, "Supprimer");
    }


}

