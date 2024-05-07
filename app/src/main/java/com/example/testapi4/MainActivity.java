package com.example.testapi4;

import android.app.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends Activity {

    EditText edit;
    TextView text;
    Button btn;
    String key = "0883b423d1404ad0aebf";

    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit = (EditText) findViewById(R.id.edit);
        text = (TextView) findViewById(R.id.text);
        btn = (Button) findViewById(R.id.btn_tomain);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                startActivity(intent);
            }
        });

        //추가 부분
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                intent.putExtra("음식이름", data);
                startActivity(intent);
            }
        });


        //여기까지
    }

    public void mOnClick(View v) {
        if (v.getId() == R.id.button) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    data = getXmlData();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text.setText(data);
                        }
                    });
                }
            }).start();
        }
    }


    String getXmlData() {

        StringBuffer buffer = new StringBuffer();

        String str = edit.getText().toString();
        String location = URLEncoder.encode(str);

        String queryUrl = "http://openapi.foodsafetykorea.go.kr/api/0883b423d1404ad0aebf/I2790/xml/1/1/DESC_KOR=\"" + location + "\"";

        try {
            URL url = new URL(queryUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();

                        if (tag.equals("row")) ;
                        else if (tag.equals("DESC_KOR")) {
                            buffer.append("음식 이름 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if (tag.equals("NUTR_CONT1")) {
                            buffer.append("칼로리 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if (tag.equals("NUTR_CONT2")) {
                            buffer.append("탄수화물 :");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if (tag.equals("NUTR_CONT3")) {
                            buffer.append("단백질 :");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n\n");
                        } else if (tag.equals("NUTR_CONT4")) {
                            buffer.append("지방 :");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();

                        if (tag.equals("item")) buffer.append("\n");
                        break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }

        return buffer.toString();
    }

}
