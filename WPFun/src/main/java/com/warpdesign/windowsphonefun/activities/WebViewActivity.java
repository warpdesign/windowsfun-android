package com.warpdesign.windowsphonefun.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ShareActionProvider;
import android.widget.Toast;



import com.warpdesign.windowsphonefun.R;

public class WebViewActivity extends Activity{
    private WebView wv = null;
    private ShareActionProvider mShareActionProvider;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        System.out.println("[WebViewActivity] onCreate");

        setContentView(R.layout.activity_web_view);

        Bundle extras = getIntent().getExtras();

        wv = (WebView)findViewById(R.id.web_view);

        if (extras != null) {
            String title = extras.getString("title");
            String content = "<!DOCTYPE html><html><head><style type=\"text/css\">img, iframe { max-width:100% !important; } a{ color:#1FA2E1; } img{ height: auto !important; }</style></head><body><h1>" + title + "</h1>" + extras.getString("content").replaceAll("\"//www.youtube", "http://www.youtube") + "</body>";
            String encoding = "utf-8";
            String mime = "text/html";
            wv.getSettings().setJavaScriptEnabled(true);
            wv.setWebChromeClient(new WebChromeClient());
            wv.setWebViewClient(new SwAWebClient());
            wv.loadDataWithBaseURL(null, content, mime, encoding, null);
            url = extras.getString("url");
            String section = extras.getString("section");
            setTitle(section);
        } else {
            System.out.println("[WebViewActivity] Ooops: could not retrieve intent extras");
        }

        // add up action
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        System.out.println("[WebViewActivity] onCreateOptionsMenu " + url);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.webview, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        setShareIntent(intent);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_share:
                System.out.println("[WebViewActivity] Need to share link!");

                return true;

            case R.id.action_open_browser:
                System.out.println("[WebViewActivity] Need to open in browser!");
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(myIntent);
                return true;

            case R.id.action_open_copy_url:
                System.out.println("[WebViewActivity] Need to copy link!");

                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                cm.setPrimaryClip(ClipData.newRawUri("URI", Uri.parse(url)));

                Toast toast = Toast.makeText(getApplicationContext(), "Lien copié dans le presse-papiers ", 1);
                toast.show();
                return true;

            // up button
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // finish() is called in super: we only override method to be able to override transition
        super.onBackPressed();

        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    private class SwAWebClient extends WebViewClient {

        ProgressDialog progressDialog;

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            /*
            TODO: do not open urls, only open images, in an overlay
            */
            if (url.startsWith("http://windowsphone.com/") || url.startsWith("http://www.wpfun.fr")) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(myIntent);
            }

            return true;
        }

    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            System.out.println("[WebViewActivity] shareActionProvider ok!");
            mShareActionProvider.setShareIntent(shareIntent);
        } else {
            System.out.println("[WebViewActivity] shareActionProvider NULL!!");
        }
    }
}