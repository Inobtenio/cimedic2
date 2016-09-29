package com.development.unobtainium.cimedic2.managers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.development.unobtainium.cimedic2.activities.LoginActivity;

/**
 * Created by Kevin on 9/16/16.
 */
public class PatientSessionManager {
    private static PatientSessionManager self;
    private final SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "EUA";
    private static final String IS_PATIENT_LOGGED_IN = "IsUserLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ID = "id";
    public static final String KEY_IMAGE = "image";
//    public static final String KEY_REMEMBER_PASSWORD = "rememberPassword";
    //public static final String KEY_TOKEN = "token";

    public PatientSessionManager(Context context){
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static PatientSessionManager getInstance(Context context) {
        if (self == null) {
            self = new PatientSessionManager(context);
        }
        return self;
    }

    //Create login session
    public void createPatientLoginSession(Integer id, String email, String name, String image){
        editor.putBoolean(IS_PATIENT_LOGGED_IN, true);
        editor.putString(KEY_ID, id.toString());
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_IMAGE, image);
        editor.commit();
    }

    public boolean checkLogin(){
        if(!this.isPatientLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
            return true;
        }
        return false;
    }

    public String getLoggedPatientEmail(){
        return pref.getString(KEY_EMAIL, null);
    }
    public String getLoggedPatientName(){
        return pref.getString(KEY_NAME, null);
    }
    public String getLoggedPatientId(){
        return pref.getString(KEY_ID, "");
    }
    public String getLoggedPatientImage(){
        return pref.getString(KEY_IMAGE, "");
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
//        if (!isPasswordRememberable()){
            editor.clear();
            editor.commit();
//        }
    }

    public void ultimateLogout(){
        editor.clear();
        editor.commit();
    }


    // Check for login
    public boolean isPatientLoggedIn(){
        return pref.getBoolean(IS_PATIENT_LOGGED_IN, false);
    }

    // Check for rememberable password
//    public boolean isPasswordRememberable(){
//        return pref.getBoolean(KEY_REMEMBER_PASSWORD, false);
//    }
}