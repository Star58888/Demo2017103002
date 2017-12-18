package com.star.demo2017103002;


import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by Star on 2017/10/30.
 */

public class MyDataHandler extends DefaultHandler {    // =處理器
    boolean isTitle = false;   //設一個boolean 抓到<Title> (startElement) 開始印資料(只取文字資料)(characters)  抓到</Title>(endElement) 結束 之後循環
    boolean isItem = false;    // 抓Title裡面的item的內容
    boolean isLink = false;    // 抓網址
    ArrayList<String> titles = new ArrayList();
    public ArrayList<String> links = new ArrayList();
    StringBuilder titleTemp = new StringBuilder();  // 標題會換行的BUG 用這個來讀
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals("title"))
        {
            isTitle = true;
        }
        if (qName.equals("item"))
        {
            isItem = true;
        }
        if (qName.equals("link"))
        {
            isLink = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equals("title"))
        {
            if (isItem) {
                // 要在isItem裡面 不然會多一段空白
                titles.add(titleTemp.toString());  // 讀入轉String
                titleTemp = new StringBuilder();    // 讀入的 titleTemp 把它清掉

            }
            isTitle = false;
        }
        if (qName.equals("item"))
        {
            isItem = false;
        }
        if (qName.equals("link"))
        {
            isLink = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (isTitle && isItem)
        {
            String data = new String(ch, start, length);
            Log.d("MyTitle", data);
            titleTemp.append(data);
//            titles.add(data);  //原本的titles刪掉 避免重複
        }
        if (isLink && isItem)
        {
            String data = new String(ch, start, length);
            Log.d("MyLink", data);
            links.add(data);
        }
    }
}

