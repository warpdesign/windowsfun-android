package com.warpdesign.windowsphonefun.tasks;

import android.os.AsyncTask;

import com.warpdesign.windowsphonefun.interfaces.OnTaskCompletedListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nicolaramz on 19/03/14.
 */
public class DownloadXMLFeedTask extends AsyncTask<Void, Void, String> {
    private OnTaskCompletedListener listener;
    private Boolean forceRequest;
    private String category;

    public DownloadXMLFeedTask(OnTaskCompletedListener listener, Boolean forceRequest, String category) {
        this.listener = listener;
        this.forceRequest = forceRequest;
        this.category = category;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;

        System.out.println("[DownloadXMLFeedTask] Getting RSS feed...");
        result = getRssFeed();

        return result;
    }

    @Override
    protected void onPostExecute(String xml) {
        if (xml != null)
            listener.onTaskCompleted(xml);
    }

    @Override
    protected void onPreExecute() {
        listener.onBeforeTask();
    }

    private String getRssFeed() {
        InputStream in = null;
        String rssFeed = null;

        try {
            URL url = new URL("http://www.wpfun.fr" + category + "/feed/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            System.out.println("[DownloadXMLFeedTask] Getting feed " + url);

            if (forceRequest == true) {
                conn.setRequestProperty("Pragma", "no-cache");
                conn.setRequestProperty("Cache-Control", "no-cache");
            } else {
                System.out.println("[DownloadWMLFeedTask] Trying to get data from cache");
            }

            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");

            return rssFeed;
        } catch (IOException e) {
            e.printStackTrace();
            listener.onNetworkError(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onNetworkError(e);
                }
            }
        }

        return rssFeed;
    }
 }