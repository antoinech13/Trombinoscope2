package com.example.trombinoscope.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.trombinoscope.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTrombinoscopes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTrombinoscopes extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    protected TextView formation, tag, date;
    protected Button validate;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddTrombinoscopes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddTrombinoscopes.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTrombinoscopes newInstance(String param1, String param2) {
        AddTrombinoscopes fragment = new AddTrombinoscopes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_trombinoscopes, container, false);
        formation = view.findViewById(R.id.formation);
        tag = view.findViewById(R.id.tag);
        date = view.findViewById(R.id.date);
        Bitmap img = StringToBitMap(tag.getText().toString());
        validate = view.findViewById(R.id.val);

        validate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(
                        R.id.action_addTrombinoscopes_to_trombinoscopes3);
                //finish();
            }
        });



        return view;
    }
    Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    Bitmap drawImg(final String text) {
        final Paint textPaint = new Paint() {
            {
                setColor(Color.GREEN);
                setTextAlign(Paint.Align.LEFT);
                setTextSize(20f);
                setAntiAlias(true);
            }
        };
        final Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);

        final Bitmap bmp = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.RGB_565); //use ARGB_8888 for better quality
        final Canvas canvas = new Canvas(bmp);
        canvas.drawText(text, 0, 20f, textPaint);

        return bmp;
    }
}