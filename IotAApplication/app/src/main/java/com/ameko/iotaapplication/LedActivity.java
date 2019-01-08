package com.ameko.iotaapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class LedActivity extends AppCompatActivity {


    private ImageButton timeButton , feelButton;
    private Context context;
    private Intent intent;
    private ImageButton djButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_led );

        context = this;

        timeButton = (ImageButton) findViewById( R.id.imageButton_Time );
        feelButton = (ImageButton) findViewById( R.id.imageButton_Feel );
        djButton = (ImageButton) findViewById(R.id.imageButton_dj);

        setTitle( "LED設定模式" );

        timeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"進入日常燈光設定",Toast.LENGTH_SHORT).show();
                intent = new Intent(context,TimeActivity.class);
                startActivity(intent);

            }
        } );

        feelButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"進入情境燈光設定",Toast.LENGTH_SHORT).show();
                intent = new Intent(context,FeelActivity.class);
                startActivity(intent);
            }
        } );

        djButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"進入DJ燈光設定",Toast.LENGTH_SHORT).show();
                intent = new Intent(context,DjActivity.class);
                startActivity(intent);
            }
        });



    }//end of onCreate

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

}//end of main class
