package com.warpdesign.windowsphonefun.activities;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.warpdesign.windowsphonefun.R;
import com.warpdesign.windowsphonefun.WPFunApp;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, new SettingsFragment()).commit();

        // add up action
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragment {
        public SettingsFragment() {

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.settings);

            Preference preference = findPreference("pref_flush_cache");

            try {
                preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference p) {
                        System.out.println("[SettingsActivity] Clearing cache");
                        WPFunApp app = (WPFunApp)WPFunApp.getCustomAppContext();
                        if (app.clearCaches()) {
                            Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.toast_cache_flushed), Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.toast_cache_not_flushed), Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        //do something
                        return false;
                    }
                });
            } catch(NullPointerException e) {

            }
        }
    }

}
