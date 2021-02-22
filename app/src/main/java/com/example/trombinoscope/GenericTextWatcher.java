package com.example.trombinoscope;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.trombinoscope.R;

public class GenericTextWatcher implements TextWatcher {
    private View view, view2;

    public GenericTextWatcher(View view, View view2)
    {
        this.view = view;
        this.view2=view2;
    }
    @Override
    public void afterTextChanged(Editable editable) {

    // TODO Auto-generated method stub
    String text = editable.toString();

            switch(view.getId())
    {

        case R.id.caseCode1:
            Log.e("Danscase1", "ds case1");
            if(text.length()==1)
                view2.findViewById(R.id.caseCode2).requestFocus();
            break;
        case R.id.caseCode2:
            if(text.length()==1)
                view2.findViewById(R.id.caseCode3).requestFocus();
            else if(text.length()==0)
                view2.findViewById(R.id.caseCode1).requestFocus();;
            break;
        case R.id.caseCode3:
            if(text.length()==1)
                view2.findViewById(R.id.caseCode4).requestFocus();
            else if(text.length()==0)
                view2.findViewById(R.id.caseCode2).requestFocus();
            break;
        case R.id.caseCode4:
            if(text.length()==0)
                view2.findViewById(R.id.caseCode5).requestFocus();
            else if (text.length()==0)
                view2.findViewById(R.id.caseCode3).requestFocus();
            break;
        case R.id.caseCode5:
            if(text.length()==0)
                view2.findViewById(R.id.caseCode4).requestFocus();
            break;
    }
}

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

}

