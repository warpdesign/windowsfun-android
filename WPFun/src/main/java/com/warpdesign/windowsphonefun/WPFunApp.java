package com.warpdesign.windowsphonefun;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;

import com.warpdesign.windowsphonefun.cache.MemoryCache;
import com.warpdesign.windowsphonefun.preferences.Preferences;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nicolas ramz on 20/03/14.
 */
public class WPFunApp extends Application {

    public static String BUILD_DATE = "lastBuildDate";

    private static Context context;
    private static Preferences prefs;
    private static String version = "Version Not Found";
    private static ArrayList caches = new ArrayList();
    static public final String[] CACHE_NAMES = {"articles.xml", "tests.xml", "interviews.xml"};
    static public final String[] CATEGORIES = {"/", "/category/tests", "/category/interview"};

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        System.out.println("[WPFunApp] Hello from app!");
        super.onCreate();
        context = getApplicationContext();
        prefs = new Preferences(context, "com.warpdesign.wpfun.settings");

        if (prefs.getLongVal(BUILD_DATE) != null) {
            System.out.println("[WPFunApp] lastPubDate = " + prefs.getLongVal(BUILD_DATE));
        } else {
            System.out.println("[WPFunApp] No lastPubDate set: first run or removed preferences ?");
        }

        initCache();

        PackageManager manager = context.getPackageManager();
        try{
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch(PackageManager.NameNotFoundException e) {
            System.out.println("[WPFunApp] No lastPubDate set: first run or removed preferences ?");
        }
    }



    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getCustomAppContext(){
        return context;
    }

    public static Preferences getPrefs() { return prefs; }

    public static String getVersionString() { return version; }

    private static void initCache() {
        for (int i = 0; i < CACHE_NAMES.length; i++) {
            System.out.println("[WPFunApp] Creating cache" + CACHE_NAMES[i]);
            caches.add(new MemoryCache(CACHE_NAMES[i]));
        }
    }

    // clear caches *and* lastPubdate
    public static boolean clearCaches() {
        boolean success = false;

        for (int i = 0; i < CACHE_NAMES.length; i++) {
            System.out.println("[WPFunApp] Clearing cache" + CACHE_NAMES[i]);
            MemoryCache cache = (MemoryCache)caches.get(i);
            try{
                prefs.setLongVal(BUILD_DATE + i, 0);
                cache.clearCache();
                success = true;
            } catch(IOException e) {

            }
        }
        return success;
    }

    public static MemoryCache getCache(int cacheNum) {
        return (MemoryCache)caches.get(cacheNum);
    }

    public String getFeedForCategory(int categoryId) {
        return CATEGORIES[categoryId];
    }
}