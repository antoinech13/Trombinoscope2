package com.example.trombinoscope.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private LinearLayout LL;
    private Drawable drawable;

    public TrombViewHolder(View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.nompopup);
        img = itemView.findViewById(R.id.fragment_main_item_image);
        imgText = itemView.findViewById(R.id.myImageViewText);
        dateView = itemView.findViewById(R.id.emailpopup);
        adminStar = itemView.findViewById(R.id.adminStar);
        editStar = itemView.findViewById(R.id.editStar);
        LL = itemView.findViewById(R.id.linearlayoutInfo);
        itemView.setOnCreateContextMenuListener(this);
        //ButterKnife.bind(this, itemView);
    }

    public void updateWithTrombi(Trombi tab){
        this.tab = tab;
        this.textView.setText(tab.getFormation());
        this.dateView.setText(tab.getDate());
        int [] color = Trombi.getGcolors();
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {color[0],color[1]});
        gd.setCornerRadius(0f);
        this.LL.setBackgroundDrawable(gd);
        this.imgText.setText(tab.getTag());
        this.imgText.setTextColor(color[0]);
        this.dateView.setTextColor(Color.WHITE);
        this.textView.setTextColor(Color.WHITE);
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

        if(tab.getRight() == 3) {
            menu.add(Menu.NONE, 3, 4, "Gérer les droits");
            menu.add(Menu.NONE, 4, 6, "Supprimer (action définitive)");
        }
        menu.add(Menu.NONE, 2,
                5, "Ne plus suivre");
        menu.add(Menu.NONE, 5,
                3, "Modifier");
    }


}

