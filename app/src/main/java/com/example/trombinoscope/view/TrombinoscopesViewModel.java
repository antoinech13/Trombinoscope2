package com.example.trombinoscope.view;

import androidx.lifecycle.ViewModel;
import com.example.trombinoscope.dataStructure.Trombi;
import java.util.ArrayList;
import java.util.List;


public class TrombinoscopesViewModel extends ViewModel {
    private List<Trombi> trombis;
    private int cpt;


    public void initTrrombinoscopesViewModel() {
        if (trombis == null){
            trombis = new ArrayList<Trombi>();
            cpt = 0;
        }
        else {
            this.trombis = trombis;
            cpt = 1;
        }
    }

    public List<Trombi> getTrombinoscopesViewModel(){
        return trombis;
    }

    public void setTrombinoscopesViewModel(List<Trombi> trombis){
        this.trombis= trombis;
    }

    public int getCpt(){
        return cpt;
    }

    public void clear(){
        trombis=null;
        cpt=0;
    }





}