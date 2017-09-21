package com.example.acer.mobilustam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by acer on 28.08.2017.
 */
public class SharedPrefManager {

    //Constants birimleri
    private static final String SHARED_PREF_NAME = "mobilustamsharedpref";
    private static final String KEY_AD = "keyad";
    private static final String KEY_SOYAD = "keysoyad";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_TELNO = "keytelno";
    private static final String KEY_ID = "keyid";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //User login metodu
    //Bu metod user data'yý Shared Pref Manager da depolayacak.
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_AD, user.getAd());
        editor.putString(KEY_SOYAD, user.getSoyad());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_TELNO, user.getTelno());
        editor.apply();
    }

    //User ýn login olup olmadýðýný kontrol eden metod
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;
    }

    //User ýn Logged olmasýný saðlayan metod
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_AD, null),
                sharedPreferences.getString(KEY_SOYAD, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_TELNO, null)
        );
    }

    //Logout metodu
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, loginekrani.class));
    }
}
