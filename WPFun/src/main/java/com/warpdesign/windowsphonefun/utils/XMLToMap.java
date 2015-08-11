package com.warpdesign.windowsphonefun.utils;

import com.warpdesign.windowsphonefun.activities.MainActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nicolaramz on 27/03/14.
 */
public class XMLToMap {
    static public ArrayList<HashMap<String, String>> parse(String rssFeed) {
        ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

        XMLParser parser = new XMLParser();

        Document doc = parser.getDomElement(rssFeed);

        NodeList nl = doc.getElementsByTagName(MainActivity.KEY_ITEM);

        for (int i = 0; i < nl.getLength(); i++) {
            HashMap<String,String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);

            map.put(MainActivity.KEY_TITLE, parser.getValue(e, MainActivity.KEY_TITLE));
            map.put(MainActivity.KEY_IMAGE, parser.getAttributeValue(e, MainActivity.KEY_IMAGE, "url"));
            map.put(MainActivity.KEY_ARTICLE_URL, parser.getValue(e, MainActivity.KEY_ARTICLE_URL));
            map.put(MainActivity.KEY_ARTICLE_CONTENT, parser.getValue(e, MainActivity.KEY_ARTICLE_CONTENT).replaceAll("<p>Cet article(.)*", ""));
            map.put(MainActivity.KEY_ARTICLE_AUTHOR, parser.getValue(e, MainActivity.KEY_ARTICLE_AUTHOR));
            map.put(MainActivity.KEY_ARTICLE_PUB_DATE, parser.getValue(e, MainActivity.KEY_ARTICLE_PUB_DATE));

            items.add(map);
        }

        return items;
    }
}
