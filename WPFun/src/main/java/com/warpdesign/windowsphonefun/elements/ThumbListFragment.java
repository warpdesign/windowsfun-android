package com.warpdesign.windowsphonefun.elements;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.warpdesign.windowsphonefun.activities.MainActivity;
import com.warpdesign.windowsphonefun.elements.adapters.ThumbListAdapter;
import com.warpdesign.windowsphonefun.interfaces.ChangeLinkListener;

import java.util.HashMap;

/**
 * Created by nicolaramz on 19/03/14.
 */
public class ThumbListFragment extends ListFragment{
    private ThumbListAdapter adapter = null;

    public ThumbListFragment() {

    }

    public void setAdapter(ThumbListAdapter adapter) {
        this.adapter = adapter;
        setListAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("[ThumbListFragment] onStart");
    }

    @Override
    public void onViewCreated(View v,Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        System.out.println("[ThumbListFragment] onViewCreagted: setting list visible");
        this.setListShown(true);
    }

    @Override
    public void onAttach(Activity activity) {
        System.out.println("[ThumbListFragment] onAttach");
        // We verify that our activity implements the listener
        if (! (activity instanceof ChangeLinkListener) )
            throw new ClassCastException();

        super.onAttach(activity);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        System.out.println("[ListFragment] onListItemClick" + position);

        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        HashMap <String, String> data = (HashMap<String, String>)adapter.getItem(position);

        System.out.println("[ThumbListFragment] calling onChangeLink listener with url " + data.get(MainActivity.KEY_ARTICLE_URL));

        ((ChangeLinkListener) getActivity()).onChangeLink(data.get(MainActivity.KEY_ARTICLE_CONTENT), data.get(MainActivity.KEY_TITLE), data.get(MainActivity.KEY_ARTICLE_URL));
    }
}
