package com.star.demo2017103002;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {
    MyDataHandler dataHandler;
    ListView lv;
    ArrayAdapter<String> adapter;

    SwipeRefreshLayout mSwipeLayout;  //下拉更新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataHandler = new MyDataHandler();
        lv = (ListView)findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(MainActivity.this , android.R.layout.simple_list_item_1 , dataHandler.titles);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

//                Uri uri = Uri.parse(dataHandler.links.get(position));
//                Intent it = new Intent(Intent.ACTION_VIEW , uri);
//                startActivity(it);
                Intent intent = new Intent(MainActivity.this , DetailActivity.class);
                intent.putExtra("url" , dataHandler.links.get(position));
                startActivity(intent);
            }
        });

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // 模仿更新 ( 2秒
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){

                    @Override
                    public void run() {

                        // 結束更新動畫
                        mSwipeLayout.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "Refresh Success", Toast.LENGTH_SHORT).show();

                    }}, 2000);
            }
        });

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                        URL url = new URL("https://udn.com/rssfeed/news/2/6638?ch=news");
                    HttpURLConnection conn =(HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    String str ;
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    while ((str = br.readLine()) != null)
                    {
                        sb.append(str);
                    }
                    br.close();
                    isr.close();
                    is.close();
                    String result = sb.toString();



                    SAXParserFactory spf = SAXParserFactory.newInstance();
                    SAXParser sp = spf.newSAXParser();
                    XMLReader xr = sp.getXMLReader();
                    xr.setContentHandler(dataHandler);
                    xr.parse(new InputSource(new StringReader(result)));
                    runOnUiThread(new Runnable() {  //必須放入主執行序
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    }










//public class MainActivity extends AppCompatActivity {
//    MyDataHandler dataHandler;
//    ListView lv;
//    ArrayAdapter<String> adapter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        dataHandler = new MyDataHandler();
//        lv = (ListView)findViewById(R.id.listView);
//        adapter = new ArrayAdapter<String>(MainActivity.this , android.R.layout.simple_list_item_1 , dataHandler.titles);
//        lv.setAdapter(adapter);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//
////                Uri uri = Uri.parse(dataHandler.links.get(position));
////                Intent it = new Intent(Intent.ACTION_VIEW , uri);
////                startActivity(it);
//                Intent intent = new Intent(MainActivity.this , DetailActivity.class);
//                intent.putExtra("url" , dataHandler.links.get(position));
//                startActivity(intent);
//            }
//        });
//
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    URL url = new URL("https://udn.com/rssfeed/news/2/6638?ch=news");
//                    HttpURLConnection conn =(HttpURLConnection) url.openConnection();
//                    StringBuilder sb = new StringBuilder();
//                    String str ;
//                    conn.connect();
//                    InputStream is = conn.getInputStream();
//                    InputStreamReader isr = new InputStreamReader(is);
//                    BufferedReader br = new BufferedReader(isr);
//                    while ((str = br.readLine()) != null)
//                    {
//                        sb.append(str);
//                    }
//                    br.close();
//                    isr.close();
//                    is.close();
//                    String result = sb.toString();
//
//
//
//                    SAXParserFactory spf = SAXParserFactory.newInstance();
//                    SAXParser sp = spf.newSAXParser();
//                    XMLReader xr = sp.getXMLReader();
//                    xr.setContentHandler(dataHandler);
//                    xr.parse(new InputSource(new StringReader(result)));
//                    runOnUiThread(new Runnable() {  //必須放入主執行序
//                        @Override
//                        public void run() {
//                            adapter.notifyDataSetChanged();
//                        }
//                    });
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (ParserConfigurationException e) {
//                    e.printStackTrace();
//                } catch (SAXException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }.start();
//
//    }
//
//}

