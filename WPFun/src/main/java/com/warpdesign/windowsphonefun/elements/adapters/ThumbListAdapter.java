package com.warpdesign.windowsphonefun.elements.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.warpdesign.windowsphonefun.activities.MainActivity;
import com.warpdesign.windowsphonefun.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.warpdesign.windowsphonefun.utils.DateUtils;

/**
 * Created by nicolas ramz on 18/03/14.
 */
public class ThumbListAdapter extends BaseAdapter{
    private Activity activity;
    private static LayoutInflater inflater=null;
    private ArrayList<HashMap<String, String>> data;

    public ThumbListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void changeDataSource( ArrayList<HashMap<String, String>> newList) {
        this.data = newList;
        notifyDataSetChanged();
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.thumb_list, null);
        }

        // get data for this position
        HashMap<String, String> article = new HashMap<String, String>();
        article = data.get(position);

        // get view elements references
        TextView title = (TextView)vi.findViewById(R.id.article_title);
        ImageView thumb_image = (ImageView)vi.findViewById(R.id.article_image);
        TextView author = (TextView)vi.findViewById(R.id.article_author);
        TextView date = (TextView)vi.findViewById(R.id.article_date);

        // set elements content
        title.setText(article.get(MainActivity.KEY_TITLE));
        author.setText(article.get(MainActivity.KEY_ARTICLE_AUTHOR) + " | ");

        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.XMLDateFormatString, Locale.ENGLISH);

        try {
            Date articleDate = dateFormat.parse(article.get(MainActivity.KEY_ARTICLE_PUB_DATE));
            // TODO: Date.toLocaleString() is depreciated
            date.setText(DateUtils.formatDate(articleDate));
        } catch(ParseException e) {
            date.setText("");
        }

        Picasso.with(activity)
                .load(article.get(MainActivity.KEY_IMAGE))
                .into(thumb_image);

        // convertView.setClickable(true);
        // convertView.setOnClickListener(activity);

        return vi;
    }
}