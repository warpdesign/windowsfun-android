package com.warpdesign.windowsphonefun.cache;

import android.content.Context;

import com.warpdesign.windowsphonefun.WPFunApp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by nicolaramz on 20/03/14.
 */
public class MemoryCache {
    String cacheContent = "";
    String cacheName = "";
    Context context;

    public MemoryCache(String cacheName) {
        context = WPFunApp.getCustomAppContext();
        this.cacheName = cacheName;

        loadOrCreateCache();
    }

    public String getCacheContent() {
        return cacheContent;
    }

    public boolean createCache() {

        setCacheContent("");
        try {
            clearCache();
        } catch(IOException e) {
            return false;
        }
        return true;
    }

    public void setCacheContent(String content) {
        cacheContent = content;
    }

    public boolean writeCache() {
        try {
            writeFileContents(cacheContent);
            return true;
        } catch(IOException e) {
            return false;
        }
    }

    public boolean loadOrCreateCache() {
        System.out.println("[MemoryCache] loadOrCreateCache");
        boolean exists = loadCache();

        if (!exists) {
            System.out.println("[MemoryCache] cache does not exist: attempt to create it");
            exists = createCache();
        } else {
            System.out.println("[MemoryCache] cache file exists: yeahhh!");
        }

        return exists;
    }

    public void clearCache() throws IOException {
        FileOutputStream fo = context.openFileOutput(cacheName, Context.MODE_PRIVATE);
        fo.close();
    }

    private boolean loadCache() {
        System.out.println("[MemoryCache] loadCache");
        boolean success = false;

        try {
            System.out.println("[MemoryCache] opening file");
            FileInputStream fi = context.openFileInput(cacheName);
            if (fi != null) {
                System.out.println("[MemoryCache] reading file contents");
                cacheContent = readFileContents(fi);
                fi.close();

                // System.out.println("[MemoryCache] read content " + cacheContent);
                success = true;
            } else {
                System.out.println("[MemoryCache] Could not open file :(");
            }
        } catch(IOException e) {
            System.out.println("[MemoryCache] createCache Exception " + e.toString());
        }
        return success;
    }

    private String readFileContents(FileInputStream fi) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(fi));
        StringBuffer buffer = new StringBuffer();
        String line;

        try {
            System.out.println("[MemoryCache] readFileContents(): attemtping to read content");
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append(System.getProperty("line.separator"));
            }
        } catch(IOException e) {
            System.out.println("[MemoryCache] Exception while reading cache");
            return "";
        }

        // System.out.println("[MemoryCache] Read content " + buffer.toString());
        return buffer.toString();
    }

    private boolean writeFileContents(String content) throws IOException {
        System.out.println("[MemoryCache] writing content to file...");
        System.out.println("[MemoryCache] ========");
        FileOutputStream fo = context.openFileOutput(cacheName, Context.MODE_PRIVATE);
        fo.write(content.getBytes("UTF-8"));
        System.out.println("[MemoryCache] ========");
        return true;
    }
}