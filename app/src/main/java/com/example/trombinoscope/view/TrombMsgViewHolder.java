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

public class TrombMsgViewHolder  extends RecyclerView.ViewHolder  {

    // @BindView(R.id.fragment_main_item) TextView textView;
    private TextView formation, tag, date;
    private ImageView img;


    public TrombMsgViewHolder(View itemView) {
        super(itemView);

        formation = itemView.findViewById(R.id.Formation);
        img = itemView.findViewById(R.id.imgTag);
        tag = itemView.findViewById(R.id.Tag);
        date = itemView.findViewById(R.id.Date);

        //ButterKnife.bind(this, itemView);
    }

    public void updateWithTrombi(Trombi tab){
        this.formation.setText(tab.getFormation());
        this.date.setText(tab.getDate());
        int [] color = Trombi.getGcolors();
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {color[0],color[1]});
        gd.setCornerRadius(0f);
        this.img.setBackground(gd);
        this.tag.setText(tab.getTag());

    }




}
