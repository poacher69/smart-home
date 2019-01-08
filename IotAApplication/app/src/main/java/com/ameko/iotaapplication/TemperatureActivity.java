package com.ameko.iotaapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TemperatureActivity extends AppCompatActivity {
    private WebView webView;
    private Intent intent;
    private Context context;
    private ImageButton buttonTemperature;
    private TextView textViewResult;
    private  static final String TAG="join";
    private StringBuilder sqlURL;
    private String webAddress="http://192.168.63.11:5000/";
    private String select = "select_data";
    private URL url;
    private GetSQLData mySelectData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        setTitle("溫溼度模式");
        context = this;

        buttonTemperature=(ImageButton) findViewById(R.id.imageButton_Temperature);
        textViewResult =(TextView) findViewById( R.id.textView_temperature );

        buttonTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewResult.setText("");
                mySelectData = new GetSQLData();
                mySelectData.execute();
            }
        });

        webView = (WebView) findViewById(R.id.webView_temp);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);

        webView.loadUrl("https://thingspeak.com/channels/637301");


        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu( menu );
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_main:
                Toast.makeText(context,"回首頁",Toast.LENGTH_SHORT).show();
                intent = new Intent(context,MainActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected( item );
    }

    private class GetSQLData extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... voids) {
            String data=null;

            sqlURL = new StringBuilder();
            sqlURL.append(webAddress);
            sqlURL.append(select);

            try {


                url = new URL(sqlURL.toString());
                Log.d(TAG, "url=" + url);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int code = conn.getResponseCode();
                Log.d(TAG,"code=" +code);

                if(code == HttpURLConnection.HTTP_OK){

                    InputStream input = conn.getInputStream();
                    InputStreamReader reader = new InputStreamReader(input);

//                    BufferedReader stringReader = new BufferedReader(reader);
//                    data = stringReader.readLine();
//                    Log.d(TAG,"data=" +data);

                    char[] buffer = new char[1024];
                    reader.read(buffer);
                    data = String.valueOf(buffer);
                    Log.d(TAG,"data=" +data);

                    input.close();

                }else {

                    data = null;
                }

            }catch (MalformedURLException e){

                e.printStackTrace();

            }catch (IOException e){

                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textViewResult.setText("最新資料如下：");


            if (s !=null) {

                try {
                    JSONArray jsonArray = new JSONArray(s);
                    int number = jsonArray.length(); //查資料有多少筆
                    Log.d( TAG, "array length =" + number );
                    StringBuilder jsonData = new StringBuilder();

                    for (int i = 0; i < number; i++) {

                        JSONObject jsonObj = jsonArray.getJSONObject(i);//逐筆取資料
                        Log.d(TAG, "jsonObj =" + jsonObj);

                        int id = jsonObj.getInt("id");
                        Log.d(TAG,"id = "+id);
                        jsonData.append("id = " + id + " , ");

                        String time_t = jsonObj.getString("time_t");
                        jsonData.append("time_t =" + time_t + "\n ");

                        String temperature = jsonObj.getString("temperature");
                        jsonData.append("      temperature = " + temperature + " , ");

                        String humidity = jsonObj.getString("humidity");
                        jsonData.append("humidity = " + humidity + "\n ");
//
//                     //   String body = jsonObj.getString("body");
//                     //   jsonData.append("body = " + body + "\n");
//
                   }

                    textViewResult.append("\n ");
                    textViewResult.append(jsonData.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(context,"There is no data.",Toast.LENGTH_SHORT).show();
            }


        }//end of onPostExecute(String s)


    }//end of select



}


