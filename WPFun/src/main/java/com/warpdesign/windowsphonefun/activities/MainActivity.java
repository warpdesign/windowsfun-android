package com.warpdesign.windowsphonefun.activities;

import android.app.ActionBar;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.warpdesign.windowsphonefun.R;
import com.warpdesign.windowsphonefun.WPFunApp;
import com.warpdesign.windowsphonefun.cache.MemoryCache;
import com.warpdesign.windowsphonefun.elements.ThumbListFragment;
import com.warpdesign.windowsphonefun.elements.adapters.ThumbListAdapter;
import com.warpdesign.windowsphonefun.interfaces.ChangeLinkListener;

import com.warpdesign.windowsphonefun.interfaces.OnTaskCompletedListener;
import com.warpdesign.windowsphonefun.tasks.DownloadXMLFeedTask;
import com.warpdesign.windowsphonefun.utils.DateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.warpdesign.windowsphonefun.utils.XMLToMap.parse;

public class MainActivity extends Activity implements ChangeLinkListener, OnTaskCompletedListener {
    static public final String KEY_ITEM = "item";
    static public final String KEY_TITLE = "title"; // parent node
    static public final String KEY_IMAGE = "enclosure";
    static public final String KEY_ARTICLE_URL = "link";
    static public final String KEY_ARTICLE_CONTENT = "content:encoded";
    static public final String KEY_ARTICLE_AUTHOR = "dc:creator";
    static public final String KEY_ARTICLE_PUB_DATE = "pubDate";

    private long lastBuildDate;

    private Menu myMenu;

    private ThumbListFragment listFragment;
    private ThumbListAdapter listAdapter;

    private String cacheContent;

    private boolean alreadyStarted = false;

    private DownloadXMLFeedTask task;

    private WPFunApp app;

    private String[] actions;

    private int currentSection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("[MainActivity] onCreate");
        super.onCreate(savedInstanceState);

        actions = getResources().getStringArray(R.array.nav_list);

        // populate preferences with default value only once
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        // on orientation change, this won't be null
        // in this case we do not want to refresh data
        if (savedInstanceState != null) {
            alreadyStarted = true;
        }

        app = (WPFunApp)WPFunApp.getCustomAppContext();

        setContentView(R.layout.activity_main);

        System.out.println("[MainActivity] new ThumbListAdapter");
        ArrayList<HashMap<String, String>> rssFeed = new ArrayList<HashMap<String, String>>();
        System.out.println("[MainActivity] " + rssFeed.size());
        listAdapter = new ThumbListAdapter(this, rssFeed);
        System.out.println("[MainActivity] new ThumbListFragment");
        this.listFragment = new ThumbListFragment();

        // replace previous fragment on orientation change
        getFragmentManager().beginTransaction().replace(R.id.container, this.listFragment).commit();

        getActionBar().setTitle("");

        createNavSpinner();

