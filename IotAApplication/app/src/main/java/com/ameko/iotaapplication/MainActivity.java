package com.ameko.iotaapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageButton button1, button2, button3;
    private ImageButton button4, button5;
    private Context context;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        button1 = (ImageButton) findViewById( R.id.imageButton_01 );
        button2 = (ImageButton) findViewById( R.id.imageButton_2 );
        button3 = (ImageButton) findViewById( R.id.imageButton_3 );
        button4 = (ImageButton) findViewById( R.id.imageButton_4 );
        button5 = (ImageButton) findViewById( R.id.imageButton_5 );

        context = this;

        button1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context,"體溫資料",Toast.LENGTH_SHORT).show();
                intent = new Intent(context,BodyActivity.class);
                startActivity(intent);

            }
        } );

        button2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Webcam功能",Toast.LENGTH_SHORT).show();
                intent = new Intent(context,WebcamActivity.class);
                startActivity(intent);

            }
        } );

        button3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Led功能",Toast.LENGTH_SHORT).show();
                intent = new Intent(context,LedActivity.class);
                startActivity(intent);

            }
        } );

        button4.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"溫溼度功能",Toast.LENGTH_SHORT).show();
                intent = new Intent(context,TemperatureActivity.class);
                startActivity(intent);

            }
        } );

        button5.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        } );



    }
}
