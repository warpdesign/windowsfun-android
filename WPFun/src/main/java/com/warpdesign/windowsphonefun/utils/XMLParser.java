package com.warpdesign.windowsphonefun.utils;

/**
 * Created by nicolas ramz on 19/03/14.
 */

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class XMLParser {

    // constructor
    public XMLParser() {

    }

    /**
     * Getting XML from URL making HTTP request
     * @param url string
     * */
    public String getXmlFromUrl(String url) {
        String xml = null;

        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML
        return xml;
    }

    /**
     * Getting XML DOM element
     * @param xml string
     * */
    public Document getDomElement(String xml){
        Document doc;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }

        return doc;
    }

    /** Getting node value
     * @param elem element
     */
    public final String getElementValue( Node elem ) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if( child.getNodeType() == Node.TEXT_NODE  ){
                        // System.out.println("[XMLParser] Returning nodeValue: " + child.getNodeValue());
                        return child.getNodeValue();
                    } else {
                        // System.out.println("[XMLParser] Returning TextContent: " + child.getTextContent());
                        return child.getTextContent();
                    }
                }
            }  else {
                System.out.println("[XMLParser] Could not get ElementValue");
            }
        } else {
            System.out.println("[XMLParser] Oops: element NULL");
        }
        return "";
    }

    public final String getAttributeValue(Node elem, String attributeName) {
        String result = "";
        NamedNodeMap attributes = elem.getAttributes();

        if (attributes != null) {
            // System.out.println("[XMLParser] Returning AttributeValue: " + attributes.getNamedItem(attributeName).getTextContent());
            return attributes.getNamedItem(attributeName).getTextContent();
        } else {
            System.out.println("No attributes for attribute" + attributeName);
        }

        return result;
    }

    /**
     * Getting value
     * @param item item to
     * @param str string
     * */
    public String getValue(Element item, String str) {
        NodeList n;

        if (!str.isEmpty()) {
            // System.out.println("[XMLParser] Getting " + str);
            n = item.getElementsByTagName(str);
            if (n != null) {
                //System.out.println("[XMLParser] Found element");
            } else {
                System.out.println("Could not find element");
            }
        } else {
            n = item.getElementsByTagName(str);
        }
        return this.getElementValue(n.item(0));
    }

    public String getAttributeValue(Element item, String tagName, String attributeName) {
        // System.out.println("[XMLParser] Getting " + attributeName);
        NodeList n = item.getElementsByTagName(tagName);

        return this.getAttributeValue(n.item(0), attributeName);
    }
}