        this.listFragment.setAdapter(listAdapter);
    }

    private void createNavSpinner() {
        /** Create an array adapter to populate dropdownlist */
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, actions);


        /** Enabling dropdown list navigation for the action bar */
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        /** Defining Navigation listener */
        ActionBar.OnNavigationListener navigationListener = new ActionBar.OnNavigationListener() {

            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                if (alreadyStarted == false || currentSection != itemPosition) {
                    currentSection = itemPosition;

                    // TODO: handle position and download correct feed
                    alreadyStarted = true;

                    System.out.println("[MainActivity] onNavigationListener, new section = " + currentSection);

                    startGetFeedTask(currentSection);
                }

                return false;
            }
        };

        /** Setting dropdown items and item navigation listener for the actionbar */
        getActionBar().setListNavigationCallbacks(adapter, navigationListener);
    }

    private void startGetFeedTask(int feedNum) {
        // TODO: only do that on first Start
        System.out.println("[MainActivity] startGetFeedTask, getting feed " + app.getFeedForCategory(currentSection));
        // in any case: load
        task = new DownloadXMLFeedTask(this, false, app.getFeedForCategory(currentSection));
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        myMenu = menu;

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        System.out.println("[MainActivity] onCreateOptionsMenu");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("[MainActivity] onOptionsItemSelected()");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            System.out.println("[MainActivity] Need to refresh list!");
            task = new DownloadXMLFeedTask(this, true, app.getFeedForCategory(currentSection));
            task.execute();

            return true;
        } else if (id == R.id.action_about) {
            System.out.println("[MainActivity] Need to display about activity");
            showAbout();
            return true;
        } else if (id == R.id.action_settings) {
            showSettings();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("[MainActivity] onStart");

        System.out.println("[MainActivity] Getting cache...");

        MemoryCache memCache = this.getCurrentCache();

        // get cache data
        cacheContent = memCache.getCacheContent();
        System.out.println("[MainActivity] Got from Cache " + cacheContent.length());

        lastBuildDate = WPFunApp.getPrefs().getLongVal(WPFunApp.BUILD_DATE + currentSection);

        // set data from cache
        if (lastBuildDate > 0 && !cacheContent.isEmpty()) {
            System.out.println("[MainActivity] Cool, we got some data!");
            // System.out.println("[MainActivity] size = " + cacheContent.length());
            ArrayList<HashMap<String, String>> rssFeed = parse(cacheContent);
            listAdapter.changeDataSource(rssFeed);
        } else {
            System.out.println("[MainActivity] Oops, cache is empty :(");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onChangeLink(String content, String title, String url) {
        System.out.println("[MainActivity] Click link listener " + url);
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.putExtra("section", actions[currentSection]);
        startActivity(intent);

        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    public void onBeforeTask() {
        System.out.println("[MainActivity] OnBeforeTask");
        // get last buildDate from prefs so that onTaskCompleted contains the correct date
        lastBuildDate = WPFunApp.getPrefs().getLongVal(WPFunApp.BUILD_DATE + currentSection);
        showLoader();
    }

    public void onTaskCompleted(String xml) {
        System.out.println("[ThumbListFragment] loadTaskCompleted");
        ArrayList<HashMap<String, String>> rssFeedMap = parse(xml);
        MemoryCache memCache = this.getCurrentCache();

        System.out.println("[MainActivity] OnTaskCompleted");

        long time = DateUtils.getTimeFromXMLString(xml);
        System.out.println("[MainActivity] Date from cache " + time);

        System.out.println("[MainActivity] new date: " + time);
        System.out.println("[MainActivity] old date: " + lastBuildDate);

        // if cache is empty we need to use downloaded stuff
        if ((time > 0 && time > lastBuildDate) || memCache.getCacheContent().isEmpty()) {
            // + display a toast message

            // save cache
            memCache.setCacheContent(xml);
            memCache.writeCache();

            // save new date in prefs
            app.getPrefs().setLongVal(WPFunApp.BUILD_DATE + currentSection, time);

            listAdapter.changeDataSource(rssFeedMap);

            System.out.println("[MainActivity] onTaskCompleted " + rssFeedMap.size());

            // show toast message
            Toast toast = Toast.makeText(getApplicationContext(), String.format(getResources().getString(R.string.articles_updated), actions[currentSection]), 2);
            toast.show();
        } else {
            // set new list content
            // TODO: shouldn't changeDataSource if pressing refresh button and there's no need to refresh
            // only when changing section in navigationSpinner
            listAdapter.changeDataSource(parse(memCache.getCacheContent()));

            // show toast message
            Toast toast = Toast.makeText(getApplicationContext(), String.format(getResources().getString(R.string.articles_uptodate), actions[currentSection]), 2);
            toast.show();
        }

        // enable refresh button
        this.hideLoader(false);
    }

    // oops, download interrupted :(
    public void onNetworkError(IOException e) {
        // Hide loader must be called from UI Thread since it's touching view
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoader(true);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // cancel task if not visible anymore
        if (task != null) {
            task.cancel(true);
            task = null;
        }
    }

    void showLoader() {
        MenuItem item = myMenu.findItem(R.id.action_refresh);
        item.setActionView(R.layout.action_refresh_progress);
        item.expandActionView();
    }

    void hideLoader(boolean showErrorMessage) {
        MenuItem item = myMenu.findItem(R.id.action_refresh);
        item.collapseActionView();
        item.setActionView(null);

        if (showErrorMessage) {
            // and display a small error message
            // TODO: add a warning sign in ActionBar ?
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_server_unavailable), 4);
            toast.show();
        }
    }

    protected void showSettings() {
        System.out.println("showSettings()");
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    protected void showAbout() {
        // Inflate the about message contents
        View messageView = getLayoutInflater().inflate(R.layout.dialog_about, null, false);
        Resources res = getResources();

        // When linking text, force to always use default color. This works
        // around a pressed color state bug.
        TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
        int defaultColor = textView.getTextColors().getDefaultColor();
        textView.setTextColor(defaultColor);

        textView = (TextView) messageView.findViewById(R.id.about_version);
        textView.setText(String.format(res.getString(R.string.about_description), WPFunApp.getVersionString()));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(R.string.app_name);
        builder.setView(messageView);
        builder.create();
        builder.show();
    }

    private MemoryCache getCurrentCache() {
        System.out.println("[MainActivity] Getting Cache " + currentSection);
        return app.getCache(currentSection);
    }
}