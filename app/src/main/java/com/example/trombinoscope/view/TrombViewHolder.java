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
    TextView textView, dateView;
    TextView imgText;
    ImageView img, adminStar;

    public TrombViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.fragment_main_item);
        img = itemView.findViewById(R.id.fragment_main_item_image);
        imgText = itemView.findViewById(R.id.myImageViewText);
        dateView = itemView.findViewById(R.id.fragment_main_item_date);
        adminStar = itemView.findViewById(R.id.adminStar);
        itemView.setOnCreateContextMenuListener(this);
        //ButterKnife.bind(this, itemView);
    }

    public void updateWithTrombi(Trombi tab){
        this.textView.setText(tab.getFormation());
        this.dateView.setText(tab.getDate());
        this.img.setBackgroundColor(Color.rgb(tab.getR(), tab.getG(), tab.getB()));
        this.imgText.setText(tab.getTag());
        this.imgText.setTextColor(Color.WHITE);
        this.imgText.setTypeface(null, Typeface.BOLD);
        if(tab.getRight() == 2)
            adminStar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        //menuInfo is null
        menu.add(Menu.NONE, 1,
                1, "Partager");
        menu.add(Menu.NONE, 2,
                2, "Supprimer");
    }


}

