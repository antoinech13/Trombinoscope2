package com.example.trombinoscope.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Trombi;


public class TrombViewHolder extends RecyclerView.ViewHolder {

   // @BindView(R.id.fragment_main_item) TextView textView;
    TextView textView, dateView;
    TextView imgText;
    ImageView img;

    public TrombViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.fragment_main_item);
        img = itemView.findViewById(R.id.fragment_main_item_image);
        imgText = itemView.findViewById(R.id.myImageViewText);
        dateView = itemView.findViewById(R.id.fragment_main_item_date);
        //ButterKnife.bind(this, itemView);
    }

    public void updateWithTrombi(Trombi tab){
        this.textView.setText(tab.getFormation());
        this.dateView.setText(tab.getDate());
        this.img.setBackgroundColor(Color.rgb(tab.getR(), tab.getG(), tab.getB()));
        this.imgText.setText(tab.getTag());
        this.imgText.setTextColor(Color.WHITE);
        this.imgText.setTypeface(null, Typeface.BOLD);
    }

    public interface OnClickTrombi{
        void onTrombiClick(Trombi tab);
    }


}

