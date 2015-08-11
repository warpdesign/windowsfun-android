package com.warpdesign.windowsphonefun.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nicolas ramz on 28/03/14.
 */
public class Preferences {
    private SharedPreferences prefs;

    public Preferences(Context context, String name) {
        System.out.println("[Preferences] creating shared preferences");
        prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public void setStringVal(String name, String value) {
        System.out.println("[Preferences] setting pref " + name + " to " + value);
        prefs.edit().putString(name, value).commit();
    }

    public void setLongVal(String name, long value) {
        System.out.println("[Preferences] setting long pref " + name + " to " + value);
        prefs.edit().putLong(name, value).commit();
    }

    public String getStringVal(String name) {
        System.out.println("[Preferences] getting pref " + name + " => " + prefs.getString(name, null));
        return prefs.getString(name, null);
    }

    public Long getLongVal(String name) {
        System.out.println("[Preferences] getting long pref " + name + " => " + prefs.getLong(name, 0));
        return prefs.getLong(name, 0);
    }

    public void removeVal(String name) {
        System.out.println("[Preferences] Removing val  " + name);
        prefs.edit().remove(name).commit();
    }
}
