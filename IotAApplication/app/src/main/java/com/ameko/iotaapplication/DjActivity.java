package com.ameko.iotaapplication;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DjActivity extends AppCompatActivity {

    private static final String TAG = "DJ";
    private Context context;
    private Intent intent;
    private Button button0, button1, button2, button3;
    private Button button4, button5, button6;
    private Button button7, button8;
    private TextView textViewDdata;
    private String remoteDeviceInfo;
    private TextView textViewBT;
    private Button buttonLink, buttonClean;
    private BluetoothAdapter btAdapter;
    private BTChatService mChatService;
    private String lampCMD;

    private final String but0 ="0";
    private final String but1 ="1";
    private final String but2 ="2";
    private final String but3 ="3";
    private final String but4 ="4";
    private final String but5 ="5";
    private final String but6 ="6";
    private final String but7 ="7";
    private final String but8 ="8";


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj);

        setTitle( "DJ燈光設定模式" );
        context = this;

        button0 =(Button) findViewById( R.id.button_1);
        button1 =(Button) findViewById( R.id.button_0);
        button2 =(Button) findViewById( R.id.button_2 );
        button3 =(Button) findViewById( R.id.button_3 );
        button4 =(Button) findViewById( R.id.button_4 );
        button5 =(Button) findViewById( R.id.button_5 );
        button6 =(Button) findViewById( R.id.button_6 );
        button7 =(Button) findViewById( R.id.button_7 );
        button8 =(Button) findViewById( R.id.button_8 );

        buttonLink = (Button) findViewById( R.id.button_Dlink );


        textViewDdata=(TextView) findViewById( R.id.textView_D );
        textViewDdata.setText("現在的模式為:\n");

        textViewBT=(TextView) findViewById( R.id.textView_DBT );

        remoteDeviceInfo = "00:21:13:02:C6:11";

        textViewBT.setText( remoteDeviceInfo );
        btAdapter =BluetoothAdapter.getDefaultAdapter();
        mChatService = new BTChatService(context,mHandler);

        buttonClean =(Button) findViewById( R.id.button_Dclean);
        buttonClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewDdata.setText("");
                textViewDdata.append("現在的模式為:\n");
            }
        });

        buttonLink = (Button) findViewById(R.id.button_Dlink);
        buttonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(remoteDeviceInfo != null){

                    String remoteMACAddress = remoteDeviceInfo;
                    Log.d(TAG,"remoteMACAddress = " +remoteMACAddress);
                    BluetoothDevice device=btAdapter.getRemoteDevice(remoteMACAddress);
                    Log.d(TAG,"device = "+device);
                    mChatService.connect(device);

                }else{
                    Toast.makeText(context,"No paired BT module.",Toast.LENGTH_SHORT).show();
                }
            }
        });



        button0.setOnClickListener( new mybuttonClick() );
        button1.setOnClickListener( new mybuttonClick() );
        button2.setOnClickListener( new mybuttonClick() );
        button3.setOnClickListener( new mybuttonClick() );
        button4.setOnClickListener( new mybuttonClick() );
        button5.setOnClickListener( new mybuttonClick() );
        button6.setOnClickListener( new mybuttonClick() );
        button7.setOnClickListener( new mybuttonClick() );
        button8.setOnClickListener( new mybuttonClick() );

    }//end of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu( menu );
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.led,menu);
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

            case R.id.menu_led:
                Toast.makeText(context,"日常模式",Toast.LENGTH_SHORT).show();
                intent = new Intent(context,LedActivity.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected( item );
    }

    // Sends a Command to remote BT device.
    private void sendCMD(String message) {

        int mState = mChatService.getState();
        Log.d(TAG, "btstate in sendMessage =" + mState);

        if (mState != BTChatService.STATE_CONNECTED) {
            Log.d(TAG, "btstate =" + mState);

            return;

        } else {

            if (message.length() > 0) {

                byte[] send = message.getBytes();
                mChatService.BTWrite(send);

            }
        }

    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;

                    String readMessage = new String(readBuf, 0, msg.arg1);

                    textViewDdata.append(readMessage );
                    Log.d(TAG,"Receive data : "+readMessage);

                    break;

                case Constants.MESSAGE_DEVICE_NAME:

                    String mConnectedDevice = msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(context, "Connected to "+ mConnectedDevice, Toast.LENGTH_SHORT).show();
                    break;

                case Constants.MESSAGE_TOAST:
                    Toast.makeText(context, msg.getData().getString(Constants.TOAST),Toast.LENGTH_SHORT).show();
                    break;

                case Constants.MESSAGE_ServerMode:

                    break;

                case Constants.MESSAGE_ClientMode:

                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            Log.d(TAG,"CarActivity onDestry()");
            mChatService.stop();
            mChatService=null;
        }
    }


    private class mybuttonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_1:
                    lampCMD=but0;
                    sendCMD(lampCMD);
                    textViewDdata.append("白\n");
                    break;


                case R.id.button_0:
                    lampCMD=but1;
                    sendCMD(lampCMD);
                    textViewDdata.append("黑\n");
                    break;

                case R.id.button_2:
                    lampCMD=but2;
                    sendCMD(lampCMD);
                    textViewDdata.append("紅\n");
                    break;

                case R.id.button_3:
                    lampCMD=but3;
                    sendCMD(lampCMD);
                    textViewDdata.append("橙\n");
                    break;

                case R.id.button_4:
                    lampCMD=but4;
                    sendCMD(lampCMD);
                    textViewDdata.append("黃\n");
                    break;


                case R.id.button_5:
                    lampCMD=but5;
                    sendCMD(lampCMD);
                    textViewDdata.append("綠\n");
                    break;

                case R.id.button_6:
                    lampCMD=but6;
                    sendCMD(lampCMD);
                    textViewDdata.append("藍\n");
                    break;

                case R.id.button_7:
                    lampCMD=but7;
                    sendCMD(lampCMD);
                    textViewDdata.append("靛\n");
                    break;

                case R.id.button_8:
                    lampCMD=but8;
                    sendCMD(lampCMD);
                    textViewDdata.append("紫\n");
                    break;
            }

        }
    }
}
