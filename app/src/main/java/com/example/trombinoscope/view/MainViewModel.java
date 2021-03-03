package com.example.trombinoscope.view;

import androidx.lifecycle.ViewModel;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;


public class MainViewModel extends ViewModel {

    private CookieStore cookieStore;
    private CookieManager manager;

    public CookieStore getCookie() {
        if (cookieStore == null) {
            cookieStore = new CookieManager().getCookieStore();

        }
        return cookieStore;
    }

    public CookieManager getManager() {
        if (manager == null) {
             manager = new CookieManager( cookieStore, CookiePolicy.ACCEPT_ALL);
        }
        return manager;
    }

}
